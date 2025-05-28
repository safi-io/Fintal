package main.java.fintal.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CustomerAccountBranchModel {
    // Customer Fields
    private String customerCNIC;
    private String customerName;
    private String customerMail;
    private String customerPhone;
    private LocalDate customerDOB;
    private String customerPassword;

    // Account Fields
    private String accountNumber;
    private String accountType;
    private BigDecimal accountCurrentBalance;
    private boolean accountIsOpened;
    private boolean accountIsDeleted;
    private LocalDate accountOpeningDate;
    private LocalDate accountLastLogin;
    private String accountCustomerCNIC;
    private String accountCustomerBranchID;

    // Branch Fields
    private String customerBranch;

    // For Dashboard Data
    public CustomerAccountBranchModel(String accountNumber, String accountType, BigDecimal accountCurrentBalance, LocalDate accountOpeningDate, LocalDate accountLastLogin, String customerBranch, String customerName) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.accountCurrentBalance = accountCurrentBalance;
        this.accountOpeningDate = accountOpeningDate;
        this.accountLastLogin = accountLastLogin;
        this.customerBranch = customerBranch;
        this.customerName = customerName;
    }


    public CustomerAccountBranchModel(String customerCNIC, String customerName, String customerMail, String customerPhone, String accountNumber, String accountType, LocalDate accountOpeningDate, String accountCustomerBranchID, String customerBranch) {
        this.customerCNIC = customerCNIC;
        this.customerName = customerName;
        this.customerMail = customerMail;
        this.customerPhone = customerPhone;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.accountOpeningDate = accountOpeningDate;
        this.accountCustomerBranchID = accountCustomerBranchID;
        this.customerBranch = customerBranch;
    }

    public CustomerAccountBranchModel(String customerCNIC, String customerName, String customerMail, String customerPhone, String accountNumber, BigDecimal accountCurrentBalance, String accountType, LocalDate accountOpeningDate, String accountCustomerBranchID, String customerBranch) {
        this.customerCNIC = customerCNIC;
        this.customerName = customerName;
        this.customerMail = customerMail;
        this.customerPhone = customerPhone;
        this.accountNumber = accountNumber;
        this.accountCurrentBalance = accountCurrentBalance;
        this.accountType = accountType;
        this.accountOpeningDate = accountOpeningDate;
        this.accountCustomerBranchID = accountCustomerBranchID;
        this.customerBranch = customerBranch;
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

    public String getCustomerPassword() {
        return customerPassword;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public BigDecimal getAccountCurrentBalance() {
        return accountCurrentBalance;
    }

    public boolean isAccountIsOpened() {
        return accountIsOpened;
    }

    public boolean isAccountIsDeleted() {
        return accountIsDeleted;
    }

    public LocalDate getAccountOpeningDate() {
        return accountOpeningDate;
    }

    public String getAccountCustomerCNIC() {
        return accountCustomerCNIC;
    }

    public String getAccountCustomerBranchID() {
        return accountCustomerBranchID;
    }

    public String getCustomerBranch() {
        return customerBranch;
    }

    public LocalDate getAccountLastLogin() {
        return accountLastLogin;
    }
}
