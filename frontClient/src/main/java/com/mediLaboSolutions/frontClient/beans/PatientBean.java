package com.mediLaboSolutions.frontClient.beans;

import lombok.*;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatientBean {
    private int patientId;
    private String firstname;
    private String lastname;
    private Date birthDate;
    private String gender;
    private String address;
    private String phoneNumber;

    public PatientBean(String firstname, String lastname, Date birthDate, String gender) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public PatientBean(String firstname, String lastname, Date birthDate, String gender, String address, String phoneNumber) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
