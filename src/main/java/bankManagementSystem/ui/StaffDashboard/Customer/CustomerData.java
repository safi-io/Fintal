package main.java.bankManagementSystem.ui.StaffDashboard.Customer;

import main.java.bankManagementSystem.controller.StaffDashboard.CustomerController;
import main.java.bankManagementSystem.model.CustomerModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.text.MessageFormat;
import java.util.List;

public class CustomerData extends JPanel {
    private final JTextField cnicField;
    private final JTable customerTable;
    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> rowSorter;
    private final CustomerController customerController;
    private final JTextField nameField;

    public CustomerData() {
        customerController = new CustomerController();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // --- Top Panel ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JLabel heading = new JLabel("All Customers", SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 24));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        topPanel.add(heading, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        cnicField = new JTextField(20);
        searchPanel.add(new JLabel("Filter by CNIC: "));
        cnicField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        searchPanel.add(cnicField);

        nameField = new JTextField(20);
        searchPanel.add(new JLabel("Filter by Name: "));
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        searchPanel.add(nameField); // Add name field

        topPanel.add(searchPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        searchPanel.add(Box.createHorizontalStrut(20)); // Spacer

        // --- Table Setup ---
        String[] columnNames = {"CNIC", "Name", "Mail", "Phone", "DOB"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        customerTable = new JTable(tableModel);
        customerTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        customerTable.setRowHeight(22);
        JScrollPane tableScrollPane = new JScrollPane(customerTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(tableScrollPane, BorderLayout.CENTER);

        // --- Bottom Panel with Print Button ---
        JButton printButton = new JButton("Print Table");
        printButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        printButton.setBackground(new Color(60, 179, 113));
        printButton.setForeground(Color.WHITE);
        printButton.setFocusPainted(false);
        printButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(printButton);
        add(bottomPanel, BorderLayout.PAGE_END);

        // --- Table Row Sorter for Filtering ---
        rowSorter = new TableRowSorter<>(tableModel);
        customerTable.setRowSorter(rowSorter);

        // --- Real-Time Filter Logic ---
        cnicField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
        });

        nameField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterTable(); }
            public void removeUpdate(DocumentEvent e) { filterTable(); }
            public void changedUpdate(DocumentEvent e) { filterTable(); }
        });

        // --- Load Data ---
        loadAllCustomers();

        // --- Print Button Logic ---
        printButton.addActionListener(e -> {
            try {
                boolean printed = customerTable.print(JTable.PrintMode.FIT_WIDTH,
                        new MessageFormat("Customer Table"),
                        new MessageFormat("Page - {0}"));
                if (!printed) {
                    JOptionPane.showMessageDialog(this, "Printing was cancelled.", "Print", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to print: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void loadAllCustomers() {
        try {
            List<CustomerModel> customers = customerController.handleGetAllCustomers();
            for (CustomerModel customer : customers) {
                tableModel.addRow(new Object[]{
                        customer.getCustomerCNIC(),
                        customer.getCustomerName(),
                        customer.getCustomerMail(),
                        customer.getCustomerPhone(),
                        customer.getCustomerDOB(),
                });
            }
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Failed to load customers: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterTable() {
        String cnicText = cnicField.getText().trim();
        String nameText = nameField.getText().trim();

        RowFilter<DefaultTableModel, Object> cnicFilter = cnicText.isEmpty()
                ? null
                : RowFilter.regexFilter("^" + cnicText, 0); // Column 0 = CNIC

        RowFilter<DefaultTableModel, Object> nameFilter = nameText.isEmpty()
                ? null
                : RowFilter.regexFilter("(?i)" + nameText, 1); // Column 1 = Name (case-insensitive)

        if (cnicFilter == null && nameFilter == null) {
            rowSorter.setRowFilter(null);
        } else if (cnicFilter != null && nameFilter != null) {
            rowSorter.setRowFilter(RowFilter.andFilter(List.of(cnicFilter, nameFilter)));
        } else {
            rowSorter.setRowFilter(cnicFilter != null ? cnicFilter : nameFilter);
        }
    }

}
