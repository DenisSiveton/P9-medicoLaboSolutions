package com.medicoLaboSolutions.frontClient.controller;

import com.medicoLaboSolutions.frontClient.beans.PatientBean;
import com.medicoLaboSolutions.frontClient.beans.PatientDTOBean;
import com.medicoLaboSolutions.frontClient.proxies.MicroservicePatientProxy;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ClientController {
    private final MicroservicePatientProxy microservicePatientProxy;

    public ClientController(MicroservicePatientProxy microservicePatientProxy) {
        this.microservicePatientProxy = microservicePatientProxy;
    }

    @RequestMapping(path = "/patients")
    //@ResponseBody
    public String patientList(Model model){
        List<PatientBean> patientsBean =  microservicePatientProxy.getAllPatients().getBody();
        model.addAttribute("patients", patientsBean);
        return "patient/list";
    }

    @RequestMapping(path = "/patients/{id}")
    public String patientInfo(@PathVariable(value = "id") int patientId, Model model){
        PatientBean patientsBean =  microservicePatientProxy.getPatient(patientId).getBody();
        model.addAttribute("patient", patientsBean);
        return "patient/info";
    }

    @RequestMapping(path = "/patients/add")
    public String addPatientShowForm(PatientBean patientBean){
        return "patient/add";
    }

    @RequestMapping(path = "/patients/validate")
    public String addPatient(@Valid PatientBean patientBean){
        try {
            microservicePatientProxy.addPatient(patientBean);
            return "redirect:/patients";
        }catch (RuntimeException runtimeException){
            return "patient/add";
        }
    }

    @RequestMapping(path = "/patients/delete/{patientId}")
    public String deletePatient(@PathVariable(value = "patientId") int id, Model model){
        ResponseEntity<PatientBean> responseEntityFromDelete = microservicePatientProxy.deletePatient(id);
        if(responseEntityFromDelete.getStatusCode() == HttpStatus.OK){
            return "redirect:/patients";
        }else{
            //deal with error
            return "patient/list";
        }
    }


    @RequestMapping(path = "/patients/update/{id}")
    public String updatePatientShowForm(@PathVariable(value = "id") int patientId, Model model){
        PatientDTOBean patientDTOBean =  microservicePatientProxy.getPatientDTO(patientId).getBody();
        model.addAttribute("patient", patientDTOBean);
        return "patient/update";
    }

    @RequestMapping(path = "/patients/update/validate/{id}")
    public String updatePatient(@PathVariable(value = "id") int patientId, @Valid PatientDTOBean patientDTOBean){
        patientDTOBean.setPatientId(patientId);
        microservicePatientProxy.updatePatient(patientId, patientDTOBean);
        return "redirect:/patients";
    }
}
