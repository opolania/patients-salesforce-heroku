package com.kar.patients.service;

import com.kar.patients.entities.Patient;

import java.util.List;

public interface PatientService {

    public abstract List<Patient> listAllPatients();
}
