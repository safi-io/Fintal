package main.java.bankManagementSystem.ui.MainPages;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

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

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    public ForgotPasswordDialog(JFrame owner) {
        super(owner, "Forgot Password", true);
        setUndecorated(true);

        // Setup FlatLaf Dark Theme
        try {
            FlatMacDarkLaf.setup();
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf in dialog: " + e);
        }

        // Main container with modern styling
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainContainer.setBackground(new Color(28, 35, 43));
        setContentPane(mainContainer);

        // Header panel with close button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Forgot Password?");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(220, 220, 220));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton closeButton = new JButton("X");
        closeButton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.setForeground(new Color(180, 180, 180));
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());
        headerPanel.add(closeButton, BorderLayout.EAST);

        mainContainer.add(headerPanel, BorderLayout.NORTH);

        // Card layout for multi-step process
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);

        // Panel 1: Role selection
        JPanel roleSelectionPanel = createRoleSelectionPanel();
        cardPanel.add(roleSelectionPanel, "ROLE_SELECTION");

        // Panel 2: Email and OTP process
        JPanel emailOtpPanel = createEmailOtpPanel();
        cardPanel.add(emailOtpPanel, "EMAIL_OTP");

        mainContainer.add(cardPanel, BorderLayout.CENTER);

        // Set dialog size and position
        setSize(450, 500);
        setLocationRelativeTo(owner);
    }

    private JPanel createRoleSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Instruction label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel instructionLabel = new JLabel("Select your role to reset password");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        instructionLabel.setForeground(new Color(170, 170, 170));
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(instructionLabel, gbc);

        // Role selection combo box
        gbc.gridy = 1;
        gbc.insets = new Insets(15, 0, 15, 0);
        String[] roles = {"Customer", "Bank Employee", "Administrator"};
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

        // Status label
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 0, 0, 0);
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setForeground(new Color(255, 120, 120));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(statusLabel, gbc);

        return panel;
    }

    private JPanel createEmailOtpPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Instruction label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel instructionLabel = new JLabel("Enter your email address to receive a verification code");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        instructionLabel.setForeground(new Color(170, 170, 170));
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(instructionLabel, gbc);

        // Email field
        gbc.gridy = 1;
        gbc.insets = new Insets(15, 0, 15, 0);
        otpEmailField = new JTextField();
        otpEmailField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your email address");
        otpEmailField.putClientProperty(FlatClientProperties.STYLE, "font: +2; margin: 5,7,5,7;");
        otpEmailField.setPreferredSize(new Dimension(300, 40));
        panel.add(otpEmailField, gbc);

        // Send OTP button
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

        // OTP Field (initially hidden)
        gbc.gridy = 3;
        otpField = new JTextField();
        otpField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter verification code");
        otpField.putClientProperty(FlatClientProperties.STYLE, "font: +2; margin: 5,7,5,7;");
        otpField.setPreferredSize(new Dimension(300, 40));
        otpField.setVisible(false);
        panel.add(otpField, gbc);

        // New Password Field (initially hidden)
        gbc.gridy = 4;
        newPasswordField = new JPasswordField();
        newPasswordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "New password");
        newPasswordField.putClientProperty(FlatClientProperties.STYLE, "font: +2; margin: 5,7,5,7;");
        newPasswordField.setPreferredSize(new Dimension(300, 40));
        newPasswordField.setVisible(false);
        panel.add(newPasswordField, gbc);

        // Confirm Password Field (initially hidden)
        gbc.gridy = 5;
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Confirm new password");
        confirmPasswordField.putClientProperty(FlatClientProperties.STYLE, "font: +2; margin: 5,7,5,7;");
        confirmPasswordField.setPreferredSize(new Dimension(300, 40));
        confirmPasswordField.setVisible(false);
        panel.add(confirmPasswordField, gbc);

        // Reset Button (initially hidden)
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

        // Status label
        gbc.gridy = 7;
        gbc.insets = new Insets(10, 0, 0, 0);
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setForeground(new Color(255, 120, 120));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(statusLabel, gbc);

        return panel;
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setForeground(isError ? new Color(255, 120, 120) : new Color(120, 255, 120));
    }

    private void sendOtp() {
        String email = otpEmailField.getText().trim();
        String selectedRole = (String) roleComboBox.getSelectedItem();

        if (email.isEmpty()) {
            showStatus("Email cannot be empty", true);
            return;
        }
        if (!isValidEmail(email)) {
            showStatus("Please enter a valid email address", true);
            return;
        }

        // Simulate OTP sending with role information
        showStatus("OTP sent to " + email + " (" + selectedRole + " account)", false);

        // Show the hidden fields
        otpEmailField.setEnabled(false);
        sendOtpButton.setVisible(false);
        otpField.setVisible(true);
        newPasswordField.setVisible(true);
        confirmPasswordField.setVisible(true);
        resetButton.setVisible(true);

        // Focus on OTP field
        otpField.requestFocusInWindow();

        // Adjust dialog size
        pack();
    }

    private void resetPassword() {
        String otp = otpField.getText().trim();
        String newPass = new String(newPasswordField.getPassword());
        String confirmPass = new String(confirmPasswordField.getPassword());
        String selectedRole = (String) roleComboBox.getSelectedItem();

        if (otp.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            showStatus("All fields are required", true);
            return;
        }
        if (!newPass.equals(confirmPass)) {
            showStatus("Passwords do not match", true);
            return;
        }

        if ("123456".equals(otp)) {
            showStatus(selectedRole + " password reset successfully!", false);

            // Disable all fields
            otpField.setEnabled(false);
            newPasswordField.setEnabled(false);
            confirmPasswordField.setEnabled(false);
            resetButton.setEnabled(false);

            // Close after delay
            Timer timer = new Timer(2000, e -> {
                dispose();
                ((Timer) e.getSource()).stop();
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            showStatus("Invalid verification code", true);
        }
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

}