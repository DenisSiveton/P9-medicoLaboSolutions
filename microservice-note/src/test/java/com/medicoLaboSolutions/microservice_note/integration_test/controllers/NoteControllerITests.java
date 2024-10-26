package com.medicoLaboSolutions.microservice_note.integration_test.controllers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.medicoLaboSolutions.microservice_note.controller.NoteController;
import com.medicoLaboSolutions.microservice_note.model.Note;
import com.medicoLaboSolutions.microservice_note.repository.NoteRepository;
import com.medicoLaboSolutions.microservice_note.service.NoteService;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NoteControllerITests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteController noteController;

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteRepository noteRepositoryUtil;

    @Autowired
    private WebApplicationContext webContext;

    private final String URI_PREFIX = "/notes";

    private ObjectMapper mapper = new ObjectMapper();

    private final String FILE_NAME = "src/test/resources/data.json";

    @BeforeAll
    public void setupMockmvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
    }

    @BeforeAll
    public void setUpData(){
        try {
            noteRepositoryUtil.deleteAll();
            TypeReference<List<Note>> jacksonTypeReference = new TypeReference<List<Note>>() {};
            String dataJSONToString = FileUtils.readFileToString(new File(FILE_NAME), StandardCharsets.UTF_8);
            List<Note> jacksonList = mapper.readValue(dataJSONToString, jacksonTypeReference);

            noteRepositoryUtil.insert(jacksonList);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @AfterAll
    public void resetDataBase(){
        noteRepositoryUtil.deleteAll();
    }
    @Test
    public void getAllPatientNotes_EachLogicLayerShouldBeOperational_ITest() throws Exception {
        //ARRANGE
        String patientId = "4";
        String uri = URI_PREFIX + "/" + patientId;


        //ACT
        MvcResult mvcResult = mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andReturn();

        //ASSERT
        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(responseBody.split("patId\":\"4", -1).length-1).isBetween(4,5);
    }

    @Test
    public void addNewNote_EachLogicLayerShouldBeOperational_ITest() throws Exception {
        //ARRANGE

        Note noteToAdd = new Note("4", "TestEarlyOnset","contentTest");

        //ACT
        MvcResult mvcResult = mockMvc.perform(post(URI_PREFIX)
                        .content(new ObjectMapper().writeValueAsString(noteToAdd))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        //ASSERT
        String response = mvcResult.getResponse().getContentAsString();
        assertThat(noteToAdd.getNote()).isEqualTo(JsonPath.parse(response).read("$.note"));
    }
}
