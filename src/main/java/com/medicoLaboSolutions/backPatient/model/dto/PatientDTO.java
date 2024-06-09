package com.medicoLaboSolutions.backPatient.model.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class PatientDTO {
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
