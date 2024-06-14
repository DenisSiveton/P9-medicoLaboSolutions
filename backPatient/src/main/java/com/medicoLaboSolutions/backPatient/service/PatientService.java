package com.medicoLaboSolutions.backPatient.service;

import com.medicoLaboSolutions.backPatient.error_handling.exceptions.PatientNotFoundException;
import com.medicoLaboSolutions.backPatient.model.pojo.Patient;
import com.medicoLaboSolutions.backPatient.model.dto.PatientDTO;
import com.medicoLaboSolutions.backPatient.repository.PatientRepository;
import com.medicoLaboSolutions.backPatient.service.mapper.PatientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientMapper patientMapper;


    public Patient findPatientByLastname(String surname) {
        return patientRepository.findByLastname(surname)
                .orElseThrow(() -> new PatientNotFoundException("Invalid Patient : No patient exists with the surname " + surname + ". Please repeat your request"));
    }

    public Iterable<Patient> findAll() {
        return patientRepository.findAll();
    }


    public Patient addNewPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient updatePatient(PatientDTO patientWithUpdatedInfo, Integer id) {
        Patient patientFromDB = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Invalid Patient Id: No patient exists with the Id " + id + ". Please repeat your request"));
        patientMapper.update(patientFromDB, patientWithUpdatedInfo);
        return patientRepository.save(patientFromDB);
    }

    public Patient deleteById(Integer id) {
        Patient patientToDelete = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Invalid Patient Id: No patient exists with the Id " + id + ". Please repeat your request"));
        patientRepository.delete(patientToDelete);
        return patientToDelete;
    }
}
