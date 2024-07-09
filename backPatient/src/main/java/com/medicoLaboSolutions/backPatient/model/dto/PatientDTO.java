package com.medicoLaboSolutions.backPatient.model.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatientDTO {

    @NotNull
    private int patientId;

    @NotNull
    @Pattern(regexp ="^[a-zA-Z]+$")
    private String firstname;

    @NotNull
    @Pattern(regexp ="^[a-zA-Z]+$")
    private String lastname;

    @NotNull
    private String address;

    @Pattern(regexp = "^\\(?([0-9]{3})\\)?[-]([0-9]{3})[-]([0-9]{4})$")
    private String phoneNumber;


}
