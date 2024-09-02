package com.medicoLaboSolutions.backPatient.unit_test.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.medicoLaboSolutions.backPatient.controller.PatientController;
import com.medicoLaboSolutions.backPatient.model.dto.PatientDTO;
import com.medicoLaboSolutions.backPatient.model.pojo.Patient;
import com.medicoLaboSolutions.backPatient.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ComponentScan("com.nnk.springboot.controllers")
public class PatientControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientController patientController;

    @MockBean
    private PatientService patientService;

    private final String URI_PREFIX = "/patients";

    @Test
    public void getAllPatients_ShouldCallTheCorrectMethod() throws Exception {
        //ARRANGE
        String uri = URI_PREFIX;


        List<Patient> patientListTest = new ArrayList<>();
        patientListTest.add(new Patient("firstnameTest1", "lastnameTest1",new Date(2000, 5,1), "M"));
        patientListTest.add(new Patient("firstnameTest2", "lastnameTest2",new Date(2003, 6,2), "F"));

        when(patientService.findAll()).thenReturn(patientListTest);

        //ACT
        MvcResult mvcResult = mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andReturn();

        //ASSERT
        String response = mvcResult.getResponse().getContentAsString();

        assertThat(patientListTest.get(0).getLastname()).isEqualTo(JsonPath.parse(response).read("$[0]lastname"));
        assertThat(patientListTest.get(1).getFirstname()).isEqualTo(JsonPath.parse(response).read("$[1]firstname"));

        verify(patientService, times(1)).findAll();
    }

    @Test
    public void getPatient_ShouldCallTheCorrectMethod() throws Exception {
        //ARRANGE
        int patientId = 4;
        String uri = URI_PREFIX + "/" + patientId;


        Patient patientToGet = new Patient("firstnameTest1", "lastnameTest1",new Date(2000, 5,1), "M");

        when(patientService.findPatientById(patientId)).thenReturn(patientToGet);

        //ACT
        MvcResult mvcResult = mockMvc.perform(get(uri)
                        .queryParam("id", String.valueOf(patientId)))
                .andExpect(status().isOk())
                .andReturn();

        //ASSERT
        String response = mvcResult.getResponse().getContentAsString();
        assertThat(patientToGet.getLastname()).isEqualTo(JsonPath.parse(response).read("$.lastname"));

        verify(patientService, times(1)).findPatientById(patientId);
    }

    @Test
    public void addPatient_ShouldCallTheCorrectMethod() throws Exception {
        //ARRANGE
        String uri = URI_PREFIX;


        Patient patientToAdd = new Patient("firstnameTest", "lastnameTest",new Date(2000, 5,1), "M");

        when(patientService.addNewPatient(patientToAdd)).thenReturn(patientToAdd);

        //ACT
        MvcResult mvcResult = mockMvc.perform(post(uri)
                    .content(new ObjectMapper().writeValueAsString(patientToAdd))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        //ASSERT
        String response = mvcResult.getResponse().getContentAsString();
        assertThat(patientToAdd.getLastname()).isEqualTo(JsonPath.parse(response).read("$.lastname"));

        verify(patientService, times(1)).addNewPatient(patientToAdd);
    }

    @Test
    public void getPatientDTO_ShouldCallTheCorrectMethod() throws Exception {
        //ARRANGE
        int patientId = 4;
        String uri = URI_PREFIX + "/dto/" + patientId;


        PatientDTO patientDTO = new PatientDTO(4, "firstNameTest", "lastNameTest", "300 second street", "888-999-7777");

        when(patientService.producePatientDTOFromPatient(patientId)).thenReturn(patientDTO);

        //ACT
        MvcResult mvcResult = mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andReturn();

        //ASSERT
        String response = mvcResult.getResponse().getContentAsString();
        assertThat(patientDTO.getLastname()).isEqualTo(JsonPath.parse(response).read("$.lastname"));

        verify(patientService, times(1)).producePatientDTOFromPatient(patientId);
    }

    @Test
    public void updatePatient_ShouldCallTheCorrectMethod() throws Exception {
        //ARRANGE
        int patientId = 4;
        String uri = URI_PREFIX + "/" + patientId;


        PatientDTO patientDTO = new PatientDTO(4, "firstNameTest", "lastNameTest", "300 second street", "888-999-7777");

        Patient patientUpdated = new Patient(4,"firstnameTest", "lastnameUpdatedTest",new Date(2000, 5,1), "M", "300 second street", "888-999-7777");

        when(patientService.updatePatient(any(PatientDTO.class), eq(patientId))).thenReturn(patientUpdated);

        //ACT
        MvcResult mvcResult = mockMvc.perform(put(uri)
                        .content(new ObjectMapper().writeValueAsString(patientDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        //ASSERT
        String response = mvcResult.getResponse().getContentAsString();
        assertThat(patientUpdated.getLastname()).isEqualTo(JsonPath.parse(response).read("$.lastname"));

        verify(patientService, times(1)).updatePatient(any(PatientDTO.class), eq(patientId));
    }

    @Test
    public void deletePatient_ShouldCallTheCorrectMethod() throws Exception {
        //ARRANGE
        int patientId = 4;
        String uri = URI_PREFIX + "/" + patientId;

        Patient patientDeleted = new Patient(4,"firstnameDeleted", "lastnameDeleted",new Date(2000, 5,1), "M", "300 second street", "888-999-7777");

        when(patientService.deleteById(patientId)).thenReturn(patientDeleted);

        //ACT
        MvcResult mvcResult = mockMvc.perform(delete(uri))
                .andExpect(status().isOk())
                .andReturn();

        //ASSERT
        String response = mvcResult.getResponse().getContentAsString();
        assertThat(patientDeleted.getLastname()).isEqualTo(JsonPath.parse(response).read("$.lastname"));

        verify(patientService, times(1)).deleteById(patientId);
    }
}
