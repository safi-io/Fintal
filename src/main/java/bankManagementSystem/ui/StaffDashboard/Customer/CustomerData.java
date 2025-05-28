package main.java.bankManagementSystem.ui.StaffDashboard.Customer;

import main.java.bankManagementSystem.controller.StaffDashboard.CustomerController;
import main.java.bankManagementSystem.model.CustomerModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.text.MessageFormat;
import java.util.List;

public class CustomerData extends JPanel {

    /* ───  Palette & Fonts shared with StaffDirectoryPanel  ─────────────────── */
    private static final Color HEADER_BG = new Color(32, 136, 203);       // same blue
    private static final Color STRIPE_BG = new Color(245, 250, 255);      // zebra odd rows
    private static final Color SELECT_BG = new Color(184, 207, 229);      // selection
    private static final Font  CELL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font  HEADER_FONT = new Font("Segoe UI", Font.BOLD, 15);

    /* ───  UI state  ────────────────────────────────────────────────────────── */
    private final JTextField cnicField;
    private final JTextField nameField;
    private final JTable     table;
    private final DefaultTableModel model;
    private final TableRowSorter<DefaultTableModel> sorter;
    private final CustomerController customerController;

    public CustomerData() {

        /* ─ general panel ─ */
        customerController = new CustomerController();
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(HEADER_BG, 2),
                "Customer Directory",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 18),
                HEADER_BG));

        /* ─ top (search) bar ─ */
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 6));
        searchBar.setBackground(Color.WHITE);

        searchBar.add(new JLabel("Filter by CNIC:"));
        cnicField = new JTextField(16);
        prettifyTextField(cnicField);
        searchBar.add(cnicField);

        searchBar.add(new JLabel("Filter by Name:"));
        nameField = new JTextField(16);
        prettifyTextField(nameField);
        searchBar.add(nameField);

        add(searchBar, BorderLayout.NORTH);

        /* ─ table ─ */
        String[] cols = {"CNIC", "Name", "Mail", "Phone", "DOB"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row))
                    c.setBackground(row % 2 == 0 ? Color.WHITE : STRIPE_BG);
                else
                    c.setBackground(SELECT_BG);
                return c;
            }
        };
        styliseTable(table);

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        add(new JScrollPane(table), BorderLayout.CENTER);

        /* ─ bottom pane (print) ─ */
        JButton printBtn = new JButton("Print Table");
        styleMainButton(printBtn);
        printBtn.addActionListener(e -> printTable());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setBackground(Color.WHITE);
        bottom.add(printBtn);
        add(bottom, BorderLayout.SOUTH);

        /* ─ listeners ─ */
        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
        };
        cnicField.getDocument().addDocumentListener(dl);
        nameField.getDocument().addDocumentListener(dl);

        /* ─ data ─ */
        loadAllCustomers();
    }

    /* ───────────────────────── helpers ───────────────────────── */

    private void loadAllCustomers() {
        model.setRowCount(0);
        try {
            List<CustomerModel> list = customerController.handleGetAllCustomers();
            for (CustomerModel c : list)
                model.addRow(new Object[]{
                        c.getCustomerCNIC(),
                        c.getCustomerName(),
                        c.getCustomerMail(),
                        c.getCustomerPhone(),
                        c.getCustomerDOB()
                });
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Failed to load customers: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filter() {
        String cnic = cnicField.getText().trim();
        String name = nameField.getText().trim();

        RowFilter<DefaultTableModel, Object> cnicF = cnic.isEmpty() ? null
                : RowFilter.regexFilter("^" + cnic, 0);
        RowFilter<DefaultTableModel, Object> nameF = name.isEmpty() ? null
                : RowFilter.regexFilter("(?i)" + name, 1);

        if (cnicF == null && nameF == null) {
            sorter.setRowFilter(null);
        } else if (cnicF != null && nameF != null) {
            sorter.setRowFilter(RowFilter.andFilter(List.of(cnicF, nameF)));
        } else {
            sorter.setRowFilter(cnicF != null ? cnicF : nameF);
        }
    }

    private void printTable() {
        try {
            boolean done = table.print(JTable.PrintMode.FIT_WIDTH,
                    new MessageFormat("Customer Table"),
                    new MessageFormat("Page - {0}"));
            if (!done)
                JOptionPane.showMessageDialog(this, "Printing cancelled.",
                        "Print", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Print failed: " + ex.getMessage(),
                    "Print", JOptionPane.ERROR_MESSAGE);
        }
    }

    /* ─ visual utils (identical to StaffDirectoryPanel) ─ */

    private static void styliseTable(JTable t) {
        t.setRowHeight(28);
        t.setFont(CELL_FONT);
        JTableHeader h = t.getTableHeader();
        h.setFont(HEADER_FONT);
        h.setBackground(HEADER_BG);
        h.setForeground(Color.WHITE);
        t.setGridColor(new Color(220, 220, 220));
    }

    private static void prettifyTextField(JTextField f) {
        f.setFont(CELL_FONT);
        f.setPreferredSize(new Dimension(170, 28));
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
