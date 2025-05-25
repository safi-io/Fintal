package main.java.bankManagementSystem.ui.AdminDashboard;

import main.java.bankManagementSystem.ui.AdminDashboard.Branch.CreateBranchForm;
import main.java.bankManagementSystem.ui.AdminDashboard.Branch.DeleteBranchForm;
import main.java.bankManagementSystem.ui.AdminDashboard.Branch.UpdateBranchForm;
import main.java.bankManagementSystem.ui.AdminDashboard.Staff.CreateStaffForm;
import main.java.bankManagementSystem.ui.AdminDashboard.Staff.DeleteStaffForm;
import main.java.bankManagementSystem.ui.AdminDashboard.Staff.UpdateStaffForm;
import main.java.bankManagementSystem.ui.AdminDashboard.Transactions.ViewTransactionsPanel;
import main.java.bankManagementSystem.ui.MainPages.LoginPage;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminDashboard extends JFrame {
    private final JPanel mainContentPanel;
    private JButton currentlySelectedButton;

    // Define consistent colors
    private static final Color NAV_BTN_BG = new Color(60, 63, 65);
    private static final Color NAV_BTN_SELECTED = new Color(100, 149, 237);

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        setUndecorated(true);

        JPanel container = new JPanel(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(new Color(30, 30, 30));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));

        JPanel navButtonsPanel = new JPanel(new GridLayout(7, 1, 0, 10));
        navButtonsPanel.setBackground(new Color(30, 30, 30));
        navButtonsPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));

        String[] options = {
                "Create Staff", "Update Staff", "Delete Staff",
                "Create Branch", "Update Branch", "Delete Branch", "Transactions Log"
        };

        for (String option : options) {
            JButton btn = new JButton(option);
            btn.setFocusPainted(false);
            btn.setBackground(NAV_BTN_BG);
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

        // Logout Button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFocusPainted(false);
        logoutButton.setBackground(new Color(139, 0, 0));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        logoutButton.setBorderPainted(false);
        logoutButton.setHorizontalAlignment(SwingConstants.LEFT);
        logoutButton.setMargin(new Insets(10, 20, 10, 20));
        logoutButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                SwingUtilities.invokeLater(() -> {
                    new LoginPage();
                    dispose();
                });
            }
        });

        JPanel logoutPanel = new JPanel(new BorderLayout());
        logoutPanel.setBackground(new Color(30, 30, 30));
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        logoutPanel.add(logoutButton, BorderLayout.SOUTH);

        sidebar.add(navButtonsPanel, BorderLayout.NORTH);
        sidebar.add(logoutPanel, BorderLayout.SOUTH);

        // Main content panel
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BorderLayout());
        mainContentPanel.setBackground(Color.WHITE);

        JLabel defaultLabel = new JLabel("Welcome to Admin Dashboard", SwingConstants.CENTER);
        defaultLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        mainContentPanel.add(defaultLabel, BorderLayout.CENTER);

        // Heading panel
        JPanel headingPanel = new JPanel(new BorderLayout());
        headingPanel.setBackground(NAV_BTN_SELECTED); // Same as selected color
        headingPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel heading = new JLabel("ADMIN DASHBOARD", SwingConstants.LEFT);
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

        container.add(headingPanel, BorderLayout.NORTH);
        container.add(sidebar, BorderLayout.WEST);
        container.add(mainContentPanel, BorderLayout.CENTER);

        add(container);
    }

    private void updateMainPanel(String title) {
        mainContentPanel.removeAll();

        switch (title) {
            case "Create Staff":
                mainContentPanel.add(new CreateStaffForm(), BorderLayout.CENTER);
                break;
            case "Update Staff":
                mainContentPanel.add(new UpdateStaffForm(), BorderLayout.CENTER);
                break;
            case "Delete Staff":
                mainContentPanel.add(new DeleteStaffForm(), BorderLayout.CENTER);
                break;
            case "Create Branch":
                mainContentPanel.add(new CreateBranchForm(), BorderLayout.CENTER);
                break;
            case "Delete Branch":
                mainContentPanel.add(new DeleteBranchForm(), BorderLayout.CENTER);
                break;
            case "Update Branch":
                mainContentPanel.add(new UpdateBranchForm(), BorderLayout.CENTER);
                break;
            case "Transactions Log":
                mainContentPanel.add(new ViewTransactionsPanel(), BorderLayout.CENTER);
        }

        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private void highlightSelectedButton(JButton selectedButton) {
        if (currentlySelectedButton != null) {
            currentlySelectedButton.setBackground(NAV_BTN_BG);
        }

        selectedButton.setBackground(NAV_BTN_SELECTED);
        currentlySelectedButton = selectedButton;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminDashboard dashboard = new AdminDashboard();
            dashboard.setVisible(true);
        });
    }
}
