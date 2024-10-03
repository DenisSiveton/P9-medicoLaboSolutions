package com.medicoLaboSolutions.microservice_diagnostic.service;

import com.medicoLaboSolutions.microservice_diagnostic.beans.NoteBean;
import com.medicoLaboSolutions.microservice_diagnostic.beans.PatientBean;
import com.medicoLaboSolutions.microservice_diagnostic.model.DiagnosticAnalysis;
import com.medicoLaboSolutions.microservice_diagnostic.utils.SymptomsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class DiagnosticService {

    @Autowired
    private SymptomsUtils symptomsUtils;
    public DiagnosticAnalysis calculateSicknessLevel(PatientBean patientToAnalyse, List<NoteBean> listOfNotes) {
        //logic analysis
            // Calculate patient's age
        long patientAge = calculateAgePatient(patientToAnalyse.getBirthDate());

            // List all the Symptoms present in the notes of the patient
        List<String> listOfSymptoms = calculateNumberOfSymptoms(listOfNotes, symptomsUtils.getListOfSymptoms());

            // Create the diagnostic analysis data with the result
        return createNewDiagnostic(patientAge, listOfSymptoms, patientToAnalyse.getGender());
    }

    private long calculateAgePatient(Date birthDate) {
        LocalDate birthLocalDate = birthDate.toLocalDate();
        LocalDate currentDate = LocalDate.now();
        return ChronoUnit.YEARS.between(birthLocalDate, currentDate);
    }

    private List<String> calculateNumberOfSymptoms(List<NoteBean> listOfNotes, List<String> listOfSymptoms) {
        if(listOfSymptoms.isEmpty()){
            return null;
        }
        else{
            List<String> listSymptomPresent = new ArrayList<>();
            for (NoteBean note : listOfNotes) {
                String noteContent = note.getNote();
                try{
                    ExecutorService executorService = Executors.newCachedThreadPool();
                    for(String symptom : listOfSymptoms) {
                        Runnable runnableTask = () ->{
                            checkIfSymptomIsPresentInNote(symptom, noteContent, listSymptomPresent);
                        };
                        executorService.execute(runnableTask);
                    }
                    executorService.shutdown();
                    executorService.awaitTermination(5, TimeUnit.MINUTES);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return listSymptomPresent;
        }
    }

    private static void checkIfSymptomIsPresentInNote(String symptom, String noteContent, List<String> listSymptomPresent) {
        String noteLowerCase = noteContent.toLowerCase();
        String symptomLowerCase = symptom.toLowerCase();
        if (noteLowerCase.contains(symptomLowerCase) && !listSymptomPresent.contains(symptomLowerCase)){
            listSymptomPresent.add(symptomLowerCase);
        }
    }

    private DiagnosticAnalysis createNewDiagnostic(long patientAge, List<String> listOfSymptoms, String gender) {
        int numberOfSymptoms = listOfSymptoms.size();
        int sicknessLevel = 1;
        if (patientAge > 30) {
            if(numberOfSymptoms >= 2 && numberOfSymptoms <= 5) {
                sicknessLevel = 2;
            }
            else if(numberOfSymptoms >= 6 && numberOfSymptoms <= 7) {
                sicknessLevel = 3;
            }
            else if(numberOfSymptoms >= 8) {
                sicknessLevel = 4;
            }
        } else if (patientAge <= 30){
            if(gender.contains("M")){
                if(numberOfSymptoms >= 3 && numberOfSymptoms <= 4) {
                    sicknessLevel = 3;
                }
                else if(numberOfSymptoms >= 5) {
                    sicknessLevel = 4;
                }
            }
            else if(gender.contains("F")){
                if(numberOfSymptoms >= 4 && numberOfSymptoms <= 6) {
                    sicknessLevel = 3;
                }
                else if(numberOfSymptoms >= 7) {
                    sicknessLevel = 4;
                }
            }
        }
        String sicknessCategory = assignCategory(sicknessLevel);
        return new DiagnosticAnalysis(patientAge, sicknessLevel, sicknessCategory, numberOfSymptoms, listOfSymptoms);
    }

    private String assignCategory(int sicknessLevel) {
        String sicknessCategory = "";
        switch (sicknessLevel){
            case 1:
                sicknessCategory = "No Risks";
                break;
            case 2:
                sicknessCategory = "Borderline";
                break;
            case 3:
                sicknessCategory = "In Danger";
                break;
            case 4:
                sicknessCategory = "Early Onset";
                break;
        }
        return sicknessCategory;
    }
}