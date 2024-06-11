package com.medicoLaboSolutions.backPatient.controller;

import com.medicoLaboSolutions.backPatient.model.Patient;
import com.medicoLaboSolutions.backPatient.model.dto.PatientDTO;
import com.medicoLaboSolutions.backPatient.model.pojo.Patient;
import com.medicoLaboSolutions.backPatient.service.PatientService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/patient")
public class PatientController {

    private Logger logger = LoggerFactory.getLogger(PatientController.class);
    @Autowired
    private PatientService patientService;

    @GetMapping("/{lastName}")
    public Patient getPatient(@PathVariable String lastname){
        logger.info("Retrieve patient info with the surname :" + lastname);
        Patient requestedPatient = patientService.findPatientByLastname(lastname);
        return requestedPatient;
    }
    @GetMapping("/")
    public Iterable<Patient> getPatients(){
        logger.info("Retrieve all patients info.");
        Iterable<Patient> requestedPatientList = patientService.findAll();
        return requestedPatientList;
    }

    /**
     * This method checks if the data from the form are consistent and valid for a new BidList.
     * If the checks pass then add the new BidList to the database.
     *      It then retrieves all BidLists from database and list them in the UI for the user.
     * If the checks fail, the user is redirected to the form for a second attempt with an error message
     *      explaining why the request failed.
     *
     * @param patient Entity constructed from the form. It will be added to the database
     * @param result Form result. May contain errors if data don't comply
     * @return URI /patients. Show table with updated BidLists
     * @return In case of error : URI bidList/add. Returns to the form for a second attempt
     */
    @PostMapping(path = "/", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public Patient validate(@Valid Patient patient, BindingResult result) {
        if (!result.hasErrors()) {
            logger.info("Add new patient.");
            return patientService.addNewPatient(patient);
        }
        return patient;
    }

    @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public Patient updatePatient(@PathVariable("id") Integer id, @Valid PatientDTO patientWithUpdatedInfo, BindingResult result){
        if(!result.hasErrors()){
            logger.info("Update patient with the id :" + id);
            return patientService.updatePatient(patientWithUpdatedInfo, id);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public Patient deletePatient(@PathVariable("id") Integer id){
        try {
            logger.info("Delete patient with the id :" + id);
            return patientService.deleteById(id);
        }catch(IllegalArgumentException illegalArgumentException){
            logger.error(illegalArgumentException.getMessage());
            return null;
        }
    }
}
