package main.java.fintal.controller.StaffDashboard;

import main.java.fintal.dao.StaffDashboard.CustomerDAO;
import main.java.fintal.model.CustomerModel;

import java.util.List;

public class CustomerController {
    private final CustomerDAO customerDAO;

    public CustomerController() {
        this.customerDAO = new CustomerDAO();
    }

    public List<CustomerModel> handleGetAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    public CustomerModel handleGetCustomerByCNIC(String CNIC) {
        return customerDAO.getCustomerByCNIC(CNIC);
    }


}
