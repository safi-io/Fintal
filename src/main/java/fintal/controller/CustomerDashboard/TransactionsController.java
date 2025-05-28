package main.java.fintal.controller.CustomerDashboard;

import main.java.fintal.dao.CustomerDashborad.TransactionsDAO;
import main.java.fintal.model.TransactionCustomerModel;

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
