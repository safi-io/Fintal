package main.java.bankManagementSystem.ui.StaffDashboard.Accounts;

import main.java.bankManagementSystem.controller.StaffDashboard.AccountController;
import main.java.bankManagementSystem.model.CustomerAccountBranchModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.text.MessageFormat;
import java.util.List;

public class AccountData extends JPanel {

    private static final Color HEADER_BG = new Color(32, 136, 203);
    private static final Color STRIPE_BG = new Color(245, 250, 255);
    private static final Color SELECT_BG = new Color(184, 207, 229);
    private static final Font CELL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 15);

    private final JTextField accountNumberField;
    private final JTextField cnicField;
    private final JTable table;
    private final DefaultTableModel model;
    private final TableRowSorter<DefaultTableModel> sorter;

    public AccountData() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(HEADER_BG, 2),
                "Account Directory",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 18),
                HEADER_BG));

        /* ── Filter Panel Wrapped in Border ── */
        JPanel filterPanelContainer = new JPanel(new BorderLayout());
        filterPanelContainer.setBackground(Color.WHITE);
        filterPanelContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                "Search Filters",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(80, 80, 80)
        ));

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchBar.setBackground(Color.WHITE);

        JLabel accLabel = new JLabel("Account No:");
        accLabel.setFont(CELL_FONT);
        searchBar.add(accLabel);

        accountNumberField = new JTextField(15);
        prettifyTextField(accountNumberField);
        searchBar.add(accountNumberField);

        JLabel cnicLabel = new JLabel("CNIC:");
        cnicLabel.setFont(CELL_FONT);
        searchBar.add(cnicLabel);

        cnicField = new JTextField(15);
        prettifyTextField(cnicField);
        searchBar.add(cnicField);

        filterPanelContainer.add(searchBar, BorderLayout.CENTER);
        add(filterPanelContainer, BorderLayout.NORTH);

        /* ── Table ── */
        String[] cols = {"CNIC", "Name", "Mail", "Phone", "Account No", "Balance", "Type", "Opening Date", "Branch ID", "Branch Name"};

        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }

            @Override public Class<?> getColumnClass(int col) {
                return String.class;
            }
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

        /* ── Buttons Panel ── */
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottom.setBackground(Color.WHITE);

        JButton printBtn = new JButton("Print Table");
        styleMainButton(printBtn);
        bottom.add(printBtn);

        JButton deleteBtn = new JButton("Delete Selected Account");
        styleDangerButton(deleteBtn);
        bottom.add(deleteBtn);

        add(bottom, BorderLayout.SOUTH);

        /* ── Listeners ── */
        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
        };
        accountNumberField.getDocument().addDocumentListener(dl);
        cnicField.getDocument().addDocumentListener(dl);

        printBtn.addActionListener(e -> printTable());
        deleteBtn.addActionListener(e -> deleteSelected());

        loadAllAccounts();
    }

    private void loadAllAccounts() {
        try {
            List<CustomerAccountBranchModel> accs = new AccountController().handleGetAllAccounts();
            model.setRowCount(0);
            for (CustomerAccountBranchModel a : accs) {
                model.addRow(new Object[]{
                        a.getCustomerCNIC(),
                        a.getCustomerName(),
                        a.getCustomerMail(),
                        a.getCustomerPhone(),
                        a.getAccountNumber(),
                        a.getAccountCurrentBalance(),
                        a.getAccountType(),
                        a.getAccountOpeningDate(),
                        a.getAccountCustomerBranchID(),
                        a.getCustomerBranch()
                });
            }
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Failed to load accounts: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filter() {
        String acc = accountNumberField.getText().trim();
        String cnic = cnicField.getText().trim();

        RowFilter<DefaultTableModel, Object> accF = acc.isEmpty() ? null : RowFilter.regexFilter("(?i)" + acc, 4);
        RowFilter<DefaultTableModel, Object> cnicF = cnic.isEmpty() ? null : RowFilter.regexFilter("(?i)" + cnic, 0);

        if (accF == null && cnicF == null) {
            sorter.setRowFilter(null);
        } else if (accF != null && cnicF != null) {
            sorter.setRowFilter(RowFilter.andFilter(List.of(accF, cnicF)));
        } else {
            sorter.setRowFilter(accF != null ? accF : cnicF);
        }
    }

    private void printTable() {
        try {
            boolean ok = table.print(JTable.PrintMode.FIT_WIDTH,
                    new MessageFormat("Account Table"),
                    new MessageFormat("Page - {0}"));
            if (!ok)
                JOptionPane.showMessageDialog(this, "Printing cancelled.", "Print", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Print failed: " + ex.getMessage(), "Print", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelected() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an account first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(viewRow);
        String accNo = model.getValueAt(modelRow, 4).toString();

        int ok = JOptionPane.showConfirmDialog(this,
                "Delete account number " + accNo + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;

        try {
            new AccountController().handleAccountDelete(accNo);
            model.removeRow(modelRow);
            JOptionPane.showMessageDialog(this, "Account deleted.");
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Failed to delete: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

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
        f.setPreferredSize(new Dimension(160, 28));
    }

    private static void styleMainButton(AbstractButton b) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBackground(HEADER_BG);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(180, 38));
    }

    private static void styleDangerButton(AbstractButton b) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBackground(new Color(220, 20, 60));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(220, 38));
    }
}
