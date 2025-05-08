package main.java.bankManagementSystem.ui.Admin.Branch;

import main.java.bankManagementSystem.controller.BranchController;
import main.java.bankManagementSystem.model.BranchModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class UpdateBranchForm extends JPanel {

    private final JComboBox<String> branchIdDropdown;
    private final JTextField branchNameField;
    private final JTextField branchAddressField;
    private final JTextField branchPhoneField;
    private final BranchController branchController;

    public UpdateBranchForm() {
        branchController = new BranchController();

        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("SansSerif", Font.BOLD, 16);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 16);

        JLabel heading = new JLabel("Update Branch");
        heading.setFont(new Font("SansSerif", Font.BOLD, 26));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(heading, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        JLabel idLabel = new JLabel("Select Branch ID:");
        idLabel.setFont(labelFont);
        gbc.gridx = 0;
        add(idLabel, gbc);

        branchIdDropdown = new JComboBox<>();
        branchIdDropdown.setFont(fieldFont);
        gbc.gridx = 1;
        add(branchIdDropdown, gbc);

        gbc.gridy++;
        JLabel nameLabel = new JLabel("Branch Name:");
        nameLabel.setFont(labelFont);
        gbc.gridx = 0;
        add(nameLabel, gbc);

        branchNameField = new JTextField(20);
        branchNameField.setFont(fieldFont);
        gbc.gridx = 1;
        add(branchNameField, gbc);

        gbc.gridy++;
        JLabel addressLabel = new JLabel("Branch Address:");
        addressLabel.setFont(labelFont);
        gbc.gridx = 0;
        add(addressLabel, gbc);

        branchAddressField = new JTextField(20);
        branchAddressField.setFont(fieldFont);
        gbc.gridx = 1;
        add(branchAddressField, gbc);

        gbc.gridy++;
        JLabel phoneLabel = new JLabel("Branch Phone:");
        phoneLabel.setFont(labelFont);
        gbc.gridx = 0;
        add(phoneLabel, gbc);

        branchPhoneField = new JTextField(20);
        branchPhoneField.setFont(fieldFont);
        gbc.gridx = 1;
        add(branchPhoneField, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        JButton updateButton = new JButton("Update Branch");
        updateButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        updateButton.setBackground(new Color(60, 179, 113));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateButton.setPreferredSize(new Dimension(200, 40));
        add(updateButton, gbc);

        // Load IDs into dropdown
        loadBranchIds();

        // On selection change
        branchIdDropdown.addActionListener((ActionEvent e) -> {
            String selectedBranchIdFull = (String) branchIdDropdown.getSelectedItem();
            int selectedId = Integer.parseInt(selectedBranchIdFull.split(" - ")[0]);

            BranchModel branch = branchController.handleGetBranchById(selectedId);
            if (branch != null) {
                branchNameField.setText(branch.getBranchName());
                branchAddressField.setText(branch.getBranchAddress());
                branchPhoneField.setText(branch.getBranchPhone());
            }
        });

        // On update click
        updateButton.addActionListener((ActionEvent e) -> {

            String selectedBranchIdFull = (String) branchIdDropdown.getSelectedItem();

            int selectedId = Integer.parseInt(selectedBranchIdFull.split(" - ")[0]);
            String name = branchNameField.getText();
            String address = branchAddressField.getText();
            String phone = branchPhoneField.getText();


            try {
                boolean success = branchController.handleUpdateBranch(selectedId, name, address, phone);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Branch updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "All fields must be filled properly.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void loadBranchIds() {
        try {
            List<String> ids = branchController.handleGetBranchIdNameList(); // You should implement this
            for (String id : ids) {
                branchIdDropdown.addItem(id);
            }
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(this, "Failed to load branch IDs.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
