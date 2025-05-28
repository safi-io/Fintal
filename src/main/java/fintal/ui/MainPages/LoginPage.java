package main.java.fintal.ui.MainPages;

import com.formdev.flatlaf.FlatLightLaf;
import main.java.fintal.controller.AdminDashboard.StaffController;
import main.java.fintal.controller.CustomerDashboard.CustomerController;
import main.java.fintal.controller.MainPages.LoginController;
import main.java.fintal.ui.AdminDashboard.AdminDashboard;
import main.java.fintal.ui.BeneficiaryDashboard.BeneficiaryDashboard;
import main.java.fintal.ui.CustomerDashboard.CustomerDashboard;
import main.java.fintal.ui.StaffDashboard.StaffDashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginPage extends JFrame {
    // Color scheme matching CreateStaffForm
    private static final Color BG_COLOR = new Color(250, 252, 254);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color PRIMARY = new Color(10, 132, 255);
    private static final Color ACCENT = new Color(48, 209, 88);
    private static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    private static final Color TEXT_SECONDARY = new Color(120, 120, 120);
    private static final Color ERROR_COLOR = new Color(255, 69, 58);

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
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
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

        // Main panel with transparent background
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBounds(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        layeredPane.add(mainPanel, JLayeredPane.PALETTE_LAYER);

        // Create the main card panel with rounded corners and shadow
        RoundedShadowPanel card = new RoundedShadowPanel(20);
        card.setBackground(CARD_BG);
        card.setBorder(new EmptyBorder(50, 60, 50, 60));
        card.setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(15, 25, 15, 25);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        gc.anchor = GridBagConstraints.CENTER;

        int row = 0;

        // Bank Logo/Title
        gc.gridx = 0;
        gc.gridy = row++;
        gc.gridwidth = 2;
        gc.insets = new Insets(0, 0, 40, 0);
        JLabel bankLogoLabel = new JLabel("FINTAL");
        bankLogoLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        bankLogoLabel.setForeground(PRIMARY);
        bankLogoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(bankLogoLabel, gc);

        // Subtitle
        gc.gridy = row++;
        gc.insets = new Insets(0, 0, 40, 0);
        JLabel subtitleLabel = new JLabel("Fintal — Digitally Engineered for Finance.");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(subtitleLabel, gc);

        // Reset insets for form fields
        gc.insets = new Insets(15, 25, 15, 25);
        gc.gridwidth = 1;

        // Email Field
        addLabeledField(card, gc, row++, "Email Address",
                createEmailField(), "Enter your email address");

        // Password Field
        addLabeledField(card, gc, row++, "Password",
                createPasswordField(), "Enter your password");

        // Role Selection
        gc.gridx = 0;
        gc.gridy = row;
        JLabel roleLabel = new JLabel("Role");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleLabel.setForeground(Color.BLACK);
        card.add(roleLabel, gc);

        gc.gridx = 1;
        roleDropdown = createRoleDropdown();
        card.add(roleDropdown, gc);
        row++;

        // Status Label
        gc.gridx = 0;
        gc.gridy = row++;
        gc.gridwidth = 2;
        gc.insets = new Insets(20, 25, 10, 25);
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(statusLabel, gc);

        // Button Panel
        gc.gridy = row++;
        gc.insets = new Insets(10, 25, 15, 25);
        JPanel buttonPanel = createButtonPanel();
        card.add(buttonPanel, gc);

        // Time Label
        gc.gridy = row++;
        gc.insets = new Insets(30, 25, 0, 25);
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        timeLabel.setForeground(Color.BLACK);
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(timeLabel, gc);

        // Add card to main panel
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.gridx = 0;
        mainGbc.gridy = 0;
        mainGbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(card, mainGbc);

        // Setup time updates
        setupTimeUpdates();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JTextField createEmailField() {
        emailField = new JTextField(25);
        styleTextField(emailField);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return emailField;
    }

    private JPasswordField createPasswordField() {
        passwordField = new JPasswordField(25);
        styleTextField(passwordField);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return passwordField;
    }

    private JComboBox<String> createRoleDropdown() {
        String[] roles = {"Customer", "Staff", "Admin"};
        JComboBox<String> dropdown = new JComboBox<>(roles);
        styleComboBox(dropdown);
        return dropdown;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setOpaque(false);

        // Login Button
        JButton loginButton = new JButton("Login");
        stylePrimaryButton(loginButton);
        loginButton.addActionListener(e -> attemptLogin());
        panel.add(loginButton);

        // Secondary buttons panel
        JPanel secondaryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        secondaryPanel.setOpaque(false);

        JButton forgotPasswordButton = new JButton("Forgot Password?");
        styleSecondaryButton(forgotPasswordButton);
        forgotPasswordButton.addActionListener(e -> {
            ForgotPasswordDialog dialog = new ForgotPasswordDialog(this);
            dialog.setVisible(true);
        });

        JButton signUpButton = new JButton("Sign Up");
        styleSecondaryButton(signUpButton);
        signUpButton.addActionListener(e -> {
            new SignUpPage();
            dispose();
        });

        secondaryPanel.add(forgotPasswordButton);
        secondaryPanel.add(signUpButton);

        JPanel combinedPanel = new JPanel(new BorderLayout());
        combinedPanel.setOpaque(false);
        combinedPanel.add(panel, BorderLayout.NORTH);
        combinedPanel.add(secondaryPanel, BorderLayout.SOUTH);

        return combinedPanel;
    }

    private void addLabeledField(JPanel parent, GridBagConstraints gc, int row,
                                 String label, JComponent field, String tooltip) {
        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 1;

        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel.setForeground(Color.BLACK);
        parent.add(jLabel, gc);

        gc.gridx = 1;
        field.setToolTipText(tooltip);
        parent.add(field, gc);
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, TEXT_SECONDARY),
                BorderFactory.createEmptyBorder(8, 5, 3, 5)
        ));
        field.setPreferredSize(new Dimension(300, 35));
        field.setBackground(CARD_BG);
    }

    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        comboBox.setBackground(CARD_BG);
        comboBox.setForeground(TEXT_PRIMARY);
        comboBox.setPreferredSize(new Dimension(300, 35));
        ((JComponent) comboBox.getRenderer()).setBorder(new EmptyBorder(5, 8, 5, 8));
    }

    private void stylePrimaryButton(JButton btn) {
        btn.setBackground(PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(PRIMARY.darker());
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(PRIMARY);
            }
        });
    }

    private void styleSecondaryButton(JButton btn) {
        btn.setBackground(CARD_BG);
        btn.setForeground(PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(PRIMARY.darker());
            }

            public void mouseExited(MouseEvent e) {
                btn.setForeground(PRIMARY);
            }
        });
    }

    private void setupTimeUpdates() {
        Timer timer = new Timer(1000, e -> updateTime());
        timer.start();
        updateTime();
    }

    private void updateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a | EEEE, MMMM dd, yyyy");
        timeLabel.setText(now.format(formatter));
    }

    private void attemptLogin() {
        showStatus("", false);
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleDropdown.getSelectedItem();

        // Input Validation
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
                String type = customerController.handlegetAccountTypeByAccountNumber(accountNumber);
                boolean isAccountOpened = customerController.handleGetIsAccountOpened(accountNumber);

                if (!isAccountOpened) {
                    JOptionPane.showMessageDialog(null,
                            "Account is not opened yet, you will be informed by mail...",
                            "Info", JOptionPane.INFORMATION_MESSAGE);
                    emailField.setText("");
                    passwordField.setText("");
                    return;
                }

                boolean isAccountDeleted = customerController.handleGetIsAccountDeleted(accountNumber);

                if (isAccountDeleted) {
                    JOptionPane.showMessageDialog(null,
                            "Account is deleted!",
                            "Info", JOptionPane.INFORMATION_MESSAGE);
                    emailField.setText("");
                    passwordField.setText("");
                    return;
                }

                if (type.equals("beneficiary")) {
                    SwingUtilities.invokeLater(() -> {
                        BeneficiaryDashboard dashboard = new BeneficiaryDashboard(accountNumber);
                        dashboard.setVisible(true);
                    });
                    dispose();
                } else {
                    SwingUtilities.invokeLater(() -> {
                        CustomerDashboard dashboard = new CustomerDashboard(accountNumber);
                        dashboard.setVisible(true);
                    });
                    dispose();
                }
            }

            showStatus("Logged In!", false);

        } catch (Exception ex) {
            showStatus("An error occurred: " + ex.getMessage(), true);
        }
    }

    private boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    public void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        if (isError) {
            statusLabel.setForeground(ERROR_COLOR);
        } else {
            statusLabel.setForeground(ACCENT.darker());
        }
        statusLabel.revalidate();
        statusLabel.repaint();
    }

    // RoundedShadowPanel class (same as CreateStaffForm)
    private static class RoundedShadowPanel extends JPanel {
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
            g2.fillRoundRect(shadowOffset, shadowOffset,
                    getWidth() - shadowGap, getHeight() - shadowGap,
                    cornerRadius, cornerRadius);

            // Draw main panel
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0,
                    getWidth() - shadowGap - shadowOffset,
                    getHeight() - shadowGap - shadowOffset,
                    cornerRadius, cornerRadius);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage());
    }
}