package com.medicoLaboSolutions.microservice_note.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "notes")
public class Note {
    @Id
    private String id;
    private String patId;
    private String patient;
    private String note;

    public Note(String patId, String patient, String note) {
        this.patId = patId;
        this.patient = patient;
        this.note = note;
    }

    public Note() {
    }
}
