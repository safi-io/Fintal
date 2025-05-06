package main.java.bankManagementSystem.ui.Admin.Branch;

import main.java.bankManagementSystem.controller.BranchController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CreateBranchForm extends JPanel {

    private final JTextField branchNameField;
    private final JTextField branchAddressField;
    private final JTextField branchPhoneField;
    private final BranchController branchController;

    public CreateBranchForm() {
        branchController = new BranchController();
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("SansSerif", Font.BOLD, 16);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 16);

        JLabel heading = new JLabel("Create New Branch");
        heading.setFont(new Font("SansSerif", Font.BOLD, 26));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(heading, gbc);

        gbc.gridwidth = 1;
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
        JButton submitButton = new JButton("Create Branch");
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        submitButton.setBackground(new Color(100, 149, 237));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.setPreferredSize(new Dimension(200, 40));

        submitButton.addActionListener((ActionEvent e) -> {
            String branchName = branchNameField.getText();
            String branchAddress = branchAddressField.getText();
            String branchPhone = branchPhoneField.getText();

            try {
                if (branchController.handleCreateBranch(branchName, branchAddress, branchPhone)) {
                    JOptionPane.showMessageDialog(this, "Branch created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "All Fields must not be null.", "Error", JOptionPane.ERROR_MESSAGE);

                }
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(submitButton, gbc);
    }
}
