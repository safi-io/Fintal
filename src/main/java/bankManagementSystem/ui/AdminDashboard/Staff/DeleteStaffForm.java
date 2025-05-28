package main.java.bankManagementSystem.ui.AdminDashboard.Staff;

import main.java.bankManagementSystem.controller.AdminDashboard.StaffController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class DeleteStaffForm extends JPanel {

    /* ===== palette & typography (matches other forms) ================= */
    private static final Color BG_COLOR       = new Color(250, 252, 254);
    private static final Color CARD_BG        = Color.WHITE;
    private static final Color PRIMARY        = new Color(255, 75, 75);   // red‑accent for delete
    private static final Color PRIMARY_HOVER  = new Color(220, 50, 50);
    private static final Color TEXT_PRIMARY   = new Color(33, 33, 33);
    private static final Color TEXT_SECONDARY = new Color(120, 120, 120);
    private static final Color ACCENT         = new Color(48, 209, 88);   // success green
    private static final Color ERROR_COLOR    = new Color(255, 69, 58);

    /* ===== fields & controller ======================================== */
    private final JComboBox<String> staffCombo = new JComboBox<>();
    private final JLabel statusLbl             = new JLabel(" ");
    private final StaffController staffCtrl    = new StaffController();

    public DeleteStaffForm() {
        /* --- outer panel ------------------------------------------------ */
        setLayout(new GridBagLayout());
        setBackground(BG_COLOR);

        /* --- card ------------------------------------------------------- */
        RoundedShadowPanel card = new RoundedShadowPanel(20);
        card.setBackground(CARD_BG);
        card.setBorder(new EmptyBorder(40, 50, 50, 50));
        card.setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets   = new Insets(15, 20, 15, 20);
        gc.fill     = GridBagConstraints.HORIZONTAL;
        gc.weightx  = 1;
        gc.anchor   = GridBagConstraints.WEST;

        int row = 0;

        /* --- heading ---------------------------------------------------- */
        gc.gridx = 0; gc.gridy = row++; gc.gridwidth = 2;
        JLabel heading = new JLabel("Delete Staff");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setForeground(TEXT_PRIMARY);
        card.add(heading, gc);
        gc.gridwidth = 1;

        /* --- staff ID combo -------------------------------------------- */
        addLabeledField(card, gc, row++, "Select Staff", staffCombo, "Choose staff to delete");
        styleCombo(staffCombo);

        /* --- delete button --------------------------------------------- */
        gc.gridx = 0; gc.gridy = row++; gc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        btnPanel.setOpaque(false);

        JButton deleteBtn = new JButton("Delete Staff");
        styleFlatButton(deleteBtn, PRIMARY, PRIMARY_HOVER);
        btnPanel.add(deleteBtn);
        card.add(btnPanel, gc);

        /* --- status label ---------------------------------------------- */
        gc.gridy = row++;
        statusLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(statusLbl, gc);

        add(card);

        /* --- load IDs and listeners ------------------------------------ */
        refreshStaffCombo();
        deleteBtn.addActionListener(e -> deleteSelectedStaff());
    }

    /* ================= delete logic ==================================== */
    private void deleteSelectedStaff() {
        String item = (String) staffCombo.getSelectedItem();
        if (item == null) { setStatus("No staff to delete.", true); return; }

        String idStr   = item.split(" _ ")[0];
        int confirm = JOptionPane.showConfirmDialog(
                this, "Are you sure you want to delete staff ID " + idStr + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            boolean ok = staffCtrl.handleDeleteStaff(idStr);
            if (ok) {
                setStatus("Staff deleted successfully!", false);
                refreshStaffCombo();
            } else {
                setStatus("Delete failed.", true);
            }
        } catch (RuntimeException ex) {
            setStatus(ex.getMessage(), true);
        }
    }

    /* ================= combo refresh =================================== */
    private void refreshStaffCombo() {
        try {
            List<String> list = staffCtrl.handleGetStaffIdNameList();
            staffCombo.setModel(new DefaultComboBoxModel<>(list.toArray(new String[0])));
            if (list.isEmpty()) setStatus("No staff available.", true);
        } catch (RuntimeException ex) {
            setStatus("Failed to load staff IDs.", true);
        }
    }

    /* ================= helper: status label =========================== */
    private void setStatus(String msg, boolean err) {
        statusLbl.setText(msg);
        statusLbl.setForeground(err ? ERROR_COLOR : ACCENT.darker());
    }

    /* ================= helper: add label + component ================== */
    private void addLabeledField(JPanel parent, GridBagConstraints gc, int row,
                                 String label, JComponent field, String tip) {
        gc.gridx = 0; gc.gridy = row;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(TEXT_SECONDARY);
        parent.add(lbl, gc);

        gc.gridx = 1;
        parent.add(field, gc);
        field.setToolTipText(tip);
    }

    /* ================= styling helpers ================================ */
    private void styleCombo(JComboBox<?> c) {
        c.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        ((JComponent) c.getRenderer()).setBorder(new EmptyBorder(2,5,2,5));
        c.setBackground(CARD_BG);
        c.setForeground(TEXT_PRIMARY);
        c.setPreferredSize(new Dimension(250, 30));
    }

    private void styleFlatButton(JButton b, Color base, Color hover) {
        b.setBackground(base);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setBorder(BorderFactory.createEmptyBorder(8,18,8,18));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true); b.setContentAreaFilled(true);
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e){ b.setBackground(hover); }
            public void mouseExited (java.awt.event.MouseEvent e){ b.setBackground(base); }
        });
    }

    /* ================= rounded‑shadow card ============================ */
    private static class RoundedShadowPanel extends JPanel {
        private final int r;
        RoundedShadowPanel(int r) { this.r = r; setOpaque(false); setLayout(new GridBagLayout()); }
        @Override protected void paintComponent(Graphics g) {
            int gap = 6, off = 4;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0,0,0,30));
            g2.fillRoundRect(off, off, getWidth()-gap, getHeight()-gap, r, r);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth()-gap-off, getHeight()-gap-off, r, r);
            g2.dispose(); super.paintComponent(g);
        }
    }
}
