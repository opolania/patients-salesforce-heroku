package com.kar.patients.facade;

import com.kar.patients.entities.Patient;
import com.kar.patients.salesforce.SalesforceAPI;
import com.kar.patients.salesforce.VO.SF_Contact_VO;
import com.kar.patients.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

 @Controller
 @RequestMapping("/sendUsers")
public class SalesforceTransferFacade {

    @Autowired
    @Qualifier("patientServiceImpl")
    private PatientService patientService;

    public void sendContactsToSalesfoce() throws Exception {
        List<Patient> patients = patientService.listAllPatients();
        System.out.println("cantidad de pacientes:"+patients.size());
        String csvContent = getCSVContent(this.getSaleforceContacts(patients));
        System.out.println(csvContent);
        SalesforceAPI salesforceAPI = new SalesforceAPI();
        String accessToken = salesforceAPI.getAuthToken();
        String batchId = salesforceAPI.createBatch(accessToken,"Contact");
        salesforceAPI.uploadBatchData(accessToken,batchId,csvContent);
        salesforceAPI.completeBatch(accessToken,batchId);

    }

    public List<SF_Contact_VO> getSaleforceContacts(List<Patient> patients){
        List<SF_Contact_VO> sf_contacts = new ArrayList<>();
        for(Patient patient :patients){
            SF_Contact_VO contact_vo = new SF_Contact_VO();
            contact_vo.setFirstName(patient.getName());
            contact_vo.setLastName(patient.getLastName());
            sf_contacts.add(contact_vo);
        }
        return sf_contacts;
    }

    public String getCSVContent (List<SF_Contact_VO>sf_contact_vos){
        String heaaders = "FIRSTNAME,LASTNAME\n";
        String content = "";
        for(SF_Contact_VO vo: sf_contact_vos){
            content += vo.toStringCSVFormat();
        }
        return  heaaders+content;
    }
}
