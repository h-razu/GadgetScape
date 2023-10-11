package Manager;

import model.DealerInfo;
import com.jfoenix.controls.JFXTextArea;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Rbkar
 */
public class ManagerInfoController implements Initializable {

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
        // TODO
    }    

    @FXML
    private void searchBtnAction(MouseEvent event) {
    }
    
    /*private void databaseRecord() throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";

        Connection con = DriverManager.getConnection(connectionUrl);

        data = FXCollections.observableArrayList();

        try {
            ResultSet rs = con.createStatement().executeQuery("");

            while (rs.next()) {

                

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
        }*/
    }
