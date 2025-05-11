package main.java.bankManagementSystem.ui.StaffDashboard.Customer;

import main.java.bankManagementSystem.controller.StaffDashboard.CustomerController;
import main.java.bankManagementSystem.model.CustomerModel;

import javax.swing.*;
import java.awt.*;

public class SearchCustomer extends JPanel {

    private final JTextField cnicField;
    private final JTextArea resultArea;
    private final CustomerController customerController;

    public SearchCustomer() {
        customerController = new CustomerController();
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("SansSerif", Font.BOLD, 16);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 16);

        JLabel heading = new JLabel("Search Customer by CNIC");
        heading.setFont(new Font("SansSerif", Font.BOLD, 24));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(heading, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        JLabel cnicLabel = new JLabel("Enter CNIC:");
        cnicLabel.setFont(labelFont);
        gbc.gridx = 0;
        add(cnicLabel, gbc);

        cnicField = new JTextField(20);
        cnicField.setFont(fieldFont);
        gbc.gridx = 1;
        add(cnicField, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        searchButton.setBackground(new Color(100, 149, 237));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.setPreferredSize(new Dimension(200, 40));

        add(searchButton, gbc);

        gbc.gridy++;
        resultArea = new JTextArea(6, 30);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultArea.setEditable(false);
        resultArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(new JScrollPane(resultArea), gbc);

        // Handle search button click
        searchButton.addActionListener(e -> {
            String cnic = cnicField.getText().trim();

            if (cnic.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter CNIC.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            CustomerModel result = customerController.handleGetCustomerByCNIC(cnic);
            if (result != null) {
                resultArea.setText("CNIC: " + result.getCustomerCNIC() + "\nName: " + result.getCustomerName() + "\nEmail: " + result.getCustomerMail() + "\nPhone: " + result.getCustomerPhone() + "\nDOB: " + result.getCustomerDOB() + "\nPassword: " + result.getCustomerPassword());
            } else {
                resultArea.setText("");
                JOptionPane.showMessageDialog(this, "No User against this CNIC!", "Error", JOptionPane.ERROR_MESSAGE);
            }


        });
    }
}
