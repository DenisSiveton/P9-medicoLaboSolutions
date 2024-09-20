package com.medicoLaboSolutions.frontClient.proxies;

import com.medicoLaboSolutions.frontClient.beans.DiagnosticAnalysisBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(contextId = "ms-diagnostic",name ="spring-cloud-gateway", url = "localhost:9000")
public interface MicroserviceDiagnosticProxy {

    @GetMapping( value = "/diagnostics/{id}", produces = "application/json")
    ResponseEntity<DiagnosticAnalysisBean> getDiagnostic(@PathVariable("id") int id);

}
