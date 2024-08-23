package com.medicoLaboSolutions.microservice_note.unit_test.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.medicoLaboSolutions.microservice_note.controller.NoteController;
import com.medicoLaboSolutions.microservice_note.model.Note;
import com.medicoLaboSolutions.microservice_note.service.NoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ComponentScan("com.nnk.springboot.controllers")
public class NoteControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteController noteController;

    @MockBean
    private NoteService noteService;

    private final String URI_PREFIX = "/notes";

    @Test
    public void getAllPatientNotes_ShouldCallTheCorrectMethod() throws Exception {
        //ARRANGE
        String patientId = "4";
        String uri = URI_PREFIX + "/" + patientId;


        List<Note> mockResult = new ArrayList<>();
        mockResult.add(new Note("4", "TestEarlyOnset","contentTest"));
        mockResult.add(new Note("4", "TestEarlyOnset","contentTest2"));

        when(noteService.findPatientListOfNotes(patientId)).thenReturn(mockResult);

        //ACT
        MvcResult mvcResult = mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andReturn();

        //ASSERT
        String response = mvcResult.getResponse().getContentAsString();
        assertThat(mockResult.get(0).getNote()).isEqualTo(JsonPath.parse(response).read("$[0]note"));
        assertThat(mockResult.get(1).getNote()).isEqualTo(JsonPath.parse(response).read("$[1]note"));

        verify(noteService, times(1)).findPatientListOfNotes(patientId);
    }

    @Test
    public void addNewNote_ShouldCallTheCorrectMethod() throws Exception {
        //ARRANGE
        String uri = URI_PREFIX;

        Note noteToAdd = new Note("4", "TestEarlyOnset","contentTest");

        when(noteService.addNewNote(noteToAdd)).thenReturn(noteToAdd);

        //ACT
        MvcResult mvcResult = mockMvc.perform(post(uri)
                        .content(new ObjectMapper().writeValueAsString(noteToAdd))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        //ASSERT
        String response = mvcResult.getResponse().getContentAsString();
        assertThat(noteToAdd.getNote()).isEqualTo(JsonPath.parse(response).read("$.note"));

        verify(noteService, times(1)).addNewNote(noteToAdd);
    }
}
