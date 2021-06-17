package com.github.invilius.service;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.github.invilius.model.CompanyDetails;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class InformationCollector implements AutoCloseable {

    public InformationCollector() {
        Configuration.browser = "chrome";
    }

    public List<CompanyDetails> getTestData() {
        List<CompanyDetails> testData = new ArrayList<>();

        testData.add(new CompanyDetails("Name of the company", Arrays.asList("mail")));
        testData.add(new CompanyDetails("Name of the company", Arrays.asList("mail", "mail")));

        return testData;
    }

    public List<CompanyDetails> parsePbhCompaniesDetails() {
        Selenide.open("https://www.gov.pl/web/poland-businessharbour-ru/itspecialist");
        List<CompanyDetails> CompanyDetailsList = Selenide.$$x("//div[2]//details").stream()
                .map(elemDetails -> parseDetails(elemDetails.$(By.tagName("summary")), elemDetails.$$(By.tagName("a"))))
                .filter(info -> !info.getCompanyEmails().isEmpty())
                .collect(Collectors.toList());
        return CompanyDetailsList;
    }



    private CompanyDetails parseDetails(SelenideElement summary, ElementsCollection aHrefList) {
        String companyName = summary.text();
        List<String> companyEmails = aHrefList.stream()
                .map(elem -> elem.attr("href"))
                .map(this::getEmailFromHref)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new CompanyDetails(companyName, companyEmails);
    }

    private String getEmailFromHref(String href) {
        Pattern emailPattern = Pattern.compile("[A-Z0-9._%+-]+@[A-Z0-9.-]+.[A-Z]{2,4}", Pattern.CASE_INSENSITIVE);
        Matcher emailMatcher = emailPattern.matcher(href);

        if (emailMatcher.find()) {
            return emailMatcher.group();
        }

        return null;
    }

    @Override
    public void close() {
        try {
            Selenide.closeWebDriver();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
