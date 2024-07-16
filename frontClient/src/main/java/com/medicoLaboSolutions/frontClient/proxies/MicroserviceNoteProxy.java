package com.medicoLaboSolutions.frontClient.proxies;

import com.medicoLaboSolutions.frontClient.beans.NoteBean;
import com.medicoLaboSolutions.frontClient.beans.PatientBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name ="spring-cloud-gateway", url ="localhost:9000")
@RequestMapping(path = "/notes")
public interface MicroserviceNoteProxy {

    @GetMapping( value = "/{id}")
    ResponseEntity<List<NoteBean>> getListNotesAboutPatient(@PathVariable("id") int id);

    @PostMapping(value = "")
    void addNote(@RequestBody NoteBean noteBean);
}
