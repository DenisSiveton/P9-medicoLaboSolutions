package com.medicoLaboSolutions.frontClient.proxies;

import com.medicoLaboSolutions.frontClient.beans.PatientBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name ="backPatient", url ="localhost:9001")
public interface MicroservicePatientProxy {
    @GetMapping(value = "/patients")
    ResponseEntity<Iterable<PatientBean>> getAllPatients();

    @GetMapping( value = "/patients/{lastname}")
    ResponseEntity<PatientBean> getPatient(@PathVariable("lastname") String lastname);
}
