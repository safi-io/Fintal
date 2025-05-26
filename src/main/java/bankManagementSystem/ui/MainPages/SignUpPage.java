package main.java.bankManagementSystem.ui.MainPages;

import com.formdev.flatlaf.FlatLightLaf;
import main.java.bankManagementSystem.controller.AdminDashboard.BranchController;
import main.java.bankManagementSystem.controller.CustomerDashboard.CustomerController;
import main.java.bankManagementSystem.controller.MainPages.SignUpController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class SignUpPage extends JFrame {

    private final BranchController branchController = new BranchController();
    private final SignUpController signUpController = new SignUpController();

    /* ── theme colours ────────────────────────────────────────── */
    private static final Color ACCENT = new Color(48, 209, 88);
    private static final Color PRIMARY = new Color(10, 132, 255);
    private static final Color ERROR_COLOR = new Color(255, 69, 58);
    private static final Color FIELD_UNDERLINE = new Color(160, 160, 160);

    /* ── regex ────────────────────────────────────────────────── */
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,7}$");
    private static final Pattern CNIC_PATTERN = Pattern.compile("^\\d{13}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{11}$");
    private static final Pattern DOB_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

    /* ── form fields ─────────────────────────────────────────── */
    private final JTextField nameField = new JTextField();
    private final JTextField cnicField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JTextField dobField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JPasswordField confirmPasswordField = new JPasswordField();
    private final JComboBox<String> accountTypeCombo = new JComboBox<>(new String[]{"Saving", "Current", "Beneficiary"});

    // Need DB Call here
    private final JComboBox<String> branchIdCombo = new JComboBox<>();

    private final JLabel statusLbl = new JLabel(" ");
    private final CustomerController customerController = new CustomerController();

    public SignUpPage() {
        try {
            FlatLightLaf.setup();
        } catch (Exception ex) {
            System.err.println("FlatLaf init failed " + ex);
        }
        buildUI();

    }

    /* ────────────────────────────────────────────────────────── */
    private void buildUI() {
        setTitle("Bank‑Management ‑ Sign‑Up");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());                     // root uses GridBag

        /* background panel with gradient */
        JPanel bg = new JPanel(null) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 40, 50), getWidth(), getHeight(), new Color(15, 25, 35));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gbc.gridy = 0;
        gbc.weightx = gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(bg, gbc);

        // ── Logo / Bank Name ───────────────────────────────
        GridBagConstraints titleGc = new GridBagConstraints();
        titleGc.gridx = 0;
        titleGc.gridy = 0;
        titleGc.gridwidth = 2;
        titleGc.insets = new Insets(0, 0, 10, 0); // Increased bottom padding
        titleGc.anchor = GridBagConstraints.CENTER;

        JLabel bankLogoLabel = new JLabel("<html><h1 style='color:#6FA3D7; font-size:48px; text-shadow: 2px 2px 4px rgba(0,0,0,0.4);'>BankXPro</h1></html>");
        bankLogoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bg.add(bankLogoLabel, titleGc);


        /* ── card panel ─────────────────────────────── */
        JPanel card = new RoundedShadowPanel(20);
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(20, 60, 50, 60));
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(840, 700));

        GridBagConstraints cg = new GridBagConstraints();
        cg.insets = new Insets(12, 18, 12, 18);
        cg.fill = GridBagConstraints.HORIZONTAL;
        cg.weightx = 1;
        int row = 0;

        cg.gridy = row++;
        cg.gridx = 0;
        cg.gridwidth = 2;
        JLabel formTitle = new JLabel("Create New Account", SwingConstants.CENTER);
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        card.add(formTitle, cg);

        /* fields */
        cg.gridwidth = 1;
        addLabeled(card, cg, row++, "Full Name", nameField);
        addLabeled(card, cg, row++, "CNIC (13 digits)", cnicField);
        addLabeled(card, cg, row++, "Email", emailField);
        addLabeled(card, cg, row++, "Phone (11 digits)", phoneField);
        addLabeled(card, cg, row++, "Date of Birth (YYYY‑MM‑DD)", dobField);
        addLabeled(card, cg, row++, "Password", passwordField);
        addLabeled(card, cg, row++, "Confirm Password", confirmPasswordField);

        /* combos in separate rows */
        addLabeled(card, cg, row++, "Account Type", accountTypeCombo);
        addLabeled(card, cg, row++, "Branch ID", branchIdCombo);

        /* buttons */
        cg.gridy = row++;
        cg.gridx = 0;
        cg.gridwidth = 2;
        JButton regBtn = new JButton("Register");
        styleFlatButton(regBtn, ACCENT);
        regBtn.addActionListener(this::attemptSignUp);
        card.add(regBtn, cg);

        JButton backBtn = new JButton("Back to Login");
        styleFlatButton(backBtn, PRIMARY);
        backBtn.addActionListener(e -> {
            new LoginPage();
            dispose();
        });
        cg.gridy = row++;
        card.add(backBtn, cg);

        /* status */
        cg.gridy = row++;
        statusLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(statusLbl, cg);

        /* add card to bg */
        GridBagConstraints cardGc = new GridBagConstraints();
        cardGc.gridx = 0;
        cardGc.gridy = 1;                  // below bank title
        cardGc.anchor = GridBagConstraints.CENTER;
        bg.add(card, cardGc);

        loadBranchList();
        setVisible(true);
    }

    /* ── helper to add label+component row ───────────────────────────── */
    private void addLabeled(JPanel parent, GridBagConstraints gc, int row, String lbl, JComponent comp) {
        gc.gridy = row;
        gc.gridx = 0;
        JLabel jlbl = new JLabel(lbl);
        jlbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        parent.add(jlbl, gc);

        gc.gridx = 1;
        if (comp instanceof JTextField tf) {
            tf.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            tf.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, FIELD_UNDERLINE));
            tf.setPreferredSize(new Dimension(300, 35));
        } else if (comp instanceof JPasswordField pf) {
            pf.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            pf.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, FIELD_UNDERLINE));
            pf.setPreferredSize(new Dimension(300, 35));
        } else if (comp instanceof JComboBox<?> cb) {
            cb.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            cb.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, FIELD_UNDERLINE));
            cb.setPreferredSize(new Dimension(300, 35));
            cb.setBackground(Color.WHITE);
            cb.setForeground(Color.DARK_GRAY);
            cb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Custom renderer for flat look
            cb.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                    setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    if (isSelected) {
                        setBackground(PRIMARY);
                        setForeground(Color.WHITE);
                    } else {
                        setBackground(Color.WHITE);
                        setForeground(Color.BLACK);
                    }
                    return c;
                }
            });
        }
        parent.add(comp, gc);
    }

    /* ── button styling ─────────────────────────────────────────────── */
    private void styleFlatButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /* ── validation & actions ───────────────────────────────────────── */
    private void attemptSignUp(ActionEvent e) {
        String errors = validateFields();
        if (!errors.isEmpty()) {
            setStatus(errors, true);
            return;
        }

        String cnic = cnicField.getText();
        String name = nameField.getText();
        String mail = emailField.getText();
        String phone = phoneField.getText();
        String dob = dobField.getText();
        String password = passwordField.getText();
        String accountType = accountTypeCombo.getSelectedItem().toString();
        String branchId = branchIdCombo.getSelectedItem().toString().split("_")[0].trim();

        try {
            boolean ok = signUpController.handleCreateCustomerAccount(name, cnic, mail, phone, dob, password, accountType, branchId);

            if (!ok) {
                setStatus("Problem while Registering! Try Later", true);
                return;
            }

            setStatus("Registration successful! Redirecting…", false);
            Timer timer = new Timer(2000, ev -> {
                new LoginPage();
                dispose();
            });
            timer.setRepeats(false);
            timer.start();
        } catch (Exception ex) {
            setStatus("Problem: " + ex.getMessage(), true);
        }


    }

    private String validateFields() {
        StringBuilder sb = new StringBuilder();
        if (nameField.getText().trim().isEmpty()) sb.append("Name required. ");
        if (!CNIC_PATTERN.matcher(cnicField.getText()).matches()) sb.append("Invalid CNIC. ");
        if (!EMAIL_PATTERN.matcher(emailField.getText()).matches()) sb.append("Invalid email. ");
        if (!PHONE_PATTERN.matcher(phoneField.getText()).matches()) sb.append("Invalid phone. ");
        if (!DOB_PATTERN.matcher(dobField.getText()).matches()) sb.append("DOB must be YYYY‑MM‑DD. ");
        else try {
            LocalDate.parse(dobField.getText());
        } catch (DateTimeParseException ex) {
            sb.append("Invalid date. ");
        }
        if (String.valueOf(passwordField.getPassword()).isBlank()) sb.append("Password required. ");
        if (!String.valueOf(passwordField.getPassword()).equals(String.valueOf(confirmPasswordField.getPassword())))
            sb.append("Passwords do not match. ");
        return sb.toString();
    }

    private void setStatus(String msg, boolean err) {
        // Wrap the message in HTML with max width constraint
        String htmlMessage = "<html><div style='width: 500px; text-align: center;'>" + msg.replace("\n", "<br>") + "</div></html>";

        statusLbl.setText(htmlMessage);
        if (err) {
            statusLbl.setForeground(new Color(255, 100, 100)); // Brighter red for dark theme
        } else {
            statusLbl.setForeground(new Color(100, 255, 100)); // Brighter green for dark theme
        }

        // Force the layout to update
        statusLbl.revalidate();
        statusLbl.repaint();
    }

    /* ── rounded‑shadow panel ───────────────────────────────────────── */
    static class RoundedShadowPanel extends JPanel {
        private final int radius;

        RoundedShadowPanel(int r) {
            radius = r;
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            int shadowOff = 4, shadowGap = 6;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 35));
            g2.fillRoundRect(shadowOff, shadowOff, getWidth() - shadowGap, getHeight() - shadowGap, radius, radius);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - shadowGap, getHeight() - shadowGap, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private void loadBranchList() {
        try {
            java.util.List<String> list = branchController.handleGetBranchIdNameList();
            if (list == null || list.isEmpty()) {
                setStatus("No branches found in the system.", true);
                branchIdCombo.removeAllItems();
                return;
            }

            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(list.toArray(new String[0]));
            branchIdCombo.setModel(model);
        } catch (Exception e) {
            setStatus("Error loading branch list.", true);
        }
    }

    /* ── launcher ───────────────────────────────────────────────────── */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignUpPage::new);
    }
}
