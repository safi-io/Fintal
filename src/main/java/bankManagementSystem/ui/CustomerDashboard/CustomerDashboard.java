package main.java.bankManagementSystem.ui.CustomerDashboard;

import main.java.bankManagementSystem.controller.CustomerDashboard.CustomerController;
import main.java.bankManagementSystem.utils.StripePaymentHandler;
import main.java.bankManagementSystem.ui.CustomerDashboard.Bills.PayBills;
import main.java.bankManagementSystem.ui.CustomerDashboard.BuildDashboard.MainDashboard;
import main.java.bankManagementSystem.ui.CustomerDashboard.Loan.LoanDashboard;
import main.java.bankManagementSystem.ui.CustomerDashboard.Profile.ProfileSettings;
import main.java.bankManagementSystem.ui.CustomerDashboard.Transactions.SendMoney;
import main.java.bankManagementSystem.ui.CustomerDashboard.Transactions.TransactionData;
import main.java.bankManagementSystem.ui.MainPages.LoginPage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class CustomerDashboard extends JFrame {

    private final JPanel mainContentPanel;
    private JButton currentlySelectedButton;
    private JPanel nav;

    public static final Color SIDEBAR_BG = new Color(25, 55, 77);
    public static final Color BUTTON_BG = new Color(45, 85, 115);
    public static final Color BUTTON_HIGHLIGHT = new Color(0, 160, 150);
    public static final Color HEADING_BG = new Color(0, 110, 160);
    public static final Color CARD_BG = new Color(245, 250, 250);

    private final CustomerController customerController;
    private final String loggedInAcc;

    public CustomerDashboard(String loggedInAcc) {
        setTitle("Customer Dashboard");
        this.loggedInAcc = loggedInAcc;
        customerController = new CustomerController();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        setUndecorated(true);

        JPanel container = new JPanel(new BorderLayout());
        container.add(buildHeadingPanel(), BorderLayout.NORTH);
        container.add(buildSidebar(), BorderLayout.WEST);

        mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(Color.WHITE);
        container.add(mainContentPanel, BorderLayout.CENTER);
        add(container);
    }

    private JPanel buildHeadingPanel() {
        JPanel headingPanel = new JPanel(new BorderLayout());
        headingPanel.setBackground(HEADING_BG);
        headingPanel.setBorder(new EmptyBorder(18, 28, 18, 28));

        JLabel heading = new JLabel("CUSTOMER DASHBOARD", SwingConstants.LEFT);
        heading.setFont(new Font("SansSerif", Font.BOLD, 34));
        heading.setForeground(Color.WHITE);

        JLabel dateLabel = new JLabel();
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        new Timer(1000, (ActionEvent e) -> dateLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a")))).start();

        headingPanel.add(heading, BorderLayout.WEST);
        headingPanel.add(dateLabel, BorderLayout.EAST);
        return headingPanel;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(280, getHeight()));

        nav = new JPanel(new GridLayout(7, 1, 0, 8));
        nav.setBackground(SIDEBAR_BG);
        nav.setBorder(new EmptyBorder(25, 10, 10, 10));
        String[] opts = {"Dashboard", "Send Money", "Add Money", "Transactions", "Pay Bills", "Loan Management", "Profile Settings"};
        for (String o : opts) nav.add(createNavButton(o));

        JButton logout = new JButton("Logout");
        styleLogoutButton(logout);
        JPanel logoutPanel = new JPanel(new BorderLayout());
        logoutPanel.setBackground(SIDEBAR_BG);
        logoutPanel.setBorder(new EmptyBorder(10, 10, 20, 10));
        logoutPanel.add(logout, BorderLayout.SOUTH);

        sidebar.add(nav, BorderLayout.NORTH);
        sidebar.add(logoutPanel, BorderLayout.SOUTH);

        SwingUtilities.invokeLater(() -> ((JButton) nav.getComponent(0)).doClick());
        return sidebar;
    }

    private void styleLogoutButton(JButton b) {
        b.setFocusPainted(false);
        b.setBackground(new Color(200, 50, 40));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 20));
        b.setBorderPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setMargin(new Insets(10, 20, 10, 20));
        b.addActionListener(e -> {
            int res = JOptionPane.showConfirmDialog(this, "Logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (res == 0) {
                customerController.handleUpdateLastLogin(loggedInAcc);
                SwingUtilities.invokeLater(() -> {
                    new LoginPage();
                    dispose();
                });
                dispose(); // Close the current window
            }
        });
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(BUTTON_BG);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 18));
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(10, 20, 10, 10));
        btn.addActionListener(e -> {
            highlightSelectedButton(btn);
            switchPanel(text);
        });
        return btn;
    }

    private void highlightSelectedButton(JButton selected) {
        if (currentlySelectedButton != null) currentlySelectedButton.setBackground(BUTTON_BG);
        selected.setBackground(BUTTON_HIGHLIGHT);
        currentlySelectedButton = selected;
    }

    private void switchPanel(String key) {


        switch (key) {
            case "Dashboard":
                mainContentPanel.removeAll();
                mainContentPanel.add(MainDashboard.buildDashboard(loggedInAcc), BorderLayout.CENTER);
                break;
            case "Send Money":
                mainContentPanel.removeAll();
                mainContentPanel.add(new SendMoney(loggedInAcc), BorderLayout.CENTER);
                break;
            case "Add Money":
                StripePaymentHandler handler = new StripePaymentHandler(loggedInAcc, () -> {
                    mainContentPanel.removeAll();
                    mainContentPanel.add(MainDashboard.buildDashboard(loggedInAcc), BorderLayout.CENTER);
                    mainContentPanel.revalidate();
                    mainContentPanel.repaint();
                });
                handler.initiatePayment();
                SwingUtilities.invokeLater(() -> ((JButton) nav.getComponent(0)).doClick());
                break;
            case "Transactions":
                mainContentPanel.removeAll();
                mainContentPanel.add(new TransactionData(loggedInAcc), BorderLayout.CENTER);
                break;
            case "Pay Bills":
                mainContentPanel.removeAll();
                mainContentPanel.add(new PayBills(loggedInAcc), BorderLayout.CENTER);
                break;
            case "Loan Management":
                mainContentPanel.removeAll();
                mainContentPanel.add(new LoanDashboard(loggedInAcc), BorderLayout.CENTER);
                break;
            case "Profile Settings":
                mainContentPanel.removeAll();
                ProfileSettings profileSettings = getProfileSettings();
                mainContentPanel.add(profileSettings, BorderLayout.CENTER);
                break;
        }

        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private ProfileSettings getProfileSettings() {
        ProfileSettings profileSettings = new ProfileSettings(loggedInAcc);

        // Add listener here
        profileSettings.addUpdateListener(() -> {
            String name = profileSettings.getNameText();
            String email = profileSettings.getEmailText();
            String phone = profileSettings.getPhoneText();
            String password = profileSettings.getPasswordText();

            // Call the controller method to update data (you'll need to implement this)
            boolean success = customerController.handleUpdateCustomerData(loggedInAcc, name, email, phone, password);

            if (success) {
                profileSettings.setStatus("Profile updated successfully", false);
                profileSettings.clearPassword();
            } else {
                profileSettings.setStatus("Update failed", true);
            }
        });
        return profileSettings;
    }

    private JComponent placeholder(String t) {
        JLabel l = new JLabel(t, SwingConstants.CENTER);
        l.setFont(new Font("SansSerif", Font.BOLD, 28));
        return l;
    }
}