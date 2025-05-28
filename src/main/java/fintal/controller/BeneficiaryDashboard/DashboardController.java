package main.java.fintal.controller.BeneficiaryDashboard;

import main.java.fintal.dao.BeneficiaryDashboard.DashboardDAO;
import main.java.fintal.model.CustomerAccountBillModel;

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
