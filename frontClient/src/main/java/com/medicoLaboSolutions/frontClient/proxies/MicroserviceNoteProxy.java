package com.medicoLaboSolutions.frontClient.proxies;

import com.medicoLaboSolutions.frontClient.beans.NoteBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(contextId = "ms-note",name ="spring-cloud-gateway", url ="${proxy.gateway.name}" + ":" + "${proxy.gateway.port}")
public interface MicroserviceNoteProxy {

    @GetMapping( value = "/notes/{id}")
    ResponseEntity<List<NoteBean>> getListNotesAboutPatient(@PathVariable("id") int id);

    @PostMapping(value = "/notes")
    void addNote(@RequestBody NoteBean noteBean);
}
