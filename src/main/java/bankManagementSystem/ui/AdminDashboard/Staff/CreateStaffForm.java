package main.java.bankManagementSystem.ui.AdminDashboard.Staff;

import main.java.bankManagementSystem.controller.AdminDashboard.BranchController;
import main.java.bankManagementSystem.controller.AdminDashboard.StaffController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

public class CreateStaffForm extends JPanel {
    // Match the color scheme from StaffProfileSettings
    private static final Color BG_COLOR = new Color(250, 252, 254);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color PRIMARY = new Color(10, 132, 255);
    private static final Color ACCENT = new Color(48, 209, 88);
    private static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    private static final Color TEXT_SECONDARY = new Color(120, 120, 120);
    private static final Color ERROR_COLOR = new Color(255, 69, 58);

    private final JTextField nameField;
    private final JTextField emailField;
    private final JTextField phoneField;
    private final JTextField cnicField;
    private final JComboBox<Integer> dayDropdown;
    private final JComboBox<String> monthDropdown;
    private final JComboBox<Integer> yearDropdown;
    private final JComboBox<String> branchDropdown;
    private final JLabel statusLbl = new JLabel(" ");
    private final StaffController staffController;
    private final BranchController branchController;

    public CreateStaffForm() {
        staffController = new StaffController();
        branchController = new BranchController();
        branchDropdown = new JComboBox<>();

        // Set overall panel properties
        setLayout(new GridBagLayout());
        setBackground(BG_COLOR);

        // Create the main card panel with rounded corners and shadow
        RoundedShadowPanel card = new RoundedShadowPanel(20);
        card.setBackground(CARD_BG);
        card.setBorder(new EmptyBorder(40, 50, 50, 50));
        card.setLayout(new GridBagLayout());

        // Setup grid constraints
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(15, 20, 15, 20);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        gc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Add heading
        gc.gridx = 0;
        gc.gridy = row++;
        gc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Create New Staff");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_PRIMARY);
        card.add(titleLabel, gc);

        // Create fields
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        cnicField = new JTextField(20);

        // Add fields to form
        addLabeledField(card, gc, row++, "Name", nameField, "Enter staff's full name");
        addLabeledField(card, gc, row++, "Email", emailField, "Enter staff's email address");
        addLabeledField(card, gc, row++, "Phone", phoneField, "Enter staff's phone number");
        addLabeledField(card, gc, row++, "CNIC", cnicField, "Enter staff's CNIC number");

        // DOB Selection
        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 1;
        JLabel dobLabel = new JLabel("Date of Birth");
        dobLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dobLabel.setForeground(TEXT_SECONDARY);
        card.add(dobLabel, gc);

        // DOB Components in a panel
        JPanel dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        dobPanel.setOpaque(false);

        // Day 1–31
        Integer[] days = IntStream.rangeClosed(1, 31).boxed().toArray(Integer[]::new);
        dayDropdown = new JComboBox<>(days);
        styleComboBox(dayDropdown);

        // Months Jan–Dec
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        monthDropdown = new JComboBox<>(months);
        styleComboBox(monthDropdown);

        // Year: 1950 to current year - 18
        int currentYear = LocalDate.now().getYear();
        Integer[] years = IntStream.rangeClosed(1950, currentYear - 18).boxed().toArray(Integer[]::new);
        yearDropdown = new JComboBox<>(years);
        styleComboBox(yearDropdown);

        dobPanel.add(dayDropdown);
        dobPanel.add(monthDropdown);
        dobPanel.add(yearDropdown);

        gc.gridx = 1;
        card.add(dobPanel, gc);
        row++;

        // Branch Selection
        gc.gridx = 0;
        gc.gridy = row;
        JLabel branchLabel = new JLabel("Branch");
        branchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        branchLabel.setForeground(TEXT_SECONDARY);
        card.add(branchLabel, gc);

        // Load branch data and style dropdown
        loadBranchIds();
        styleComboBox(branchDropdown);

        gc.gridx = 1;
        card.add(branchDropdown, gc);
        row++;

        // Create button panel
        gc.gridx = 0;
        gc.gridy = row++;
        gc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        btnPanel.setOpaque(false);

        JButton submitButton = new JButton("Create Staff");
        styleFlatButton(submitButton, PRIMARY);

        btnPanel.add(submitButton);
        card.add(btnPanel, gc);

        // Status label
        gc.gridy = row++;
        statusLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(statusLbl, gc);

        // Submit button action
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
                setStatus("Invalid date selected.", true);
                return;
            }

            String branchItem = (String) branchDropdown.getSelectedItem();
            if (branchItem == null || branchItem.equals("Select Branch")) {
                setStatus("Please select a branch.", true);
                return;
            }

            int branchId = Integer.parseInt(branchItem.split(" - ")[0]);

            try {
                if (staffController.handleCreateStaff(name, email, phone, cnic, dob, branchId)) {
                    setStatus("Staff created successfully!", false);
                    clearForm();
                } else {
                    setStatus("All fields are required.", true);
                }
            } catch (RuntimeException ex) {
                setStatus(ex.getMessage(), true);
            }
        });

        // Add card to main panel
        add(card);
    }

    private void clearForm() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        cnicField.setText("");
        // Reset dropdowns to first item
        dayDropdown.setSelectedIndex(0);
        monthDropdown.setSelectedIndex(0);
        yearDropdown.setSelectedIndex(0);
    }

    private void setStatus(String message, boolean isError) {
        statusLbl.setText(message);
        statusLbl.setForeground(isError ? ERROR_COLOR : ACCENT.darker());
    }

    private void loadBranchIds() {
        try {
            List<String> ids = branchController.handleGetBranchIdNameList();
            for (String id : ids) {
                branchDropdown.addItem(id);
            }
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            setStatus("Failed to load branch IDs.", true);
        }
    }

    private void addLabeledField(JPanel parent, GridBagConstraints gc, int row, String label, JTextField field, String tooltip) {
        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 1;

        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel.setForeground(TEXT_SECONDARY);
        parent.add(jLabel, gc);

        gc.gridx = 1;
        styleTextField(field);
        field.setToolTipText(tooltip);
        parent.add(field, gc);
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, TEXT_SECONDARY));
        field.setPreferredSize(new Dimension(250, 30));
    }

    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        comboBox.setBackground(CARD_BG);
        comboBox.setForeground(TEXT_PRIMARY);
        ((JComponent) comboBox.getRenderer()).setBorder(new EmptyBorder(2, 5, 2, 5));
    }

    private void styleFlatButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.darker());
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
    }

    // Copy of RoundedShadowPanel from StaffProfileSettings
    private static class RoundedShadowPanel extends JPanel {
        private final int cornerRadius;

        RoundedShadowPanel(int radius) {
            super();
            this.cornerRadius = radius;
            setOpaque(false);
            setLayout(new GridBagLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            int shadowGap = 6;
            int shadowOffset = 4;

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillRoundRect(shadowOffset, shadowOffset, getWidth() - shadowGap, getHeight() - shadowGap, cornerRadius, cornerRadius);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - shadowGap - shadowOffset, getHeight() - shadowGap - shadowOffset, cornerRadius, cornerRadius);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}