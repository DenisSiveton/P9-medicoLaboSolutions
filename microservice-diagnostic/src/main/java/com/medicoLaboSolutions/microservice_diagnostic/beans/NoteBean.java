package com.medicoLaboSolutions.microservice_diagnostic.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NoteBean {

    private String id;
    private String patId;
    private String patient;
    private String note;

    @Override
    public String toString() {
        return "NoteBean{" +
                "id='" + id + '\'' +
                ", patId='" + patId + '\'' +
                ", patient='" + patient + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
