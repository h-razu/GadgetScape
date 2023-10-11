package Manager;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.ProductInfo;

/**
 * FXML Controller class
 *
 * @author Rbkar
 */
public class UpdateProductInfoController implements Initializable {

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
    @FXML
    private JFXButton UpdateBtn;
    public static  int productID;
    public static int productQuantity;
    @FXML
    private JFXButton RefreshBtn;
    @FXML
    private AnchorPane UpdatePane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
    void databaseRecord() throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";
            Connection con = DriverManager.getConnection(connectionUrl);
            
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

    @FXML
    private void UpdateBtnAction(ActionEvent event) throws IOException {
         if (ProductInfoTable.getSelectionModel().getSelectedItem() != null) {
           
            ProductInfo data = ProductInfoTable.getSelectionModel().getSelectedItem();
            productID = data.getProduct_ID();
            productQuantity= data.getQuantity();
            Pane view = new FXMLLoader().load(getClass().getResource("/Manager/UpdateProductPopUp.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(view));
            stage.show();
        } else {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("No data selected");
            a.show();
        }
    }

    @FXML
    private void RefreshBtnAction(ActionEvent event) {
         try {
           Parent ProductDashboar= FXMLLoader.load(getClass().getResource("/Manager/ManagerDashboard.fxml"));
           Scene scr = new Scene(ProductDashboar);
           Stage mp = (Stage) ((Node) event.getSource()).getScene().getWindow();
           mp.setScene(scr);
           mp.setTitle("Product Dashboard");
           mp.show();
          
        } catch (Exception ex) {
            System.out.println("can't load");
        }
    }
    
}
