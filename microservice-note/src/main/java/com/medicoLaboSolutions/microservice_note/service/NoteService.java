package com.medicoLaboSolutions.microservice_note.service;

import com.medicoLaboSolutions.microservice_note.exceptions.NoteNotFoundException;
import com.medicoLaboSolutions.microservice_note.model.Note;
import com.medicoLaboSolutions.microservice_note.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public List<Note> findPatientListOfNotes (String patientId){
        return noteRepository.findByPatId(patientId);
    }

    public Note addNewNote(Note note) {
        return noteRepository.insert(note);
    }
}
