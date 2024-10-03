package com.medicoLaboSolutions.backPatient.controller;

import com.medicoLaboSolutions.backPatient.model.dto.PatientDTO;
import com.medicoLaboSolutions.backPatient.model.pojo.Patient;
import com.medicoLaboSolutions.backPatient.service.PatientService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/patients")
public class PatientController {

    private Logger logger = LoggerFactory.getLogger(PatientController.class);
    @Autowired
    private PatientService patientService;

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable("id")  int id){
        logger.info("Retrieve patient info with the Id : {}", id);
        Patient patientFound = patientService.findPatientById(id);
        return ResponseEntity.ok(patientFound);
    }
    @GetMapping("")
    public ResponseEntity<List<Patient>> getAllPatients(){
        logger.info("Retrieve all patients info.");
        List<Patient> listOfPatient = patientService.findAll();
        return ResponseEntity.ok(listOfPatient);
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
    @PostMapping(path = "")
    public ResponseEntity<?> addPatient(@Valid @RequestBody Patient patient, BindingResult result) {
        logger.info("Request : Add a new Patient");
        if (!result.hasErrors()) {
            Patient patientAdded = patientService.addNewPatient(patient);
            logger.info("{} {} wad added as a new patient.", patientAdded.getFirstname(), patientAdded.getLastname());

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{lastname}")
                    .buildAndExpand(patientAdded.getLastname())
                    .toUri();

            return ResponseEntity.created(location).body(patientAdded);
        }
        List<FieldError> fieldErrorList = result.getFieldErrors();
        logger.info("Error: The given information are not correct");
        return new ResponseEntity(fieldErrorList, HttpStatus.OK);
    }

    @GetMapping(path ="/dto/{id}")
    public ResponseEntity<PatientDTO> getPatientDTO(@PathVariable("id") int id){
        logger.info("Request : Get patientDTO info with the id : {}", id);
        PatientDTO patientDTOFound = patientService.producePatientDTOFromPatient(id);
        return ResponseEntity.ok(patientDTOFound);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable("id") Integer id, @Valid @RequestBody PatientDTO patientWithUpdatedInfo, BindingResult result){
        logger.info("Request : Update patient with the id : {}",id);
        if(!result.hasErrors()){
            Patient patientUpdated =  patientService.updatePatient(patientWithUpdatedInfo, id);
            logger.info("Patient with the id : {} was updated",id);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{lastname}")
                    .buildAndExpand(patientUpdated.getLastname())
                    .toUri();

            return ResponseEntity.created(location).body(patientUpdated);
        }
        List<FieldError> fieldErrorList = result.getFieldErrors();
        logger.info("Error: The given information are not correct");
        return new ResponseEntity(fieldErrorList, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Patient> deletePatient(@PathVariable("id") Integer id){
        logger.info("Request : Delete patient with the id : {}",id);
        Patient patientDeleted = patientService.deleteById(id);
        logger.info("Patient with the id : {} was deleted",id);
        return ResponseEntity.ok(patientDeleted);
    }
}
