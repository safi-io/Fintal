package main.java.bankManagementSystem.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountModel {
    private String accountNumber;
    private String accountType;
    private BigDecimal accountCurrentBalance;
    private boolean accountIsOpened;
    private boolean accountIsDeleted;
    private LocalDate accountOpeningDate;
    private String accountCustomerCNIC;
    private String accountCustomerBranchID;

    public AccountModel(String accountNumber, String accountType, BigDecimal accountCurrentBalance, boolean accountIsOpened, boolean accountIsDeleted, LocalDate accountOpeningDate, String accountCustomerCNIC, String accountCustomerBranchID) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.accountCurrentBalance = accountCurrentBalance;
        this.accountIsOpened = accountIsOpened;
        this.accountIsDeleted = accountIsDeleted;
        this.accountOpeningDate = accountOpeningDate;
        this.accountCustomerCNIC = accountCustomerCNIC;
        this.accountCustomerBranchID = accountCustomerBranchID;
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
}
