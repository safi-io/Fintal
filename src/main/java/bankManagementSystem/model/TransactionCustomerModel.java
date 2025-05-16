package main.java.bankManagementSystem.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TransactionCustomerModel {
    private String transactionType;
    private BigDecimal transactionAmount;
    private String transactionDescription;
    private String counterPartyAccountNumber;
    private String counterPartyName;
    private Timestamp timestamp;


    public TransactionCustomerModel(String transactionType, BigDecimal transactionAmount, String transactionDescription, String counterPartyAccountNumber, String counterPartyName, Timestamp timestamp) {
        this.transactionType = transactionType;
        this.transactionAmount = transactionAmount;
        this.transactionDescription = transactionDescription;
        this.counterPartyAccountNumber = counterPartyAccountNumber;
        this.counterPartyName = counterPartyName;
        this.timestamp = timestamp;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public String getCounterPartyAccountNumber() {
        return counterPartyAccountNumber;
    }

    public String getCounterPartyName() {
        return counterPartyName;
    }
}
