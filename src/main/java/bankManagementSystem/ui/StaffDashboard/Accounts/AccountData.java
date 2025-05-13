package main.java.bankManagementSystem.ui.StaffDashboard.Accounts;

import main.java.bankManagementSystem.controller.StaffDashboard.AccountController;
import main.java.bankManagementSystem.model.CustomerAccountModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.text.MessageFormat;
import java.util.List;

public class AccountData extends JPanel {
    private final JTextField accountNumberField;
    private final JTextField cnicField;
    private final JTable accountTable;
    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> rowSorter;

    public AccountData() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // --- Top Panel with Heading and Search Fields ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JLabel heading = new JLabel("All Bank Accounts", SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 24));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        topPanel.add(heading, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        accountNumberField = new JTextField(15);
        cnicField = new JTextField(15);

        accountNumberField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        cnicField.setFont(new Font("SansSerif", Font.PLAIN, 16));

        searchPanel.add(new JLabel("Filter by Account Number:"));
        searchPanel.add(accountNumberField);
        searchPanel.add(new JLabel("Filter by CNIC:"));
        searchPanel.add(cnicField);

        topPanel.add(searchPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // --- Table Setup ---
        String[] columnNames = {
                "CNIC", "Name", "Mail", "Phone","Account No", "Balance", "Type",
                "Opening Date", "Branch ID", "Branch Name"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        accountTable = new JTable(tableModel);
        accountTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        accountTable.setRowHeight(22);

        JScrollPane scrollPane = new JScrollPane(accountTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);



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

        JButton deleteButton = new JButton("Delete Selected Account");
        deleteButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        deleteButton.setBackground(new Color(220, 20, 60)); // Crimson red
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bottomPanel.add(deleteButton);

        deleteButton.addActionListener(e -> {
            int selectedRow = accountTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select an account to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Convert from view index to model index in case of sorting/filtering
            int modelRow = accountTable.convertRowIndexToModel(selectedRow);
            String accountNumber = tableModel.getValueAt(modelRow, 4).toString(); // Account No is column index 4

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete account number: " + accountNumber + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    AccountController controller = new AccountController();
                    controller.handleAccountDelete(accountNumber);

                    tableModel.removeRow(modelRow); // Remove from UI
                    JOptionPane.showMessageDialog(this, "Account deleted successfully.");
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(this, "Failed to delete account: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



        // --- Row Sorter & Filter Logic ---
        rowSorter = new TableRowSorter<>(tableModel);
        accountTable.setRowSorter(rowSorter);

        DocumentListener filterListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterTable(); }
            public void removeUpdate(DocumentEvent e) { filterTable(); }
            public void changedUpdate(DocumentEvent e) { filterTable(); }
        };

        accountNumberField.getDocument().addDocumentListener(filterListener);
        cnicField.getDocument().addDocumentListener(filterListener);

        // --- Load Data ---
        loadAllAccounts();

        // --- Print Button Logic ---
        printButton.addActionListener(e -> {
            try {
                boolean printed = accountTable.print(JTable.PrintMode.FIT_WIDTH,
                        new MessageFormat("Account Table"),
                        new MessageFormat("Page - {0}"));
                if (!printed) {
                    JOptionPane.showMessageDialog(this, "Printing was cancelled.", "Print", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to print: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void loadAllAccounts() {
        AccountController accountController = new AccountController();
        List<CustomerAccountModel> accounts = accountController.handleGetAllAccounts();

        for (CustomerAccountModel acc : accounts) {
            tableModel.addRow(new Object[]{
                    acc.getCustomerCNIC(),
                    acc.getCustomerName(),
                    acc.getCustomerMail(),
                    acc.getCustomerPhone(),
                    acc.getAccountNumber(),
                    acc.getAccountCurrentBalance(),
                    acc.getAccountType(),
                    acc.getAccountOpeningDate(),
                    acc.getAccountCustomerBranchID(),
                    acc.getCustomerBranch()
            });
        }
    }

    private void filterTable() {
        String accountSearch = accountNumberField.getText().trim();
        String cnicSearch = cnicField.getText().trim();

        RowFilter<DefaultTableModel, Object> accountFilter = RowFilter.regexFilter("(?i)" + accountSearch, 4); // column 0: Account No
        RowFilter<DefaultTableModel, Object> cnicFilter = RowFilter.regexFilter("(?i)" + cnicSearch, 0);         // column 6: CNIC

        rowSorter.setRowFilter(RowFilter.andFilter(List.of(accountFilter, cnicFilter)));
    }
}
