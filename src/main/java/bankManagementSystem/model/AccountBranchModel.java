package main.java.bankManagementSystem.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountBranchModel {

    private String accountNumber;
    private String accountType;
    private BigDecimal accountCurrentBalance;
    private LocalDate accountOpeningDate;
    private LocalDate accountLastLogin;
    private String customerBranch;

    public AccountBranchModel(String accountNumber, String accountType, BigDecimal accountCurrentBalance, LocalDate accountOpeningDate, LocalDate accountLastLogin, String customerBranch) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.accountCurrentBalance = accountCurrentBalance;
        this.accountOpeningDate = accountOpeningDate;
        this.accountLastLogin = accountLastLogin;
        this.customerBranch = customerBranch;
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

    public LocalDate getAccountOpeningDate() {
        return accountOpeningDate;
    }

    public LocalDate getAccountLastLogin() {
        return accountLastLogin;
    }

    public String getCustomerBranch() {
        return customerBranch;
    }
}
