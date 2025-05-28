package main.java.fintal.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CustomerAccountBillModel {
    private BigDecimal unPaidAmount;
    private BigDecimal paidAmount;
    private int unPaidCount;
    private int paidCount;
    private LocalDate lastLogin;
    private String beneficiaryName;
    private String beneficiaryAccountNumber;

    public CustomerAccountBillModel() {}

    public CustomerAccountBillModel(BigDecimal unPaidAmount, BigDecimal paidAmount, int unPaidCount, int paidCount, LocalDate lastLogin, String beneficiaryName, String beneficiaryAccountNumber) {
        this.unPaidAmount = unPaidAmount;
        this.paidAmount = paidAmount;
        this.unPaidCount = unPaidCount;
        this.paidCount = paidCount;
        this.lastLogin = lastLogin;
        this.beneficiaryName = beneficiaryName;
        this.beneficiaryAccountNumber = beneficiaryAccountNumber;
    }

    public void setUnPaidAmount(BigDecimal unPaidAmount) {
        this.unPaidAmount = unPaidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public void setUnPaidCount(int unPaidCount) {
        this.unPaidCount = unPaidCount;
    }

    public void setPaidCount(int paidCount) {
        this.paidCount = paidCount;
    }

    public void setLastLogin(LocalDate lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public void setBeneficiaryAccountNumber(String beneficiaryAccountNumber) {
        this.beneficiaryAccountNumber = beneficiaryAccountNumber;
    }

    public BigDecimal getUnPaidAmount() {
        return unPaidAmount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public int getUnPaidCount() {
        return unPaidCount;
    }

    public int getPaidCount() {
        return paidCount;
    }

    public LocalDate getLastLogin() {
        return lastLogin;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public String getBeneficiaryAccountNumber() {
        return beneficiaryAccountNumber;
    }
}
