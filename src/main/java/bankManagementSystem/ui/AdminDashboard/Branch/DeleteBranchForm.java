package main.java.bankManagementSystem.ui.AdminDashboard.Branch;

import main.java.bankManagementSystem.controller.AdminDashboard.BranchController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class DeleteBranchForm extends JPanel {

    private JComboBox<String> branchComboBox;
    private final BranchController branchController;

    public DeleteBranchForm() {
        branchController = new BranchController();
        updateComboBox();
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("SansSerif", Font.BOLD, 16);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 16);

        // Heading Label
        JLabel heading = new JLabel("Delete Branch");
        heading.setFont(new Font("SansSerif", Font.BOLD, 26));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(heading, gbc);

        // Label for ComboBox
        gbc.gridwidth = 1;
        gbc.gridy++;

        JLabel selectBranchLabel = new JLabel("Select Branch ID:");
        selectBranchLabel.setFont(labelFont);
        gbc.gridx = 0;
        add(selectBranchLabel, gbc);

        // ComboBox for Branch IDs

        // DB OPERATION NEEDED HERE TODO
        branchComboBox.setFont(fieldFont);
        gbc.gridx = 1;
        add(branchComboBox, gbc);

        // Delete Button
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        JButton deleteButton = new JButton("Delete Branch");
        deleteButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        deleteButton.setBackground(new Color(255, 69, 0)); // Red color for delete
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.setPreferredSize(new Dimension(200, 40));

        deleteButton.addActionListener((ActionEvent e) -> {
            String selectedBranchIdFull = (String) branchComboBox.getSelectedItem();
            String selectedBranchId = selectedBranchIdFull.split(" - ")[0];

            try {
                if (selectedBranchId != null) {

                    // Need Validation here
                    int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this branch?", "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (result == JOptionPane.YES_OPTION) {
                        boolean isDeleted = branchController.handleDeleteBranch(selectedBranchId);
                        if (isDeleted) {
                            JOptionPane.showMessageDialog(this, "Branch deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to delete branch.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        updateComboBox();
                    }
                }
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(deleteButton, gbc);
    }

    private void updateComboBox() {
        List<String> idNameList = branchController.handleGetBranchIdNameList();
        if(idNameList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Branches Currently in the System", "Error", JOptionPane.ERROR_MESSAGE);
            branchComboBox.removeAllItems();
            return;
        }
        if (branchComboBox == null) {
            branchComboBox = new JComboBox<>(idNameList.toArray(new String[0]));
        } else {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(idNameList.toArray(new String[0]));
            branchComboBox.setModel(model);
        }
    }

}
