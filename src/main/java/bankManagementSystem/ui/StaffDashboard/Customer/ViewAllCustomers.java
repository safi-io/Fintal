package main.java.bankManagementSystem.ui.StaffDashboard.Customer;

import main.java.bankManagementSystem.controller.StaffDashboard.CustomerController;
import main.java.bankManagementSystem.model.CustomerModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.MessageFormat;
import java.util.List;

public class ViewAllCustomers extends JPanel {

    private final CustomerController customerController;

    public ViewAllCustomers() {
        customerController = new CustomerController();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel heading = new JLabel("All Customers", SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 24));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(heading, BorderLayout.NORTH);

        List<CustomerModel> customers;
        try {
            customers = customerController.handleGetAllCustomers();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Failed to load customers: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTable table = getJTable(customers);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setReorderingAllowed(false); // prevent column drag

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Print Button
        JButton printButton = new JButton("Print Table");
        printButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        printButton.setBackground(new Color(60, 179, 113));
        printButton.setForeground(Color.WHITE);
        printButton.setFocusPainted(false);
        printButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        printButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        printButton.addActionListener(e -> {
            try {
                boolean printed = table.print(JTable.PrintMode.FIT_WIDTH,
                        new MessageFormat("Customer Table"),
                        new MessageFormat("Page - {0}"));
                if (!printed) {
                    JOptionPane.showMessageDialog(this, "Printing was cancelled.", "Print", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to print: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

// Add print button at the bottom
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(printButton);
        add(bottomPanel, BorderLayout.SOUTH);


        add(scrollPane, BorderLayout.CENTER);
    }

    private static JTable getJTable(List<CustomerModel> customers) {
        String[] columnNames = {"CNIC", "Name", "Mail", "Phone", "DOB", "Password"};

        // Custom table model to make table non-editable
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // disable cell editing
            }
        };

        for (CustomerModel customer : customers) {
            Object[] row = {
                    customer.getCustomerCNIC(),
                    customer.getCustomerName(),
                    customer.getCustomerMail(),
                    customer.getCustomerPhone(),
                    customer.getCustomerDOB(),
                    customer.getCustomerPassword()
            };
            tableModel.addRow(row);
        }

        JTable table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(22);
        return table;
    }
}
