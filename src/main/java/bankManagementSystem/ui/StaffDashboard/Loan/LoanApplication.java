package main.java.bankManagementSystem.ui.StaffDashboard.Loan;

import main.java.bankManagementSystem.controller.StaffDashboard.LoanController;
import main.java.bankManagementSystem.model.LoanApplicationModel;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LoanApplication extends JPanel {

    private static final Color HEADER_BG = new Color(32, 136, 203);  // Blue
    private static final Color APPROVE_GREEN = new Color(46, 204, 113);

    private JTable table;
    private DefaultTableModel tableModel;
    private final LoanController loanController;

    public LoanApplication() {
        loanController = new LoanController();

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(HEADER_BG, 2),
                "Loan Applications",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 18),
                HEADER_BG
        ));

        createTable();

        // Print Button Panel
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
                "Loan ID", "Amount Requested", "Tenure (months)", "Purpose",
                "Date Applied", "Note", "Account Number", "Approve"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only Approve button editable (last column)
                return column == 7;
            }
        };

        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                if (col != 7) { // skip approve button column
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

        // FIX: Use 7 here for Approve column index
        TableColumn approveCol = table.getColumnModel().getColumn(7);
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
        List<LoanApplicationModel> loans;

        try {
            loans = loanController.handleGetLoanApplications();
            if (loans.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No Loan Applications in System", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Failed to load loan applications: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (LoanApplicationModel loan : loans) {
            tableModel.addRow(new Object[]{
                    loan.getLoanID(),
                    loan.getLoanAmount().toString(),
                    loan.getLoanTenure(),
                    loan.getLoanPurpose(),
                    loan.getLoanDate() != null ? loan.getLoanDate().format(dtf) : "",
                    loan.getLoanNote(),
                    loan.getLoanAccountNumber(),
                    "Approve"
            });
        }
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
            setBackground(APPROVE_GREEN);
            return this;
        }
    }

    public class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private final LoanApplication parentPanel;
        private int selectedRow;
        private String label;

        public ButtonEditor(JCheckBox checkBox, LoanApplication parentPanel) {
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
                int loanId = (int) parentPanel.table.getValueAt(selectedRow, 0);
                BigDecimal loanAmount = new BigDecimal(parentPanel.table.getValueAt(selectedRow, 1).toString());
                String accountNumber = parentPanel.table.getValueAt(selectedRow, 6).toString();

                try {
                    // Approve and disburse full loan amount without asking user
                    loanController.handleApproveLoanAndDisbruse(loanId, loanAmount, accountNumber);
                    JOptionPane.showMessageDialog(button, "Loan approved and disbursed successfully!");
                    parentPanel.loadTableData();
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(button, "Error: " + ex.getMessage(), "Approval Failed", JOptionPane.ERROR_MESSAGE);
                }

                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "Approve" : value.toString();
            button.setText(label);
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
