package com.medicoLaboSolutions.backPatient.service.mapper;

import com.medicoLaboSolutions.backPatient.model.dto.PatientDTO;
import com.medicoLaboSolutions.backPatient.model.pojo.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface PatientMapper {

    public void update (@MappingTarget Patient patient, PatientDTO patientDTO);
}
