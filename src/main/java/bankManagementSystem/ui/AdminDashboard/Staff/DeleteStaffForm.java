package main.java.bankManagementSystem.ui.AdminDashboard.Staff;

import main.java.bankManagementSystem.controller.AdminDashboard.StaffController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import java.util.List;

public class DeleteStaffForm extends JPanel {

    private JComboBox<String> staffComboBox;
    private final StaffController staffController;

    public DeleteStaffForm() {
        staffController = new StaffController();
        updateComboBox();
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("SansSerif", Font.BOLD, 16);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 16);

        // Heading Label
        JLabel heading = new JLabel("Delete Staff");
        heading.setFont(new Font("SansSerif", Font.BOLD, 26));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(heading, gbc);

        // Label for ComboBox
        gbc.gridwidth = 1;
        gbc.gridy++;

        JLabel selectStaffLabel = new JLabel("Select Staff ID:");
        selectStaffLabel.setFont(labelFont);
        gbc.gridx = 0;
        add(selectStaffLabel, gbc);

        staffComboBox.setFont(fieldFont);
        gbc.gridx = 1;
        add(staffComboBox, gbc);

        // Delete Button
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        JButton deleteButton = new JButton("Delete Staff");
        deleteButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        deleteButton.setBackground(new Color(255, 69, 0)); // Red color for delete
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.setPreferredSize(new Dimension(200, 40));

        deleteButton.addActionListener((ActionEvent e) -> {
            String selectedStaffIdFull = (String) staffComboBox.getSelectedItem();
            String selectedStaffId = selectedStaffIdFull.split(" - ")[0];

            try {
                if (selectedStaffId != null) {

                    // Need Validation here
                    int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this staff?", "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (result == JOptionPane.YES_OPTION) {
                        boolean isDeleted = staffController.handleDeleteStaff(selectedStaffId);
                        if (isDeleted) {
                            JOptionPane.showMessageDialog(this, "Staff deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to delete staff.", "Error", JOptionPane.ERROR_MESSAGE);
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
        List<String> idNameList = staffController.handleGetStaffIdNameList();
        if(idNameList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Staff Currently in the System", "Error", JOptionPane.ERROR_MESSAGE);
            staffComboBox.removeAllItems();
            return;
        }
        if (staffComboBox == null) {
            staffComboBox = new JComboBox<>(idNameList.toArray(new String[0]));
        } else {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(idNameList.toArray(new String[0]));
            staffComboBox.setModel(model);
        }
    }

}
