package main.java.bankManagementSystem.ui.CustomerDashboard.Transactions;

import main.java.bankManagementSystem.controller.CustomerDashboard.SendMoneyController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SendMoney extends JPanel {

    // Modern color palette
    private static final Color BG_COLOR = new Color(250, 252, 254);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color PRIMARY = new Color(10, 132, 255);
    private static final Color ACCENT = new Color(48, 209, 88);
    private static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    private static final Color TEXT_SECONDARY = new Color(120, 120, 120);
    private static final Color ERROR_COLOR = new Color(255, 69, 58);

    private final JTextField recipAccTxt = new JTextField();
    public final JLabel recipNameLbl = new JLabel("—");
    private final JTextField amountTxt = new JTextField();
    private final JTextField noteTxt = new JTextField();

    private final JButton checkBtn = new JButton("Check");
    private final JButton sendBtn = new JButton("Send Money");

    private final JLabel statusLbl = new JLabel(" "); // placeholder for consistent height

    private final SendMoneyController controller;

    public SendMoney(String loggedInAcc) {
        this.controller = new SendMoneyController(loggedInAcc, this);

        setLayout(new GridBagLayout());
        setBackground(BG_COLOR);

        JPanel card = new RoundedShadowPanel(20);
        card.setBackground(CARD_BG);
        card.setBorder(new EmptyBorder(40, 50, 50, 50));
        card.setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(15, 20, 15, 20);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        gc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Recipient Label
        gc.gridx = 0; gc.gridy = row++; gc.gridwidth = 2;
        JLabel recipLabel = new JLabel("Receiver Details:");
        recipLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        recipLabel.setForeground(TEXT_PRIMARY);
        card.add(recipLabel, gc);

        // Recipient Account with Check button
        gc.gridy = row++;
        gc.gridwidth = 1;
        JLabel recipAccLabel = new JLabel("Account No.");
        recipAccLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        recipAccLabel.setForeground(TEXT_SECONDARY);
        card.add(recipAccLabel, gc);

        gc.gridx = 1;
        JPanel inputCheckPanel = new JPanel(new BorderLayout(10, 0));
        inputCheckPanel.setOpaque(false);

        recipAccTxt.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        recipAccTxt.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, TEXT_SECONDARY));
        recipAccTxt.setPreferredSize(new Dimension(220, 30));
        recipAccTxt.setToolTipText("Enter recipient account number");
        inputCheckPanel.add(recipAccTxt, BorderLayout.CENTER);

        styleFlatButton(checkBtn, PRIMARY);
        inputCheckPanel.add(checkBtn, BorderLayout.EAST);

        card.add(inputCheckPanel, gc);

        // Recipient Name Display
        gc.gridx = 0; gc.gridy = row++; gc.gridwidth = 2;
        JPanel recipNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        recipNamePanel.setOpaque(false);
        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameLabel.setForeground(TEXT_SECONDARY);
        recipNameLbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        recipNameLbl.setForeground(TEXT_PRIMARY);
        recipNamePanel.add(nameLabel);
        recipNamePanel.add(recipNameLbl);
        card.add(recipNamePanel, gc);

        // Transfer Details Label
        gc.gridy = row++;
        JLabel transferLabel = new JLabel("Transaction Details:");
        transferLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        transferLabel.setForeground(TEXT_PRIMARY);
        card.add(transferLabel, gc);

        // Amount Input
        gc.gridy = row++;
        gc.gridwidth = 1;
        JLabel amountLabel = new JLabel("Amount (PKR)");
        amountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        amountLabel.setForeground(TEXT_SECONDARY);
        card.add(amountLabel, gc);

        gc.gridx = 1;
        amountTxt.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        amountTxt.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, TEXT_SECONDARY));
        amountTxt.setPreferredSize(new Dimension(220, 30));
        amountTxt.setToolTipText("Enter amount to transfer");
        card.add(amountTxt, gc);

        // Description Input
        gc.gridx = 0; gc.gridy = row++;
        JLabel noteLabel = new JLabel("Description");
        noteLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        noteLabel.setForeground(TEXT_SECONDARY);
        card.add(noteLabel, gc);

        gc.gridx = 1;
        noteTxt.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        noteTxt.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, TEXT_SECONDARY));
        noteTxt.setPreferredSize(new Dimension(220, 30));
        noteTxt.setToolTipText("Optional note");
        card.add(noteTxt, gc);

        // Buttons Panel
        gc.gridx = 0; gc.gridy = row++; gc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        btnPanel.setOpaque(false);
        styleFlatButton(sendBtn, ACCENT);
        btnPanel.add(checkBtn);
        btnPanel.add(sendBtn);
        card.add(btnPanel, gc);

        // Status Label
        gc.gridy = row++;
        statusLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(statusLbl, gc);

        add(card);

        sendBtn.setEnabled(false);

        // Listeners to Controller
        checkBtn.addActionListener(e -> controller.lookupRecipient(recipAccTxt.getText().trim()));
        sendBtn.addActionListener(e -> controller.doTransfer(
                recipAccTxt.getText().trim(),
                amountTxt.getText().trim(),
                noteTxt.getText().trim()
        ));
    }

    public void setRecipientName(String name) {
        recipNameLbl.setText(name == null ? "—" : name);
        setSendEnabled(name != null);
    }

    public void setStatus(String message, boolean error) {
        statusLbl.setText(message);
        statusLbl.setForeground(error ? ERROR_COLOR : ACCENT.darker());
    }

    public void setSendEnabled(boolean enabled) {
        sendBtn.setEnabled(enabled);
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

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(color);
            }
        });
    }

    // Custom JPanel with rounded corners + shadow
    static class RoundedShadowPanel extends JPanel {
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

            // Draw shadow
            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillRoundRect(shadowOffset, shadowOffset, getWidth() - shadowGap, getHeight() - shadowGap, cornerRadius, cornerRadius);

            // Draw white panel
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - shadowGap - shadowOffset, getHeight() - shadowGap - shadowOffset, cornerRadius, cornerRadius);

            g2.dispose();
            super.paintComponent(g);
        }
    }
    public void clearFields() {
        recipAccTxt.setText("");
        recipNameLbl.setText("—");
        amountTxt.setText("");
        noteTxt.setText("");
        setSendEnabled(false);
    }
}
