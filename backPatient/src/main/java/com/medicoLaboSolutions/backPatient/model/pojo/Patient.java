package com.medicoLaboSolutions.backPatient.model.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
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
    @NotEmpty(message = "This field is mandatory")
    @Pattern(regexp ="^[a-zA-Z]+$", message = "Firstname must be alphabetical (contains only the following letters : a-z and A-Z")
    private String firstname;

    @Column(name = "lastname")
    @NotEmpty(message = "The field is mandatory")
    @Pattern(regexp ="^[a-zA-Z]+$", message = "Lastname must be alphabetical (contains only the following letters : a-z and A-Z)")
    private String lastname;

    @Column(name = "birth_date")
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    @JsonFormat(lenient= OptBoolean.FALSE)
    private Date birthDate;

    @Column(name = "gender")
    @NotEmpty(message = "The field is mandatory")
    @Pattern(regexp ="^[F|M]{1}$", message = "The gender is either 'F' (for female) or 'M'(for male)")
    @Size(min = 1, max = 1, message = "Enter either 'F' or 'M' for your gender")
    private String gender;

    @Column(name = "address")
    @NotEmpty(message = "The field is mandatory")
    private String address;

    @Column(name = "phone_number")
    @NotEmpty(message = "The field is mandatory")
    @Pattern(regexp = "^\\(?([0-9]{3})\\)?[-]([0-9]{3})[-]([0-9]{4})$",
            message = "The phone number format is the following : XXX-XXX-XXX.\nFor instance a valid phone number is : 012-345-6789.")
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
