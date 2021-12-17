package com.kar.patients;

import com.kar.patients.entities.Patient;
import com.kar.patients.facade.SalesforceTransferFacade;
import com.kar.patients.repository.PatientRepository;
import com.kar.patients.salesforce.SalesforceAPI;
import com.kar.patients.salesforce.VO.SF_Contact_VO;
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
    private SalesforceTransferFacade salesforceTransferFacade;

    @PostConstruct
    public void init() {
        List<Patient> patients = new ArrayList();
        patients.add(new Patient(1,"Carolina","Lopez"));
        patients.add(new Patient(2,"Lorena","Lopez"));
        patients.add(new Patient(3,"John","Zapata"));
        patients.add(new Patient(4,"Carlos","Polania"));
        patientRepository.saveAll(patients);
        try {
            salesforceTransferFacade.sendContactsToSalesfoce();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        SpringApplication.run(PatientsApplication.class, args);
    }

}
