package main.java.bankManagementSystem.ui.StaffDashboard.Accounts;

import main.java.bankManagementSystem.controller.StaffDashboard.AccountController;
import main.java.bankManagementSystem.model.CustomerAccountModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class AccountsApplication extends JPanel {

    private final AccountController accountController;
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    public AccountsApplication() {
        accountController = new AccountController();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel heading = new JLabel("All Account Applications", SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 24));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(heading, BorderLayout.NORTH);

        // Create table and add to UI
        createTable();

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
                boolean printed = table.print(JTable.PrintMode.FIT_WIDTH);
                if (!printed) {
                    JOptionPane.showMessageDialog(this, "Printing was cancelled.", "Print", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to print: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(printButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void createTable() {
        String[] columnNames = {"CNIC", "Name", "Mail", "Phone", "Account No.", "Type", "Opening Date", "Branch ID.", "Name", "Approve"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 9;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setReorderingAllowed(false);

        table.getColumnModel().getColumn(9).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(9).setCellEditor(new ButtonEditor(new JCheckBox(), this));

        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        add(scrollPane, BorderLayout.CENTER);

        loadTableData();
    }

    public void loadTableData() {
        tableModel.setRowCount(0);

        List<CustomerAccountModel> accounts;
        try {
            accounts = accountController.handleGetAccountApplications();
            if (accounts.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No Applications in System", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Failed to load account applications: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (CustomerAccountModel account : accounts) {
            tableModel.addRow(new Object[]{formatTitleCase(account.getCustomerCNIC()), formatTitleCase(account.getCustomerName()), formatTitleCase(account.getCustomerMail()), formatTitleCase(account.getCustomerPhone()), formatTitleCase(account.getAccountNumber()), formatTitleCase(account.getAccountType()), formatTitleCase(account.getAccountOpeningDate().toString()), formatTitleCase(account.getAccountCustomerBranchID()), formatTitleCase(account.getCustomerBranch()), "Approve"});
        }
    }

    private String formatTitleCase(String text) {
        if (text == null || text.trim().isEmpty()) return text;

        String[] words = text.trim().split("\\s+");
        StringBuilder formatted = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                formatted.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase()).append(" ");
            }
        }
        return formatted.toString().trim();
    }


    public class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBorderPainted(false);
            setFont(new Font("SansSerif", Font.BOLD, 14));
            setBackground(new Color(46, 204, 113)); // Green
            setForeground(Color.WHITE);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Approve" : value.toString());
            return this;
        }
    }

    public class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private String label;
        private final AccountsApplication parentPanel;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox, AccountsApplication parentPanel) {
            super(checkBox);
            this.parentPanel = parentPanel;
            button = new JButton();
            button.setOpaque(true);
            button.setBorderPainted(false);
            button.setFont(new Font("SansSerif", Font.BOLD, 14));
            button.setBackground(new Color(46, 204, 113)); // Green
            button.setForeground(Color.WHITE);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            button.addActionListener((ActionEvent e) -> {
                String accId = parentPanel.table.getValueAt(selectedRow, 4).toString(); // Account Number is column 4
                int confirm = JOptionPane.showConfirmDialog(button, "Are you sure you want to approve this account?", "Confirm Approval", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        boolean isApproved = accountController.handleApproveAccount(accId);
                        JOptionPane.showMessageDialog(button, "Account approved successfully!");

                        // Update the table data after approval
                        parentPanel.tableModel.setValueAt("Approved", selectedRow, 9); // Update the 'Approve' button to 'Approved'

                        // Refresh the table data (reload if necessary)
                        parentPanel.loadTableData();
                    } catch (RuntimeException ex) {
                        JOptionPane.showMessageDialog(button, "Error: " + ex.getMessage(), "Approval Failed", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "Approve" : value.toString();
            button.setText(label);
            selectedRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            return super.stopCellEditing();
        }
    }

}
