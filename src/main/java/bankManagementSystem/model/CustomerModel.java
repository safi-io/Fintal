package main.java.bankManagementSystem.model;

import java.time.LocalDate;

public class CustomerModel {
    private String customerCNIC;
    private String customerName;
    private String customerMail;
    private String customerPhone;
    private LocalDate customerDOB;
    private String customerPassword;

    public CustomerModel(String customerCNIC, String customerName, String customerMail, String customerPhone, LocalDate customerDOB, String customerPassword) {
        this.customerCNIC = customerCNIC;
        this.customerName = customerName;
        this.customerMail = customerMail;
        this.customerPhone = customerPhone;
        this.customerDOB = customerDOB;
        this.customerPassword = customerPassword;
    }

    public CustomerModel(String customerCNIC, String customerName, String customerMail, String customerPhone, LocalDate customerDOB) {
        this.customerCNIC = customerCNIC;
        this.customerName = customerName;
        this.customerMail = customerMail;
        this.customerPhone = customerPhone;
        this.customerDOB = customerDOB;
    }

    public String getCustomerPassword() {
        return customerPassword;
    }

    public String getCustomerCNIC() {
        return customerCNIC;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerMail() {
        return customerMail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public LocalDate getCustomerDOB() {
        return customerDOB;
    }
}
