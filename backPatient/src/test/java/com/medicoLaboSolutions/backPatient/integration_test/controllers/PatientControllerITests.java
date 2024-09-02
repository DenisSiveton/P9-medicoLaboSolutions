package com.medicoLaboSolutions.backPatient.integration_test.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medicoLaboSolutions.backPatient.controller.PatientController;
import com.medicoLaboSolutions.backPatient.model.dto.PatientDTO;
import com.medicoLaboSolutions.backPatient.model.pojo.Patient;
import com.medicoLaboSolutions.backPatient.repository.PatientRepository;
import com.medicoLaboSolutions.backPatient.service.PatientService;
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

import java.nio.charset.StandardCharsets;
import java.sql.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PatientControllerITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientController patientController;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private WebApplicationContext webContext;

    private final String URI_PREFIX = "/patients";

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    public void setupMockmvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
    }


    /*
       *************************************
       ***                               ***
       ***       Optimal use cases       ***
       ***                               ***
       *************************************
     */

    @Test
    public void getPatient_EachLogicLayerShouldBeOperational_ITest() throws Exception {
        //ARRANGE
        String patientId = "2";
        String uri = URI_PREFIX + "/" + patientId;

        //ACT
        MvcResult mvcResult = mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andReturn();

        //ASSERT
        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(responseBody.contains("\"phoneNumber\":\"200-333-4444\""));
    }

    @Test
    public void getAllPatient_EachLogicLayerShouldBeOperational_ITest() throws Exception {
        //ACT
        MvcResult mvcResult = mockMvc.perform(get(URI_PREFIX))
                .andExpect(status().isOk())
                .andReturn();

        //ASSERT
        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(responseBody.contains("\"phoneNumber\":\"400-555-6666\""));
        assertThat(responseBody.split("phoneNumber\":\"", -1).length-1).isEqualTo(4);
    }

    @Test
    public void addPatient_EachLogicLayerShouldBeOperational_ITest() throws Exception {
        //ARRANGE
        Patient patientToAdd = new Patient("firstnameTestToDelete", "lastnameTestToDelete",
                new Date(2000, 5,1), "M", "1 test street", "555-444-3333");

        //ACT
        MvcResult mvcResult = mockMvc.perform(post(URI_PREFIX)
                        .content(new ObjectMapper().writeValueAsString(patientToAdd))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        //ASSERT
        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(responseBody.contains("\"phoneNumber\":\"555-444-3333\""));
    }

    @Test
    public void getPatientDTO_EachLogicLayerShouldBeOperational_ITest() throws Exception {
        //ARRANGE
        String patientId = "4";
        String uri = URI_PREFIX + "/dto/" + patientId;

        //ACT
        MvcResult mvcResult = mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andReturn();

        //ASSERT
        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(responseBody.contains("\"lastname\":\"TestInDanger\""));
    }

    @Test
    public void updatePatient_EachLogicLayerShouldBeOperational_ITest() throws Exception {
        //ARRANGE
        String patientId = "2";
        String uri = URI_PREFIX + "/" + patientId;

        PatientDTO patientDTOWithUpdatedData = new PatientDTO(2, "TestUpdated", "TestEarlyOnsetDanger", "4 Valley Dr", "400-555-6677");

        //ACT
        MvcResult mvcResult = mockMvc.perform(put(uri)
                        .content(new ObjectMapper().writeValueAsString(patientDTOWithUpdatedData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        //ASSERT
        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(responseBody.contains("\"firstname\":\"TestUpdated\""));
        assertThat(responseBody.contains("\"lastname\":\"TestEarlyOnsetDanger\""));
        assertThat(responseBody.contains("\"phoneNumber\":\"400-555-6677\""));
    }

    @Test
    public void deletePatient_EachLogicLayerShouldBeOperational_ITest() throws Exception {
        //ARRANGE
        String patientId = "4";
        String uri = URI_PREFIX + "/" + patientId;

        //ACT
        MvcResult mvcResult = mockMvc.perform(delete(uri))
                .andExpect(status().isOk())
                .andReturn();

        //ASSERT
        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(responseBody.contains("\"firstname\":\"Test\""));
        assertThat(responseBody.contains("\"lastname\":\"TestEarlyOnset\""));
        assertThat(responseBody.contains("\"phoneNumber\":\"400-555-6666\""));
    }

    /*
     *************************************
     ***                               ***
     ***       Exceptions cases        ***
     ***                               ***
     *************************************
     */

    @Test
    public void getPatient_WithIncorrectPatientId_ShouldThrowException_ITest() throws Exception {
        //ARRANGE
        String patientId = "6";
        String uri = URI_PREFIX + "/" + patientId;

        String expectedMessage = "Invalid Patient Id: No patient exists with the Id 6. Please repeat your request";

        //ACT
        MvcResult mvcResult = mockMvc.perform(get(uri))
                .andExpect(status().isNotFound())
                .andReturn();
        Exception exception = mvcResult.getResolvedException();

        //ASSERT
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }

    @Test
    public void addPatient_WithIncorrectPatientData_ShouldThrowException_ITest() throws Exception {
        //ARRANGE
        Patient patientToAdd = new Patient("firstnameTestWith1Error", "lastnameTestToDelete",
                new Date(2000, 5,1), "M", "1 test street", "555-444-3333");

        //ACT
        MvcResult mvcResult = mockMvc.perform(post(URI_PREFIX)
                        .content(new ObjectMapper().writeValueAsString(patientToAdd))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void updatePatient_WithIncorrectPatientDTO_ShouldThrowException_ITest() throws Exception {
        //ARRANGE
        String patientId = "4";
        String uri = URI_PREFIX + "/" + patientId;

        PatientDTO patientDTOWithUpdatedData = new PatientDTO(4, "TestUpdatedWith1Error", "TestEarlyOnsetDanger", "4 Valley Dr", "400-555-6677");

        String expectedMessage = "Invalid Patient Id: No patient exists with the Id 5. Please repeat your request";

        //ACT
        MvcResult mvcResult = mockMvc.perform(put(uri)
                        .content(new ObjectMapper().writeValueAsString(patientDTOWithUpdatedData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
    @Test
    public void updatePatient_WithIncorrectPatientId_ShouldThrowException_ITest() throws Exception {
        //ARRANGE
        String patientId = "6";
        String uri = URI_PREFIX + "/" + patientId;

        PatientDTO patientDTOWithUpdatedData = new PatientDTO(5, "TestUpdated", "TestEarlyOnsetDanger", "4 Valley Dr", "400-555-6677");

        String expectedMessage = "Invalid Patient Id: No patient exists with the Id 6. Please repeat your request";

        //ACT
        MvcResult mvcResult = mockMvc.perform(put(uri)
                        .content(new ObjectMapper().writeValueAsString(patientDTOWithUpdatedData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
        Exception exception = mvcResult.getResolvedException();

        //ASSERT
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }

    @Test
    public void deletePatient_WithIncorrectId_ShouldThrowException_ITest() throws Exception {
        //ARRANGE
        String patientId = "6";
        String uri = URI_PREFIX + "/" + patientId;

        String expectedMessage = "Invalid Patient Id: No patient exists with the Id 6. Please repeat your request";

        //ACT
        MvcResult mvcResult = mockMvc.perform(delete(uri))
                .andExpect(status().isNotFound())
                .andReturn();
        Exception exception = mvcResult.getResolvedException();

        //ASSERT
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }



}
