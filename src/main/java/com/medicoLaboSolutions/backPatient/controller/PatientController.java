package com.medicoLaboSolutions.backPatient.controller;

import com.medicoLaboSolutions.backPatient.model.Patient;
import com.medicoLaboSolutions.backPatient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PatientController {
    @Autowired
    private PatientService patientService;

    @GetMapping("/patient")
    public String getPatient(Model model, @RequestParam String surname){
        Patient requestedPatient = patientService.findPatientBySurname(surname)
                .orElseThrow(() -> new IllegalArgumentException("No registered patient has this :" + surname));
        model.addAttribute("patient", requestedPatient);
        return "patient";
    }
    @GetMapping("/patients")
    public String getPatients(Model model){
        Iterable<Patient> requestedPatientList = patientService.findAll();
        model.addAttribute("patients", requestedPatientList);
        return "patient/list";
    }
}
