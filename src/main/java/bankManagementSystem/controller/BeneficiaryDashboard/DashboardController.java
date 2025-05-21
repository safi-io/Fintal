package main.java.bankManagementSystem.controller.BeneficiaryDashboard;

import main.java.bankManagementSystem.dao.BeneficiaryDashboard.DashboardDAO;
import main.java.bankManagementSystem.model.CustomerAccountBillModel;

public class DashboardController {
    private final DashboardDAO dashboardDAO;

    public DashboardController() {
        this.dashboardDAO = new DashboardDAO();
    }

    public CustomerAccountBillModel dashboardData(String beneficiaryAccountNumber) {

        CustomerAccountBillModel data = new CustomerAccountBillModel();

        data.setUnPaidAmount(dashboardDAO.getUnPaidAmount(beneficiaryAccountNumber));

        data.setPaidAmount(dashboardDAO.getPaidAmount(beneficiaryAccountNumber));

        data.setUnPaidCount(dashboardDAO.getUnPaidCount(beneficiaryAccountNumber));

        data.setPaidCount(dashboardDAO.getPaidCount(beneficiaryAccountNumber));

        data.setLastLogin(dashboardDAO.getLastLogin(beneficiaryAccountNumber));

        data.setBeneficiaryName(dashboardDAO.getBeneficiaryName(beneficiaryAccountNumber));

        data.setBeneficiaryAccountNumber(beneficiaryAccountNumber);


        return data;

    }


}
