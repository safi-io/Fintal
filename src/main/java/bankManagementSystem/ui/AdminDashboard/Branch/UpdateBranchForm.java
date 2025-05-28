package main.java.bankManagementSystem.ui.AdminDashboard.Branch;

import main.java.bankManagementSystem.controller.AdminDashboard.BranchController;
import main.java.bankManagementSystem.model.BranchModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class UpdateBranchForm extends JPanel {

    private final JComboBox<String> branchDropdown = new JComboBox<>();
    private final JTextField nameField = new JTextField(20);
    private final JTextField addressField = new JTextField(20);
    private final JTextField phoneField = new JTextField(20);
    private final JLabel statusLabel = new JLabel(" ");

    private final BranchController controller = new BranchController();

    /* --- Constants --- */
    private static final Color BG_COLOR       = new Color(245, 248, 250);
    private static final Color CARD_BG        = Color.WHITE;
    private static final Color PRIMARY        = new Color(60, 179, 113);
    private static final Color PRIMARY_HOVER  = new Color(50, 160, 100);
    private static final Color TEXT_PRIMARY   = new Color(33, 33, 33);
    private static final Color TEXT_SECONDARY = new Color(120, 120, 120);
    private static final Color ERROR_COLOR    = new Color(255, 69, 58);
    private static final Color ACCENT_COLOR   = new Color(48, 209, 88);

    public UpdateBranchForm() {
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
        JLabel heading = new JLabel("Update Branch");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setForeground(TEXT_PRIMARY);
        card.add(heading, gc);
        gc.gridwidth = 1;

        styleField(nameField);
        styleField(addressField);
        styleField(phoneField);
        styleComboBox(branchDropdown);

        addLabeledField(card, gc, row++, "Select Branch", branchDropdown);
        addLabeledField(card, gc, row++, "Branch Name", nameField);
        addLabeledField(card, gc, row++, "Branch Address", addressField);
        addLabeledField(card, gc, row++, "Branch Phone", phoneField);

        /* --- Button --- */
        gc.gridx = 0; gc.gridy = row++; gc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton updateBtn = new JButton("Update Branch");
        styleFlatButton(updateBtn, PRIMARY, PRIMARY_HOVER);
        buttonPanel.add(updateBtn);
        card.add(buttonPanel, gc);

        /* --- Status --- */
        gc.gridy = row;
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(statusLabel, gc);

        add(card);

        loadBranchIds();

        branchDropdown.addActionListener(e -> populateSelectedBranch());

        updateBtn.addActionListener(e -> handleUpdate());
    }

    private void populateSelectedBranch() {
        String selected = (String) branchDropdown.getSelectedItem();
        if (selected == null || selected.isEmpty()) return;

        try {
            int id = Integer.parseInt(selected.split(" _ ")[0]);
            BranchModel branch = controller.handleGetBranchById(id);
            if (branch != null) {
                nameField.setText(branch.getBranchName());
                addressField.setText(branch.getBranchAddress());
                phoneField.setText(branch.getBranchPhone());
                setStatus("Branch data loaded.", false);
            }
        } catch (Exception ex) {
            setStatus("Error loading branch details.", true);
        }
    }

    private void handleUpdate() {
        String selected = (String) branchDropdown.getSelectedItem();
        if (selected == null || selected.isEmpty()) {
            setStatus("No branch selected.", true);
            return;
        }

        int id = Integer.parseInt(selected.split(" _ ")[0]);
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            setStatus("All fields must be filled.", true);
            return;
        }

        try {
            boolean success = controller.handleUpdateBranch(id, name, address, phone);
            if (success) {
                setStatus("Branch updated successfully!", false);
            } else {
                setStatus("Update failed. Try again.", true);
            }
        } catch (Exception ex) {
            setStatus("Error: " + ex.getMessage(), true);
        }
    }

    private void loadBranchIds() {
        try {
            List<String> ids = controller.handleGetBranchIdNameList();
            for (String item : ids) {
                branchDropdown.addItem(item);
            }
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Failed to load branch IDs.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setForeground(isError ? ERROR_COLOR : ACCENT_COLOR.darker());
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

    private void styleField(JTextField f) {
        f.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
    }

    private void styleComboBox(JComboBox<?> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        combo.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));
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

    /* === Shadow card === */
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
