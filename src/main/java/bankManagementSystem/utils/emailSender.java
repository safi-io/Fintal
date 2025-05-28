package main.java.bankManagementSystem.utils;

import com.sendgrid.*;

import java.io.IOException;
import java.io.InputStream;
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
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("Unable to find config.properties");
                return null;
            }
            props.load(input);
        } catch (IOException e) {
            System.err.println("Error loading config.properties: " + e.getMessage());
            return null;
        }

        return props.getProperty("sendgrid.secret.key");
    }

    public void sendEmail(String toEmail, String subject, String content) {
        emailSender emailService = new emailSender();
        emailService.emailConfigurations(toEmail, subject, content);
    }

}