package com.medicoLaboSolutions.backPatient.model.pojo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Data
@Setter
@Getter
@Entity
@Table(name= "patient")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private int patientId;

    @Column(name = "firstname")
    @NotNull
    @Pattern(regexp ="^[a-zA-Z]+$")
    private String firstname;

    @Column(name = "lastname")
    @NotNull
    @Pattern(regexp ="^[a-zA-Z]+$")
    private String lastname;

    @Column(name = "birth_date")
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private Date birthDate;

    @Column(name = "gender")
    @Pattern(regexp ="^[F|M]{1}$")
    @Size(min = 1, max = 1)
    private String gender;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    @Pattern(regexp = "^\\(?([0-9]{3})\\)?[-]([0-9]{3})[-]([0-9]{4})$")
    private String phoneNumber;

    public Patient(String firstname, String lastname, Date birthDate, String gender) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public Patient(String firstname, String lastname, Date birthDate, String gender, String address, String phoneNumber) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
