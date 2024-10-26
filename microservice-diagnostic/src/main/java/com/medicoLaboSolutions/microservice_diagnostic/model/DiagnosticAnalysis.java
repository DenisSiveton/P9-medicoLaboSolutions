package com.medicoLaboSolutions.microservice_diagnostic.model;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticAnalysis {
    private long patientAge;
    private int sicknessLevel;
    private String sicknessCategory;
    private int numberOfSymptoms;
    private List<String> listOfSymptoms;


}
