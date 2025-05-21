package main.java.bankManagementSystem.ui.BeneficiaryDashboard.Bills;

import com.opencsv.CSVReader;
import main.java.bankManagementSystem.controller.BeneficiaryDashboard.BillController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.List;

/**
 * Lets a beneficiary upload a CSV of bills and then push the parsed rows
 * to the database.
 */
public class BillUploadPanel extends JPanel {

    /* ---------- styling constants (same palette as LoanDashboard) ---------- */
    private static final Color BG_COLOR = new Color(248, 250, 252);
    private static final Color PRIMARY = new Color(11, 132, 255);
    private static final Color PRIMARY_DARK = PRIMARY.darker();
    private static final Color ACCENT = new Color(46, 204, 113);
    private static final Color ERROR_COLOR = new Color(255, 71, 64);
    private static final Color TEXT_SECONDARY = new Color(120, 120, 120);
    private static final Color HEADER_BG = new Color(32, 136, 203);

    /* ---------- UI widgets ---------- */
    private final DefaultTableModel tableModel = new DefaultTableModel();
    private final JTable table = new JTable(tableModel);

    private final JButton uploadBtn = new JButton("Upload CSV File");
    private final JButton sendBtn = new JButton("Send to Database");
    private final JLabel feedback = new JLabel(" ");

    private String beneficiaryAccountNumber;
    private final BillController billController;

    public BillUploadPanel(String beneficiaryAccountNumber) {
        this.beneficiaryAccountNumber = beneficiaryAccountNumber;
        this.billController = new BillController();
        setBackground(BG_COLOR);
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(25, 35, 25, 35));

        add(buildUploadCard(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        add(feedback, BorderLayout.SOUTH);
    }

    /* ────────────────── Upload card (rounded) ────────────────── */
    private JPanel buildUploadCard() {
        RoundedShadowPanel card = new RoundedShadowPanel(18);
        card.setBackground(Color.WHITE);
        card.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 18));

        styleMainButton(uploadBtn);
        styleMainButton(sendBtn);

        uploadBtn.addActionListener(e -> loadCSV());
        sendBtn.addActionListener(e -> sendToDatabase());

        card.add(uploadBtn);
        card.add(sendBtn);
        return card;
    }

    /* ────────────────── Table panel ──────────────────────────── */
    private JPanel buildTablePanel() {
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTableHeader h = table.getTableHeader();
        h.setFont(new Font("Segoe UI", Font.BOLD, 14));
        h.setBackground(HEADER_BG);
        h.setForeground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(HEADER_BG, 2), "Parsed Bills", TitledBorder.LEADING, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 18), HEADER_BG));

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    /* ────────────────── CSV loader ───────────────────────────── */
    private void loadCSV() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Select CSV file");
        int res = fc.showOpenDialog(this);
        if (res != JFileChooser.APPROVE_OPTION) return;

        try (CSVReader reader = new CSVReader(new FileReader(fc.getSelectedFile()))) {
            List<String[]> rows = reader.readAll();
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);

            if (!rows.isEmpty()) {
                // header
                for (String col : rows.get(0)) tableModel.addColumn(col);
                // data
                for (int i = 1; i < rows.size(); i++) tableModel.addRow(rows.get(i));
            }
            showFeedback("Loaded " + (rows.size() - 1) + " rows.", false);
        } catch (Exception ex) {
            showFeedback("Failed to read CSV: " + ex.getMessage(), true);
            ex.printStackTrace();
        }
    }

    /* ────────────────── Send to DB stub ──────────────────────── */

    /**
     * Push parsed rows to DB with feedback & basic error handling
     */
    private void sendToDatabase() {
        int rows = tableModel.getRowCount();
        if (rows == 0) {
            showFeedback("⚠️  No data to send.", true);
            return;
        }

        int success = 0, failed = 0;
        StringBuilder failedRows = new StringBuilder();

        for (int i = 0; i < rows; i++) {
            try {
                String refNo = tableModel.getValueAt(i, 0).toString().trim();
                String name = tableModel.getValueAt(i, 1).toString().trim();
                BigDecimal amt = new BigDecimal(tableModel.getValueAt(i, 2).toString().trim());

                // Call your DAO / controller
                billController.handleCreateBill(beneficiaryAccountNumber, refNo, name, amt);
                success++;

            } catch (Exception ex) {                       // catch & continue
                failed++;
                failedRows.append("Row ").append(i + 1)
                        .append(": ").append(ex.getMessage()).append("\n");
            }
        }

        /* ---------- user feedback ---------- */
        if (failed == 0) {
            tableModel.setRowCount(0);                     // clear only on full success
            showFeedback("✅ " + success + " bill(s) uploaded successfully.", false);
        } else {
            tableModel.setRowCount(0);                     // clear only on full success
            showFeedback("Saved " + success + ", failed " + failed + ". Details shown.", true);
            JOptionPane.showMessageDialog(
                    this,
                    "Some rows could not be saved:\n\n" + failedRows,
                    "Upload Errors",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }


    /* ────────────────── helpers ───────────────────────────────── */
    private void styleMainButton(JButton b) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 15));
        b.setForeground(Color.WHITE);
        b.setBackground(PRIMARY);
        b.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                b.setBackground(PRIMARY_DARK);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                b.setBackground(PRIMARY);
            }
        });
    }

    private void showFeedback(String msg, boolean error) {
        feedback.setText(msg);
        feedback.setForeground(error ? ERROR_COLOR : ACCENT.darker());
    }

    /* ────────────────── inner rounded shadow panel ───────────── */
    private static class RoundedShadowPanel extends JPanel {
        private final int r;

        RoundedShadowPanel(int r) {
            this.r = r;
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            int gap = 6, off = 4;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 25));
            g2.fillRoundRect(off, off, getWidth() - gap, getHeight() - gap, r, r);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - gap - off, getHeight() - gap - off, r, r);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
