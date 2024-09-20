package com.medicoLaboSolutions.microservice_diagnostic.unit_test.controllers;

import com.jayway.jsonpath.JsonPath;
import com.medicoLaboSolutions.microservice_diagnostic.beans.NoteBean;
import com.medicoLaboSolutions.microservice_diagnostic.beans.PatientBean;
import com.medicoLaboSolutions.microservice_diagnostic.controller.DiagnosticController;
import com.medicoLaboSolutions.microservice_diagnostic.model.DiagnosticAnalysis;
import com.medicoLaboSolutions.microservice_diagnostic.proxies.MicroserviceNoteProxy;
import com.medicoLaboSolutions.microservice_diagnostic.proxies.MicroservicePatientProxy;
import com.medicoLaboSolutions.microservice_diagnostic.service.DiagnosticService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DiagnosticControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DiagnosticController diagnosticController;

    @MockBean
    private DiagnosticService diagnosticService;

    @MockBean
    private MicroservicePatientProxy microservicePatientProxy;

    @MockBean
    private MicroserviceNoteProxy microserviceNoteProxy;

    private final String URI_PREFIX = "/diagnostics";

    @Test
    public void getDiagnostic_ShouldCallTheCorrectMethod() throws Exception {
        //ARRANGE
        String patientId = "4";
        String uri = URI_PREFIX + "/" + patientId;

        ResponseEntity<PatientBean> mockPatientRB = new ResponseEntity<>(new PatientBean(), HttpStatus.OK);
        ResponseEntity<List<NoteBean>> mockNotesRB = new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);

        DiagnosticAnalysis mockResult = new DiagnosticAnalysis(60, 2,"Borderline", 4, new ArrayList<>());

        when(microservicePatientProxy.getPatient(4)).thenReturn(mockPatientRB);
        when(microserviceNoteProxy.getListNotesAboutPatient(4)).thenReturn(mockNotesRB);
        when(diagnosticService.calculateSicknessLevel(mockPatientRB.getBody(), mockNotesRB.getBody())).thenReturn(mockResult);

        //ACT
        MvcResult mvcResult = mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andReturn();

        //ASSERT
        String response = mvcResult.getResponse().getContentAsString();
        assertThat(mockResult.getSicknessLevel()).isEqualTo(JsonPath.parse(response).read("$.sicknessLevel"));
        assertThat(mockResult.getSicknessCategory()).isEqualTo(JsonPath.parse(response).read("$.sicknessCategory"));

        verify(microservicePatientProxy, times(1)).getPatient(4);
        verify(microserviceNoteProxy, times(1)).getListNotesAboutPatient(4);
        verify(diagnosticService, times(1)).calculateSicknessLevel(mockPatientRB.getBody(), mockNotesRB.getBody());
    }
}
