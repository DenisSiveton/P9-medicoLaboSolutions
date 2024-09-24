package com.medicoLaboSolutions.microservice_diagnostic.proxies;


import com.medicoLaboSolutions.microservice_diagnostic.beans.PatientBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(contextId = "ms-patient",name ="spring-cloud-gateway", url = "spring-cloud-gateway:9000")
public interface MicroservicePatientProxy {

    @GetMapping( value = "/patients/{id}")
    ResponseEntity<PatientBean> getPatient(@PathVariable("id") int id);
}
