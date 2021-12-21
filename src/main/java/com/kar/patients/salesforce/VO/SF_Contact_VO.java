package com.kar.patients.salesforce.VO;

public class SF_Contact_VO {
    private static final String DELIMETER = ",";
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }





    public String toStringCSVFormat() {
        return new StringBuilder().append(this.id).append(DELIMETER).append(this.firstName).append(DELIMETER).append(this.lastName).append("\n").toString();
    }
}
