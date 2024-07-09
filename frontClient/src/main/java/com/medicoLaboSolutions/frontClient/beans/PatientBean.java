package com.medicoLaboSolutions.frontClient.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientBean {
    private int patientId;
    private String firstname;
    private String lastname;
    private Date birthDate;
    private String gender;
    private String address;
    private String phoneNumber;

    @Override
    public String toString() {
        return "PatientBean{" +
                "patientId=" + patientId +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", birthDate=" + birthDate +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
