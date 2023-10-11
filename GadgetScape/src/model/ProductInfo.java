package model;

/**
 *
 * @author Rbkar
 */
public class ProductInfo {
    int Product_ID;
    String Product_Name;
    String Category;
    float Price;
    int Quantity;

    public ProductInfo(int Product_ID, String Product_Name, String Category, float Price, int Quantity) {
        this.Product_ID = Product_ID;
        this.Product_Name = Product_Name;
        this.Category = Category;
        this.Price = Price;
        this.Quantity = Quantity;
    }

    public int getProduct_ID() {
        return Product_ID;
    }

    public void setProduct_ID(int Product_ID) {
        this.Product_ID = Product_ID;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String Product_Name) {
        this.Product_Name = Product_Name;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float Price) {
        this.Price = Price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }
    
}
