package com.kar.patients;

import com.kar.patients.entities.Patient;
import com.kar.patients.facade.SalesforceTransferService;
import com.kar.patients.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class PatientsApplication {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private SalesforceTransferService salesforceTransferService;

    @PostConstruct
    public void init() {
        List<Patient> patients = new ArrayList();
        patients.add(new Patient(1,"Carolina","Lopez",""));
        patients.add(new Patient(2,"Lorena","Lopez",""));
        patientRepository.saveAll(patients);
        try {
            salesforceTransferService.sendContactsToSalesforce();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        SpringApplication.run(PatientsApplication.class, args);
    }

}
