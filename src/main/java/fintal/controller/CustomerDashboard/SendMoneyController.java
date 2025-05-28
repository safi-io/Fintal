package main.java.fintal.controller.CustomerDashboard;

import main.java.fintal.dao.CustomerDashborad.TransactionsDAO;
import main.java.fintal.ui.CustomerDashboard.Transactions.SendMoney;

import javax.swing.*;
import java.math.BigDecimal;

/**
 * Controller handles business logic between UI and DAO.
 */
public class SendMoneyController {

    private final String senderAcc;
    private final SendMoney ui;

    private final AccountController accountCtrl = new AccountController();
    private final TransactionsDAO txDao = new TransactionsDAO();

    public SendMoneyController(String loggedInAcc, SendMoney ui) {
        this.senderAcc = loggedInAcc;
        this.ui = ui;
    }

    public void lookupRecipient(String recipientAcc) {
        ui.setRecipientName("—");
        if (recipientAcc.isEmpty()) {
            ui.setStatus("Enter recipient account number.", true);
            return;
        }
        if (recipientAcc.equals(senderAcc)) {
            ui.setStatus("You can't send money to your own account.", true);
            return;
        }

        SwingWorker<String, Void> worker = new SwingWorker<>() {

            @Override
            protected String doInBackground() {
                return accountCtrl.handleGetCustomerNameUsingAccountNumber(recipientAcc);
            }

            @Override
            protected void done() {
                try {
                    String name = get();
                    if (name == null) {
                        ui.setRecipientName(null);
                        ui.setStatus("Account not found.", true);
                    } else {
                        ui.setRecipientName(name);
                        ui.setStatus("Account verified.", false);
                    }
                } catch (Exception e) {
                    ui.setStatus("Lookup failed: " + e.getMessage(), true);
                }
            }
        };
        worker.execute();
    }

    public void doTransfer(String recipientAcc, String amountStr, String note) {
        if (ui.recipNameLbl.getText().equals("—")) {   // package‑private access or expose getter
            ui.setStatus("Verify recipient first.", true);
            return;
        }
        if (recipientAcc.isEmpty() || amountStr.isEmpty()) {
            ui.setStatus("Recipient and amount required.", true);
            return;
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            ui.setStatus("Invalid amount.", true);
            return;
        }

        ui.setSendEnabled(false);

        SwingWorker<Long, Void> worker = new SwingWorker<>() {
            @Override
            protected Long doInBackground() throws Exception {
                return txDao.transferMoney(
                        senderAcc, recipientAcc,
                        amount, note.isBlank() ? "Transfer" : note);
            }

            @Override
            protected void done() {
                ui.setSendEnabled(true);
                try {
                    long txnId = get(); // success
                    ui.setStatus("Transfer successful!", false);
                    // 3. Show receipt
                    String html = "<html>"
                            + "<h2 style='margin-top:0; color:#2E86C1;'>Transfer Receipt</h2>"
                            + "<div style='font-family:Arial, sans-serif; font-size:14px; color:#34495E;'>"
                            + "<p><b>Transaction ID:</b> " + txnId + "</p>"
                            + "<p><b>From Account Number:</b> " + senderAcc + "</p>"
                            + "<p><b>To Account Number:</b> " + recipientAcc + "</p>"
                            + "<p><b>Receiver Name:</b> " + ui.recipNameLbl.getText() + "</p>"
                            + "<p><b>Amount:</b> PKR " + amount + "</p>"
                            + "<p><b>Note:</b> " + note + "</p>"
                            + "<p><b>Time:</b> " + java.time.LocalDateTime.now() + "</p>"
                            + "</div>"
                            + "</html>";
                    JOptionPane.showMessageDialog(null, html,
                            "Receipt", JOptionPane.INFORMATION_MESSAGE);
                    // 4. clear UI inputs
                    ui.clearFields();
                } catch (Exception e) {
                    ui.setStatus("Transfer failed: " + e.getMessage(), true);
                }
            }
        };
        worker.execute();
    }
}
