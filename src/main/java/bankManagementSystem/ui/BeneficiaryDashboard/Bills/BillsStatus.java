package main.java.bankManagementSystem.ui.BeneficiaryDashboard.Bills;

import main.java.bankManagementSystem.controller.BeneficiaryDashboard.BillController;
import main.java.bankManagementSystem.model.BillModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class BillsStatus extends JPanel {

    private final JTable table;
    private final DefaultTableModel model;
    private final List<BillModel> originalData;
    private final BillController controller;

    public BillsStatus(String beneficiaryAccountNumber) {
        controller = new BillController();

        // Replace with dynamic account number as needed
        originalData = controller.handleGetAllBills(beneficiaryAccountNumber);

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(32, 136, 203), 2),
                "Bills Overview",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 18),
                new Color(32, 136, 203)));

        String[] cols = {
                "Reference #", "Consumer Name", "Amount", "Status"
        };

        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer rend, int row, int col) {
                Component c = super.prepareRenderer(rend, row, col);
                if (!isRowSelected(row)) {
                    String status = getModel().getValueAt(row, 3).toString();
                    c.setBackground("Paid".equalsIgnoreCase(status)
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

        JComboBox<String> statusBox = new JComboBox<>(new String[]{"All", "Paid", "Unpaid"});
        prettifyCombo(statusBox);
        filterBar.add(new JLabel("Status:"));
        filterBar.add(statusBox);

        statusBox.addActionListener(e -> applyStatusFilter(statusBox.getSelectedItem().toString()));

        JButton print = new JButton("Print Table");
        styleMainButton(print);
        print.addActionListener(this::printTable);

        JPanel printPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
        printPane.setBackground(Color.WHITE);
        printPane.add(print);

        add(filterBar, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(printPane, BorderLayout.SOUTH);

        // Load initially
        applyStatusFilter("All");
    }

    private void applyStatusFilter(String selected) {
        model.setRowCount(0);
        for (BillModel bill : originalData) {
            boolean isPaid = bill.isPaid();
            if ("All".equals(selected) ||
                    ("Paid".equals(selected) && isPaid) ||
                    ("Unpaid".equals(selected) && !isPaid)) {
                model.addRow(new Object[]{
                        bill.getReferenceNumber(),
                        bill.getConsumerName(),
                        bill.getAmount(),
                        isPaid ? "Paid" : "Unpaid"
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
        t.getColumnModel().getColumn(2).setCellRenderer(rightAlign());
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
