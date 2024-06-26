package com.medicoLaboSolutions.frontClient.controller;

import com.medicoLaboSolutions.frontClient.beans.PatientBean;
import com.medicoLaboSolutions.frontClient.proxies.MicroservicePatientProxy;
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

    @RequestMapping(path = "/patients/{lastname}")
    public String patientInfo(@PathVariable(value = "lastname") String patientLastname, Model model){
        PatientBean patientsBean =  microservicePatientProxy.getPatient(patientLastname).getBody();
        model.addAttribute("patient", patientsBean);
        return "patient/info";
    }
}
