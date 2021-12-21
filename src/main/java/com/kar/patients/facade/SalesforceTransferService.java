package com.kar.patients.facade;

import com.kar.patients.entities.Patient;
import com.kar.patients.repository.PatientRepository;
import com.kar.patients.salesforce.SalesforceAPI;
import com.kar.patients.salesforce.VO.SF_Contact_VO;
import com.kar.patients.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("patientservice")
public class SalesforceTransferService {

    @Autowired
    @Qualifier("patientServiceImpl")
    private PatientService patientService;



    public void sendContactsToSalesfoce() throws Exception {
        List<Patient> patients = patientService.listAllPatients();
        System.out.println("number of patients:"+patients.size());
        String csvContent = getCSVContent(this.getSaleforceContacts(patients));
        System.out.println(csvContent);
        SalesforceAPI salesforceAPI = new SalesforceAPI();
        String accessToken = salesforceAPI.getAuthToken();
        String batchId = salesforceAPI.createBatch(accessToken,"Contact");
        salesforceAPI.uploadBatchData(accessToken,batchId,csvContent);
        salesforceAPI.completeBatch(accessToken,batchId);

    }


    private List<SF_Contact_VO> getSaleforceContacts(List<Patient> patients){
        List<SF_Contact_VO> sf_contacts = new ArrayList<>();
        for(Patient patient :patients){
            SF_Contact_VO contact_vo = new SF_Contact_VO();
            contact_vo.setId(patient.getId());
            contact_vo.setFirstName(patient.getName());
            contact_vo.setLastName(patient.getLastName());
            sf_contacts.add(contact_vo);
        }
        return sf_contacts;
    }

    private String getCSVContent (List<SF_Contact_VO>sf_contact_vos){
        String heaaders = "patientsId__c,FIRSTNAME,LASTNAME\n";
        String content = "";
        for(SF_Contact_VO vo: sf_contact_vos){
            content += vo.toStringCSVFormat();
        }
        return  heaaders+content;
    }
}
