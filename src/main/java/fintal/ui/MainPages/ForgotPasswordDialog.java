package main.java.fintal.ui.MainPages;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import main.java.fintal.controller.MainPages.ForgetController;
import main.java.fintal.utils.emailSender;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordDialog extends JDialog {

    private JComboBox<String> roleComboBox;
    private JTextField otpEmailField;
    private JTextField otpField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JLabel statusLabel;
    private JButton nextButton;
    private JButton sendOtpButton;
    private JButton resetButton;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private String generatedOTP;

    private final ForgetController forgetController;

    // E‑mail validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    public ForgotPasswordDialog(JFrame owner) {
        super(owner, "Forgot Password", true);
        this.forgetController = new ForgetController();
        setUndecorated(true);

        // ---  UI setup  --------------------------------------------------------
        try {
            FlatMacDarkLaf.setup();
        } catch (Exception e) {
            System.err.println("Failed to initialise FlatLaf in dialog: " + e);
        }

        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainContainer.setBackground(new Color(28, 35, 43));
        setContentPane(mainContainer);

        header(mainContainer);
        body(mainContainer);

        setSize(450, 500);
        setLocationRelativeTo(owner);
    }

    // --------------------------------------------------------------------- UI blocks
    private void header(JPanel container) {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("Forgot Password?");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(220, 220, 220));
        header.add(title, BorderLayout.WEST);

        JButton close = new JButton("X");
        close.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        close.setContentAreaFilled(false);
        close.setBorderPainted(false);
        close.setFocusPainted(false);
        close.setForeground(new Color(180, 180, 180));
        close.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        close.addActionListener(e -> dispose());
        header.add(close, BorderLayout.EAST);

        container.add(header, BorderLayout.NORTH);
    }

    private void body(JPanel container) {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);

        cardPanel.add(createRoleSelectionPanel(), "ROLE_SELECTION");
        cardPanel.add(createEmailOtpPanel(), "EMAIL_OTP");

        container.add(cardPanel, BorderLayout.CENTER);
    }

    // -------------------------------------  Panel 1 : choose role -------------
    private JPanel createRoleSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Instruction
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel instruction = new JLabel("Select your role to reset password");
        instruction.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        instruction.setForeground(new Color(170, 170, 170));
        instruction.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(instruction, gbc);

        // Combo box
        gbc.gridy = 1;
        gbc.insets = new Insets(15, 0, 15, 0);
        String[] roles = {"Customer", "Staff"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        roleComboBox.setBackground(new Color(40, 40, 40));
        roleComboBox.setForeground(Color.WHITE);
        roleComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(new EmptyBorder(5, 10, 5, 10));
                return this;
            }
        });
        roleComboBox.setPreferredSize(new Dimension(300, 40));
        panel.add(roleComboBox, gbc);

        // Next button
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        nextButton = new JButton("Next");
        nextButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_BORDERLESS);
        nextButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nextButton.setBackground(new Color(65, 134, 199));
        nextButton.setForeground(Color.WHITE);
        nextButton.setFocusPainted(false);
        nextButton.setPreferredSize(new Dimension(300, 45));
        nextButton.addActionListener(e -> cardLayout.show(cardPanel, "EMAIL_OTP"));
        panel.add(nextButton, gbc);

        // Status
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 0, 0, 0);
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setForeground(new Color(255, 120, 120));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(statusLabel, gbc);

        return panel;
    }

    // -------------------------------------  Panel 2 : e‑mail + OTP ------------
    private JPanel createEmailOtpPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Instruction
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel instruction = new JLabel("Enter your email address to receive a verification code");
        instruction.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        instruction.setForeground(new Color(170, 170, 170));
        instruction.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(instruction, gbc);

        // E‑mail field
        gbc.gridy = 1;
        gbc.insets = new Insets(15, 0, 15, 0);
        otpEmailField = new JTextField();
        otpEmailField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your email address");
        otpEmailField.putClientProperty(FlatClientProperties.STYLE, "font: +2; margin: 5,7,5,7;");
        otpEmailField.setPreferredSize(new Dimension(300, 40));
        panel.add(otpEmailField, gbc);

        // Send‑OTP button
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        sendOtpButton = new JButton("Send Verification Code");
        sendOtpButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_BORDERLESS);
        sendOtpButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sendOtpButton.setBackground(new Color(65, 134, 199));
        sendOtpButton.setForeground(Color.WHITE);
        sendOtpButton.setFocusPainted(false);
        sendOtpButton.setPreferredSize(new Dimension(300, 45));
        sendOtpButton.addActionListener(e -> sendOtp());
        panel.add(sendOtpButton, gbc);

        // OTP + password fields (initially hidden)
        gbc.gridy = 3;
        otpField = new JTextField();
        otpField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter verification code");
        otpField.putClientProperty(FlatClientProperties.STYLE, "font: +2; margin: 5,7,5,7;");
        otpField.setPreferredSize(new Dimension(300, 40));
        otpField.setVisible(false);
        panel.add(otpField, gbc);

        gbc.gridy = 4;
        newPasswordField = new JPasswordField();
        newPasswordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "New password");
        newPasswordField.putClientProperty(FlatClientProperties.STYLE, "font: +2; margin: 5,7,5,7;");
        newPasswordField.setPreferredSize(new Dimension(300, 40));
        newPasswordField.setVisible(false);
        panel.add(newPasswordField, gbc);

        gbc.gridy = 5;
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Confirm new password");
        confirmPasswordField.putClientProperty(FlatClientProperties.STYLE, "font: +2; margin: 5,7,5,7;");
        confirmPasswordField.setPreferredSize(new Dimension(300, 40));
        confirmPasswordField.setVisible(false);
        panel.add(confirmPasswordField, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(20, 0, 10, 0);
        resetButton = new JButton("Reset Password");
        resetButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_BORDERLESS);
        resetButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resetButton.setBackground(new Color(65, 134, 199));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFocusPainted(false);
        resetButton.setPreferredSize(new Dimension(300, 45));
        resetButton.addActionListener(e -> resetPassword());
        resetButton.setVisible(false);
        panel.add(resetButton, gbc);

        // Status
        gbc.gridy = 7;
        gbc.insets = new Insets(10, 0, 0, 0);
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setForeground(new Color(255, 120, 120));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(statusLabel, gbc);

        return panel;
    }

    // --------------------------------------------------------------------- helpers
    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setForeground(isError ? new Color(255, 120, 120) : new Color(120, 255, 120));
    }

    // ---------------------------------------------------  Step 1 : send OTP ---
    private void sendOtp() {
        final String email = otpEmailField.getText().trim();
        final String selectedRole = (String) roleComboBox.getSelectedItem();

        if (email.isEmpty()) {
            showStatus("E‑mail cannot be empty", true);
            return;
        }
        if (!isValidEmail(email)) {
            showStatus("Please enter a valid e‑mail address", true);
            return;
        }

        // NEW  ➜  check that the e‑mail exists for the chosen role
        if (!forgetController.handleEmailExists(selectedRole, email)) {
            showStatus("E‑mail not found for selected role", true);
            return;
        }

        // Generate + send OTP
        generatedOTP = forgetController.generateOTP();
        emailSender mail = new emailSender();
        mail.sendEmail(email, "Here is your requested OTP", generatedOTP + " is your OTP for resetting password!");

        showStatus("OTP sent to " + email + ". Please check your spam folder as well!", false);

        // Reveal next‑step fields
        otpEmailField.setEnabled(false);
        sendOtpButton.setVisible(false);
        otpField.setVisible(true);
        newPasswordField.setVisible(true);
        confirmPasswordField.setVisible(true);
        resetButton.setVisible(true);

        otpField.requestFocusInWindow();
        pack();
    }

    // -------------------------------------------------  Step 2 : reset pwd ----
    private void resetPassword() {
        final String otp = otpField.getText().trim();
        final String newPass = new String(newPasswordField.getPassword());
        final String confirmPass = new String(confirmPasswordField.getPassword());
        final String selectedRole = (String) roleComboBox.getSelectedItem();
        final String email = otpEmailField.getText().trim();

        if (otp.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            showStatus("All fields are required", true);
            return;
        }
        if (!newPass.equals(confirmPass)) {
            showStatus("Passwords do not match", true);
            return;
        }

        if (generatedOTP.equals(otp)) {
            // Call to update password in DB
            boolean success = forgetController.handleUpdatePassword(selectedRole, email, newPass);
            if (success) {
                showStatus(selectedRole + " password reset successfully!", false);

                otpField.setEnabled(false);
                newPasswordField.setEnabled(false);
                confirmPasswordField.setEnabled(false);
                resetButton.setEnabled(false);

                new Timer(2000, e -> {
                    dispose();
                    ((Timer) e.getSource()).stop();
                }) {{
                    setRepeats(false);
                }}.start();
            } else {
                showStatus("Failed to reset password. Please try again.", true);
            }
        } else {
            showStatus("Invalid verification code", true);
        }
    }

    private boolean isValidEmail(String email) {
        Matcher m = EMAIL_PATTERN.matcher(email == null ? "" : email);
        return m.matches();
    }
}