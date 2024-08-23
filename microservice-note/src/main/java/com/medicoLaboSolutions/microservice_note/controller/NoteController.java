package com.medicoLaboSolutions.microservice_note.controller;

import com.medicoLaboSolutions.microservice_note.model.Note;
import com.medicoLaboSolutions.microservice_note.repository.NoteRepository;
import com.medicoLaboSolutions.microservice_note.service.NoteService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/notes")
public class NoteController {

    private Logger logger = LoggerFactory.getLogger(NoteController.class);

    @Autowired
    private NoteService noteService;

    @GetMapping("/{id}")
    public ResponseEntity<List<Note>> getAllPatientNotes(@PathVariable(value = "id") int patientId){
        logger.info("Retrieve all notes from the patient with the Id {}.", patientId);
        List<Note> listOfNotes = noteService.findPatientListOfNotes(String.valueOf(patientId));
        return ResponseEntity.ok(listOfNotes);
    }

    @PostMapping("")
    public ResponseEntity<Note> addNewNote(@Valid @RequestBody Note note, BindingResult result){
        logger.info("Request : Add a new Note");
        if (!result.hasErrors()) {
            Note noteAdded = noteService.addNewNote(note);
            logger.info("A new note was added to the patient with the Id : {}.", noteAdded.getPatId());

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{lastname}")
                    .buildAndExpand(noteAdded.getPatId())
                    .toUri();

            return ResponseEntity.created(location).body(noteAdded);
        }
        return ResponseEntity.badRequest().build();
    }
}
