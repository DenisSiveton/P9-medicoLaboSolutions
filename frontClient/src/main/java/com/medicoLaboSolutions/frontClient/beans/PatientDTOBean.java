package com.medicoLaboSolutions.frontClient.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTOBean {

    private int patientId;
    private String firstname;
    private String lastname;
    private String address;
    private String phoneNumber;

    @Override
    public String toString() {
        return "PatientDTOBean{" +
                "patientId=" + patientId +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
