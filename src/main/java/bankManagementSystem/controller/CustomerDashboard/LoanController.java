package main.java.bankManagementSystem.controller.CustomerDashboard;


import main.java.bankManagementSystem.dao.CustomerDashborad.LoanDAO;
import main.java.bankManagementSystem.model.LoanApplicationModel;

import java.math.BigDecimal;
import java.util.List;

public class LoanController {
    private final LoanDAO loanDAO;

    public LoanController() {
        this.loanDAO = new LoanDAO();
    }

    public List<LoanApplicationModel> handleGetLoanApplicationByAccountNumber(String accountNumber) {
        return loanDAO.getLoanApplicationByAccountNumber(accountNumber);
    }

    public boolean handleSubmitLoanApplication(LoanApplicationModel loanApplication) {
        return loanDAO.submitLoanApplication(loanApplication);
    }

    public BigDecimal handleGetOutstandingLoanBalance(String accountNumber) {
        return loanDAO.getOutstandingLoanBalance(accountNumber);
    }

}
