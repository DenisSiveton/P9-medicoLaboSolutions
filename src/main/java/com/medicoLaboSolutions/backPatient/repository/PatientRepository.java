package com.medicoLaboSolutions.backPatient.repository;

import com.medicoLaboSolutions.backPatient.model.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Integer> {

    public Optional<Patient> findBySurname(String surname);
}
