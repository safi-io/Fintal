package main.java.bankManagementSystem.ui.CustomerDashboard.Profile;

import main.java.bankManagementSystem.controller.CustomerDashboard.CustomerController;
import main.java.bankManagementSystem.model.CustomerModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProfileSettings extends JPanel {

    private static final Color BG_COLOR = new Color(250, 252, 254);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color PRIMARY = new Color(10, 132, 255);
    private static final Color ACCENT = new Color(48, 209, 88);
    private static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    private static final Color TEXT_SECONDARY = new Color(120, 120, 120);
    private static final Color ERROR_COLOR = new Color(255, 69, 58);

    private final JTextField nameTxt = new JTextField();
    private final JTextField emailTxt = new JTextField();
    private final JPasswordField passwordTxt = new JPasswordField();
    private final JTextField phoneTxt = new JTextField();

    private final JButton updateBtn = new JButton("Update");

    private final JLabel statusLbl = new JLabel(" ");
    private final CustomerController customerController;

    public ProfileSettings(String accountNumber) {
        setLayout(new GridBagLayout());
        customerController = new CustomerController();
        setBackground(BG_COLOR);

        JPanel card = new RoundedShadowPanel(20);
        card.setBackground(CARD_BG);
        card.setBorder(new EmptyBorder(40, 50, 50, 50));
        card.setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(15, 20, 15, 20);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        gc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Title
        gc.gridx = 0;
        gc.gridy = row++;
        gc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Edit Profile");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_PRIMARY);
        card.add(titleLabel, gc);

        // Create labeled inputs
        addLabeledField(card, gc, row++, "Name", nameTxt, "Enter your full name");
        addLabeledField(card, gc, row++, "Email", emailTxt, "Enter your email address");
        addLabeledField(card, gc, row++, "Password", passwordTxt, "Enter a new password (leave blank to keep current)");
        addLabeledField(card, gc, row++, "Phone", phoneTxt, "Enter your phone number");

        // Buttons panel with only Update button
        gc.gridx = 0;
        gc.gridy = row++;
        gc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        btnPanel.setOpaque(false);
        styleFlatButton(updateBtn, ACCENT);
        btnPanel.add(updateBtn);
        card.add(btnPanel, gc);

        // Status label
        gc.gridy = row++;
        statusLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(statusLbl, gc);

        loadCustomerData(accountNumber);
        add(card);
    }

    private void addLabeledField(JPanel parent, GridBagConstraints gc, int row, String label, JTextField field, String tooltip) {
        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 1;

        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel.setForeground(TEXT_SECONDARY);
        parent.add(jLabel, gc);

        gc.gridx = 1;
        styleTextField(field);
        field.setToolTipText(tooltip);
        parent.add(field, gc);
    }

    private void addLabeledField(JPanel parent, GridBagConstraints gc, int row, String label, JPasswordField field, String tooltip) {
        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 1;

        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel.setForeground(TEXT_SECONDARY);
        parent.add(jLabel, gc);

        gc.gridx = 1;
        stylePasswordField(field);
        field.setToolTipText(tooltip);
        parent.add(field, gc);
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, TEXT_SECONDARY));
        field.setPreferredSize(new Dimension(250, 30));
    }

    private void stylePasswordField(JPasswordField field) {
        styleTextField(field);  // Same styling as text field
    }

    private void styleFlatButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.darker());
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
    }

    // Custom JPanel with rounded corners + shadow
    static class RoundedShadowPanel extends JPanel {
        private final int cornerRadius;

        RoundedShadowPanel(int radius) {
            super();
            this.cornerRadius = radius;
            setOpaque(false);
            setLayout(new GridBagLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            int shadowGap = 6;
            int shadowOffset = 4;

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw shadow
            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillRoundRect(shadowOffset, shadowOffset, getWidth() - shadowGap, getHeight() - shadowGap, cornerRadius, cornerRadius);

            // Draw white panel
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - shadowGap - shadowOffset, getHeight() - shadowGap - shadowOffset, cornerRadius, cornerRadius);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private void loadCustomerData(String accountNumber) {
        CustomerModel customer = customerController.handleGetCustomerByAccountNumber(accountNumber);

        if (customer != null) {
            setNameText(customer.getCustomerName());
            setEmailText(customer.getCustomerMail());
            setPhoneText(customer.getCustomerPhone());
            clearPassword();
            setStatus("Profile loaded", false);
        } else {
            setStatus("Customer not found", true);
        }
    }

    // === Public API for controller ===

    public void setNameText(String name) {
        nameTxt.setText(name);
    }

    public void setEmailText(String email) {
        emailTxt.setText(email);
    }

    public void setPhoneText(String phone) {
        phoneTxt.setText(phone);
    }

    public void clearPassword() {
        passwordTxt.setText("");
    }

    public String getNameText() {
        return nameTxt.getText().trim();
    }

    public String getEmailText() {
        return emailTxt.getText().trim();
    }

    public String getPhoneText() {
        return phoneTxt.getText().trim();
    }

    public String getPasswordText() {
        return new String(passwordTxt.getPassword());
    }

    public void setStatus(String message, boolean isError) {
        statusLbl.setText(message);
        statusLbl.setForeground(isError ? ERROR_COLOR : ACCENT.darker());
    }

    public void addUpdateListener(Runnable listener) {
        updateBtn.addActionListener(e -> listener.run());
    }


}
