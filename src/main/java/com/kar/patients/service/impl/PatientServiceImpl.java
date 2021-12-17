package com.kar.patients.service.impl;

import com.kar.patients.entities.Patient;
import com.kar.patients.repository.PatientRepository;
import com.kar.patients.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component("patientServiceImpl")
public class PatientServiceImpl implements PatientService {
    @Autowired
    @Qualifier("patientRepository")
    private PatientRepository patientRepository;
    @Override
    public List<Patient> listAllPatients() {

        return StreamSupport
                .stream(patientRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
}
