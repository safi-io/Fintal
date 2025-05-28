package main.java.fintal.controller.CustomerDashboard;


import main.java.fintal.dao.CustomerDashborad.LoanDAO;
import main.java.fintal.model.LoanApplicationModel;

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
