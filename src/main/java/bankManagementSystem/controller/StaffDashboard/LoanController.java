package main.java.bankManagementSystem.controller.StaffDashboard;

import main.java.bankManagementSystem.dao.StaffDashboard.LoanDAO;
import main.java.bankManagementSystem.model.LoanApplicationModel;

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
