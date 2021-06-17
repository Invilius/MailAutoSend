package com.github.invilius.service;

import com.github.invilius.model.CompanyDetails;
import org.testng.annotations.Test;

import java.util.List;

public class InformationCollectorTest {

    //Some test to check the functionality of parsePbhCompaniesDetails() method.

    @Test
    void parsePbhCompaniesDetailsTest() {
        InformationCollector infoCollect = new InformationCollector();
        List<CompanyDetails> pbhCompanyDetailsTest = infoCollect.parsePbhCompaniesDetails();
        pbhCompanyDetailsTest.stream().forEach(obj -> System.out.println(obj));
    }
}