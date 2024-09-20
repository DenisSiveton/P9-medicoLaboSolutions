package com.medicoLaboSolutions.microservice_diagnostic.controller;

import com.medicoLaboSolutions.microservice_diagnostic.beans.NoteBean;
import com.medicoLaboSolutions.microservice_diagnostic.beans.PatientBean;
import com.medicoLaboSolutions.microservice_diagnostic.model.DiagnosticAnalysis;
import com.medicoLaboSolutions.microservice_diagnostic.proxies.MicroserviceNoteProxy;
import com.medicoLaboSolutions.microservice_diagnostic.proxies.MicroservicePatientProxy;
import com.medicoLaboSolutions.microservice_diagnostic.service.DiagnosticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/diagnostics")
public class DiagnosticController {

    @Autowired
    private DiagnosticService diagnosticService;

    private Logger logger = LoggerFactory.getLogger(DiagnosticController.class);

    private final MicroservicePatientProxy microservicePatientProxy;

    private final MicroserviceNoteProxy microserviceNoteProxy;

    public DiagnosticController(MicroservicePatientProxy microservicePatientProxy, MicroserviceNoteProxy microserviceNoteProxy) {
        this.microservicePatientProxy = microservicePatientProxy;
        this.microserviceNoteProxy = microserviceNoteProxy;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DiagnosticAnalysis> getDiagnostic(@PathVariable("id") int patientId){
        logger.info("Request : Calculate diagnostic");
        logger.info("Retrieve info of the patient with the Id {}.", patientId);
        PatientBean patientToAnalyse = microservicePatientProxy.getPatient(patientId).getBody();

        logger.info("Retrieve the notes of the patient with the Id {}.", patientId);
        List< NoteBean> listOfNotes = microserviceNoteProxy.getListNotesAboutPatient(patientId).getBody();

        logger.info("Calculate the diagnostic of the patient with the Id {}.", patientId);
        DiagnosticAnalysis diagnosticAnalysis = diagnosticService.calculateSicknessLevel(patientToAnalyse, listOfNotes);
        return ResponseEntity.ok(diagnosticAnalysis);
    }
}
