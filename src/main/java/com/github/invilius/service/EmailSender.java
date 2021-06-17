package com.github.invilius.service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class EmailSender {

    private final Properties emailProperties;
    private final String subject;
    private final String from;
    private final Session session;


    public EmailSender(Properties emailProperties, String username, String password, String subject, String from) {
        this.emailProperties = emailProperties;
        this.subject = subject;
        this.from = from;

        this.session = createSession(username, password);
    }

    private Session createSession(String username, String password) {
        return Session.getInstance(emailProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }



    public void sendEmail(InternetAddress[] sendTo, String message, List<File> attachment) throws MessagingException, IOException {
        Message emailMessage = new MimeMessage(session);
        emailMessage.setFrom(new InternetAddress(from));
        emailMessage.setRecipients(Message.RecipientType.TO, sendTo);
        emailMessage.setSubject(subject);

        MimeBodyPart messagePart = new MimeBodyPart();
        messagePart.setContent(message, "text/html");
        MimeBodyPart attachmentOne = new MimeBodyPart();
        attachmentOne.attachFile(attachment.get(0));
        MimeBodyPart attachmentTwo = new MimeBodyPart();
        attachmentTwo.attachFile(attachment.get(1));

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messagePart);
        multipart.addBodyPart(attachmentOne);
        multipart.addBodyPart(attachmentTwo);
        emailMessage.setContent(multipart);

        Transport.send(emailMessage);
    }


}
