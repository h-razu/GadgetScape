package Manager;

import com.jfoenix.controls.JFXTextArea;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.ProductInfo;

/**
 * FXML Controller class
 *
 * @author Rbkar
 */
public class ProductInfoController implements Initializable {

    @FXML
    private TableColumn<ProductInfo, String> productIdCol;
    @FXML
    private TableColumn<ProductInfo, String> productNameCol;
    @FXML
    private TableColumn<ProductInfo, String> productCategoryCol;
    @FXML
    private TableColumn<ProductInfo, String> productPriceCol;
    @FXML
    private JFXTextArea searchTxtField;
    @FXML
    private ImageView searchBtn;
    @FXML
    private TableView<ProductInfo> ProductInfoTable;
    @FXML
    private TableColumn<ProductInfo, String> productQuantityCol;
    
    Connection con = null;
    
    ObservableList<ProductInfo> ProductData = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            // TODO
            databaseRecord();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProductInfoController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ProductInfoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void searchBtnAction(MouseEvent event) throws ClassNotFoundException, SQLException {
          Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";
        Connection conn = DriverManager.getConnection(connectionUrl);

        ProductInfoTable.getItems().clear();
        String SearchValue = searchTxtField.getText();

        String SearchQuery = "Select *from Products where Product_ID LIKE '%" + SearchValue + "%' OR Product_Name LIKE '%" + SearchValue + "%'"
                + " OR Price LIKE '%" + SearchValue + "%' OR Category LIKE '%"+ SearchValue + "%'"
                + " OR Quantity LIKE '%" + SearchValue + "%'";
        try {
            ResultSet rs = conn.createStatement().executeQuery(SearchQuery);

            while (rs.next()) {

                ProductData.add(new ProductInfo(rs.getInt("Product_ID"), rs.getString("Product_Name"), rs.getString("Category"), rs.getFloat("Price"), rs.getInt("Quantity")));

            }
            productIdCol.setCellValueFactory(new PropertyValueFactory<>("Product_ID"));
            productNameCol.setCellValueFactory(new PropertyValueFactory<>("Product_Name"));
            productCategoryCol.setCellValueFactory(new PropertyValueFactory<>("Category"));
            productPriceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
            productQuantityCol.setCellValueFactory(new PropertyValueFactory<>("Quantity"));

            ProductInfoTable.setItems(ProductData);
            conn.close();


        } catch (SQLException ex) {

            System.out.println("Error" + ex.getMessage());
        }
    }
    private void databaseRecord() throws ClassNotFoundException, SQLException{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";
            con = DriverManager.getConnection(connectionUrl);
            
            ProductData = FXCollections.observableArrayList();
            
            try {
            ResultSet rs = con.createStatement().executeQuery("Select Product_ID, Product_Name ,Category, Price,Quantity from Products");

            while (rs.next()) {

                ProductData.add(new ProductInfo(rs.getInt("Product_ID"), rs.getString("Product_Name"), rs.getString("Category"), rs.getFloat("Price"), rs.getInt("Quantity")));

            }
            productIdCol.setCellValueFactory(new PropertyValueFactory<>("Product_ID"));
            productNameCol.setCellValueFactory(new PropertyValueFactory<>("Product_Name"));
            productCategoryCol.setCellValueFactory(new PropertyValueFactory<>("Category"));
            productPriceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
            productQuantityCol.setCellValueFactory(new PropertyValueFactory<>("Quantity"));

            ProductInfoTable.setItems(ProductData);

            con.close();

        } catch (SQLException ex) {

            System.out.println("Error" + ex.getMessage());
        }
            
    }
    
}
