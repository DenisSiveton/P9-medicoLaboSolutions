package com.medicoLaboSolutions.microservice_diagnostic.integration_test.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.medicoLaboSolutions.microservice_diagnostic.beans.NoteBean;
import com.medicoLaboSolutions.microservice_diagnostic.beans.PatientBean;
import com.medicoLaboSolutions.microservice_diagnostic.controller.DiagnosticController;
import com.medicoLaboSolutions.microservice_diagnostic.proxies.MicroserviceNoteProxy;
import com.medicoLaboSolutions.microservice_diagnostic.proxies.MicroservicePatientProxy;
import com.medicoLaboSolutions.microservice_diagnostic.service.DiagnosticService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DiagnosticControllerITests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DiagnosticController diagnosticController;

    @Autowired
    private DiagnosticService diagnosticService;

    @MockBean
    private MicroservicePatientProxy microservicePatientProxy;

    @MockBean
    private MicroserviceNoteProxy microserviceNoteProxy;

    @Autowired
    private WebApplicationContext webContext;

    private final String URI_PREFIX = "/diagnostics";

    private ObjectMapper mapper = new ObjectMapper();

    private static long millisInYear;

    @BeforeAll
    public void setupData() {
        millisInYear = (long) (1000*60*60*24*365.25);
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
    }

    @Test
    public void getDiagnostic_ShouldCallTheCorrectMethod() throws Exception {
        //ARRANGE
        String patientId = "4";
        String uri = URI_PREFIX + "/" + patientId;

        //Set BirthDate to XX years ago
        Date patientBirthDate = new Date(System.currentTimeMillis()-millisInYear*25);
        //Create Patient with associated Notes
        PatientBean patientToAnalyse = new PatientBean(50, "firstNameTest", "lastNameTest",
                patientBirthDate, "F", "123 main street", "555-888-9988");
        List<NoteBean> noteListOfPatient = new ArrayList<>();
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Présence de vertiges, d'anticorps le mois dernier." ));
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Le patient est maintenant fumeur." ));
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Le taux de cholestérol a augmenté ce dernier mois. Prise de poids anormale par rapport à la taille du patient." ));
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Une rechute de la présence d'hémoglobine A1C"));

        ResponseEntity<PatientBean> mockPatientRB = new ResponseEntity<>(patientToAnalyse, HttpStatus.OK);
        ResponseEntity<List<NoteBean>> mockNotesRB = new ResponseEntity<>(noteListOfPatient, HttpStatus.OK);

        when(microservicePatientProxy.getPatient(4)).thenReturn(mockPatientRB);
        when(microserviceNoteProxy.getListNotesAboutPatient(4)).thenReturn(mockNotesRB);

        //ACT
        MvcResult mvcResult = mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andReturn();

        //ASSERT
        String response = mvcResult.getResponse().getContentAsString();
        assertThat(4).isEqualTo(JsonPath.parse(response).read("$.sicknessLevel"));
    }
}
