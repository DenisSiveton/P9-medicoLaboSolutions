package com.medicoLaboSolutions.microservice_note.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException(String s) {super(s);
    }
}