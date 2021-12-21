package com.kar.patients.entities;

import com.kar.patients.salesforce.VO.SF_Contact_VO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Patient {
    @javax.persistence.Id
    private Integer Id;
    private String name;
    private String lastName;
    private String email;

    public static Patient getPatientFromSalesforceVo(SF_Contact_VO contact_vo){

        Patient patient =  new Patient();
        patient.Id = contact_vo.getId();
        patient.name = contact_vo.getFirstName();
        patient.lastName = contact_vo.getLastName();
        patient.email = contact_vo.getEmail();
        return patient;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
