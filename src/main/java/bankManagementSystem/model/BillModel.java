package main.java.bankManagementSystem.model;

import java.math.BigDecimal;

public class BillModel {
    private String beneficiaryAccountNumber;
    private String referenceNumber;
    private String consumerName;
    private BigDecimal amount;
    private boolean isPaid;



    // For Receiving Data from DB
    public BillModel(String referenceNumber, String consumerName, BigDecimal amount, boolean isPaid) {
        this.referenceNumber = referenceNumber;
        this.consumerName = consumerName;
        this.amount = amount;
        this.isPaid = isPaid;
    }

    // For Sending Data to DB
    public BillModel(String beneficiaryAccountNumber, String referenceNumber, String consumerName, BigDecimal amount) {
        this.beneficiaryAccountNumber = beneficiaryAccountNumber;
        this.referenceNumber = referenceNumber;
        this.consumerName = consumerName;
        this.amount = amount;
    }

    public String getBeneficiaryAccountNumber() {
        return beneficiaryAccountNumber;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public boolean isPaid() {
        return isPaid;
    }
}
