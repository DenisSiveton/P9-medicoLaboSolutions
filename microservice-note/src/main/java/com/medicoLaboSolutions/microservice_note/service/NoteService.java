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
        List<Note> listOfNotes = noteRepository.findByPatId(patientId);
        if(listOfNotes.size() == 0){
            throw new NoteNotFoundException("Invalid Note : No note exists with the Id " + patientId + ". Please repeat your request with another id");
        }
        else{
            return listOfNotes;
        }
    }

    public Note addNewNote(Note note) {
        return noteRepository.insert(note);
    }
}
