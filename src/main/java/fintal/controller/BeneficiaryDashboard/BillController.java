package main.java.fintal.controller.BeneficiaryDashboard;

import main.java.fintal.dao.BeneficiaryDashboard.BillDAO;
import main.java.fintal.model.BillModel;

import java.math.BigDecimal;
import java.util.List;

public class BillController {
    private final BillDAO billsDAO;

    public BillController() {
        this.billsDAO = new BillDAO();
    }

    public void handleCreateBill(String beneficiaryAccountNumber, String referenceNumber, String consumerName, BigDecimal Amount) {
        BillModel bill = new BillModel(beneficiaryAccountNumber, referenceNumber, consumerName, Amount);

        if (bill.getConsumerName().isEmpty() || bill.getBeneficiaryAccountNumber().isEmpty() || bill.getReferenceNumber().isEmpty()) {
            return;
        }

        try {
            billsDAO.createBranch(bill);

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    public List<BillModel> handleGetAllBills(String beneficiaryAccountNumber) {
        return billsDAO.getAllBills(beneficiaryAccountNumber);
    }
}
