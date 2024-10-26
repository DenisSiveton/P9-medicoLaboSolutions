package com.medicoLaboSolutions.backPatient.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatientDTO {

    @NotNull
    private int patientId;

    @NotEmpty(message = "This field is mandatory")
    @Pattern(regexp ="^[a-zA-Z]+$", message = "Firstname must be alphabetical (contains only the following letters : a-z and A-Z)")
    private String firstname;

    @NotEmpty(message = "The field is mandatory")
    @Pattern(regexp ="^[a-zA-Z]+$", message = "Lastname must be alphabetical (contains only the following letters : a-z and A-Z)")
    private String lastname;

    @NotEmpty(message = "The field is mandatory")
    private String address;

    @NotEmpty(message = "The field is mandatory")
    @Pattern(regexp = "^\\(?([0-9]{3})\\)?[-]([0-9]{3})[-]([0-9]{4})$", message = "The phone number format is the following : XXX-XXX-XXX.\nFor instance a valid phone number is : 012-345-6789.")
    private String phoneNumber;


}
