package model;

/**
 *
 * @author Rbkar
 */
public class DealerInfo {
   private int DealerID;
   private String DealerName;
   private String DealerPhone;
   private String DealerEmail;
   private String DealerAddress;

    public DealerInfo(int DealerID, String DealerName, String DealerPhone, String DealerEmail, String DealerAddress) {
        this.DealerID = DealerID;
        this.DealerName = DealerName;
        this.DealerPhone = DealerPhone;
        this.DealerEmail = DealerEmail;
        this.DealerAddress = DealerAddress;
    }

    public int getDealerID() {
        return DealerID;
    }

    public void setDealerID(int DealerID) {
        this.DealerID = DealerID;
    }

    public String getDealerName() {
        return DealerName;
    }

    public void setDealerName(String DealerName) {
        this.DealerName = DealerName;
    }

    public String getDealerPhone() {
        return DealerPhone;
    }

    public void setDealerPhone(String DealerPhone) {
        this.DealerPhone = DealerPhone;
    }

    public String getDealerEmail() {
        return DealerEmail;
    }

    public void setDealerEmail(String DealerEmail) {
        this.DealerEmail = DealerEmail;
    }

    public String getDealerAddress() {
        return DealerAddress;
    }

    public void setDealerAddress(String DealerAddress) {
        this.DealerAddress = DealerAddress;
    }
   
   
}
