package main.java.bankManagementSystem.ui.AdminDashboard.Transactions;

import main.java.bankManagementSystem.controller.CustomerDashboard.TransactionsController;
import main.java.bankManagementSystem.model.TransactionCustomerModel;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ViewTransactionsPanel extends JPanel {

    private final JTextField accField = new JTextField(15);
    private final JButton loadBtn = new JButton("Load");
    private final JButton clearBtn = new JButton("Clear");

    private final JComboBox<String> typeBox = new JComboBox<>(new String[]{"All", "CREDIT", "DEBIT"});

    private final DefaultTableModel model;
    private final JTable table;

    private final TransactionsController controller = new TransactionsController();
    private List<TransactionCustomerModel> currentData;   // holds latest fetch

    public ViewTransactionsPanel() {

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(32, 136, 203), 2), "View Transactions", 0, 0, new Font("Segoe UI", Font.BOLD, 18), new Color(32, 136, 203)));

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        topBar.setBackground(Color.WHITE);

        accField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        accField.setToolTipText("Enter account number");

        styleActionButton(loadBtn, new Color(48, 209, 88));
        styleActionButton(clearBtn, new Color(255, 99, 71));

        topBar.add(new JLabel("Account #:"));
        topBar.add(accField);
        topBar.add(loadBtn);
        topBar.add(clearBtn);

        /* ── FILTER BAR : credit / debit ───────────────────────────── */
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        filterBar.setBackground(Color.WHITE);

        prettifyCombo(typeBox);
        filterBar.add(new JLabel("Type:"));
        filterBar.add(typeBox);

        /* ── combine input & filter to NORTH ───────────────────────── */
        JPanel north = new JPanel(new BorderLayout());
        north.setBackground(Color.WHITE);
        north.add(topBar, BorderLayout.NORTH);
        north.add(filterBar, BorderLayout.SOUTH);
        add(north, BorderLayout.NORTH);

        /* ── TABLE setup ───────────────────────────────────────────── */
        String[] cols = {"Type", "Amount", "Description", "Counter‑party A/C", "Counter‑party Name", "Date & Time"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component comp = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row)) {
                    String t = getModel().getValueAt(row, 0).toString();
                    comp.setBackground("CREDIT".equalsIgnoreCase(t) ? new Color(230, 255, 230) : new Color(255, 235, 235));
                } else comp.setBackground(new Color(184, 207, 229));
                return comp;
            }
        };
        styliseTable(table);
        JScrollPane jsp = new JScrollPane(table);
        jsp.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(jsp, BorderLayout.CENTER);

        /* ── SOUTH : print button ──────────────────────────────────── */
        JButton printBtn = new JButton("Print Table");
        styleActionButton(printBtn, new Color(32, 136, 203));

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));
        south.setBackground(Color.WHITE);
        south.add(printBtn);
        add(south, BorderLayout.SOUTH);

        /* ── Listeners ─────────────────────────────────────────────── */
        loadBtn.addActionListener(e -> loadTransactions());
        clearBtn.addActionListener(e -> clearAll());
        typeBox.addActionListener(e -> applyTypeFilter());
        printBtn.addActionListener(this::printTable);

        // initial state
        toggleTableEnabled(false);
    }

    /* ─────────────────── helpers ─────────────────────────────────────────── */

    private void loadTransactions() {
        String acc = accField.getText().trim();
        if (acc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter account number", "Input", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            currentData = controller.handleGetAllTransactionsByAccountNumber(acc);
            if (currentData == null || currentData.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No Account or Transactions Found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            applyTypeFilter();
            toggleTableEnabled(true);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearAll() {
        accField.setText("");
        model.setRowCount(0);
        currentData = null;
        toggleTableEnabled(false);
    }

    private void applyTypeFilter() {
        if (currentData == null) {
            model.setRowCount(0);
            return;
        }
        String sel = typeBox.getSelectedItem().toString();
        model.setRowCount(0);
        for (TransactionCustomerModel tx : currentData) {
            if ("All".equals(sel) || tx.getTransactionType().equalsIgnoreCase(sel)) {
                model.addRow(new Object[]{tx.getTransactionType(), tx.getTransactionAmount(), tx.getTransactionDescription(), tx.getCounterPartyAccountNumber(), tx.getCounterPartyName(), tx.getTimestamp().toString()});
            }
        }
    }

    private void printTable(ActionEvent e) {
        try {
            if (table.print())
                JOptionPane.showMessageDialog(this, "Printing complete.", "Print", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Print failed: " + ex.getMessage(), "Print", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void toggleTableEnabled(boolean enabled) {
        table.setEnabled(enabled);
        typeBox.setEnabled(enabled);
    }

    /* ── styling utilities (copied from your existing panel) ───────── */
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

    private static DefaultTableCellRenderer rightAlign() {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.RIGHT);
        return r;
    }

    private static void prettifyCombo(JComboBox<?> box) {
        box.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        box.setBackground(Color.WHITE);
    }

    private static void styleActionButton(AbstractButton b, Color bg) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(120, 32));
    }
}
