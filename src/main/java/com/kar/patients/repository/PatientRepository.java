package com.kar.patients.repository;

import com.kar.patients.entities.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("patientRepository")
public interface PatientRepository extends CrudRepository<Patient,Integer> {
}
