package com.medicoLaboSolutions.frontClient.proxies;

import com.medicoLaboSolutions.frontClient.beans.PatientBean;
import com.medicoLaboSolutions.frontClient.beans.PatientDTOBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name ="spring-cloud-gateway", url ="localhost:9000")
public interface MicroservicePatientProxy {

    @GetMapping(value = "/patients", produces = "application/json")
    ResponseEntity<List<PatientBean>> getAllPatients();

    @GetMapping( value = "/patients/{id}")
    ResponseEntity<PatientBean> getPatient(@PathVariable("id") int id);

    @GetMapping( value = "/patients/dto/{id}")
    ResponseEntity<PatientDTOBean> getPatientDTO(@PathVariable("id") int id);

    @PostMapping(value = "/patients")
    void addPatient(@RequestBody PatientBean patientBean);

    @PutMapping(value ="/patients/{id}")
    void updatePatient(@PathVariable("id") int id, @RequestBody PatientDTOBean patientDTOBean);
    @DeleteMapping( value = "/patients/{id}")
    ResponseEntity<PatientBean> deletePatient(@PathVariable("id") int id);



}
