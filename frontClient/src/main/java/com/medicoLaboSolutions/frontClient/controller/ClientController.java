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

/**
 * This class serves as a main controller of the whole application.
 * It serves as a request hub since all requests will go through the frontClient controller
 *
 * It is linked with the controller of all microservices:
 *  - microservice-patient
 *  - microservice-note
 *  - microservice-diagnostic
 *
 * The request are divided as different categories:
 *  - patient related requests (CRUD, Create and Update forms)
 *  - note related request (CR of CRUD, Create form)
 *  - diagnostic related request
 *
 * @version 1.0
 */
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

    /**
     * This method searches for every Patient registered to the database.
     *
     * @return html page with Model containing relevant data
     * @return In case of error : Exception is thrown with specific message
     */
    @RequestMapping(path = "/patients")
    //@ResponseBody
    public String patientList(Model model){
        List<PatientBean> patientsBean =  microservicePatientProxy.getAllPatients().getBody();
        model.addAttribute("patients", patientsBean);
        return "patient/list";
    }

    /**
     * This method searches for a specific Patient using an Id number.
     * It will retrieve all the information regarding a patient and list all its data (information, notes and associated diagnostic).
     *
     * @param patientId Id number that serves as an identifier for the database.
     * @return html page with Model containing relevant data
     * @return In case of error : Exception is thrown with specific message
     */
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

    /**
     * This method displays a form to add a new Patient.
     *
     * @return html page with the Patient add form
     */
    @RequestMapping(path = "/patients/add")
    public String addPatientShowForm(PatientBean patientBean){
        return "patient/add";
    }

    /**
     * This method checks if the data for the Patient are valid.
     * If so then the new Patient is saved in the database and .
     * Otherwise, all the errors are shown to the user on the form.
     *
     * @param patientBean patient data that will be checked.
     * @return html page with updated list of Patients.
     * @return In case of error : Form is updated with error messages.
     */
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
    /**
     * This method deletes the Patient whose Id matches the given Id.
     *
     * @param id Id of the Patient meant to be deleted.
     *
     * @return html page with updated list of Patients.
     */
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

    /**
     * This method displays a form to update an existing Patient.
     *
     * @return html page with the Patient update form
     */
    @RequestMapping(path = "/patients/update/{id}")
    public String updatePatientShowForm(@PathVariable(value = "id") int patientId, Model model){
        PatientDTOBean patientDTOBean =  microservicePatientProxy.getPatientDTO(patientId).getBody();
        model.addAttribute("patient", patientDTOBean);
        return "patient/update";
    }

    /**
     * This method checks if the updated data for the existing Patient are valid.
     * If so then the Patient is updated in the database.
     * Otherwise, all the errors are shown to the user on the form.
     *
     * @param patientDTOBean patient data that will be checked for update.
     * @param patientId Id of the existing Patient.
     * @return html page with updated list of Patients.
     * @return In case of error : Form is updated with error messages.
     */
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

    /**
     * This method displays a form to add a new Note.
     *
     * @return html page with the Patient update form
     */
    @RequestMapping(path = "/notes/add")
    public String addPatientShowForm(NoteBean noteBean, Model model){
        model.addAttribute("noteBean", noteBean);
        return "note/add";
    }

    /**
     * This method checks if the data for the new Note are valid.
     * If so then the new Note is added in the database.
     * Otherwise, all the errors are shown to the user on the form.
     *
     * @param noteBean Note data that will be checked.
     * @return html page with updated Patient info page.
     * @return In case of error : Form is updated with error messages.
     */
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
