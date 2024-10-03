package com.medicoLaboSolutions.frontClient.controller;

import com.medicoLaboSolutions.frontClient.beans.DiagnosticAnalysisBean;
import com.medicoLaboSolutions.frontClient.beans.NoteBean;
import com.medicoLaboSolutions.frontClient.beans.PatientBean;
import com.medicoLaboSolutions.frontClient.beans.PatientDTOBean;
import com.medicoLaboSolutions.frontClient.proxies.MicroserviceDiagnosticProxy;
import com.medicoLaboSolutions.frontClient.proxies.MicroserviceNoteProxy;
import com.medicoLaboSolutions.frontClient.proxies.MicroservicePatientProxy;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;

@Controller
public class ClientController {

    private Logger logger = LoggerFactory.getLogger(ClientController.class);
    private final MicroservicePatientProxy microservicePatientProxy;

    private final MicroserviceNoteProxy microserviceNoteProxy;

    private final MicroserviceDiagnosticProxy microserviceDiagnosticProxy;

    public ClientController(MicroservicePatientProxy microservicePatientProxy, MicroserviceNoteProxy microserviceNoteProxy, MicroserviceDiagnosticProxy microserviceDiagnosticProxy) {
        this.microservicePatientProxy = microservicePatientProxy;
        this.microserviceNoteProxy = microserviceNoteProxy;
        this.microserviceDiagnosticProxy = microserviceDiagnosticProxy;
    }

    /*

    *****************************
    ***   Method of Patient   ***
    *****************************

     */
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
        List<NoteBean> notesBean = microserviceNoteProxy.getListNotesAboutPatient(patientId).getBody();
        model.addAttribute("notes", notesBean);
        DiagnosticAnalysisBean diagnosticAnalysisBean = microserviceDiagnosticProxy.getDiagnostic(patientId).getBody();
        model.addAttribute("diagnostic", diagnosticAnalysisBean);
        return "patient/info";
    }

    @RequestMapping(path = "/patients/add")
    public String addPatientShowForm(PatientBean patientBean){
        return "patient/add";
    }

    @RequestMapping(path = "/patients/validate")
    public String addPatient(@Valid PatientBean patientBean, Model model){
        ResponseEntity<?> result = microservicePatientProxy.addPatient(patientBean);
        if(result.getBody().toString().contains("patientId")){
            return "redirect:/patients";
        }
        else{
            List<HashMap> fieldErrorList = (List<HashMap>) result.getBody();
            addErrorsToModel(model, fieldErrorList);
            logger.error("Binding result errors in PatientDTO data");
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
    public String updatePatient(@PathVariable(value = "id") int patientId, @Valid PatientDTOBean patientDTOBean, Model model){
        patientDTOBean.setPatientId(patientId);
        ResponseEntity<?> result = microservicePatientProxy.updatePatient(patientId, patientDTOBean);
        if(result.getBody().toString().contains("patientId")){
            logger.info("Binding result has NO errors in PatientDTO data. Patient was successfully added");
            return "redirect:/patients";
        }
        else{
            List<HashMap> fieldErrorList = (List<HashMap>) result.getBody();
            addErrorsToModel(model, fieldErrorList);
            logger.error("Binding result errors in PatientDTO data");
            model.addAttribute("patient", patientDTOBean);
            return "patient/update";
        }
    }

    private void addErrorsToModel(Model model, List<HashMap> fieldErrorList) {
        for (HashMap fieldError : fieldErrorList) {
            model.addAttribute(fieldError.get("field")+"Error", fieldError.get("defaultMessage"));
        }
    }

    /*

     *****************************
     ***     Method of Note    ***
     *****************************

     */

    @RequestMapping(path = "/notes/add")
    public String addPatientShowForm(NoteBean noteBean, Model model){
        model.addAttribute("noteBean", noteBean);
        return "note/add";
    }

    @RequestMapping(path = "/notes/validate")
    public String addPatient(@Valid NoteBean noteBean){
        try {
            microserviceNoteProxy.addNote(noteBean);
            return "redirect:/patients";
        }catch (RuntimeException runtimeException){
            return "note/add";
        }
    }
}
