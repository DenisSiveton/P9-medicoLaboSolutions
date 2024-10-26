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

/**
 * This class serves as a controller for the diagnostic application
 *
 * It is linked with the controller of two others microservices:
 *  - microservice-patient
 *  - microservice-note
 *
 * It contains a request to calculate a diagnostic of a patient and exposes the result as a json data
 */
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

    /**
     * This method establishes a diagnostic for a specific Patient using an Id number.
     * Below is the process:
     *  - retrieve Patient info from Id
     *  - retrieve all Notes associated to the Patient
     *  - calculate the diagnostic of the Patient based on the Patient's age and Notes's content.
     *
     * @param patientId Id number that serves as an identifier for the database.
     * @return ResponseEntity containing the data of the diagnostic
     */
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
