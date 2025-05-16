package main.java.bankManagementSystem.controller.CustomerDashboard;

import main.java.bankManagementSystem.dao.StaffDashboard.TransactionsDAO;
import main.java.bankManagementSystem.model.TransactionCustomerModel;

import java.util.List;

public class TransactionsController {
    private final TransactionsDAO transactionDAO;

    public TransactionsController() {
        this.transactionDAO = new TransactionsDAO();
    }

    public List<TransactionCustomerModel> handleGetAllTransactionsByAccountNumber(String accountNumber) {
        return transactionDAO.getAllTransactionsByAccountNumber(accountNumber);
    }
}
