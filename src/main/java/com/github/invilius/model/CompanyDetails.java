package com.github.invilius.model;

import java.util.List;

public class CompanyDetails {
    private String companyName;
    private List<String> companyEmails;


    public CompanyDetails(String companyName, List<String> companyEmails) {
        this.companyName = companyName;
        this.companyEmails = companyEmails;
    }

    public String getCompanyName() {
        return companyName;
    }

    public List<String> getCompanyEmails() {
        return companyEmails;
    }

    @Override
    public String toString() {
        return "com.github.invilius.model.Company details{" +
                "contactName='" + companyName + '\'' +
                ", contactEmails=" + companyEmails +
                '}';
    }
}
