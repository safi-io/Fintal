package main.java.bankManagementSystem.ui.StaffDashboard;

import main.java.bankManagementSystem.controller.CustomerDashboard.CustomerController;
import main.java.bankManagementSystem.controller.StaffDashboard.StaffController;
import main.java.bankManagementSystem.ui.CustomerDashboard.Profile.ProfileSettings;
import main.java.bankManagementSystem.ui.StaffDashboard.Accounts.AccountsApplication;
import main.java.bankManagementSystem.ui.StaffDashboard.Accounts.AccountData;
import main.java.bankManagementSystem.ui.StaffDashboard.Customer.CustomerData;
import main.java.bankManagementSystem.ui.StaffDashboard.Profile.StaffProfileSettings;
import main.java.bankManagementSystem.ui.StaffDashboard.Staff.StaffDirectoryPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StaffDashboard extends JFrame {
    private final JPanel mainContentPanel;
    private JButton currentlySelectedButton;

    private final StaffController staffController;

    public StaffDashboard() {
        setTitle("Staff Dashboard");
        staffController = new StaffController();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        setUndecorated(true);

        JPanel container = new JPanel(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(new Color(40, 40, 60));
        sidebar.setPreferredSize(new Dimension(300, getHeight()));

        JPanel navButtonsPanel = new JPanel(new GridLayout(11, 1, 0, 10));
        navButtonsPanel.setBackground(new Color(40, 40, 60));
        navButtonsPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));

        String[] options = {"Account Applications", "Customers Data", "Accounts Data", "Loans", "Transaction Logs", "Staff Directory", "Edit Profile"};

        for (String option : options) {
            JButton btn = new JButton(option);
            btn.setFocusPainted(false);
            btn.setBackground(new Color(70, 80, 100));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("SansSerif", Font.BOLD, 18));
            btn.setBorderPainted(false);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setMargin(new Insets(10, 20, 10, 10));
            btn.addActionListener(e -> {
                highlightSelectedButton(btn);
                updateMainPanel(option);
            });

            navButtonsPanel.add(btn);
        }

        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFocusPainted(false);
        logoutButton.setBackground(new Color(200, 0, 0));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        logoutButton.setBorderPainted(false);
        logoutButton.setHorizontalAlignment(SwingConstants.LEFT);
        logoutButton.setMargin(new Insets(10, 20, 10, 20));
        logoutButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        JPanel logoutPanel = new JPanel(new BorderLayout());
        logoutPanel.setBackground(new Color(40, 40, 60));
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        logoutPanel.add(logoutButton, BorderLayout.SOUTH);

        sidebar.add(navButtonsPanel, BorderLayout.NORTH);
        sidebar.add(logoutPanel, BorderLayout.SOUTH);

        // Main content panel
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BorderLayout());
        mainContentPanel.setBackground(Color.WHITE);

        JLabel defaultLabel = new JLabel("Welcome to Staff Dashboard", SwingConstants.CENTER);
        defaultLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        mainContentPanel.add(defaultLabel, BorderLayout.CENTER);

        // Top heading panel
        JPanel headingPanel = new JPanel(new BorderLayout());
        headingPanel.setBackground(new Color(65, 105, 225)); // Royal Blue
        headingPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel heading = new JLabel("STAFF DASHBOARD", SwingConstants.LEFT);
        heading.setFont(new Font("SansSerif", Font.BOLD, 36));
        heading.setForeground(Color.WHITE);

        JLabel dateLabel = new JLabel();
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        Timer timer = new Timer(1000, (ActionEvent e) -> {
            String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a"));
            dateLabel.setText(currentDateTime);
        });
        timer.start();

        headingPanel.add(heading, BorderLayout.WEST);
        headingPanel.add(dateLabel, BorderLayout.EAST);

        // Layout composition
        container.add(headingPanel, BorderLayout.NORTH);
        container.add(sidebar, BorderLayout.WEST);
        container.add(mainContentPanel, BorderLayout.CENTER);

        add(container);
    }

    private void updateMainPanel(String title) {
        mainContentPanel.removeAll();
        mainContentPanel.setLayout(new BorderLayout());

        switch (title) {
            case "Account Applications":
                mainContentPanel.removeAll();
                mainContentPanel.add(new AccountsApplication(), BorderLayout.CENTER);
                mainContentPanel.revalidate();
                mainContentPanel.repaint();
                break;
            case "Customers Data":
                mainContentPanel.removeAll();
                mainContentPanel.add(new CustomerData(), BorderLayout.CENTER);
                mainContentPanel.revalidate();
                mainContentPanel.repaint();
                break;
            case "Accounts Data":
                mainContentPanel.removeAll();
                mainContentPanel.add(new AccountData(), BorderLayout.CENTER);
                mainContentPanel.revalidate();
                mainContentPanel.repaint();
                break;
            case "Loans":
                break;
            case "Transaction Logs":
                break;
            case "Staff Directory":
                mainContentPanel.removeAll();
                mainContentPanel.add(new StaffDirectoryPanel(), BorderLayout.CENTER);
                mainContentPanel.revalidate();
                mainContentPanel.repaint();
                break;
            case "Edit Profile":
                StaffProfileSettings profileSettings = getProfileSettings();
                mainContentPanel.add(profileSettings, BorderLayout.CENTER);
                break;

            default:
                JLabel unknown = new JLabel("Unknown Option: " + title, SwingConstants.CENTER);
                unknown.setFont(new Font("SansSerif", Font.BOLD, 28));
                mainContentPanel.add(unknown, BorderLayout.CENTER);
                break;
        }


        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private void highlightSelectedButton(JButton selectedButton) {
        if (currentlySelectedButton != null) {
            currentlySelectedButton.setBackground(new Color(70, 80, 100));
        }

        selectedButton.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        currentlySelectedButton = selectedButton;
    }

    private StaffProfileSettings getProfileSettings() {
        StaffProfileSettings profileSettings = new StaffProfileSettings(6);

        // Add listener here
        profileSettings.addUpdateListener(() -> {
            String name = profileSettings.getNameText();
            String email = profileSettings.getEmailText();
            String phone = profileSettings.getPhoneText();
            String password = profileSettings.getPasswordText();

            // Call the controller method to update data (you'll need to implement this)
            boolean success = staffController.handleUpdateStaffData("6", name, email, phone, password);

            if (success) {
                profileSettings.setStatus("Profile updated successfully", false);
                profileSettings.clearPassword();
            } else {
                profileSettings.setStatus("Update failed", true);
            }
        });
        return profileSettings;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StaffDashboard dashboard = new StaffDashboard();
            dashboard.setVisible(true);
        });
    }
}