package com.github.invilius;

import com.github.invilius.model.CompanyDetails;
import com.github.invilius.service.EmailSender;
import com.github.invilius.service.InformationCollector;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class Application {

    public static final String USERNAME = "";
    public static final String PASSWORD = "";
    public static final String SUBJECT = "";
    public static final String MY_EMAIL = "";
    public static final String FOLDER = "filesformail";

    public static void main(String[] args) {
        try (InformationCollector ic = new InformationCollector()) {
            System.out.println("Start application");


            List<CompanyDetails> pbhCompanyDetails = ic.parsePbhCompaniesDetails();

            //For Test purposes I will create my own company to test
            //List<CompanyDetails> pbhCompanyDetails = ic.getTestData();

            Properties prop = new Properties();
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "587");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.starttls.enable", "true");

            EmailSender emailSender = new EmailSender(prop, USERNAME, PASSWORD, SUBJECT, MY_EMAIL);

            for (CompanyDetails company : pbhCompanyDetails) {
                System.out.println("Sending email to " + company.getCompanyName());

                InternetAddress[] sendTo = InternetAddress.parse(String.join(",", company.getCompanyEmails()));

                StringBuilder message = new StringBuilder("Greetings, ");
                message.append(company.getCompanyName())
                        .append("!");

                //You can use HTML here

                emailSender.sendEmail(sendTo, message.toString(), getAllFilesFromResource());

                System.out.println("Email to company " + company.getCompanyName() + " sent successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("All emails sent successfully, close application");
    }

    private static List<File> getAllFilesFromResource() throws URISyntaxException, IOException {
        ClassLoader classLoader = Application.class.getClassLoader();
        URL resource = classLoader.getResource(FOLDER);
        List<File> collect = Files.walk(Paths.get(resource.toURI()))
                .filter(Files::isRegularFile)
                .map(x -> x.toFile())
                .collect(Collectors.toList());

        if (resource == null) {
            throw new IllegalArgumentException("Folder not found! " + FOLDER);
        } else {
            return collect;
        }
    }

}
