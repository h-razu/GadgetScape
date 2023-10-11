package Manager;

import com.jfoenix.controls.JFXTextArea;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
public class DeleteProductController implements Initializable {

    @FXML
    private TableView<ProductInfo> ProductInfoTable;
    @FXML
    private TableColumn<ProductInfo, String> productIdCol;
    @FXML
    private TableColumn<ProductInfo, String> productNameCol;
    @FXML
    private TableColumn<ProductInfo, String> productCategoryCol;
    @FXML
    private TableColumn<ProductInfo, String> productPriceCol;
    @FXML
    private TableColumn<ProductInfo, String> productQuantityCol;
    @FXML
    private JFXTextArea searchTxtField;
    @FXML
    private ImageView searchBtn;
    private ObservableList<ProductInfo> ProductData;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            // TODO
            databaseRecord();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UpdateProductInfoController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(UpdateProductInfoController.class.getName()).log(Level.SEVERE, null, ex);
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
                + " OR Price LIKE '%" + SearchValue + "%' OR Category LIKE '%" + SearchValue + "%'"
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

    void databaseRecord() throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";
        Connection con = DriverManager.getConnection(connectionUrl);

        ProductData = FXCollections.observableArrayList();///Why?

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

    @FXML
    private void DeleteBtnAction(ActionEvent event) throws ClassNotFoundException {

        if (ProductInfoTable.getSelectionModel().getSelectedItem() != null) {
            ProductInfo data = ProductInfoTable.getSelectionModel().getSelectedItem();
            int id = data.getProduct_ID();

            try {
                Connection con;

                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";
                con = DriverManager.getConnection(connectionUrl);

                String sql = "delete from Products where Product_ID=" + id + "";
                Statement stmt = con.createStatement();
                stmt.executeUpdate(sql);
                con.close();
                ProductInfoTable.getItems().remove(data);
            } catch (SQLException ex) {
                System.out.println("Error:" + ex);

            }
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.WARNING);
            errorAlert.setHeaderText("No Selection");
            errorAlert.setContentText("Please Choose An Item...");
            errorAlert.showAndWait();
        }
    }

}
