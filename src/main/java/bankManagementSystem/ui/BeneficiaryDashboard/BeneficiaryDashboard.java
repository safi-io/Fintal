package main.java.bankManagementSystem.ui.BeneficiaryDashboard;


import main.java.bankManagementSystem.controller.CustomerDashboard.CustomerController;
import main.java.bankManagementSystem.ui.BeneficiaryDashboard.Bills.BillUploadPanel;
import main.java.bankManagementSystem.ui.BeneficiaryDashboard.Bills.BillsStatus;
import main.java.bankManagementSystem.ui.BeneficiaryDashboard.BuildDashboard.MainDashboard;
import main.java.bankManagementSystem.ui.MainPages.LoginPage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class BeneficiaryDashboard extends JFrame {

    private final JPanel mainContentPanel;
    private JButton currentlySelectedButton;

    /* custom palette */
    // Updated palette
    public static final Color SIDEBAR_BG = new Color(31, 59, 77);   // #1F3B4D
    public static final Color BUTTON_BG = new Color(42, 81, 110);  // #2A516E
    public static final Color BUTTON_HIGHLIGHT = new Color(74, 189, 172); // #4ABDAC
    public static final Color HEADING_BG = new Color(17, 100, 102);  // #116466
    public static final Color CARD_BG = new Color(224, 228, 228);// #E0E4E4

    private final String accountNumber;
    private final CustomerController customerController;


    public BeneficiaryDashboard(String accountNumber) {
        this.customerController = new CustomerController();
        this.accountNumber = accountNumber;
        setTitle("Beneficiary Dashboard");
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

    /* ────────────────── heading bar ────────────────── */
    private JPanel buildHeadingPanel() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(HEADING_BG);
        bar.setBorder(new EmptyBorder(18, 28, 18, 28));

        JLabel title = new JLabel("BENEFICIARY DASHBOARD", SwingConstants.LEFT);
        title.setFont(new Font("SansSerif", Font.BOLD, 34));
        title.setForeground(Color.WHITE);

        JLabel clock = new JLabel();
        clock.setFont(new Font("SansSerif", Font.PLAIN, 18));
        clock.setForeground(Color.WHITE);
        clock.setHorizontalAlignment(SwingConstants.RIGHT);
        new Timer(1000, (ActionEvent e) -> clock.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a")))).start();

        bar.add(title, BorderLayout.WEST);
        bar.add(clock, BorderLayout.EAST);
        return bar;
    }

    /* ────────────────── sidebar ────────────────────── */
    private JPanel buildSidebar() {
        JPanel side = new JPanel(new BorderLayout());
        side.setBackground(SIDEBAR_BG);
        side.setPreferredSize(new Dimension(280, getHeight()));

        JPanel nav = new JPanel(new GridLayout(5, 1, 0, 8));
        nav.setBorder(new EmptyBorder(25, 10, 10, 10));
        nav.setBackground(SIDEBAR_BG);
        String[] opts = {"Dashboard", "Upload Bills", "Bills Status"};
        for (String o : opts) nav.add(createNavButton(o));

        JButton logout = new JButton("Logout");
        styleLogout(logout);
        JPanel logoutPane = new JPanel(new BorderLayout());
        logoutPane.setBackground(SIDEBAR_BG);
        logoutPane.setBorder(new EmptyBorder(10, 10, 20, 10));
        logoutPane.add(logout, BorderLayout.SOUTH);

        side.add(nav, BorderLayout.NORTH);
        side.add(logoutPane, BorderLayout.SOUTH);

        // auto select first
        SwingUtilities.invokeLater(() -> ((JButton) nav.getComponent(0)).doClick());
        return side;
    }

    private JButton createNavButton(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBackground(BUTTON_BG);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 18));
        b.setBorderPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setMargin(new Insets(10, 20, 10, 10));
        b.addActionListener(e -> {
            highlightButton(b);
            switchPanel(text);
        });
        return b;
    }

    private void highlightButton(JButton sel) {
        if (currentlySelectedButton != null) currentlySelectedButton.setBackground(BUTTON_BG);
        sel.setBackground(BUTTON_HIGHLIGHT);
        currentlySelectedButton = sel;
    }

    private void styleLogout(JButton b) {
        b.setFocusPainted(false);
        b.setBackground(new Color(192, 57, 43));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 20));
        b.setBorderPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setMargin(new Insets(10, 20, 10, 20));
        b.addActionListener(e -> {

            // Update Last Login

            int res = JOptionPane.showConfirmDialog(this, "Logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (res == 0) {
                customerController.handleUpdateLastLogin(accountNumber);
                SwingUtilities.invokeLater(() -> {
                    new LoginPage();
                    dispose();
                });
            }
        });
    }

    /* ────────────────── panel switch ────────────────── */
    private void switchPanel(String key) {
        mainContentPanel.removeAll();
        switch (key) {
            case "Dashboard":
                MainDashboard dashboardBuilder = new MainDashboard();
                mainContentPanel.add(dashboardBuilder.buildDashboard(accountNumber), BorderLayout.CENTER);
                break;
            case "Upload Bills":
                mainContentPanel.add(new BillUploadPanel(accountNumber), BorderLayout.CENTER);
                break;
            case "Bills Status":
                mainContentPanel.add(new BillsStatus(accountNumber), BorderLayout.CENTER);
                break;
        }
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private JComponent placeholder(String msg) {
        JLabel l = new JLabel(msg, SwingConstants.CENTER);
        l.setFont(new Font("SansSerif", Font.BOLD, 28));
        return l;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BeneficiaryDashboard("1100").setVisible(true));
    }
}
