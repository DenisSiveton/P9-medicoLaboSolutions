package com.medicoLaboSolutions.microservice_note.controller;

import com.medicoLaboSolutions.microservice_note.model.Note;
import com.medicoLaboSolutions.microservice_note.repository.NoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/notes")
public class NoteController {

    private Logger logger = LoggerFactory.getLogger(NoteController.class);

    @Autowired
    private NoteRepository noteRepository;

    @GetMapping("/{id}")
    public ResponseEntity<List<Note>> getAllPatientNotes(@PathVariable(value = "id") int patientId){
        logger.info("Retrieve all notes from the patient with the Id {}.", patientId);
        List<Note> listOfNotes = noteRepository.findByPatId(String.valueOf(patientId));
        return ResponseEntity.ok(listOfNotes);
    }
}
