package main.java.bankManagementSystem.controller.CustomerDashboard;

import main.java.bankManagementSystem.dao.StaffDashboard.CustomerDAO;
import main.java.bankManagementSystem.model.CustomerModel;

public class CustomerController {
    private final CustomerDAO customerDAO;

    public CustomerController() {
        this.customerDAO = new CustomerDAO();
    }

    public CustomerModel handleGetCustomerByAccountNumber(String accountNumber) {
        return customerDAO.handleGetCustomerByAccountNumber(accountNumber);
    }

    public boolean handleUpdateCustomerData(String accountNumber, String customerName, String customerMail, String customerPhone, String customerPassword) {
        return customerDAO.updateCustomerByAccountNumber(accountNumber, customerName, customerMail, customerPhone, customerPassword);
    }
}
