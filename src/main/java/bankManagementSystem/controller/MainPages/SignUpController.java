package main.java.bankManagementSystem.controller.MainPages;

import main.java.bankManagementSystem.dao.MainPages.SignUpDAO;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;

public class SignUpController {
    private SignUpDAO signupDAO;

    public SignUpController() {
        this.signupDAO = new SignUpDAO();
    }

    public boolean handleCreateCustomerAccount(String name, String cnic, String mail, String phone, String date, String password, String accountType, String branchId) throws Exception {

        // Fixing Data for Customer Creation

        // Parsing Date
        java.sql.Date sqlDate = java.sql.Date.valueOf(LocalDate.parse(date));

        // Hashing Password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Fixing Data for Account Creation

        // Generate Account Number
        String accountNumber =
                (char) ('A' + Math.random() * 26) +
                        "" +
                        (char) ('A' + Math.random() * 26) +
                        "-" +
                        String.format("%07d", (int) (Math.random() * 10_000_000));

        return signupDAO.createCustomerAccount(cnic, name, mail, phone, sqlDate, hashedPassword, accountNumber, accountType.toLowerCase(), branchId);
    }
}
