package main.java.fintal.ui.CustomerDashboard.Loan;

import main.java.fintal.controller.CustomerDashboard.LoanController;
import main.java.fintal.model.LoanApplicationModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.List;

/**
 * Dashboard:
 * • apply for a new loan via controller
 * • load previous loans from DB on start / after submit
 * • show live totals & pending amount
 */
public class LoanDashboard extends JPanel {

    /* ---------- style constants ---------- */
    private static final Color BG_COLOR = new Color(248, 250, 252);
    private static final Color PRIMARY = new Color(11, 132, 255);
    private static final Color PRIMARY_DARK = PRIMARY.darker();
    private static final Color ACCENT = new Color(46, 204, 113);
    private static final Color ERROR_COLOR = new Color(255, 71, 64);
    private static final Color TEXT_SECONDARY = new Color(120, 120, 120);

    /* ---------- controller & state ---------- */
    private final LoanController loanController = new LoanController();
    private final String accountNumber;   // provided by caller (logged‑in customer)

    /* ---------- widgets ---------- */
    private final JTextField amountTxt = new JTextField();
    private final JTextField tenureTxt = new JTextField();
    private final JComboBox<String> purposeCombo = new JComboBox<>(new String[]{"Education", "Business", "Personal", "Vehicle"});
    private final JTextArea notesTxt = new JTextArea(3, 20);
    private final JButton submitBtn = new JButton("Submit Application");
    private final JLabel feedback = new JLabel(" ");

    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Amount (PKR)", "Tenure (Months)", "Purpose", "Apply Date", "Is Approved"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    private final JLabel totalLbl = new JLabel("Total applications: 0");
    private final JLabel toReturnAmountLbl = new JLabel("Remaining amount: PKR 0");

    /* ---------- ctor ---------- */
    public LoanDashboard(String accountNumber) {
        this.accountNumber = accountNumber;

        setBackground(BG_COLOR);
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 30, 20, 30));

        add(buildFormPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        add(buildSummaryPanel(), BorderLayout.EAST);

        loadLoansFromDB();          // initial fetch
    }

    /* ---------- DB → table ---------- */
    private void loadLoansFromDB() {
        model.setRowCount(0);
        List<LoanApplicationModel> loans = loanController.handleGetLoanApplicationByAccountNumber(accountNumber);
        for (LoanApplicationModel ln : loans) {
            model.addRow(new Object[]{ln.getLoanID(), ln.getLoanAmount(), ln.getLoanTenure(), ln.getLoanPurpose(), ln.getLoanDate(),   // <- tweak if needed
                    ln.isLoanIsApproved()});
        }
        updateSummary();
    }

    /* ---------- submit ---------- */
    private void handleSubmit() {
        String amtStr = amountTxt.getText().trim();
        String tenStr = tenureTxt.getText().trim();

        if (amtStr.isEmpty() || tenStr.isEmpty()) {
            showFeedback("Please fill in Amount and Tenure.", true);
            return;
        }
        double amt;
        int ten;
        try {
            amt = Double.parseDouble(amtStr);
            ten = Integer.parseInt(tenStr);
            if (amt <= 0 || ten <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            showFeedback("Amount and Tenure must be positive numbers.", true);
            return;
        }

        LoanApplicationModel loan = new LoanApplicationModel();               // <- build your model
        loan.setLoanAccountNumber(accountNumber);           // if field exists
        loan.setLoanAmount(BigDecimal.valueOf(amt));
        loan.setLoanTenure(ten);
        loan.setLoanPurpose((String) purposeCombo.getSelectedItem());
        loan.setLoanNote(notesTxt.getText().trim());       // if field exists
        loan.setLoanIsApproved(false);

        boolean ok = loanController.handleSubmitLoanApplication(loan);
        if (ok) {
            clearForm();
            loadLoansFromDB();                          // refresh table & totals
            showFeedback("Loan request sent for review.", false);
        } else {
            showFeedback("Failed to submit. Please try again.", true);
        }
    }

    /* ---------- UI builders ---------- */
    private JPanel buildFormPanel() {
        JPanel card = new RoundedShadowPanel(20);
        card.setBackground(Color.WHITE);
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(12, 12, 12, 12);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;

        int row = 0;
        gc.gridx = 0;
        gc.gridy = row++;
        gc.gridwidth = 2;
        JLabel title = new JLabel("Apply for New Loan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        card.add(title, gc);

        addField(card, gc, row++, "Amount (PKR)", amountTxt, "e.g. 500000");
        addField(card, gc, row++, "Tenure (Months)", tenureTxt, "e.g. 24");

        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 1;
        JLabel plbl = new JLabel("Purpose");
        styleLabel(plbl);
        card.add(plbl, gc);
        gc.gridx = 1;
        purposeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        purposeCombo.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, TEXT_SECONDARY));
        card.add(purposeCombo, gc);
        row++;

        gc.gridx = 0;
        gc.gridy = row;
        JLabel nlbl = new JLabel("Notes");
        styleLabel(nlbl);
        card.add(nlbl, gc);
        gc.gridx = 1;
        notesTxt.setLineWrap(true);
        notesTxt.setWrapStyleWord(true);
        notesTxt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        notesTxt.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, TEXT_SECONDARY));
        JScrollPane notesPane = new JScrollPane(notesTxt);
        notesPane.setBorder(null);
        card.add(notesPane, gc);
        row++;

        gc.gridx = 0;
        gc.gridy = row++;
        gc.gridwidth = 2;
        JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnWrap.setOpaque(false);
        styleButton(submitBtn);
        btnWrap.add(submitBtn);
        card.add(btnWrap, gc);

        gc.gridy = row;
        feedback.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        card.add(feedback, gc);

        submitBtn.addActionListener(e -> handleSubmit());
        return card;
    }

    private JPanel buildTablePanel() {
        /* header & grid colours */
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTableHeader h = table.getTableHeader();
        h.setFont(new Font("Segoe UI", Font.BOLD, 14));
        h.setBackground(new Color(32, 136, 203));
        h.setForeground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));

        JScrollPane tablePane = new JScrollPane(table);
        tablePane.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(32, 136, 203), 2), "Previous Loan Applications", 0, 0, new Font("Segoe UI", Font.BOLD, 18), new Color(32, 136, 203)));
        panel.add(tablePane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildSummaryPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        totalLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        toReturnAmountLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        p.add(totalLbl);
        p.add(Box.createVerticalStrut(8));
        p.add(toReturnAmountLbl);
        return p;
    }

    /* ---------- helpers ---------- */
    private void addField(JPanel parent, GridBagConstraints gc, int row, String label, JTextField field, String hint) {
        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 1;
        JLabel l = new JLabel(label);
        styleLabel(l);
        parent.add(l, gc);
        gc.gridx = 1;
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, TEXT_SECONDARY));
        field.setToolTipText(hint);
        parent.add(field, gc);
    }

    private void styleLabel(JLabel lbl) {
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(TEXT_SECONDARY);
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(PRIMARY);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(PRIMARY_DARK);
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(PRIMARY);
            }
        });
    }

    private void clearForm() {
        amountTxt.setText("");
        tenureTxt.setText("");
        notesTxt.setText("");
        purposeCombo.setSelectedIndex(0);
    }

    private void updateSummary() {
        int rows = model.getRowCount();
        double pending = 0;
        for (int i = 0; i < rows; i++) {
            Object statusVal = model.getValueAt(i, 5);
            if (statusVal instanceof Boolean && (Boolean) statusVal) {
                pending += Double.parseDouble(model.getValueAt(i, 1).toString()); // column 1 is Amount
            }

        }
        totalLbl.setText("Total applications: " + rows);
        toReturnAmountLbl.setText("Remaining amount: PKR " + String.format("%,.0f", pending));
    }

    private void showFeedback(String msg, boolean err) {
        feedback.setText(msg);
        feedback.setForeground(err ? ERROR_COLOR : ACCENT.darker());
    }

    /* ---------- rounded shadow panel ---------- */
    private static class RoundedShadowPanel extends JPanel {
        private final int radius;

        RoundedShadowPanel(int r) {
            radius = r;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            int gap = 6, off = 4;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 25));
            g2.fillRoundRect(off, off, getWidth() - gap, getHeight() - gap, radius, radius);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - gap - off, getHeight() - gap - off, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
