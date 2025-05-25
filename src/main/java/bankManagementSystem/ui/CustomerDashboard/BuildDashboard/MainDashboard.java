package main.java.bankManagementSystem.ui.CustomerDashboard.BuildDashboard;

import main.java.bankManagementSystem.controller.CustomerDashboard.AccountController;
import main.java.bankManagementSystem.controller.CustomerDashboard.LoanController;
import main.java.bankManagementSystem.model.CustomerAccountBranchModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static main.java.bankManagementSystem.ui.CustomerDashboard.CustomerDashboard.CARD_BG;

public class MainDashboard {

    public static JPanel buildDashboard(String loggedInUser) {

        AccountController accountController = new AccountController();
        LoanController loanController = new LoanController();

        CustomerAccountBranchModel data = accountController.handleGetDashboardData(loggedInUser);

        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(60, 60, 60, 60));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(25, 25, 25, 25);
        g.fill = GridBagConstraints.BOTH;
        g.weightx = g.weighty = 1;

        // 📅 Last Login
        String lastLoginText = (data.getAccountLastLogin() != null)
                ? data.getAccountLastLogin().toString()
                : "First Login!";

        statCard(new Utils.AlphaFadeLabel(lastLoginText, 1000), "Last Login", 28, new Color(50, 50, 50));


        JPanel[] cards = {
                // 💰 Total Balance
                statCard(new Utils.CounterLabel(0, data.getAccountCurrentBalance().doubleValue(), 2500), "Total Balance", 48, new Color(0, 150, 90)),

                // 💳 Account Number
                statCard(new Utils.AlphaFadeLabel(data.getAccountNumber(), 1000), "Account Number", 36, new Color(30, 30, 30)),

                // 🧍 Customer Name
                statCard(new Utils.AlphaFadeLabel(data.getCustomerName().toUpperCase(), 1000), "Customer Name", 28, new Color(50, 50, 50)),

                statCard(new Utils.AlphaFadeLabel(lastLoginText, 1000), "Last Login", 28, new Color(50, 50, 50)),

                // 🏦 Branch
                statCard(new Utils.AlphaFadeLabel(data.getCustomerBranch().toUpperCase(), 1000), "Branch Name", 28, new Color(50, 50, 50)),

                // 📘 Account Type
                statCard(new Utils.AlphaFadeLabel(data.getAccountType().toUpperCase(), 1000), "Account Type", 28, new Color(50, 50, 50)),

                // 🗓️ Opening Date
                statCard(new Utils.AlphaFadeLabel(data.getAccountOpeningDate().toString(), 1000), "Account Opening Date", 28, new Color(50, 50, 50)),

                // 🔴 Outstanding Loan (Added Card)
                statCard(new Utils.CounterLabel(0, -Math.abs(loanController.handleGetOutstandingLoanBalance(data.getAccountNumber()).doubleValue()), // Ensures negative
                        2500), "Outstanding Loan", 40, new Color(180, 0, 0) // Red
                )};

        int row = 0, col = 0;
        for (JPanel c : cards) {
            g.gridy = row;
            g.gridx = col;
            p.add(c, g);
            if (++col == 3) {
                col = 0;
                row++;
            }
        }
        return p;
    }

    private static JPanel statCard(JLabel value, String title, int size, Color color) {
        value.setFont(new Font("SansSerif", Font.BOLD, size));
        value.setHorizontalAlignment(SwingConstants.CENTER);
        value.setForeground(color);

        JLabel sub = new JLabel(title, SwingConstants.CENTER);
        sub.setFont(new Font("SansSerif", Font.PLAIN, 20));
        sub.setForeground(Color.DARK_GRAY);

        JPanel card = new Utils.RoundedPanel(20);
        card.setBackground(CARD_BG);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(25, 25, 25, 25));
        card.add(value, BorderLayout.CENTER);
        card.add(sub, BorderLayout.SOUTH);
        return card;
    }
}
