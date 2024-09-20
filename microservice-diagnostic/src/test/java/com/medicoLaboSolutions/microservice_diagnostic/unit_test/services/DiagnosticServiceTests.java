package com.medicoLaboSolutions.microservice_diagnostic.unit_test.services;

import com.medicoLaboSolutions.microservice_diagnostic.beans.NoteBean;
import com.medicoLaboSolutions.microservice_diagnostic.beans.PatientBean;
import com.medicoLaboSolutions.microservice_diagnostic.model.DiagnosticAnalysis;
import com.medicoLaboSolutions.microservice_diagnostic.service.DiagnosticService;
import com.medicoLaboSolutions.microservice_diagnostic.utils.SymptomsUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DiagnosticServiceTests {

    @Autowired
    private DiagnosticService diagnosticService;

    @Autowired
    private SymptomsUtils symptomsUtils;

    private static long millisInYear;

    @BeforeAll
    public void setUpData(){
        millisInYear = (long) (1000*60*60*24*365.25);
    }

    /*

     ***********************************
     ***   Tests for Older patient   ***
     ***********************************

     */

    @Test
    @DisplayName("This test calculate the diagnostic for a +30 years patient with no risks and should return level 1")
    public void whenPatientWithRiskNone_calculateSicknessLevel_ShouldReturnLevelOfSicknessEqualsOne(){
        //ARRANGE
        //Set BirthDate to XX years ago
        Date patientBirthDate = new Date(System.currentTimeMillis()-millisInYear*60);
        //Create Patient with associated Notes
        PatientBean patientToAnalyse = new PatientBean(50, "firstNameTest", "lastNameTest",
                patientBirthDate, "M", "123 main street", "555-888-9988");
        List<NoteBean> noteListOfPatient = new ArrayList<>();
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Aucun risque détectés jusqu'à présent." ));
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Tout va bien. Le patient se sent normal." ));

        //ACT
        DiagnosticAnalysis diagnosticAnalysis = diagnosticService.calculateSicknessLevel(patientToAnalyse, noteListOfPatient);

        //ASSERT
        assertThat(diagnosticAnalysis.getSicknessLevel()).isEqualTo(1);
        assertThat(diagnosticAnalysis.getNumberOfSymptoms()).isEqualTo(0);
    }
    @Test
    @DisplayName("This test calculate the diagnostic for a +30 years patient with a \"Borderline\" profile and should return level 2")
    public void whenPatientWithBorderline_calculateSicknessLevel_ShouldReturnLevelOfSicknessEqualsTwo(){
        //ARRANGE
            //Set BirthDate to XX years ago
        Date patientBirthDate = new Date(System.currentTimeMillis()-millisInYear*60);
            //Create Patient with associated Notes
        PatientBean patientToAnalyse = new PatientBean(50, "firstNameTest", "lastNameTest",
                patientBirthDate, "M", "123 main street", "555-888-9988");
        List<NoteBean> noteListOfPatient = new ArrayList<>();
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Présence de vertiges, d'anticorps le mois dernier." ));
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Le patient est maintenant fumeur." ));

        //ACT
        DiagnosticAnalysis diagnosticAnalysis = diagnosticService.calculateSicknessLevel(patientToAnalyse, noteListOfPatient);

        //ASSERT
        assertThat(diagnosticAnalysis.getSicknessLevel()).isEqualTo(2);
        assertThat(diagnosticAnalysis.getNumberOfSymptoms()).isEqualTo(3);
    }
    @Test
    @DisplayName("This test calculate the diagnostic for a +30 years patient with an \"In Danger\" profile and should return level 3")
    public void whenPatientWithInDanger_calculateSicknessLevel_ShouldReturnLevelOfSicknessEqualsThree(){
        //ARRANGE
        //Set BirthDate to XX years ago
        Date patientBirthDate = new Date(System.currentTimeMillis()-millisInYear*60);
        //Create Patient with associated Notes
        PatientBean patientToAnalyse = new PatientBean(50, "firstNameTest", "lastNameTest",
                patientBirthDate, "M", "123 main street", "555-888-9988");
        List<NoteBean> noteListOfPatient = new ArrayList<>();
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Présence de vertiges, d'anticorps le mois dernier." ));
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Le patient est maintenant fumeur." ));
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Le taux de cholestérol a augmenté ce dernier mois. Prise de poids anormale." ));

        //ACT
        DiagnosticAnalysis diagnosticAnalysis = diagnosticService.calculateSicknessLevel(patientToAnalyse, noteListOfPatient);

        //ASSERT
        assertThat(diagnosticAnalysis.getSicknessLevel()).isEqualTo(3);
        assertThat(diagnosticAnalysis.getNumberOfSymptoms()).isEqualTo(6);
    }
    @Test
    @DisplayName("This test calculate the diagnostic for a +30 years patient with an \"Early Onset\" profile and should return level 4")
    public void whenPatientWithEarlyOnset_calculateSicknessLevel_ShouldReturnLevelOfSicknessEqualsFour(){
        //ARRANGE
        //Set BirthDate to XX years ago
        Date patientBirthDate = new Date(System.currentTimeMillis()-millisInYear*60);
        //Create Patient with associated Notes
        PatientBean patientToAnalyse = new PatientBean(50, "firstNameTest", "lastNameTest",
                patientBirthDate, "M", "123 main street", "555-888-9988");
        List<NoteBean> noteListOfPatient = new ArrayList<>();
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Présence de vertiges, d'anticorps le mois dernier." ));
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Le patient est maintenant fumeur." ));
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Le taux de cholestérol a augmenté ce dernier mois. Prise de poids anormale par rapport à la taille du patient." ));
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Une rechute de la présence d'hémoglobine A1C"));
        //ACT
        DiagnosticAnalysis diagnosticAnalysis = diagnosticService.calculateSicknessLevel(patientToAnalyse, noteListOfPatient);

        //ASSERT
        assertThat(diagnosticAnalysis.getSicknessLevel()).isEqualTo(4);
        assertThat(diagnosticAnalysis.getNumberOfSymptoms()).isGreaterThan(8);
    }

    /*

     ****************************************
     ***   Tests for Young male patient   ***
     ****************************************

     */
    @Test
    @DisplayName("This test calculate the diagnostic for a -30 years male patient with an \"In Danger\" profile and should return level 3")
    public void whenYoungMalePatientWithInDanger_calculateSicknessLevel_ShouldReturnLevelOfSicknessEqualsThree(){
        //ARRANGE
        //Set BirthDate to XX years ago
        Date patientBirthDate = new Date(System.currentTimeMillis()-millisInYear*25);
        //Create Patient with associated Notes
        PatientBean patientToAnalyse = new PatientBean(50, "firstNameTest", "lastNameTest",
                patientBirthDate, "M", "123 main street", "555-888-9988");
        List<NoteBean> noteListOfPatient = new ArrayList<>();
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Présence de vertiges, d'anticorps le mois dernier." ));
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Une rechute de la présence d'hémoglobine A1C"));
        //ACT
        DiagnosticAnalysis diagnosticAnalysis = diagnosticService.calculateSicknessLevel(patientToAnalyse, noteListOfPatient);

        //ASSERT
        assertThat(diagnosticAnalysis.getSicknessLevel()).isEqualTo(3);
        assertThat(diagnosticAnalysis.getNumberOfSymptoms()).isBetween(3,4);
    }

    @Test
    @DisplayName("This test calculate the diagnostic for a -30 years male patient with an \"Early Onset\" profile and should return level 4")
    public void whenYoungMalePatientWithEarlyOnset_calculateSicknessLevel_ShouldReturnLevelOfSicknessEqualsFour(){
        //ARRANGE
        //Set BirthDate to XX years ago
        Date patientBirthDate = new Date(System.currentTimeMillis()-millisInYear*25);
        //Create Patient with associated Notes
        PatientBean patientToAnalyse = new PatientBean(50, "firstNameTest", "lastNameTest",
                patientBirthDate, "M", "123 main street", "555-888-9988");
        List<NoteBean> noteListOfPatient = new ArrayList<>();
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Présence de vertiges, d'anticorps le mois dernier." ));
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Le patient est maintenant fumeur." ));
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Une rechute de la présence d'hémoglobine A1C"));
        //ACT
        DiagnosticAnalysis diagnosticAnalysis = diagnosticService.calculateSicknessLevel(patientToAnalyse, noteListOfPatient);

        //ASSERT
        assertThat(diagnosticAnalysis.getSicknessLevel()).isEqualTo(4);
        assertThat(diagnosticAnalysis.getNumberOfSymptoms()).isGreaterThanOrEqualTo(5);
    }

    /*

     ******************************************
     ***   Tests for Young female patient   ***
     ******************************************

     */

    @Test
    @DisplayName("This test calculate the diagnostic for a -30 years female patient with an \"In Danger\" profile and should return level 3")
    public void whenYoungFemalePatientWithInDanger_calculateSicknessLevel_ShouldReturnLevelOfSicknessEqualsThree(){
        //ARRANGE
        //Set BirthDate to XX years ago
        Date patientBirthDate = new Date(System.currentTimeMillis()-millisInYear*25);
        //Create Patient with associated Notes
        PatientBean patientToAnalyse = new PatientBean(50, "firstNameTest", "lastNameTest",
                patientBirthDate, "F", "123 main street", "555-888-9988");
        List<NoteBean> noteListOfPatient = new ArrayList<>();
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Présence de vertiges, d'anticorps le mois dernier." ));
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Le patient est maintenant fumeur." ));
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Une rechute de la présence d'hémoglobine A1C"));
        //ACT
        DiagnosticAnalysis diagnosticAnalysis = diagnosticService.calculateSicknessLevel(patientToAnalyse, noteListOfPatient);

        //ASSERT
        assertThat(diagnosticAnalysis.getSicknessLevel()).isEqualTo(3);
        assertThat(diagnosticAnalysis.getNumberOfSymptoms()).isBetween(4,6);
    }

    @Test
    @DisplayName("This test calculate the diagnostic for a -30 years female patient with an \"Early Onset\" profile and should return level 4")
    public void whenYoungFemalePatientWithEarlyOnset_calculateSicknessLevel_ShouldReturnLevelOfSicknessEqualsFour(){
        //ARRANGE
        //Set BirthDate to XX years ago
        Date patientBirthDate = new Date(System.currentTimeMillis()-millisInYear*25);
        //Create Patient with associated Notes
        PatientBean patientToAnalyse = new PatientBean(50, "firstNameTest", "lastNameTest",
                patientBirthDate, "F", "123 main street", "555-888-9988");
        List<NoteBean> noteListOfPatient = new ArrayList<>();
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Présence de vertiges, d'anticorps le mois dernier." ));
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Le patient est maintenant fumeur." ));
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Le taux de cholestérol a augmenté ce dernier mois. Prise de poids anormale par rapport à la taille du patient." ));
        noteListOfPatient.add(new NoteBean("1", "50", "lastNameTest", "Une rechute de la présence d'hémoglobine A1C"));
        //ACT
        DiagnosticAnalysis diagnosticAnalysis = diagnosticService.calculateSicknessLevel(patientToAnalyse, noteListOfPatient);

        //ASSERT
        assertThat(diagnosticAnalysis.getSicknessLevel()).isEqualTo(4);
        assertThat(diagnosticAnalysis.getNumberOfSymptoms()).isGreaterThanOrEqualTo(7);
    }
}
