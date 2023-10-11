/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import model.DealerInfo;
import model.ProductInfo;

/**
 * FXML Controller class
 *
 * @author 88016
 */
public class DealerInfoController implements Initializable {

    @FXML
    private TableView<DealerInfo> DealerTable;
    @FXML
    private TableColumn<DealerInfo, String> dealerIdCol;
    @FXML
    private TableColumn<DealerInfo, String> dealerNameCol;
    @FXML
    private TableColumn<DealerInfo, String> dealerPhoneCol;
    @FXML
    private TableColumn<DealerInfo, String> dealerEmailCol;
    @FXML
    private TableColumn<DealerInfo, String> dealerAddressCol;
    @FXML
    private JFXTextArea searchTxtField;
    @FXML
    private ImageView searchBtn;

    private ObservableList<DealerInfo> data;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
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

        DealerTable.getItems().clear();
        String SearchValue = searchTxtField.getText();

        String SearchQuery = "Select *from Dealers where Dealer_ID LIKE '%" + SearchValue + "%' OR Company_Name LIKE '%" + SearchValue + "%'"
                + " OR Phone LIKE '%" + SearchValue + "%' OR Email LIKE '%" + SearchValue + "%' OR Addresses LIKE '%"+ SearchValue + "%'";
        try {
            ResultSet rs = conn.createStatement().executeQuery(SearchQuery);

            while (rs.next()) {
                data.add(new DealerInfo(rs.getInt("Dealer_ID"), rs.getString("Company_Name"), rs.getString("Phone"), rs.getString("Email"), rs.getString("Addresses")));
            }
            dealerIdCol.setCellValueFactory(new PropertyValueFactory<>("DealerID"));
            dealerNameCol.setCellValueFactory(new PropertyValueFactory<>("DealerName"));
            dealerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("DealerPhone"));
            dealerEmailCol.setCellValueFactory(new PropertyValueFactory<>("DealerEmail"));
            dealerAddressCol.setCellValueFactory(new PropertyValueFactory<>("DealerAddress"));

            DealerTable.setItems(data);
            conn.close();

        } catch (SQLException ex) {

            System.out.println("Error" + ex.getMessage());
        }
    }

    private void databaseRecord() throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";

        Connection con = DriverManager.getConnection(connectionUrl);

        data = FXCollections.observableArrayList();

        try {
            ResultSet rs = con.createStatement().executeQuery("Select Dealer_ID, Company_Name ,Phone, Email,Addresses from Dealers");

            while (rs.next()) {
                 data.add(new DealerInfo(rs.getInt("Dealer_ID"), rs.getString("Company_Name"), rs.getString("Phone"), rs.getString("Email"), rs.getString("Addresses")));
            }
            dealerIdCol.setCellValueFactory(new PropertyValueFactory<>("DealerID"));
            dealerNameCol.setCellValueFactory(new PropertyValueFactory<>("DealerName"));
            dealerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("DealerPhone"));
            dealerEmailCol.setCellValueFactory(new PropertyValueFactory<>("DealerEmail"));
            dealerAddressCol.setCellValueFactory(new PropertyValueFactory<>("DealerAddress"));

            DealerTable.setItems(data);

            con.close();

        } catch (SQLException ex) {

            System.out.println("Error" + ex.getMessage());
        }
     
    }
}
