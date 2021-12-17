package com.kar.patients.entities;

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
//    private String email;
//    private String city;
//    private String language;
//    private String income;
}
