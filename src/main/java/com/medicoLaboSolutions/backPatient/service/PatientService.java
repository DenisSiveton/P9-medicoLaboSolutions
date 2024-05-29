package com.medicoLaboSolutions.backPatient.service;

import com.medicoLaboSolutions.backPatient.model.Patient;
import com.medicoLaboSolutions.backPatient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;


    public Optional<Patient> findPatientByLastname(String surname) {
        return patientRepository.findByLastname(surname);
    }

    public Iterable<Patient> findAll() {
        return patientRepository.findAll();
    }
}
