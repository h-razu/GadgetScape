package model;

public class All_Products {

    int productID;
    String productName;
    String productStatus;
    float productPrice;

    public All_Products(int productID, String productName, String productStatus, float productPrice) {
        this.productID = productID;
        this.productName = productName;
        this.productStatus = productStatus;
        this.productPrice = productPrice;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }
    
    public float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

}
