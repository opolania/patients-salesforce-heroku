package com.kar.patients.controller;

import com.kar.patients.entities.Patient;
import com.kar.patients.facade.SalesforceTransferService;
import com.kar.patients.repository.PatientRepository;
import com.kar.patients.salesforce.VO.SF_Contact_VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("patientSynchronization")
public class ContactSyncController {

    @Autowired
    private SalesforceTransferService salesforceTransferService;

    @PostMapping
    public List<Patient> transferContactsToSalesforce() throws Exception {
        return salesforceTransferService.sendContactsToSalesforce();
    }
}
