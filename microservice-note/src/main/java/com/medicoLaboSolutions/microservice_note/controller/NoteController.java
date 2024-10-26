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

/**
 * This class serves as a controller for the Note entity.
 * Only the "Create" and "Read" requests (from CRUD) are implemented and works with the associated Service and Repository layer.
 */
@RestController
@RequestMapping(path = "/notes")
public class NoteController {

    private Logger logger = LoggerFactory.getLogger(NoteController.class);

    @Autowired
    private NoteService noteService;

    /**
     * This method retrieves all the notes associated with a specific Patient
     * It asks the service layer to get all the data from the repository layer.
     *
     * @param patientId the Id of the patient whose notes are requested.
     * @return ResponseEntity containing the list of Notes as its Body
     */
    @GetMapping("/{id}")
    public ResponseEntity<List<Note>> getAllPatientNotes(@PathVariable(value = "id") int patientId){
        logger.info("Retrieve all notes from the patient with the Id {}.", patientId);
        List<Note> listOfNotes = noteService.findPatientListOfNotes(String.valueOf(patientId));
        return ResponseEntity.ok(listOfNotes);
    }

    /**
     * This method checks if the data from the form are consistent and valid for a new Note.
     * If the checks pass then the new Note is added to the database.
     *      It then retrieves the URI for the new Note and send it as a result.
     * If the checks fail, the controller gathers all the fields error and parse them back as a result.
     *
     * @param note Entity constructed from the form. It will be added to the database
     * @param result Form result. May contain errors if data don't comply
     * @return ResponseEntity containing the URI and the added Note
     * @return In case of error : ResponseEntity containing the different fields error
     */
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
