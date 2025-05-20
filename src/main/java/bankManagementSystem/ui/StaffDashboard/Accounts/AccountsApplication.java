package main.java.bankManagementSystem.ui.StaffDashboard.Accounts;

import main.java.bankManagementSystem.controller.StaffDashboard.AccountController;
import main.java.bankManagementSystem.model.CustomerAccountBranchModel;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class AccountsApplication extends JPanel {

    private static final Color HEADER_BG = new Color(32, 136, 203);  // Blue
    private static final Color APPROVE_GREEN = new Color(46, 204, 113);

    private JTable table;
    private DefaultTableModel tableModel;
    private final AccountController accountController;

    public AccountsApplication() {
        accountController = new AccountController();

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(HEADER_BG, 2),
                "Account Applications",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 18),
                HEADER_BG
        ));

        createTable();

        // === Print Button Panel ===
        JButton printButton = new JButton("Print Table");
        styleMainButton(printButton);
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

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(printButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void createTable() {
        String[] columnNames = {
                "CNIC", "Name", "Mail", "Phone", "Account No.",
                "Type", "Opening Date", "Branch ID", "Branch Name", "Approve"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) {
                return column == 9;
            }
        };

        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                // Don't change background color for the button column
                if (col != 9) {
                    if (!isRowSelected(row))
                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 250, 255));
                    else
                        c.setBackground(new Color(184, 207, 229));
                }
                return c;
            }
        };

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(32);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(HEADER_BG);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        // Assign renderer & editor to Approve button column
        TableColumn approveCol = table.getColumnModel().getColumn(9);
        approveCol.setCellRenderer(new ButtonRenderer());
        approveCol.setCellEditor(new ButtonEditor(new JCheckBox(), this));
        approveCol.setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        loadTableData();
    }

    public void loadTableData() {
        tableModel.setRowCount(0);
        List<CustomerAccountBranchModel> accounts;

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

        for (CustomerAccountBranchModel account : accounts) {
            tableModel.addRow(new Object[]{
                    formatTitleCase(account.getCustomerCNIC()),
                    formatTitleCase(account.getCustomerName()),
                    formatTitleCase(account.getCustomerMail()),
                    formatTitleCase(account.getCustomerPhone()),
                    formatTitleCase(account.getAccountNumber()),
                    formatTitleCase(account.getAccountType()),
                    formatTitleCase(account.getAccountOpeningDate().toString()),
                    formatTitleCase(account.getAccountCustomerBranchID()),
                    formatTitleCase(account.getCustomerBranch()),
                    "Approve"
            });
        }
    }

    private String formatTitleCase(String text) {
        if (text == null || text.trim().isEmpty()) return text;
        String[] words = text.trim().split("\\s+");
        StringBuilder formatted = new StringBuilder();
        for (String word : words) {
            formatted.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1).toLowerCase()).append(" ");
        }
        return formatted.toString().trim();
    }

    private static void styleMainButton(AbstractButton b) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBackground(HEADER_BG);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(150, 38));
    }

    public static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setContentAreaFilled(true);
            setBorderPainted(false);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setBackground(APPROVE_GREEN);
            setForeground(Color.WHITE);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText((value == null) ? "Approve" : value.toString());
            // Always ensure the button keeps its green color
            setBackground(APPROVE_GREEN);
            return this;
        }
    }

    public class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private final AccountsApplication parentPanel;
        private int selectedRow;
        private String label;

        public ButtonEditor(JCheckBox checkBox, AccountsApplication parentPanel) {
            super(checkBox);
            this.parentPanel = parentPanel;

            button = new JButton();
            button.setOpaque(true);
            button.setContentAreaFilled(true);
            button.setBorderPainted(false);
            button.setFont(new Font("Segoe UI", Font.BOLD, 14));
            button.setBackground(APPROVE_GREEN);
            button.setForeground(Color.WHITE);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            button.addActionListener((ActionEvent e) -> {
                String accId = parentPanel.table.getValueAt(selectedRow, 4).toString();
                int confirm = JOptionPane.showConfirmDialog(button, "Approve this account?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        accountController.handleApproveAccount(accId);
                        JOptionPane.showMessageDialog(button, "Account approved successfully!");
                        parentPanel.loadTableData();
                    } catch (RuntimeException ex) {
                        JOptionPane.showMessageDialog(button, "Error: " + ex.getMessage(), "Approval Failed", JOptionPane.ERROR_MESSAGE);
                    }
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "Approve" : value.toString();
            button.setText(label);
            // Always ensure the button keeps its green color when being edited
            button.setBackground(APPROVE_GREEN);
            selectedRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }
    }
}