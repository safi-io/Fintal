package main.java.bankManagementSystem.utils;

import com.sendgrid.*;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class emailSender {
    private final String apiKey;

    public emailSender() {
        this.apiKey = loadSendGridApiKey();
    }

    public void emailConfigurations(String toEmail, String subject, String content) {
        Email from = new Email("rana228safi@gmail.com");
        Email to = new Email(toEmail);
        Content emailContent = new Content("text/plain", content);
        Mail mail = new Mail(from, subject, to, emailContent);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);
            System.out.println("Status Code: " + response.getStatusCode());

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                System.out.println("Email sent successfully!");
            } else {
                System.out.println("Failed to send email. Body: " + response.getBody());
            }

        } catch (IOException ex) {
            System.err.println("Error sending email: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private String loadSendGridApiKey() {
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            props.load(fis);
            return props.getProperty("sendgrid.secret.key");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load Send Grid API key.");
            return null;
        }
    }

    public void sendEmail(String toEmail, String subject, String content) {
        emailSender emailService = new emailSender();
        emailService.emailConfigurations(toEmail, subject, content);
    }

}