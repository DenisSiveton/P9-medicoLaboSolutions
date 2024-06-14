package com.medicoLaboSolutions.backPatient.unit_test.repositories;

import com.medicoLaboSolutions.backPatient.model.pojo.Patient;
import com.medicoLaboSolutions.backPatient.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PatientTests {

	@Autowired
	private PatientRepository patientRepository;

	private Patient patientTest;

	@BeforeEach
	public void setupTestData(){
		// Given : Setup object for test
		patientTest = new Patient("firstnameTest", "lastnameTest",new Date(1989, 9,8), "M");
	}

	@Test
	@DisplayName("Test that saves a patient")
	public void givenPatientObject_whenSave_thenReturnSavedPatient() {
		// ACT
		Patient savedPatient = patientRepository.save(patientTest);

		//ASSERT
		assertThat(savedPatient).isNotNull();
		assertThat(savedPatient.getPatientId()).isGreaterThan(0);

	}

	@Test
	@DisplayName("Test that gets a list of patients")
	public void givenPatientList_whenFindAll_thenReturnPatient(){
		// ACT
		List<Patient> patients = StreamSupport.stream(patientRepository.findAll().spliterator(), false).toList();

		// ASSERT
		assertThat(patients).isNotEmpty();
		assertThat(patients.size()).isEqualTo(4);
		assertThat(patients.get(0).getPhoneNumber()).isEqualTo("100-222-3333");
	}

	@Test
	@DisplayName("Test to get Patient by Id")
	public void givenPatientObject_whenFindById_thenReturnPatientObject() {
		// ARRANGE
		patientRepository.save(patientTest);

		// ACT
		Patient getPatient = patientRepository.findById(patientTest.getPatientId()).get();

		// ASSERT
		assertThat(getPatient).isNotNull();
		assertThat(getPatient.getFirstname()).isEqualTo("firstnameTest");
	}

	@Test
	@DisplayName("Test for get Patient update operation")
	public void givenPatientObject_whenUpdate_thenReturnUpdatedPatientObject() {

		// ARRANGE
		patientRepository.save(patientTest);

		// ACT
		Patient getPatient = patientRepository.findById(patientTest.getPatientId()).get();

		getPatient.setFirstname("firstnameUpdated");

		Patient updatedPatient = patientRepository.save(getPatient);

		// ASSERT
		assertThat(updatedPatient).isNotNull();
		assertThat(updatedPatient.getFirstname()).isEqualTo("firstnameUpdated");
	}


	// JUnit test for delete employee operation
	@Test
	@DisplayName("JUnit test for delete employee operation")
	public void givenPatientObject_whenDelete_thenRemovePatient() {

		// ARRANGE

		patientRepository.save(patientTest);

		// ACT
		patientRepository.deleteById(patientTest.getPatientId());
		Optional<Patient> deletePatient = patientRepository.findById(patientTest.getPatientId());

		// ASSERT
		assertThat(deletePatient).isEmpty();
	}
}
