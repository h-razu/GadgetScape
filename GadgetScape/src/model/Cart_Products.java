
package model;

public class Cart_Products {
    int ProductID;
    String name;
    int quantity;
    float price;

    public Cart_Products() {
    }

    public Cart_Products(int ProductID, String name, int quantity, float price) {
        this.ProductID = ProductID;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int ProductID) {
        this.ProductID = ProductID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
    
    
    
}
