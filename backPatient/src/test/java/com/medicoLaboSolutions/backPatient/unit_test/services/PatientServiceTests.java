package com.medicoLaboSolutions.backPatient.unit_test.services;

import com.medicoLaboSolutions.backPatient.model.dto.PatientDTO;
import com.medicoLaboSolutions.backPatient.model.pojo.Patient;
import com.medicoLaboSolutions.backPatient.repository.PatientRepository;
import com.medicoLaboSolutions.backPatient.service.PatientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class PatientServiceTests {

    @Autowired
    private PatientService patientService;
    @MockBean
    private PatientRepository patientRepository;

    @Test
    public void givenPatientService_whenFindPatientById_thenCorrectMethodMustBeCalled (){
        //ARRANGE
        int patientId = 4;

        Patient patientFound = new Patient(4,"firstnameDeleted", "lastnameDeleted",new Date(2000, 5,1), "M", "300 second street", "888-999-7777");

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patientFound));

        //ACT
        Patient result = patientService.findPatientById(patientId);

        //ASSERT
        assertThat(result.getLastname()).isEqualTo(patientFound.getLastname());
        verify(patientRepository, times(1)).findById(patientId);

    }

    @Test
    public void givenPatientService_whenFindAll_thenReturnCorrectException (){
        //ARRANGE
        String patientId = "4";
        List<Patient> patientListTest = new ArrayList<>();
        patientListTest.add(new Patient("firstnameTest1", "lastnameTest1",new Date(2000, 5,1), "M"));
        patientListTest.add(new Patient("firstnameTest2", "lastnameTest2",new Date(2003, 6,2), "F"));

        String expectedMessage = "Invalid Note : No note exists with the Id 4. Please repeat your request with another id";

        when(patientRepository.findAll()).thenReturn(patientListTest);

        //ACT
        List<Patient> result = patientService.findAll();

        //ASSERT
        assertThat(result.get(1).getLastname()).isEqualTo(patientListTest.get(1).getLastname());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    public void givenPatientService_whenAddNewPatient_thenCorrectMethodMustBeCalled (){
        //ARRANGE
        Patient patientToAdd = new Patient("firstnameTest1", "lastnameTestToAdd",new Date(2000, 5,1), "M");

        when(patientRepository.save(patientToAdd)).thenReturn(patientToAdd);

        //ACT
        Patient result = patientService.addNewPatient(patientToAdd);

        //ASSERT
        assertThat(result.getLastname()).isEqualTo(patientToAdd.getLastname());
        verify(patientRepository, times(1)).save(patientToAdd);
    }

    @Test
    public void givenPatientService_whenUpdatePatient_thenCorrectMethodMustBeCalled (){
        //ARRANGE
        int patientId = 4;
        PatientDTO patientDTO = new PatientDTO(4, "firstNameTestUpdated", "lastNameTestUpdated", "300 second street", "888-999-7777");
        Patient patientToUpdate = new Patient("firstnameTest", "lastnameTest",new Date(2000, 5,1), "M");

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patientToUpdate));
        when(patientRepository.save(patientToUpdate)).thenReturn(patientToUpdate);

        //ACT
        Patient result = patientService.updatePatient(patientDTO, patientId);

        //ASSERT
        assertThat(result.getLastname()).isEqualTo(patientDTO.getLastname());
        assertThat(result.getAddress()).isNotNull();
        verify(patientRepository, times(1)).findById(patientId);
        verify(patientRepository, times(1)).save(patientToUpdate);
    }

    @Test
    public void givenPatientService_whenDeleteById_thenCorrectMethodMustBeCalled (){
        //ARRANGE
        int patientId = 4;
        Patient patientToDelete = new Patient("firstnameTestToDelete", "lastnameTestToDelete",new Date(2000, 5,1), "M");

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patientToDelete));

        //ACT
        Patient result = patientService.deleteById(patientId);

        //ASSERT
        assertThat(result.getLastname()).isEqualTo(patientToDelete.getLastname());
        verify(patientRepository, times(1)).findById(patientId);
        verify(patientRepository, times(1)).delete(patientToDelete);
    }

    @Test
    public void givenPatientService_whenProducePatientDTOFromPatient_thenCorrectMethodMustBeCalled (){
        //ARRANGE
        int patientId = 4;
        Patient patientDataForPatientDTO = new Patient("firstnameTestToDelete", "lastnameTestToDelete",
                new Date(2000, 5,1), "M", "1 test street", "555-444-3333");

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patientDataForPatientDTO));

        //ACT
        PatientDTO result = patientService.producePatientDTOFromPatient(patientId);

        //ASSERT
        assertThat(result.getLastname()).isEqualTo(patientDataForPatientDTO.getLastname());
        verify(patientRepository, times(1)).findById(patientId);
    }
}
