package main.java.bankManagementSystem.controller.CustomerDashboard;

import main.java.bankManagementSystem.dao.CustomerDashborad.BeneficiaryDAO;

import java.util.List;

public class BeneficiaryController {

    private final BeneficiaryDAO beneficiryDAO;

    public BeneficiaryController() {
        this.beneficiryDAO = new BeneficiaryDAO();
    }

    public List<String> handleGetBeneficiaryAccountNumbers() {
        return beneficiryDAO.getBeneficiaryAccountNumbers();
    }

}
