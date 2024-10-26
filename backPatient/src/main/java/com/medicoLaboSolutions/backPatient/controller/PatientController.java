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

/**
 * This class serves as a controller for the Patient entity.
 * All CRUD requests are implemented and works with the associated Service and Repository layer.
 */
@RestController
@RequestMapping(path = "/patients")
public class PatientController {

    private Logger logger = LoggerFactory.getLogger(PatientController.class);
    @Autowired
    private PatientService patientService;

    /**
     * This method searches for a specific Patient using an Id number.
     *
     * @param id Id number that serves as an identifier for the database.
     * @return ResponseEntity containing the found Patient
     * @return In case of error : Exception is thrown with specific message
     */
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable("id")  int id){
        logger.info("Retrieve patient info with the Id : {}", id);
        Patient patientFound = patientService.findPatientById(id);
        return ResponseEntity.ok(patientFound);
    }
    /**
     * This method retrieves all the Patients registered in the database.
     * It asks the service layer to get all the data from the repository layer.
     *
     * @return ResponseEntity containing the list of Patients as its Body
     */
    @GetMapping("")
    public ResponseEntity<List<Patient>> getAllPatients(){
        logger.info("Retrieve all patients info.");
        List<Patient> listOfPatient = patientService.findAll();
        return ResponseEntity.ok(listOfPatient);
    }

    /**
     * This method checks if the data from the form are consistent and valid for a new Patient.
     * If the checks pass then the new Patient is added to the database.
     *      It then retrieves the URI for the new Patient and send it as a result.
     * If the checks fail, the controller gathers all the fields error and parse them back as a result.
     *
     * @param patient Entity constructed from the form. It will be added to the database
     * @param result Form result. May contain errors if data don't comply
     * @return ResponseEntity containing the URI and the added Patient
     * @return In case of error : ResponseEntity containing the different fields error
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

    /**
     * This method checks if the Patient with the specified "Id" is present in the database.
     * If present, a patientDTO is created based on the Patient's data and then is returned as a result.
     *
     * @param id Id number that serves as an identifier for the database.
     * @return ResponseEntity containing the data of the patientDTO
     */
    @GetMapping(path ="/dto/{id}")
    public ResponseEntity<PatientDTO> getPatientDTO(@PathVariable("id") int id){
        logger.info("Request : Get patientDTO info with the id : {}", id);
        PatientDTO patientDTOFound = patientService.producePatientDTOFromPatient(id);
        return ResponseEntity.ok(patientDTOFound);
    }

    /**
     * This method updates a specific Patient with the specified "Id" and the updated info sent in the request.
     *
     * @param id Id number that serves as an identifier for the database.
     * @param patientWithUpdatedInfo updated data of the patient that will be updated
     * @return ResponseEntity containing the updated Patient
     * @return In case of error : ResponseEntity containing the different fields error
     */
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

    /**
     * This method searches the Patient with the Id "id" and if present it deletes it.
     *
     * @param id Id number that serves as an identifier for the database.
     * @return ResponseEntity containing the data of the deleted Patient
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Patient> deletePatient(@PathVariable("id") Integer id){
        logger.info("Request : Delete patient with the id : {}",id);
        Patient patientDeleted = patientService.deleteById(id);
        logger.info("Patient with the id : {} was deleted",id);
        return ResponseEntity.ok(patientDeleted);
    }
}
