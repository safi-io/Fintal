package main.java.bankManagementSystem.ui.BeneficiaryDashboard.BuildDashboard;

import main.java.bankManagementSystem.controller.BeneficiaryDashboard.DashboardController;
import main.java.bankManagementSystem.model.CustomerAccountBillModel;
import main.java.bankManagementSystem.ui.CustomerDashboard.BuildDashboard.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import static main.java.bankManagementSystem.ui.BeneficiaryDashboard.BeneficiaryDashboard.CARD_BG;


public class MainDashboard {
    private DashboardController dashboardController;

    public JPanel buildDashboard(String beneficiaryAccountNumber) {
        this.dashboardController = new DashboardController();
        CustomerAccountBillModel data = dashboardController.dashboardData(beneficiaryAccountNumber);

        // ── Dummy data ───────────────────────────────────────────────
        BigDecimal totalUnpaidAmount = data.getUnPaidAmount();
        BigDecimal totalPaidAmount = data.getPaidAmount();
        int unpaidCount = data.getUnPaidCount();
        int paidCount = data.getPaidCount();
        LocalDate lastLoginDate = data.getLastLogin();
        String beneficiaryName = data.getBeneficiaryName();
        String accountNumber = data.getBeneficiaryAccountNumber();
        // ─────────────────────────────────────────────────────────────

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(60, 60, 60, 60));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(25, 25, 25, 25);
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1;
        gc.weighty = 1;

        JPanel[] cards = {statCard(new Utils.CounterLabel(0, totalUnpaidAmount.doubleValue(), 2500), "Unpaid Amount", 46, new Color(192, 57, 43)),

                statCard(new Utils.CounterLabel(0, totalPaidAmount.doubleValue(), 2500), "Paid Amount", 46, new Color(39, 174, 96)),

                statCard(new Utils.AlphaFadeLabel(String.valueOf(unpaidCount), 2000), "Unpaid Bills", 40, new Color(231, 76, 60)),

                statCard(new Utils.AlphaFadeLabel(String.valueOf(paidCount), 2000), "Paid Bills", 40, new Color(46, 204, 113)),

                statCard(new Utils.AlphaFadeLabel(lastLoginDate.toString(), 1000), "Last Login", 28, new Color(52, 73, 94)),

                statCard(new Utils.AlphaFadeLabel(beneficiaryName.toUpperCase(), 1000), "Beneficiary", 28, new Color(52, 73, 94)),

                statCard(new Utils.AlphaFadeLabel(accountNumber, 1000), "Account Number", 32, new Color(52, 73, 94))};

        int row = 0, col = 0;
        for (JPanel c : cards) {
            gc.gridy = row;
            gc.gridx = col;
            grid.add(c, gc);
            if (++col == 3) {
                col = 0;
                row++;
            }
        }
        return grid;
    }

    /* ───────────────── helper: a single stat card ───────────────── */
    private JPanel statCard(JLabel value, String title, int fontSize, Color valueColor) {

        value.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        value.setHorizontalAlignment(SwingConstants.CENTER);
        value.setForeground(valueColor);

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
