package com.medicoLaboSolutions.backPatient.service;

import com.medicoLaboSolutions.backPatient.error_handling.exceptions.PatientNotFoundException;
import com.medicoLaboSolutions.backPatient.model.pojo.Patient;
import com.medicoLaboSolutions.backPatient.model.dto.PatientDTO;
import com.medicoLaboSolutions.backPatient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public Patient findPatientById(int id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Invalid Patient Id: No patient exists with the Id " + id + ". Please repeat your request"));
    }

    public List<Patient> findAll() {
        List<Patient> patientList = StreamSupport.stream(patientRepository.findAll().spliterator(), false).toList();
        return patientList;
    }


    public Patient addNewPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient updatePatient(PatientDTO patientWithUpdatedInfo, Integer id) {
        Patient patientFromDB = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Invalid Patient Id: No patient exists with the Id " + id + ". Please repeat your request"));
        updatePatientData(patientFromDB, patientWithUpdatedInfo);
        return patientRepository.save(patientFromDB);
    }

    private void updatePatientData(Patient patientFromDB, PatientDTO patientWithUpdatedInfo) {
        patientFromDB.setFirstname( patientWithUpdatedInfo.getFirstname() );
        patientFromDB.setLastname( patientWithUpdatedInfo.getLastname() );
        patientFromDB.setAddress( patientWithUpdatedInfo.getAddress() );
        patientFromDB.setPhoneNumber( patientWithUpdatedInfo.getPhoneNumber() );
    }

    public Patient deleteById(Integer id) {
        Patient patientToDelete = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Invalid Patient Id: No patient exists with the Id " + id + ". Please repeat your request"));
        patientRepository.delete(patientToDelete);
        return patientToDelete;
    }

    public PatientDTO producePatientDTOFromPatient(int id) {
        Patient patientFound = this.findPatientById(id);
        return new PatientDTO(patientFound.getPatientId(), patientFound.getFirstname(), patientFound.getLastname(), patientFound.getAddress(), patientFound.getPhoneNumber());
    }
}
