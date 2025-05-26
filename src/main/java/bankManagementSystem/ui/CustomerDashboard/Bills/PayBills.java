package main.java.bankManagementSystem.ui.CustomerDashboard.Bills;

import main.java.bankManagementSystem.controller.CustomerDashboard.BeneficiaryController;
import main.java.bankManagementSystem.controller.CustomerDashboard.BillController;
import main.java.bankManagementSystem.model.BillModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Lets a customer:
 * 1. pick a beneficiary
 * 2. enter a bill reference number
 * 3. fetch / display the bill
 * 4. pay the bill
 */
public class PayBills extends JPanel {

    /* ───── palette (same family as other customer panels) ───── */
    private static final Color BG_COLOR = new Color(248, 250, 252);
    private static final Color PRIMARY = new Color(32, 136, 203);
    private static final Color PRIMARY_DARK = PRIMARY.darker();
    private static final Color TEXT_SECONDARY = new Color(120, 120, 120);
    private static final Color ACCENT_OK = new Color(46, 204, 113);
    private static final Color ACCENT_ERR = new Color(231, 76, 60);

    /* ───── controllers ───── */
    private final BeneficiaryController beneficiaryController = new BeneficiaryController();
    private final BillController billController = new BillController();

    /* ───── UI widgets ───── */
    private final JComboBox<String> beneficiaryBox = new JComboBox<>();
    private final JTextField referenceTxt = new JTextField(18);
    private final JButton searchBtn = new JButton("Search");
    private final JLabel refLbl = new JLabel("—");
    private final JLabel consumerLbl = new JLabel("—");
    private final JLabel amountLbl = new JLabel("—");
    private final JLabel statusLbl = new JLabel(" ");
    private final JLabel paidFlagLbl = new JLabel(" ");
    private final JButton payBtn = new JButton("Pay Bill");

    private final String loggedInAcc;

    /* ───── state ───── */
    private String currentBeneficiaryAcc = null;
    private BillModel fetchedBill = null;   // holds the bill once fetched

    public PayBills(String loggedInAcc) {
        this.loggedInAcc = loggedInAcc;
        setBackground(BG_COLOR);
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(25, 40, 25, 40));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(16, 18, 16, 18);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;

        int row = 0;

        /* === heading === */
        gc.gridx = 0;
        gc.gridy = row++;
        gc.gridwidth = 2;
        JLabel heading = new JLabel("Pay a Bill");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        add(heading, gc);

        /* === beneficiary select === */
        gc.gridwidth = 1;
        addLabel("Beneficiary", gc, row);
        gc.gridx = 1;
        styleCombo(beneficiaryBox);
        beneficiaryBox.addItem("Loading…");
        loadBeneficiaries();
        add(beneficiaryBox, gc);
        row++;

        /* === reference search === */
        addLabel("Reference No.", gc, row);
        gc.gridx = 1;
        JPanel searchPane = new JPanel(new BorderLayout(8, 0));
        searchPane.setOpaque(false);
        styleField(referenceTxt);
        styleButton(searchBtn);
        searchPane.add(referenceTxt, BorderLayout.CENTER);
        searchPane.add(searchBtn, BorderLayout.EAST);
        add(searchPane, gc);
        row++;

        /* === result card === */
        gc.gridx = 0;
        gc.gridy = row++;
        gc.gridwidth = 2;
        JPanel card = buildResultCard();
        add(card, gc);

        /* === pay + feedback === */
        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 2;
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        styleButton(payBtn);
        payBtn.setEnabled(false);
        bottom.add(payBtn, BorderLayout.EAST);
        statusLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bottom.add(statusLbl, BorderLayout.WEST);
        add(bottom, gc);

        /* === listeners === */
        searchBtn.addActionListener(e -> fetchBill());
        payBtn.addActionListener(e -> payBill());
    }

    /* ─────────────────── helper UI builders ─────────────────── */
    private void addLabel(String txt, GridBagConstraints gc, int row) {
        gc.gridx = 0;
        gc.gridy = row;
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        l.setForeground(TEXT_SECONDARY);
        add(l, gc);
    }

    private JPanel buildResultCard() {
        JPanel p = new RoundedPanel(18);
        p.setBackground(Color.WHITE);
        p.setLayout(new GridLayout(3, 2, 8, 4));
        p.setBorder(new EmptyBorder(18, 22, 18, 22));

        p.add(cardLabel("Reference Number:"));
        p.add(refLbl);
        p.add(cardLabel("Consumer Name:"));
        p.add(consumerLbl);
        p.add(cardLabel("Amount (PKR):"));
        p.add(amountLbl);

        for (Component c : p.getComponents()) {
            if (c instanceof JLabel lab) {
                lab.setFont(new Font("Segoe UI", Font.BOLD, 16));
            }
        }
        return p;
    }

    private JLabel cardLabel(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(TEXT_SECONDARY);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return l;
    }

    private void styleField(JTextField f) {
        f.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        f.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, TEXT_SECONDARY));
    }

    private void styleCombo(JComboBox<?> c) {
        c.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        c.setBackground(Color.WHITE);
    }

    private void styleButton(JButton b) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 15));
        b.setForeground(Color.WHITE);
        b.setBackground(PRIMARY);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(6, 20, 6, 20));
        b.setOpaque(true);
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                b.setBackground(PRIMARY_DARK);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                b.setBackground(PRIMARY);
            }
        });
    }

    /* ─────────────────── logic ─────────────────── */
    private void loadBeneficiaries() {
        beneficiaryBox.removeAllItems();
        try {
            List<String> list = beneficiaryController.handleGetBeneficiaryAccountNumbers();
            for (String acc : list) beneficiaryBox.addItem(acc);
            if (!list.isEmpty()) currentBeneficiaryAcc = list.get(0);
        } catch (Exception ex) {
            showStatus("Failed to load beneficiaries.", true);
        }
        beneficiaryBox.addActionListener(e -> currentBeneficiaryAcc = (String) beneficiaryBox.getSelectedItem());
    }

    private void fetchBill() {

        // ── reset UI ──────────────────────────────────────────────
        fetchedBill = null;
        payBtn.setEnabled(false);
        clearCard();
        showStatus(" ", false);

        // ── basic validations ────────────────────────────────────
        if (currentBeneficiaryAcc == null) {
            showStatus("Select a beneficiary first.", true);
            return;
        }
        String ref = referenceTxt.getText().trim();
        if (ref.isEmpty()) {
            showStatus("Enter a reference number.", true);
            return;
        }

        // ── call controller ──────────────────────────────────────
        try {
            BillModel bill = billController.handleGetBill(currentBeneficiaryAcc.split("_")[0], ref);

            if (bill == null) {
                showStatus("No bill found for that reference.", true);
                return;
            }

            /* ---------- populate card ---------- */
            refLbl.setText(bill.getReferenceNumber());
            consumerLbl.setText(bill.getConsumerName());
            amountLbl.setText("PKR " + bill.getAmount().toPlainString());

            // paid / unpaid flag
            if (bill.isPaid()) {
                paidFlagLbl.setText("PAID");
                paidFlagLbl.setForeground(new Color(39, 174, 96));   // green
                showStatus("Bill is already paid.", true);           // red message bar
                payBtn.setEnabled(false);
            } else {
                paidFlagLbl.setText("UNPAID");
                paidFlagLbl.setForeground(new Color(192, 57, 43));   // red
                fetchedBill = bill;
                payBtn.setEnabled(true);
                showStatus("Bill ready to pay.", false);
            }

        } catch (Exception ex) {
            ex.printStackTrace();    // optional: log
            showStatus("Error fetching bill. Please try again.", true);
        }
    }

    /* ─────────────────── pay & receipt ─────────────────── */
    private void payBill() {
        if (fetchedBill == null) return;

        try {
            boolean ok = billController.handlePayBill(
                    loggedInAcc,
                    currentBeneficiaryAcc.split("_")[0],
                    fetchedBill.getReferenceNumber(),
                    fetchedBill.getAmount());

            if (!ok) {                       // DAO said “no”
                showStatus("Payment failed.", true);
                return;
            }

            /* ---------- success ---------- */
            showStatus("Payment successful!", false);

            // Build an HTML receipt
            String html =
                    "<html>" +
                            "<h2 style='margin-top:0;color:#2E86C1;font-family:Arial;'>Payment Receipt</h2>" +
                            "<div style='font-family:Arial;font-size:13px;color:#34495E;'>" +
                            "<p><b>Reference # :</b> " + fetchedBill.getReferenceNumber() + "</p>" +
                            "<p><b>Beneficiary A/C :</b> " + currentBeneficiaryAcc + "</p>" +
                            "<p><b>Consumer :</b> " + fetchedBill.getConsumerName() + "</p>" +
                            "<p><b>Amount :</b> PKR " + fetchedBill.getAmount() + "</p>" +
                            "<p><b>Paid From A/C :</b> " + loggedInAcc + "</p>" +
                            "<p><b>Date & Time :</b> " + java.time.LocalDateTime.now() + "</p>" +
                            "</div>" +
                            "</html>";

            JOptionPane.showMessageDialog(
                    SwingUtilities.getWindowAncestor(this),
                    html,
                    "Payment Receipt",
                    JOptionPane.INFORMATION_MESSAGE);

            /* ---------- clean up UI ---------- */
            payBtn.setEnabled(false);
            clearCard();
            referenceTxt.setText("");
            fetchedBill = null;

        } catch (Exception ex) {
            ex.printStackTrace();
            showStatus("Error during payment: " + ex.getMessage(), true);
        }
    }


    private void clearCard() {
        refLbl.setText("—");
        consumerLbl.setText("—");
        amountLbl.setText("—");
        paidFlagLbl.setText("—");
    }

    private void showStatus(String msg, boolean err) {
        statusLbl.setText(msg);
        statusLbl.setForeground(err ? ACCENT_ERR : ACCENT_OK);
    }

    /* ─────────────────── rounded panel helper ─────────────────── */
    private static class RoundedPanel extends JPanel {
        private final int r;

        RoundedPanel(int radius) {
            r = radius;
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), r, r);
            super.paintComponent(g2);
            g2.dispose();
        }
    }
}
