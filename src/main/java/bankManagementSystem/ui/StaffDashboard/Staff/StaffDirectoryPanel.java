package main.java.bankManagementSystem.ui.StaffDashboard.Staff;

import main.java.bankManagementSystem.controller.StaffDashboard.StaffController;
import main.java.bankManagementSystem.model.StaffModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class StaffDirectoryPanel extends JPanel {

    private static final Color HEADER_BG = new Color(32, 136, 203);

    private final JTable table;
    private final DefaultTableModel model;
    private final TableRowSorter<DefaultTableModel> sorter;
    private final JTextField nameFilterField;

    public StaffDirectoryPanel(int staffId) {

        StaffController staffController = new StaffController();
        List<StaffModel> originalData = staffController.handleGetStaffDataOtherThanId(staffId);

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(HEADER_BG, 2),
                "Staff Directory",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 18),
                HEADER_BG));

        String[] cols = {"ID", "Name", "Email", "Phone", "Branch ID"};

        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int col) {
                return (col == 0 || col == 4) ? Integer.class : String.class;
            }
        };

        table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row))
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 250, 255));
                else
                    c.setBackground(new Color(184, 207, 229));
                return c;
            }
        };
        styliseTable(table);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // === Filter by Name ===
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(HEADER_BG, 1),
                "Filter",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                HEADER_BG));

        filterPanel.add(new JLabel("Name:"));
        nameFilterField = new JTextField(20);
        nameFilterField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filterPanel.add(nameFilterField);

        nameFilterField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { applyNameFilter(); }
            public void removeUpdate(DocumentEvent e) { applyNameFilter(); }
            public void changedUpdate(DocumentEvent e) { applyNameFilter(); }
        });

        // === Print button ===
        JButton printBtn = new JButton("Print Table");
        styleMainButton(printBtn);
        printBtn.addActionListener(this::printTable);
        JPanel printPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
        printPane.setBackground(Color.WHITE);
        printPane.add(printBtn);

        add(filterPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(printPane, BorderLayout.SOUTH);

        // Load initial data
        for (StaffModel st : originalData) {
            model.addRow(new Object[]{
                    st.getStaffId(),
                    st.getStaffName(),
                    st.getStaffMail(),
                    st.getStaffPhone(),
                    st.getStaffBranchId()
            });
        }
    }

    private void applyNameFilter() {
        String query = nameFilterField.getText().trim();
        if (query.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query, 1)); // column 1 = Name
        }
    }

    private void printTable(ActionEvent e) {
        try {
            if (table.print()) {
                JOptionPane.showMessageDialog(this, "Printing complete.",
                        "Print", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Print failed: " + ex.getMessage(),
                    "Print", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void styliseTable(JTable t) {
        t.setRowHeight(28);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTableHeader h = t.getTableHeader();
        h.setFont(new Font("Segoe UI", Font.BOLD, 15));
        h.setBackground(HEADER_BG);
        h.setForeground(Color.WHITE);
        t.setGridColor(new Color(220, 220, 220));
    }

    private static void styleMainButton(AbstractButton b) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBackground(HEADER_BG);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(150, 38));
    }
}
