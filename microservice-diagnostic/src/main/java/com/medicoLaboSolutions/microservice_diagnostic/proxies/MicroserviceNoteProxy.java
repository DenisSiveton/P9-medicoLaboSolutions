package com.medicoLaboSolutions.microservice_diagnostic.proxies;


import com.medicoLaboSolutions.microservice_diagnostic.beans.NoteBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(contextId = "ms-note",name ="spring-cloud-gateway", url = "${proxy.gateway.name}" + ":" + "${proxy.gateway.port}")
public interface MicroserviceNoteProxy {

    @GetMapping( value = "/notes/{id}")
    ResponseEntity<List<NoteBean>> getListNotesAboutPatient(@PathVariable("id") int id);
}
