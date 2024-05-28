package com.medicoLaboSolutions.backPatient.repository;

import com.medicoLaboSolutions.backPatient.model.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Integer> {

}
