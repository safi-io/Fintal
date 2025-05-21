package main.java.bankManagementSystem.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LoanApplicationModel {
    private int loanID;
    private BigDecimal loanAmount;
    private String loanAccountNumber;
    private int loanTenure;
    private String loanPurpose;
    private LocalDate loanDate;
    private String loanNote;
    private boolean loanIsApproved;

    public LoanApplicationModel() {
    }

    // For Showing History in Customer Dashboard

    public LoanApplicationModel(int loanID, BigDecimal loanAmount, int loanTenure, String loanPurpose,LocalDate loanDate, boolean loanIsApproved) {
        this.loanID = loanID;
        this.loanAmount = loanAmount;
        this.loanTenure = loanTenure;
        this.loanPurpose = loanPurpose;
        this.loanDate = loanDate;
        this.loanIsApproved = loanIsApproved;
    }

    // For Getting All Applications

    public LoanApplicationModel(int loanID, BigDecimal loanAmount, int loanTenure, String loanPurpose, String loanNote, LocalDate loanDate, String loanAccountNumber) {
        this.loanID = loanID;
        this.loanAmount = loanAmount;
        this.loanTenure = loanTenure;
        this.loanPurpose = loanPurpose;
        this.loanNote = loanNote;
        this.loanDate = loanDate;
        this.loanAccountNumber = loanAccountNumber;
    }



    public int getLoanID() {
        return loanID;
    }

    public void setLoanID(int loanID) {
        this.loanID = loanID;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getLoanAccountNumber() {
        return loanAccountNumber;
    }

    public void setLoanAccountNumber(String loanAccountNumber) {
        this.loanAccountNumber = loanAccountNumber;
    }

    public int getLoanTenure() {
        return loanTenure;
    }

    public void setLoanTenure(int loanTenure) {
        this.loanTenure = loanTenure;
    }

    public String getLoanPurpose() {
        return loanPurpose;
    }

    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public String getLoanNote() {
        return loanNote;
    }

    public void setLoanNote(String loanNote) {
        this.loanNote = loanNote;
    }

    public boolean isLoanIsApproved() {
        return loanIsApproved;
    }

    public void setLoanIsApproved(boolean loanIsApproved) {
        this.loanIsApproved = loanIsApproved;
    }
}
