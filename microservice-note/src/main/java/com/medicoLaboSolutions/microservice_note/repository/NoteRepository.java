package com.medicoLaboSolutions.microservice_note.repository;

import com.medicoLaboSolutions.microservice_note.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends MongoRepository<Note, String > {

    public List<Note> findByPatId(String id);
}
