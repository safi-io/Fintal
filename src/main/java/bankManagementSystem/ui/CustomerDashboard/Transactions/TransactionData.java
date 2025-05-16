package main.java.bankManagementSystem.ui.CustomerDashboard.Transactions;

import main.java.bankManagementSystem.controller.CustomerDashboard.TransactionsController;
import main.java.bankManagementSystem.model.TransactionCustomerModel;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class TransactionData extends JPanel {

    private final JTable table;
    private final DefaultTableModel model;

    private final List<TransactionCustomerModel> originalData;
    private final TransactionsController transactionsController;

    public TransactionData() {
        System.out.println("hello");
        transactionsController = new TransactionsController();

        // Replace hardcoded account number with actual dynamic value
        originalData = transactionsController.handleGetAllTransactionsByAccountNumber("20");

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(32, 136, 203), 2),
                "Transaction History",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 18),
                new Color(32, 136, 203)));

        String[] cols = {
                "Type", "Amount", "Description",
                "Counter‑party A/C", "Counter‑party Name",
                "Date & Time"
        };

        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer rend, int row, int col) {
                Component c = super.prepareRenderer(rend, row, col);
                if (!isRowSelected(row)) {
                    String type = getModel().getValueAt(row, 0).toString();
                    c.setBackground("CREDIT".equalsIgnoreCase(type)
                            ? new Color(230, 255, 230)
                            : new Color(255, 235, 235));
                } else {
                    c.setBackground(new Color(184, 207, 229));
                }
                return c;
            }
        };

        styliseTable(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        filterBar.setBackground(Color.WHITE);

        JComboBox<String> typeBox = new JComboBox<>(new String[]{"All", "CREDIT", "DEBIT"});
        prettifyCombo(typeBox);
        filterBar.add(new JLabel("Type:"));
        filterBar.add(typeBox);

        typeBox.addActionListener(e -> applyTypeFilter(typeBox.getSelectedItem().toString()));

        JButton print = new JButton("Print Table");
        styleMainButton(print);
        print.addActionListener(this::printTable);

        JPanel printPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
        printPane.setBackground(Color.WHITE);
        printPane.add(print);

        add(filterBar, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(printPane, BorderLayout.SOUTH);

        // Load all initially
        applyTypeFilter("All");
    }

    private void applyTypeFilter(String typeSel) {
        model.setRowCount(0);
        for (TransactionCustomerModel tx : originalData) {
            if ("All".equals(typeSel) || tx.getTransactionType().equalsIgnoreCase(typeSel)) {
                model.addRow(new Object[]{
                        tx.getTransactionType(),
                        tx.getTransactionAmount(),
                        tx.getTransactionDescription(),
                        tx.getCounterPartyAccountNumber(),
                        tx.getCounterPartyName(),
                        tx.getTimestamp().toString()
                });
            }
        }
    }

    private static void styliseTable(JTable t) {
        t.setRowHeight(28);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTableHeader h = t.getTableHeader();
        h.setFont(new Font("Segoe UI", Font.BOLD, 15));
        h.setBackground(new Color(32, 136, 203));
        h.setForeground(Color.WHITE);
        t.setGridColor(new Color(220, 220, 220));
        t.getColumnModel().getColumn(1).setCellRenderer(rightAlign());
    }

    private static void prettifyCombo(JComboBox<?> box) {
        box.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        box.setBackground(Color.WHITE);
    }

    private static void styleMainButton(AbstractButton b) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBackground(new Color(32, 136, 203));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(150, 38));
    }

    private void printTable(ActionEvent e) {
        try {
            if (table.print()) {
                JOptionPane.showMessageDialog(this, "Printing complete.",
                        "Print", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Print failed: " + ex.getMessage(),
                    "Print", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static DefaultTableCellRenderer rightAlign() {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.RIGHT);
        return r;
    }
}
