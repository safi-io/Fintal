package main.java.bankManagementSystem.ui.MainPages;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import main.java.bankManagementSystem.controller.AdminDashboard.StaffController;
import main.java.bankManagementSystem.controller.CustomerDashboard.CustomerController;
import main.java.bankManagementSystem.controller.MainPages.LoginController;
import main.java.bankManagementSystem.ui.AdminDashboard.AdminDashboard;
import main.java.bankManagementSystem.ui.BeneficiaryDashboard.BeneficiaryDashboard;
import main.java.bankManagementSystem.ui.CustomerDashboard.CustomerDashboard;
import main.java.bankManagementSystem.ui.StaffDashboard.StaffDashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.border.EmptyBorder;

public class LoginPage extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleDropdown;
    private JLabel statusLabel;
    private JLabel timeLabel;

    private final LoginController loginController;
    private final StaffController staffController;
    private final CustomerController customerController;

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    public LoginPage() {
        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf: " + e);
        }

        this.loginController = new LoginController();
        this.staffController = new StaffController();
        this.customerController = new CustomerController();

        setTitle("Bank Management System - Login");
        setUndecorated(true); // Remove window decorations
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // --- Main Layered Pane for Background and Content ---
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        setContentPane(layeredPane);

        // --- Dark Gradient Background Panel ---
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                // Darker, richer gradient
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 40, 50),     // Dark blue-grey start
                        getWidth(), getHeight(), new Color(15, 25, 35) // Even darker blue-grey end
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        backgroundPanel.setBounds(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);

        // --- Content Panel (for logo, login form, and time) ---
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false); // Make it transparent to see the background
        contentPanel.setBorder(new EmptyBorder(50, 50, 50, 50)); // Padding

        GridBagConstraints gbc = new GridBagConstraints();

        // --- Logo/Bank Name ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 60, 0); // Increased bottom padding
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel bankLogoLabel = new JLabel("<html><h1 style='color:#6FA3D7; font-size:48px; text-shadow: 2px 2px 4px rgba(0,0,0,0.4);'>BankXPro</h1></html>"); // Lighter blue for dark theme
        bankLogoLabel.setFont(new Font("Segoe UI", Font.BOLD, 100)); // Larger font size
        contentPanel.add(bankLogoLabel, gbc);

        // --- Login Form Panel ---
        JPanel loginFormPanel = new JPanel(new GridBagLayout());
        loginFormPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50)); // Increased internal padding
        loginFormPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 30; background: rgba(255, 255, 255, 0.1); shadow-width: 12; shadow-color: rgba(0,0,0,0.5); border: 1px solid rgba(255,255,255,0.1);"); // Semi-transparent, larger shadow, subtle border

        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(loginFormPanel, gbc);

        // --- Email Field ---
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 25, 0); // Increased bottom padding
        gbc.fill = GridBagConstraints.HORIZONTAL; // Keep horizontal fill so it centers and expands if needed, but preferred size will take precedence
        emailField = new JTextField(30); // Set preferred column width (e.g., 30 characters)
        emailField.setPreferredSize(new Dimension(emailField.getPreferredSize().width, 60));
        emailField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Email Address");
        emailField.putClientProperty(FlatClientProperties.STYLE, "font: 24; padding: 15px;"); // Larger text, more internal padding
        loginFormPanel.add(emailField, gbc);

        // --- Password Field ---
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 25, 0);
        passwordField = new JPasswordField(30); // Set preferred column width (e.g., 30 characters)
        passwordField.setPreferredSize(new Dimension(passwordField.getPreferredSize().width, 60));
        passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
        passwordField.putClientProperty(FlatClientProperties.STYLE, "font: 24; padding: 15px;"); // Larger text, more internal padding
        loginFormPanel.add(passwordField, gbc);

        // --- Drop Down Role ---
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 40, 0); // Increased bottom padding
        String[] roles = {"Customer", "Staff", "Admin"};
        roleDropdown = new JComboBox<>(roles);
        roleDropdown.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Select Role");
        roleDropdown.putClientProperty(FlatClientProperties.STYLE, "font: 24; padding: 10px;"); // Much larger text, more internal padding
        roleDropdown.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"); // Make it even wider
        loginFormPanel.add(roleDropdown, gbc);


        // --- Login Button ---
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 15, 0);
        JButton loginButton = new JButton("Login");
        loginButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        loginButton.setBackground(new Color(60, 141, 188)); // Still a vibrant blue
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(loginButton.getFont().deriveFont(Font.BOLD, 20f)); // Significantly larger
        loginButton.setPreferredSize(new Dimension(250, 55)); // Larger button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptLogin();
            }
        });
        loginFormPanel.add(loginButton, gbc);

        // --- Forgot Password Button ---
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 0, 10, 0);
        JButton forgotPasswordButton = new JButton("Forgot Password?");
        forgotPasswordButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_BORDERLESS);
        forgotPasswordButton.setForeground(Color.BLACK); // Lighter blue for dark theme
        forgotPasswordButton.setFont(forgotPasswordButton.getFont().deriveFont(Font.PLAIN, 15f)); // Larger text
        loginFormPanel.add(forgotPasswordButton, gbc);

        // Inside LoginPage's constructor, where forgotPasswordButton is set up:
        forgotPasswordButton.addActionListener(e -> {
            ForgotPasswordDialog dialog = new ForgotPasswordDialog(this); // 'this' refers to the LoginPage JFrame
            dialog.setVisible(true);
        });

        // --- Sign Up Button ---
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 0, 0, 0);
        JButton signUpButton = new JButton("Don't have an account? Sign Up");
        signUpButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_BORDERLESS);
        signUpButton.setForeground(Color.BLACK); // Lighter blue for dark theme
        signUpButton.setFont(signUpButton.getFont().deriveFont(Font.PLAIN, 15f)); // Larger text
        signUpButton.addActionListener(e -> {
            new SignUpPage();
            dispose();
        });
        loginFormPanel.add(signUpButton, gbc);


        // --- Status Label (for errors/messages) ---
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 0, 0, 0); // More top padding
        statusLabel = new JLabel("");
        statusLabel.setForeground(new Color(255, 100, 100)); // Brighter red for dark theme
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD, 16f)); // Larger and bold
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginFormPanel.add(statusLabel, gbc);

        // --- System Time Label ---
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2; // Below the login form panel
        gbc.gridwidth = 2;
        gbc.insets = new Insets(60, 0, 0, 0); // More top padding
        gbc.anchor = GridBagConstraints.CENTER;
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18)); // Larger font
        timeLabel.setForeground(new Color(180, 180, 180)); // Lighter grey for dark theme
        contentPanel.add(timeLabel, gbc);

        // Center the content panel on the layered pane
        contentPanel.setBounds(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        layeredPane.add(contentPanel, JLayeredPane.PALETTE_LAYER);

        // Update time every second
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTime();
            }
        });
        timer.start();

        updateTime(); // Initial time update

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a | EEEE, MMMM dd,yyyy");
        timeLabel.setText(now.format(formatter));
    }

    private void attemptLogin() {
        showStatus("", false);
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleDropdown.getSelectedItem();

        // --- Input Validation ---
        if (email.isEmpty() || password.isEmpty()) {
            showStatus("Email and Password cannot be empty.", true);
            return;
        }

        if (!isValidEmail(email)) {
            showStatus("Please enter a valid email address.", true);
            return;
        }

        // Backend call
        try {
            boolean ok = loginController.isCredentialsValid(email, password, role);

            if (!ok) {
                showStatus("Wrong Credentials!", true);
                return;
            }

            if (role.toLowerCase().equals("admin")) {
                SwingUtilities.invokeLater(() -> {
                    AdminDashboard dashboard = new AdminDashboard();
                    dashboard.setVisible(true);
                });
                dispose();
            } else if (role.toLowerCase().equals("staff")) {
                SwingUtilities.invokeLater(() -> {
                    StaffDashboard dashboard = new StaffDashboard(staffController.handleGetIdByMail(email));
                    dashboard.setVisible(true);
                });
                dispose();
            } else {
                // Fetch Account Number
                String accountNumber = customerController.handlegetAccountNumberByMail(email);

                // Fetch Type
                String type = customerController.handlegetAccountTypeByAccountNumber(accountNumber);

                // Is Account Opened
                boolean isAccountOpened = customerController.handleGetIsAccountOpened(accountNumber);

                if (!isAccountOpened) {
                    JOptionPane.showMessageDialog(null, "Account is not opened yet, you will be informed by mail...", "Info", JOptionPane.INFORMATION_MESSAGE);
                    emailField.setText("");
                    passwordField.setText("");
                    return;
                }

                if (type.equals("beneficiary")) {
                    SwingUtilities.invokeLater(() -> {
                        BeneficiaryDashboard dashboard = new BeneficiaryDashboard(accountNumber);
                        dashboard.setVisible(true);
                    });
                    dispose(); // Close the current window
                } else {
                    SwingUtilities.invokeLater(() -> {
                        CustomerDashboard dashboard = new CustomerDashboard(accountNumber);
                        dashboard.setVisible(true);
                    });
                    dispose(); // Close the current window
                }


            }

            showStatus("Logged In!", false);

        } catch (Exception ex) {
            // Catch exceptions thrown by your backend (e.g., from DAO/Controller)
            showStatus("An error occurred: " + ex.getMessage(), true);
        }


    }

    private boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    public void showStatus(String message, boolean isError) {
        // Wrap the message in HTML with max width constraint
        String htmlMessage = "<html><div style='width: 230px; text-align: center;'>" + message.replace("\n", "<br>") + "</div></html>";

        statusLabel.setText(htmlMessage);
        if (isError) {
            statusLabel.setForeground(new Color(255, 100, 100)); // Brighter red for dark theme
        } else {
            statusLabel.setForeground(new Color(100, 255, 100)); // Brighter green for dark theme
        }

        // Force the layout to update
        statusLabel.revalidate();
        statusLabel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage());
    }
}