package main.java.bankManagementSystem.ui.AdminDashboard.Staff;

import main.java.bankManagementSystem.controller.AdminDashboard.BranchController;
import main.java.bankManagementSystem.controller.AdminDashboard.StaffController;
import main.java.bankManagementSystem.model.StaffModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.stream.IntStream;

public class UpdateStaffForm extends JPanel {

    private static final Color BG_COLOR = new Color(250, 252, 254);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color PRIMARY = new Color(10, 132, 255);
    private static final Color ACCENT = new Color(48, 209, 88);
    private static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    private static final Color TEXT_SECONDARY = new Color(120, 120, 120);
    private static final Color ERROR_COLOR = new Color(255, 69, 58);

    private final JComboBox<String> staffIdDropdown = new JComboBox<>();
    private final JTextField staffNameField = new JTextField(20);
    private final JTextField staffEmailField = new JTextField(20);
    private final JTextField staffPhoneField = new JTextField(20);
    private final JTextField staffCnicField = new JTextField(20);
    private final JComboBox<String> branchDropdown = new JComboBox<>();
    private final JComboBox<String> dayDropdown = new JComboBox<>();
    private final JComboBox<String> monthDropdown = new JComboBox<>();
    private final JComboBox<Integer> yearDropdown;
    private final JLabel statusLbl = new JLabel(" ");

    private final StaffController staffController = new StaffController();
    private final BranchController branchController = new BranchController();

    public UpdateStaffForm() {
        setLayout(new GridBagLayout());
        setBackground(BG_COLOR);

        RoundedShadowPanel card = new RoundedShadowPanel(20);
        card.setBackground(CARD_BG);
        card.setBorder(new EmptyBorder(40, 50, 50, 50));
        card.setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(15, 20, 15, 20);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        gc.anchor = GridBagConstraints.WEST;

        int row = 0;

        gc.gridx = 0;
        gc.gridy = row++;
        gc.gridwidth = 2;
        JLabel heading = new JLabel("Update Staff");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setForeground(TEXT_PRIMARY);
        card.add(heading, gc);
        gc.gridwidth = 1;

        addLabeledField(card, gc, row++, "Select Staff", staffIdDropdown, "Choose staff to edit");
        styleComboBox(staffIdDropdown);

        addLabeledField(card, gc, row++, "Name", staffNameField, "Enter staff name");
        addLabeledField(card, gc, row++, "Email", staffEmailField, "Enter staff e‑mail");
        addLabeledField(card, gc, row++, "Phone", staffPhoneField, "Enter staff phone");
        addLabeledField(card, gc, row++, "CNIC", staffCnicField, "Enter staff CNIC");

        addLabeledField(card, gc, row++, "Branch", branchDropdown, "Select branch");
        styleComboBox(branchDropdown);

        // DOB Label
        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 1;
        JLabel dobLabel = new JLabel("Date of Birth");
        dobLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dobLabel.setForeground(TEXT_SECONDARY);
        card.add(dobLabel, gc);

        // DOB Dropdown Panel
        JPanel dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        dobPanel.setOpaque(false);
        loadDays();
        loadMonths();
        yearDropdown = new JComboBox<>(IntStream.rangeClosed(1950, LocalDate.now().getYear() - 18).boxed().toArray(Integer[]::new));
        styleComboBox(dayDropdown);
        styleComboBox(monthDropdown);
        styleComboBox(yearDropdown);
        dobPanel.add(dayDropdown);
        dobPanel.add(monthDropdown);
        dobPanel.add(yearDropdown);

        gc.gridx = 1;
        gc.gridy = row++;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.WEST;
        card.add(dobPanel, gc);
        gc.fill = GridBagConstraints.HORIZONTAL;

        gc.gridx = 0;
        gc.gridy = row++;
        gc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        btnPanel.setOpaque(false);
        JButton updateBtn = new JButton("Update Staff");
        styleFlatButton(updateBtn, PRIMARY);
        btnPanel.add(updateBtn);
        card.add(btnPanel, gc);

        gc.gridy = row++;
        statusLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(statusLbl, gc);

        add(card);

        loadStaffIds();
        staffIdDropdown.addActionListener(e -> fetchAndPopulate());
        updateBtn.addActionListener(e -> updateStaff());
    }

    private void updateStaff() {
        if (staffIdDropdown.getSelectedItem() == null) {
            setStatus("Please select a staff ID.", true);
            return;
        }

        int staffId = Integer.parseInt(staffIdDropdown.getSelectedItem().toString().split(" _ ")[0]);
        String name = staffNameField.getText().trim();
        String email = staffEmailField.getText().trim();
        String phone = staffPhoneField.getText().trim();
        String cnic = staffCnicField.getText().trim();

        String branchItem = (String) branchDropdown.getSelectedItem();
        if (branchItem == null) {
            setStatus("Select branch.", true);
            return;
        }
        int branchId = Integer.parseInt(branchItem.split(" _ ")[0]);

        Object dayObj = dayDropdown.getSelectedItem();
        Object monthObj = monthDropdown.getSelectedItem();
        Object yearObj = yearDropdown.getSelectedItem();

        if (dayObj == null || monthObj == null || yearObj == null) {
            setStatus("Please select a complete date.", true);
            return;
        }

        int day = Integer.parseInt(dayObj.toString());
        int month = Integer.parseInt(monthObj.toString());
        int year = (int) yearObj;

        LocalDate dob;
        try {
            dob = LocalDate.of(year, month, day);
        } catch (Exception ex) {
            setStatus("Invalid date.", true);
            return;
        }

        boolean ok = staffController.handleUpdateStaff(staffId, name, email, phone, cnic, dob, branchId);
        if (ok) {
            setStatus("Staff updated successfully!", false);
        } else {
            setStatus("All fields must be filled correctly.", true);
        }
    }

    private void fetchAndPopulate() {
        String sel = (String) staffIdDropdown.getSelectedItem();
        if (sel == null) return;
        int id = Integer.parseInt(sel.split(" _ ")[0]);

        StaffModel st = staffController.handleGetStaffById(id);
        if (st == null) {
            setStatus("Staff not found.", true);
            return;
        }

        staffNameField.setText(st.getStaffName());
        staffEmailField.setText(st.getStaffMail());
        staffPhoneField.setText(st.getStaffPhone());
        staffCnicField.setText(st.getStaffCNIC());

        loadBranches(st.getStaffBranchId());

        String[] dmy = st.getStaffDOB().toString().split("-");
        dayDropdown.setSelectedItem(dmy[2]);
        monthDropdown.setSelectedItem(dmy[1]);
        yearDropdown.setSelectedItem(Integer.parseInt(dmy[0]));
        setStatus(" ", false);
    }

    private void loadStaffIds() {
        try {
            for (String item : staffController.handleGetStaffIdNameList())
                staffIdDropdown.addItem(item);
        } catch (RuntimeException ex) {
            setStatus("Failed to load staff IDs.", true);
        }
    }

    private void loadBranches(Integer current) {
        branchDropdown.removeAllItems();
        try {
            for (String b : branchController.handleGetBranchIdNameList())
                branchDropdown.addItem(b);
            if (current != null) for (int i = 0; i < branchDropdown.getItemCount(); i++)
                if (branchDropdown.getItemAt(i).startsWith(current + " ")) branchDropdown.setSelectedIndex(i);
        } catch (RuntimeException ex) {
            setStatus("Failed to load branches.", true);
        }
    }

    private void loadDays() {
        for (int i = 1; i <= 31; i++) dayDropdown.addItem(String.format("%02d", i));
    }

    private void loadMonths() {
        for (int i = 1; i <= 12; i++) monthDropdown.addItem(String.format("%02d", i));
    }

    private void addLabeledField(JPanel parent, GridBagConstraints gc, int row, String label, JComponent field, String tooltip) {
        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 1;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(TEXT_SECONDARY);
        parent.add(lbl, gc);

        gc.gridx = 1;
        if (field instanceof JTextField) styleTextField((JTextField) field);
        if (field instanceof JComboBox) styleComboBox((JComboBox<?>) field);
        field.setToolTipText(tooltip);
        parent.add(field, gc);
    }

    private void styleTextField(JTextField f) {
        f.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        f.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, TEXT_SECONDARY));
        f.setPreferredSize(new Dimension(250, 30));
    }

    private void styleComboBox(JComboBox<?> c) {
        c.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        ((JComponent) c.getRenderer()).setBorder(new EmptyBorder(2, 5, 2, 5));
        c.setBackground(CARD_BG);
        c.setForeground(TEXT_PRIMARY);
    }

    private void styleFlatButton(JButton b, Color col) {
        b.setBackground(col);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                b.setBackground(col.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                b.setBackground(col);
            }
        });
    }

    private void setStatus(String msg, boolean err) {
        statusLbl.setText(msg);
        statusLbl.setForeground(err ? ERROR_COLOR : ACCENT.darker());
    }

    private static class RoundedShadowPanel extends JPanel {
        private final int radius;

        RoundedShadowPanel(int r) {
            radius = r;
            setOpaque(false);
            setLayout(new GridBagLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            int gap = 6, off = 4;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillRoundRect(off, off, getWidth() - gap, getHeight() - gap, radius, radius);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - gap - off, getHeight() - gap - off, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

}
