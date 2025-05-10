package main.java.bankManagementSystem.controller.StaffDashboard;

import main.java.bankManagementSystem.dao.AdminDashboard.StaffDAO;
import main.java.bankManagementSystem.dao.StaffDashboard.CustomerDAO;
import main.java.bankManagementSystem.model.CustomerModel;

import java.util.List;

public class CustomerController {
    private final CustomerDAO customerDAO;

    public CustomerController() {
        this.customerDAO = new CustomerDAO();
    }

    public List<CustomerModel> handleGetAllCustomers() {
        return customerDAO.getAllCustomers();
    }
}
