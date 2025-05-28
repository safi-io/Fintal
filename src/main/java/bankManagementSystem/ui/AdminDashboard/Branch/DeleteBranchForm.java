package main.java.bankManagementSystem.ui.AdminDashboard.Branch;

import main.java.bankManagementSystem.controller.AdminDashboard.BranchController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class DeleteBranchForm extends JPanel {

    private final JComboBox<String> branchDropdown = new JComboBox<>();
    private final JLabel statusLabel = new JLabel(" ");
    private final BranchController branchController = new BranchController();

    /* --- Constants --- */
    private static final Color BG_COLOR       = new Color(245, 248, 250);
    private static final Color CARD_BG        = Color.WHITE;
    private static final Color DELETE_COLOR   = new Color(255, 69, 58);
    private static final Color DELETE_HOVER   = new Color(220, 50, 40);
    private static final Color TEXT_PRIMARY   = new Color(33, 33, 33);
    private static final Color TEXT_SECONDARY = new Color(120, 120, 120);
    private static final Color ERROR_COLOR    = new Color(255, 0, 0);
    private static final Color SUCCESS_COLOR  = new Color(48, 209, 88);

    public DeleteBranchForm() {
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
        JLabel heading = new JLabel("Delete Branch");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setForeground(TEXT_PRIMARY);
        card.add(heading, gc);
        gc.gridwidth = 1;

        /* --- Branch Dropdown --- */
        styleComboBox(branchDropdown);
        addLabeledField(card, gc, row++, "Select Branch", branchDropdown);

        /* --- Delete Button --- */
        gc.gridx = 0; gc.gridy = row++; gc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton deleteButton = new JButton("Delete Branch");
        styleFlatButton(deleteButton, DELETE_COLOR, DELETE_HOVER);
        buttonPanel.add(deleteButton);
        card.add(buttonPanel, gc);

        /* --- Status --- */
        gc.gridy = row;
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(statusLabel, gc);

        add(card);

        loadBranchList();

        deleteButton.addActionListener((ActionEvent e) -> {
            String selected = (String) branchDropdown.getSelectedItem();
            if (selected == null || selected.isEmpty()) {
                setStatus("No branch selected.", true);
                return;
            }

            String selectedId = selected.split(" _ ")[0];

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this branch?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    boolean deleted = branchController.handleDeleteBranch(selectedId);
                    if (deleted) {
                        setStatus("Branch deleted successfully.", false);
                        loadBranchList();
                    } else {
                        setStatus("Failed to delete branch.", true);
                    }
                } catch (RuntimeException ex) {
                    setStatus("Error: " + ex.getMessage(), true);
                }
            }
        });
    }

    private void loadBranchList() {
        try {
            List<String> list = branchController.handleGetBranchIdNameList();
            if (list == null || list.isEmpty()) {
                setStatus("No branches found in the system.", true);
                branchDropdown.removeAllItems();
                return;
            }

            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(list.toArray(new String[0]));
            branchDropdown.setModel(model);
            setStatus("Select a branch to delete.", false);
        } catch (Exception e) {
            setStatus("Error loading branch list.", true);
        }
    }

    private void setStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setForeground(isError ? ERROR_COLOR : SUCCESS_COLOR.darker());
    }

    private void addLabeledField(JPanel panel, GridBagConstraints gc, int row, String label, JComponent input) {
        gc.gridx = 0; gc.gridy = row;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(TEXT_SECONDARY);
        panel.add(lbl, gc);

        gc.gridx = 1;
        panel.add(input, gc);
    }

    private void styleComboBox(JComboBox<?> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        combo.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
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

    /* === Shadow Panel === */
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
