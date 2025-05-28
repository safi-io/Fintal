package main.java.bankManagementSystem.utils;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import main.java.bankManagementSystem.controller.CustomerDashboard.AccountController;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

public class StripePaymentHandler {

    private final String priceId = "price_1RSatp8bnhoi4jgtfTZdXw2U"; // Hardcoded price ID
    private final String accountNumber;
    private final String stripeApiKey;
    private final Runnable onSuccessCallback;

    private final AccountController accountController;

    public StripePaymentHandler(String accountNumber, Runnable onSuccessCallback) {
        this.accountNumber = accountNumber;
        this.onSuccessCallback = onSuccessCallback;
        this.accountController = new AccountController();
        this.stripeApiKey = loadStripeSecretKey(); // ✅ Assign stripe key here
    }

    private String loadStripeSecretKey() {
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

        return props.getProperty("stripe.secret.key");
    }


    public void initiatePayment() {
        try {
            if (stripeApiKey == null || stripeApiKey.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Stripe API key is missing.");
                return;
            }

            Stripe.apiKey = stripeApiKey;

            SessionCreateParams params = SessionCreateParams.builder().addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD).setMode(SessionCreateParams.Mode.PAYMENT).setSuccessUrl("https://example.com/success?session_id={CHECKOUT_SESSION_ID}").setCancelUrl("https://example.com/cancel").putMetadata("user_id", accountNumber).addLineItem(SessionCreateParams.LineItem.builder().setPrice(priceId).setQuantity(1L).build()).build();

            Session session = Session.create(params);
            String sessionId = session.getId();
            String checkoutUrl = session.getUrl();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(checkoutUrl));
            } else {
                JOptionPane.showMessageDialog(null, "Open this URL manually:\n" + checkoutUrl);
            }

            // Background thread to poll payment status
            new Thread(() -> pollPaymentStatus(sessionId)).start();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Payment initiation failed: " + ex.getMessage());
        }
    }

    private void pollPaymentStatus(String sessionId) {
        final int timeoutMillis = 60 * 1000; // 1 minute
        final int intervalMillis = 5000;
        long startTime = System.currentTimeMillis();

        try {
            while (true) {
                Session session = Session.retrieve(sessionId);
                String paymentStatus = session.getPaymentStatus();
                String sessionStatus = session.getStatus();


                if ("paid".equals(paymentStatus) || "complete".equals(sessionStatus)) {
                    boolean ok = accountController.handleAddStripeAmount(accountNumber);
                    if (ok) {
                        JOptionPane.showMessageDialog(null, "Payment Successful! Check Dashboard for new Balance!");

                        if (onSuccessCallback != null) {
                            SwingUtilities.invokeLater(onSuccessCallback); // ✅ Refresh dashboard
                        }
                    }
                    break;
                }

                if ("expired".equals(sessionStatus) || "canceled".equals(sessionStatus)) {
                    JOptionPane.showMessageDialog(null, "Payment Failed or Cancelled.");
                    break;
                }

                if (System.currentTimeMillis() - startTime > timeoutMillis) {
                    JOptionPane.showMessageDialog(null, "Payment timed out. Please try again.");
                    break;
                }

                Thread.sleep(intervalMillis);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error polling payment status: " + e.getMessage());
        }
    }
}
