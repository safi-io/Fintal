package main.java.bankManagementSystem.model.AdminDashbaord;

public class BranchModel {

    private int branchId;             // Corresponds to BRANCH_ID in the database
    private String branchName;        // Corresponds to BRANCH_NAME
    private String branchAddress;     // Corresponds to BRANCH_ADDRESS
    private String branchPhone;       // Corresponds to BRANCH_PHONE

    // Constructor to initialize the branch object
    public BranchModel(String branchName, String branchAddress, String branchPhone) {
        this.branchName = branchName;
        this.branchAddress = branchAddress;
        this.branchPhone = branchPhone;
    }

    public BranchModel(int branchId, String branchName, String branchAddress, String branchPhone) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.branchAddress = branchAddress;
        this.branchPhone = branchPhone;
    }

    // Getters and Setters for the branch data

    public int getBranchId() {
        return branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public String getBranchPhone() {
        return branchPhone;
    }

    // Optionally override toString method to display branch data nicely
    @Override
    public String toString() {
        return "Branch [branchId=" + branchId + ", branchName=" + branchName + ", branchAddress=" + branchAddress
                + ", branchPhone=" + branchPhone + " ]";
    }
}
