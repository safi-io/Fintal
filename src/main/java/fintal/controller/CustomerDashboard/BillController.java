package main.java.fintal.controller.CustomerDashboard;

import main.java.fintal.dao.CustomerDashborad.BillDAO;
import main.java.fintal.model.BillModel;

import java.math.BigDecimal;


public class BillController {
    private final BillDAO billsDAO;

    public BillController() {
        this.billsDAO = new BillDAO();
    }

    public BillModel handleGetBill(String beneficiaryAccountNumber, String referenceNumber) {
        return billsDAO.getBill(beneficiaryAccountNumber, referenceNumber);
    }

    public boolean handlePayBill(String customerAccount, String beneficiaryAccount, String billReferenceNumber, BigDecimal billAmount) throws Exception {
        return billsDAO.payBill(customerAccount, beneficiaryAccount, billReferenceNumber, billAmount);
    }


}
