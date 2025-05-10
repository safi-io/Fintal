package main.java.bankManagementSystem.ui.StaffDashboard.Customer;

import main.java.bankManagementSystem.controller.StaffDashboard.CustomerController;
import main.java.bankManagementSystem.model.CustomerModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        add(scrollPane, BorderLayout.CENTER);
    }

    private static JTable getJTable(List<CustomerModel> customers) {
        String[] columnNames = {"CNIC", "Name", "Mail", "Phone", "DOB", "Password"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (CustomerModel customer : customers) {
            Object[] row = {customer.getCustomerCNIC(), customer.getCustomerName(), customer.getCustomerMail(), customer.getCustomerPhone(), customer.getCustomerDOB(), customer.getCustomerPassword()};
            tableModel.addRow(row);
        }

        JTable table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(22);
        return table;
    }
}
