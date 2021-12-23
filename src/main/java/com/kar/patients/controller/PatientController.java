package com.kar.patients.controller;

import com.kar.patients.entities.Patient;
import com.kar.patients.facade.SalesforceTransferService;
import com.kar.patients.repository.PatientRepository;
import com.kar.patients.salesforce.VO.SF_Contact_VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("contactManagement")
public class PatientController {
    @Autowired
    private PatientRepository patientRepository;



    @PostMapping
    public List<Patient> createPatients(@RequestBody List<SF_Contact_VO> contacts_vo) {
        List<Patient> patients = new ArrayList<>();
        System.out.println("cantidad pacientes recibidos:"+contacts_vo.size());
        for(SF_Contact_VO contact:contacts_vo){
            System.out.println("contact values:"+contact.toStringCSVFormat());
            Patient patient = Patient.getPatientFromSalesforceVo(contact);
            patients.add(patient);
        }
        return (List<Patient>) patientRepository.saveAll(patients);
    }

    @PutMapping
    public List<Patient> updatePatients(@RequestBody List<SF_Contact_VO> contacts_vo) {
        System.out.println("cantidad pacientes recibidos:"+contacts_vo.size());
        Set<Integer> contactsIds = new HashSet<>();
        Map<Integer,SF_Contact_VO> contactVoById = new Hashtable<>();
        List<Patient> patients = new ArrayList<>();
        for(SF_Contact_VO contact:contacts_vo){
            System.out.println("contact values:"+contact.toStringCSVFormat());
            patients.add(Patient.getPatientFromSalesforceVo(contact));
        }
//        List<Patient> patients = (List<Patient>) patientRepository.findAllById(contactVoById.keySet());
//        List<Patient> modifiedPatients = new ArrayList<>();
//        for(Patient patient:patients){
//            SF_Contact_VO contact_vo = contactVoById.get(patient.getId());
//             patient = Patient.getPatientFromSalesforceVo(contact_vo);
//            System.out.println(patient.toString());
//            modifiedPatients.add(patient);
//        }
        return (List<Patient>) patientRepository.saveAll(patients);
    }


}
