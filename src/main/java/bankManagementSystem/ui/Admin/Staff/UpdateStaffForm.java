package main.java.bankManagementSystem.ui.Admin.Staff;

import main.java.bankManagementSystem.controller.BranchController;
import main.java.bankManagementSystem.controller.StaffController;
import main.java.bankManagementSystem.model.StaffModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

public class UpdateStaffForm extends JPanel {

    private final JComboBox<String> staffIdDropdown;
    private final JTextField staffNameField;
    private final JTextField staffEmailField;
    private final JTextField staffPhoneField;
    private final JTextField staffCnicField;
    private final JComboBox<String> branchDropdown;
    private final JComboBox<String> dayDropdown;
    private final JComboBox<String> monthDropdown;
    private JComboBox<Integer> yearDropdown;
    private final StaffController staffController;
    private final BranchController branchController;

    public UpdateStaffForm() {
        staffController = new StaffController();
        branchController = new BranchController();

        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("SansSerif", Font.BOLD, 16);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 16);

        JLabel heading = new JLabel("Update Staff");
        heading.setFont(new Font("SansSerif", Font.BOLD, 26));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(heading, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        // Staff ID Dropdown (can't be updated)
        JLabel idLabel = new JLabel("Select Staff ID:");
        idLabel.setFont(labelFont);
        gbc.gridx = 0;
        add(idLabel, gbc);

        staffIdDropdown = new JComboBox<>();
        staffIdDropdown.setFont(fieldFont);
        gbc.gridx = 1;
        add(staffIdDropdown, gbc);

        gbc.gridy++;
        JLabel nameLabel = new JLabel("Staff Name:");
        nameLabel.setFont(labelFont);
        gbc.gridx = 0;
        add(nameLabel, gbc);

        staffNameField = new JTextField(20);
        staffNameField.setFont(fieldFont);
        gbc.gridx = 1;
        add(staffNameField, gbc);

        gbc.gridy++;
        JLabel emailLabel = new JLabel("Staff Email:");
        emailLabel.setFont(labelFont);
        gbc.gridx = 0;
        add(emailLabel, gbc);

        staffEmailField = new JTextField(20);
        staffEmailField.setFont(fieldFont);
        gbc.gridx = 1;
        add(staffEmailField, gbc);

        gbc.gridy++;
        JLabel phoneLabel = new JLabel("Staff Phone:");
        phoneLabel.setFont(labelFont);
        gbc.gridx = 0;
        add(phoneLabel, gbc);

        staffPhoneField = new JTextField(20);
        staffPhoneField.setFont(fieldFont);
        gbc.gridx = 1;
        add(staffPhoneField, gbc);

        gbc.gridy++;
        JLabel cnicLabel = new JLabel("Staff CNIC:");
        cnicLabel.setFont(labelFont);
        gbc.gridx = 0;
        add(cnicLabel, gbc);

        staffCnicField = new JTextField(20);
        staffCnicField.setFont(fieldFont);
        gbc.gridx = 1;
        add(staffCnicField, gbc);

        gbc.gridy++;
        JLabel branchLabel = new JLabel("Branch:");
        branchLabel.setFont(labelFont);
        gbc.gridx = 0;
        add(branchLabel, gbc);

        branchDropdown = new JComboBox<>();
        branchDropdown.setFont(fieldFont);
        gbc.gridx = 1;
        add(branchDropdown, gbc);

        gbc.gridy++;
        JLabel dobLabel = new JLabel("Staff Date of Birth:");
        dobLabel.setFont(labelFont);
        gbc.gridx = 0;
        add(dobLabel, gbc);

        // Drop-downs for day, month, and year selection
        dayDropdown = new JComboBox<>();
        dayDropdown.setFont(fieldFont);
        loadDays();
        gbc.gridx = 1;
        add(dayDropdown, gbc);

        // Month Dropdown
        monthDropdown = new JComboBox<>();
        monthDropdown.setFont(fieldFont);
        loadMonths();
        gbc.gridx = 2;
        add(monthDropdown, gbc);

        // Year Dropdown
        loadYears();
        yearDropdown.setFont(fieldFont);
        gbc.gridx = 3;
        add(yearDropdown, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        JButton updateButton = new JButton("Update Staff");
        updateButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        updateButton.setBackground(new Color(60, 179, 113));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateButton.setPreferredSize(new Dimension(200, 40));
        add(updateButton, gbc);

        // Load Staff IDs into dropdown
        loadStaffIds();

        // On selection change
        staffIdDropdown.addActionListener((ActionEvent e) -> {
            String selectedStaffIdFull = (String) staffIdDropdown.getSelectedItem();
            int selectedId = Integer.parseInt(selectedStaffIdFull.split(" - ")[0]);

            StaffModel staff = staffController.handleGetStaffById(selectedId);
            if (staff != null) {
                staffNameField.setText(staff.getStaffName());
                staffEmailField.setText(staff.getStaffMail());
                staffPhoneField.setText(staff.getStaffPhone());
                staffCnicField.setText(staff.getStaffCNIC());
                loadBranches(staff.getStaffBranchId());

                String stringDate = staff.getStaffDOB().toString();
                String[] dobParts = stringDate.split("-");
                dayDropdown.setSelectedItem(dobParts[2]);
                monthDropdown.setSelectedItem(dobParts[1]);
                yearDropdown.setSelectedItem(dobParts[0]);
            }
        });

        // On update button click
        updateButton.addActionListener((ActionEvent e) -> {

            String selectedStaffIdFull = (String) staffIdDropdown.getSelectedItem();
            int selectedId = Integer.parseInt(selectedStaffIdFull.split(" - ")[0]);
            String name = staffNameField.getText();
            String email = staffEmailField.getText();
            String phone = staffPhoneField.getText();
            String cnic = staffCnicField.getText();
            String selectedBranchIdFull = (String) branchDropdown.getSelectedItem();
            int selectedBranchId = Integer.parseInt(selectedBranchIdFull.split(" - ")[0]);

            // Get selected DOB values from drop-downs
            String day = (String) dayDropdown.getSelectedItem();
            String month = (String) monthDropdown.getSelectedItem();
            String year = yearDropdown.getSelectedItem().toString();
            String dob = year + "-" + month + "-" + day;

            try {
                boolean success = staffController.handleUpdateStaff(selectedId, name, email, phone, cnic, LocalDate.parse(dob), selectedBranchId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Staff updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "All fields must be filled properly.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                staffNameField.setText("");
                staffCnicField.setText("");
                staffEmailField.setText("");
                staffPhoneField.setText("");
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }


        });
    }

    private void loadStaffIds() {
        try {
            List<String> ids = staffController.handleGetStaffIdNameList(); // You should implement this in the StaffController
            for (String id : ids) {
                staffIdDropdown.addItem(id);
            }
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(this, "Failed to load staff IDs.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadBranches(Integer currentBranchId) {
        try {
            // Assuming you have a method to get branches and their IDs
            List<String> branches = branchController.handleGetBranchIdNameList(); // This returns a list of "ID - Branch Name"

            branchDropdown.removeAllItems(); // Remove previous items from dropdown

            // If no branch assigned, add a placeholder item in the dropdown
            if (currentBranchId == 0) {
                branchDropdown.addItem("Select Branch(No branch allocated yet)");
            }

            // Add each branch to the dropdown
            for (String branch : branches) {
                branchDropdown.addItem(branch); // Each branch is added to the dropdown
            }

            // Pre-select the branch based on the currentBranchId, if one is assigned
            if (currentBranchId != 0) {
                for (int i = 0; i < branchDropdown.getItemCount(); i++) {
                    String branch = branchDropdown.getItemAt(i); // Get the branch item
                    int branchId = Integer.parseInt(branch.split(" - ")[0]); // Extract the ID part

                    // Compare with the current branch ID
                    if (branchId == currentBranchId) {
                        branchDropdown.setSelectedIndex(i); // Set the matching branch as selected
                        break; // Exit the loop once we find the matching branch
                    }
                }
            }

        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(this, "Failed to load branches.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDays() {
        for (int i = 1; i <= 31; i++) {
            dayDropdown.addItem(i < 10 ? "0" + i : String.valueOf(i));
        }
    }

    private void loadMonths() {
        for (int i = 1; i <= 12; i++) {
            monthDropdown.addItem(i < 10 ? "0" + i : String.valueOf(i));
        }
    }

    private void loadYears() {
        int currentYear = LocalDate.now().getYear();
        Integer[] years = IntStream.rangeClosed(1950, currentYear - 18).boxed().toArray(Integer[]::new);
        yearDropdown = new JComboBox<>(years);
    }
}
