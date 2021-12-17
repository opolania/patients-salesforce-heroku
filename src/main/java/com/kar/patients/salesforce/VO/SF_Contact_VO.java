package com.kar.patients.salesforce.VO;

public class SF_Contact_VO {
    private static final String DELIMETER = ",";
    private String FirstName;
    private String LastName;

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String toStringCSVFormat(){
        return new StringBuilder().append(this.FirstName).append(",").append(this.LastName).append("\n").toString();
    }
}
