package main.java.bankManagementSystem.model;

import java.time.LocalDate;

public class StaffModel {
    private int staffId;
    private String staffName;
    private String staffMail;
    private String staffPhone;
    private String staffCNIC;
    private LocalDate staffDOB;
    private String staffPassword;
    private int staffBranchId;

    // Creation
    public StaffModel(String staffName, String staffMail, String staffPhone, String staffCNIC, LocalDate staffDOB, String staffPassword, int staffBranchId) {
        this.staffName = staffName;
        this.staffMail = staffMail;
        this.staffPhone = staffPhone;
        this.staffCNIC = staffCNIC;
        this.staffDOB = staffDOB;
        this.staffPassword = staffPassword;
        this.staffBranchId = staffBranchId;
    }

    // DAO, Update
    public StaffModel(int staffId, String staffName, String staffMail, String staffPhone, String staffCNIC, LocalDate staffDOB, int staffBranchId) {
        this.staffId = staffId;
        this.staffName = staffName;
        this.staffMail = staffMail;
        this.staffPhone = staffPhone;
        this.staffCNIC = staffCNIC;
        this.staffDOB = staffDOB;
        this.staffBranchId = staffBranchId;
    }

    public int getStaffId() {
        return staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public String getStaffMail() {
        return staffMail;
    }

    public String getStaffPhone() {
        return staffPhone;
    }

    public String getStaffCNIC() {
        return staffCNIC;
    }

    public LocalDate getStaffDOB() {
        return staffDOB;
    }

    public String getStaffPassword() {return staffPassword;}

    public int getStaffBranchId() {
        return staffBranchId;
    }

    @Override
    public String toString() {
        return "Staff [staffId=" + staffId + ", staffName=" + staffName + ", staffMail=" + staffMail + ", staffBranchId=" + staffBranchId + " ]";
    }
}
