package main.java.bankManagementSystem.ui.Admin.Staff;

import javax.swing.*;
import java.awt.*;


public class CreateStaffForm extends JPanel {
    public CreateStaffForm() {
        setLayout(new GridLayout(5, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        add(nameField);

        add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        add(emailField);

        add(new JLabel("Position:"));
        JTextField positionField = new JTextField();
        add(positionField);

        add(new JLabel("Phone:"));
        JTextField phoneField = new JTextField();
        add(phoneField);

        JButton submit = new JButton("Submit");
        submit.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String position = positionField.getText();
            String phone = phoneField.getText();

            JOptionPane.showMessageDialog(this,
                    "Staff Created:\nName: " + name + "\nEmail: " + email +
                            "\nPosition: " + position + "\nPhone: " + phone);
        });

        add(new JLabel()); // for spacing
        add(submit);
    }
}
