package com.medicoLaboSolutions.frontClient.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DiagnosticAnalysisBean {

    private long patientAge;
    private int sicknessLevel;
    private String sicknessCategory;
    private int numberOfSymptoms;
    private List<String> listOfSymptoms;

    @Override
    public String toString() {
        return "DiagnosticAnalysisBean{" +
                "patientAge=" + patientAge +
                ", sicknessLevel=" + sicknessLevel +
                ", sicknessCategory='" + sicknessCategory + '\'' +
                ", numberOfSymptoms=" + numberOfSymptoms +
                ", listOfSymptoms=" + listOfSymptoms +
                '}';
    }
}
