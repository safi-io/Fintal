package main.java.bankManagementSystem.controller.CustomerDashboard;

import main.java.bankManagementSystem.dao.StaffDashboard.CustomerDAO;
import main.java.bankManagementSystem.model.CustomerModel;
import org.mindrot.jbcrypt.BCrypt;

public class CustomerController {
    private final CustomerDAO customerDAO;

    public CustomerController() {
        this.customerDAO = new CustomerDAO();
    }

    public CustomerModel handleGetCustomerByAccountNumber(String accountNumber) {
        return customerDAO.handleGetCustomerByAccountNumber(accountNumber);
    }

    public boolean handleUpdateCustomerData(String accountNumber, String customerName, String customerMail, String customerPhone, String customerPassword) {

        if (!customerPassword.isEmpty()) {
            String hashedPassword = BCrypt.hashpw(customerPassword, BCrypt.gensalt());
            return customerDAO.updateCustomerByAccountNumber(accountNumber, customerName, customerMail, customerPhone, hashedPassword);
        } else {
            return customerDAO.updateCustomerByAccountNumber(accountNumber, customerName, customerMail, customerPhone, customerPassword);
        }

    }

    public String handlegetAccountNumberByMail(String mail) {
        return customerDAO.getAccountNumberByMail(mail);
    }

    public String handlegetAccountTypeByAccountNumber(String id) {
        return customerDAO.getAccountTypeByAccountNumber(id);
    }

    public void handleUpdateLastLogin(String accountNumber) {
        customerDAO.updateLastLogin(accountNumber);
    }

    public boolean handleGetIsAccountOpened(String accountNumber) {
        return customerDAO.getIsAccountOpened(accountNumber);
    }

    public boolean handleGetIsAccountDeleted(String accountNumber) {
        return customerDAO.getIsAccountDeleted(accountNumber);
    }
}