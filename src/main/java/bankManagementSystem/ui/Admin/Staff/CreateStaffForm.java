package main.java.bankManagementSystem.ui.Admin.Staff;

import main.java.bankManagementSystem.controller.BranchController;
import main.java.bankManagementSystem.controller.StaffController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

public class CreateStaffForm extends JPanel {

    private final JTextField nameField;
    private final JTextField emailField;
    private final JTextField phoneField;
    private final JTextField cnicField;
    private final JComboBox<Integer> dayDropdown;
    private final JComboBox<String> monthDropdown;
    private final JComboBox<Integer> yearDropdown;
    private final JComboBox<String> branchDropdown;
    private final StaffController staffController;
    private final BranchController branchController;

    public CreateStaffForm() {
        staffController = new StaffController();
        branchController = new BranchController();
        branchDropdown = new JComboBox<>();
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("SansSerif", Font.BOLD, 16);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 16);

        JLabel heading = new JLabel("Create New Staff");
        heading.setFont(new Font("SansSerif", Font.BOLD, 26));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(heading, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        addLabeledField("Name:", nameField = new JTextField(20), gbc, labelFont, fieldFont);
        addLabeledField("Email:", emailField = new JTextField(20), gbc, labelFont, fieldFont);
        addLabeledField("Phone:", phoneField = new JTextField(20), gbc, labelFont, fieldFont);
        addLabeledField("CNIC:", cnicField = new JTextField(20), gbc, labelFont, fieldFont);

        // DOB Dropdowns
        gbc.gridy++;
        JLabel dobLabel = new JLabel("Date of Birth:");
        dobLabel.setFont(labelFont);
        gbc.gridx = 0;
        add(dobLabel, gbc);

        JPanel dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        dobPanel.setBackground(Color.WHITE);

        // Day 1–31
        Integer[] days = IntStream.rangeClosed(1, 31).boxed().toArray(Integer[]::new);
        dayDropdown = new JComboBox<>(days);

        // Months Jan–Dec
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        monthDropdown = new JComboBox<>(months);

        // Year: 1950 to current year - 18
        int currentYear = LocalDate.now().getYear();
        Integer[] years = IntStream.rangeClosed(1950, currentYear - 18).boxed().toArray(Integer[]::new);
        yearDropdown = new JComboBox<>(years);

        // Add to panel
        dobPanel.add(dayDropdown);
        dobPanel.add(monthDropdown);
        dobPanel.add(yearDropdown);

        gbc.gridx = 1;
        add(dobPanel, gbc);

        // Branch
        gbc.gridy++;
        JLabel branchLabel = new JLabel("Branch:");
        branchLabel.setFont(labelFont);
        gbc.gridx = 0;
        add(branchLabel, gbc);

        loadBranchIds();
        branchDropdown.setFont(fieldFont);
        gbc.gridx = 1;
        add(branchDropdown, gbc);

        // Submit Button
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        JButton submitButton = new JButton("Create Staff");
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        submitButton.setBackground(new Color(100, 149, 237));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.setPreferredSize(new Dimension(200, 40));

        submitButton.addActionListener((ActionEvent e) -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String cnic = cnicField.getText();

            int day = (int) dayDropdown.getSelectedItem();
            int month = monthDropdown.getSelectedIndex() + 1;
            int year = (int) yearDropdown.getSelectedItem();

            LocalDate dob;
            try {
                dob = LocalDate.of(year, month, day);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid date selected.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String branchItem = (String) branchDropdown.getSelectedItem();
            if (branchItem == null || branchItem.equals("Select Branch")) {
                JOptionPane.showMessageDialog(this, "Please select a branch.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int branchId = Integer.parseInt(branchItem.split(" - ")[0]);

            try {
                if (staffController.handleCreateStaff(name, email, phone, cnic, dob, branchId)) {
                    JOptionPane.showMessageDialog(this, "Staff created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(submitButton, gbc);
    }

    private void loadBranchIds() {
        try {
            List<String> ids = branchController.handleGetBranchIdNameList();
            for (String id : ids) {
                branchDropdown.addItem(id);
            }
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(this, "Failed to load branch IDs.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addLabeledField(String label, JTextField field, GridBagConstraints gbc, Font labelFont, Font fieldFont) {
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(labelFont);
        gbc.gridx = 0;
        add(jLabel, gbc);

        field.setFont(fieldFont);
        gbc.gridx = 1;
        add(field, gbc);
        gbc.gridy++;
    }
}
