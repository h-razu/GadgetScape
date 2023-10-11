package model;

/**
 *
 * @author Rbkar
 */
public class EmployeeInfo {

    private int EmpID;
    private String EmpName;
    private String Phone;
    private String Email;
    private String Address;
    private String Role;

    public EmployeeInfo(int EmpID, String EmpName, String Phone, String Email, String Address, String Role) {
        this.EmpID = EmpID;
        this.EmpName = EmpName;
        this.Phone = Phone;
        this.Email = Email;
        this.Address = Address;
        this.Role = Role;
    }

    public int getEmpID() {
        return EmpID;
    }

    public void setEmpID(int EmpID) {
        this.EmpID = EmpID;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String EmpName) {
        this.EmpName = EmpName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String Role) {
        this.Role = Role;
    }

}
