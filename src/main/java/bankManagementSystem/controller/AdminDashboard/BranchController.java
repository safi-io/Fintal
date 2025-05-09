package main.java.bankManagementSystem.controller.AdminDashboard;

import main.java.bankManagementSystem.dao.AdminDashboard.BranchDAO;
import main.java.bankManagementSystem.model.BranchModel;

import java.util.List;

public class BranchController {

    private final BranchDAO branchDAO;

    public BranchController() {
        this.branchDAO = new BranchDAO();
    }

    public boolean handleCreateBranch(String branchName, String branchAddress, String branchPhone) {

        // Create a Branch model object from the input
        BranchModel branch = new BranchModel(branchName, branchAddress, branchPhone);

        if (branch.getBranchName().isEmpty() || branch.getBranchAddress().isEmpty() || branch.getBranchPhone().isEmpty()) {
            return false;
        }

        // Delegate the database operation to DAO
        try {
            return branchDAO.createBranch(branch);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean handleDeleteBranch(String selectedBranchID) {

        try {
            return branchDAO.deleteBranch(selectedBranchID);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean handleUpdateBranch(int branchId, String branchName, String branchAddress, String branchPhone) {
        // Create a Branch model object from the input
        BranchModel branch = new BranchModel(branchId, branchName, branchAddress, branchPhone);

        if (branch.getBranchName().isEmpty() || branch.getBranchAddress().isEmpty() || branch.getBranchPhone().isEmpty()) {
            return false;
        }

        // Delegate the database operation to DAO
        try {
            return branchDAO.updateBranch(branch);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Integer> handleGetAllBranchIds() {
        try {
            return branchDAO.getAllBranchIds();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public BranchModel handleGetBranchById(int branchId) {
        return branchDAO.getBranchById(branchId);
    }

    public List<String> handleGetBranchIdNameList() {
        try {
            return branchDAO.getBranchIdNameList();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
