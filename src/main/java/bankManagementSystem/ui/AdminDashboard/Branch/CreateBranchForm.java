package main.java.bankManagementSystem.ui.AdminDashboard.Branch;

import main.java.bankManagementSystem.controller.AdminDashboard.BranchController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CreateBranchForm extends JPanel {

    private final JTextField nameField = new JTextField(20);
    private final JTextField addressField = new JTextField(20);
    private final JTextField phoneField = new JTextField(20);
    private final JLabel statusLbl = new JLabel(" ");

    private final BranchController controller = new BranchController();

    /* --- Color & font constants --- */
    private static final Color BG_COLOR       = new Color(245, 248, 250);
    private static final Color CARD_BG        = Color.WHITE;
    private static final Color PRIMARY        = new Color(48, 120, 245);
    private static final Color PRIMARY_HOVER  = new Color(40, 100, 210);
    private static final Color TEXT_PRIMARY   = new Color(33, 33, 33);
    private static final Color TEXT_SECONDARY = new Color(120, 120, 120);
    private static final Color ACCENT         = new Color(48, 209, 88);
    private static final Color ERROR_COLOR    = new Color(255, 69, 58);

    public CreateBranchForm() {
        setLayout(new GridBagLayout());
        setBackground(BG_COLOR);

        RoundedShadowPanel card = new RoundedShadowPanel(20);
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(40, 50, 50, 50));
        card.setBackground(CARD_BG);

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(15, 20, 15, 20);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        gc.anchor = GridBagConstraints.WEST;

        int row = 0;

        /* --- Heading --- */
        gc.gridx = 0; gc.gridy = row++; gc.gridwidth = 2;
        JLabel heading = new JLabel("Create New Branch");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setForeground(TEXT_PRIMARY);
        card.add(heading, gc);
        gc.gridwidth = 1;

        /* --- Fields --- */
        styleField(nameField);
        styleField(addressField);
        styleField(phoneField);

        addLabeledField(card, gc, row++, "Branch Name", nameField, "Enter branch name");
        addLabeledField(card, gc, row++, "Branch Address", addressField, "Enter branch address");
        addLabeledField(card, gc, row++, "Branch Phone", phoneField, "Enter branch phone number");

        /* --- Submit button --- */
        gc.gridx = 0; gc.gridy = row++; gc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);

        JButton submitBtn = new JButton("Create Branch");
        styleFlatButton(submitBtn, PRIMARY, PRIMARY_HOVER);
        btnPanel.add(submitBtn);

        card.add(btnPanel, gc);

        /* --- Status Label --- */
        gc.gridy = row;
        statusLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(statusLbl, gc);

        add(card);

        /* --- Submit action --- */
        submitBtn.addActionListener(e -> handleSubmit());
    }

    /* === Logic === */
    private void handleSubmit() {
        String name = nameField.getText().trim();
        String addr = addressField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || addr.isEmpty() || phone.isEmpty()) {
            setStatus("All fields are required.", true);
            return;
        }

        try {
            boolean created = controller.handleCreateBranch(name, addr, phone);
            if (created) {
                setStatus("Branch created successfully!", false);
                nameField.setText(""); addressField.setText(""); phoneField.setText("");
            } else {
                setStatus("Failed to create branch.", true);
            }
        } catch (RuntimeException ex) {
            setStatus("Error: " + ex.getMessage(), true);
        }
    }

    /* === Helpers === */
    private void setStatus(String msg, boolean isError) {
        statusLbl.setText(msg);
        statusLbl.setForeground(isError ? ERROR_COLOR : ACCENT.darker());
    }

    private void addLabeledField(JPanel parent, GridBagConstraints gc, int row,
                                 String label, JTextField field, String tooltip) {
        gc.gridx = 0; gc.gridy = row;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(TEXT_SECONDARY);
        parent.add(lbl, gc);

        gc.gridx = 1;
        field.setToolTipText(tooltip);
        parent.add(field, gc);
    }

    private void styleField(JTextField f) {
        f.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
    }

    private void styleFlatButton(JButton b, Color base, Color hover) {
        b.setBackground(base);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(hover); }
            public void mouseExited(java.awt.event.MouseEvent e) { b.setBackground(base); }
        });
    }

    /* === Rounded card with drop shadow === */
    private static class RoundedShadowPanel extends JPanel {
        private final int radius;
        RoundedShadowPanel(int r) { this.radius = r; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            int shadowGap = 6, shadowOffset = 4;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillRoundRect(shadowOffset, shadowOffset, getWidth() - shadowGap, getHeight() - shadowGap, radius, radius);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - shadowGap - shadowOffset, getHeight() - shadowGap - shadowOffset, radius, radius);
            g2.dispose(); super.paintComponent(g);
        }
    }
}
