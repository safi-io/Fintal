package main.java.fintal.controller.StaffDashboard;

import main.java.fintal.dao.StaffDashboard.LoanDAO;
import main.java.fintal.model.LoanApplicationModel;

import java.math.BigDecimal;
import java.util.List;

public class LoanController {
    private final LoanDAO loanDAO;

    public LoanController() {
        this.loanDAO = new LoanDAO();
    }

    public List<LoanApplicationModel> handleGetLoanApplications() {
        return loanDAO.getLoanApplications();
    }

    public void handleApproveLoanAndDisbruse(int loanId, BigDecimal loanAmount, String accountNumber) {
        loanDAO.approveLoanAndDisburse(loanId,loanAmount, accountNumber);
    }
}
