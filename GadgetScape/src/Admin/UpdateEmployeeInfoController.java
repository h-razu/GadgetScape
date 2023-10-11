package Admin;

import model.EmployeeInfo;
import com.jfoenix.controls.JFXTextArea;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Rbkar
 */
public class UpdateEmployeeInfoController implements Initializable {

    private ObservableList<EmployeeInfo> data = FXCollections.observableArrayList();
    ;

    @FXML
    private TableColumn<EmployeeInfo, String> empIDcol;
    @FXML
    private TableColumn<EmployeeInfo, String> empNameCol;
    @FXML
    private TableColumn<EmployeeInfo, String> empPhoneCol;
    @FXML
    private TableColumn<EmployeeInfo, String> empEmailCol;
    @FXML
    private TableColumn<EmployeeInfo, String> empAddressCol;
    @FXML
    private JFXTextArea searchTxtField;
    @FXML
    private ImageView searchBtn;

    @FXML
    private TableView<EmployeeInfo> updateEmpInfoTable;
    @FXML
    private Button updateBtn;
    @FXML
    private AnchorPane updateEmployeeInfoPane;
    static int empID;
    @FXML
    private Button RefreshBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            databaseRecord();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UpdateEmployeeInfoController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(UpdateEmployeeInfoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void databaseRecord() throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";

        Connection con = DriverManager.getConnection(connectionUrl);

        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT Emp_ID,EmpName,Phone,Email,Addresses,Roles FROM Employee");

            while (rs.next()) {

                data.add(new EmployeeInfo(rs.getInt("Emp_ID"), rs.getString("EmpName"), rs.getString("Phone"), rs.getString("Email"), rs.getString("Addresses"), rs.getString("Roles")));

            }
            empIDcol.setCellValueFactory(new PropertyValueFactory<>("EmpID"));
            empNameCol.setCellValueFactory(new PropertyValueFactory<>("EmpName"));
            empPhoneCol.setCellValueFactory(new PropertyValueFactory<>("Phone"));
            empEmailCol.setCellValueFactory(new PropertyValueFactory<>("Email"));
            empAddressCol.setCellValueFactory(new PropertyValueFactory<>("Address"));

            updateEmpInfoTable.setItems(data);

            con.close();

        } catch (SQLException ex) {

            System.out.println("Error" + ex.getMessage());
        }
    }

    @FXML
    private void searchBtnAction(MouseEvent event) throws SQLException, ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";
        Connection conn = DriverManager.getConnection(connectionUrl);

        updateEmpInfoTable.getItems().clear();
        String SearchValue = searchTxtField.getText();

        String SearchQuery = "Select *from Employee where Emp_ID LIKE '%" + SearchValue + "%' OR Phone LIKE '%" + SearchValue + "%'"
                + " OR Email LIKE '%" + SearchValue + "%' OR EmpName LIKE '%" + SearchValue + "%'";
        try {
            ResultSet rs = conn.createStatement().executeQuery(SearchQuery);

            while (rs.next()) {
                data.add(new EmployeeInfo(rs.getInt("Emp_ID"), rs.getString("EmpName"), rs.getString("Phone"), rs.getString("Email"), rs.getString("Addresses"), rs.getString("Roles")));
            }
            empIDcol.setCellValueFactory(new PropertyValueFactory<>("EmpID"));
            empNameCol.setCellValueFactory(new PropertyValueFactory<>("EmpName"));
            empPhoneCol.setCellValueFactory(new PropertyValueFactory<>("Phone"));
            empEmailCol.setCellValueFactory(new PropertyValueFactory<>("Email"));
            empAddressCol.setCellValueFactory(new PropertyValueFactory<>("Address"));

            updateEmpInfoTable.setItems(data);

        } catch (SQLException ex) {

            System.out.println("Error" + ex.getMessage());
        }
    }

    @FXML
    private void updateBtnAction(MouseEvent event) throws IOException {
        if (updateEmpInfoTable.getSelectionModel().getSelectedItem() != null) {
            EmployeeInfo data = updateEmpInfoTable.getSelectionModel().getSelectedItem();
            empID = data.getEmpID();

            Pane view = new FXMLLoader().load(getClass().getResource("/Admin/updateEmployeeInfoPopUp.fxml"));
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
    private void refershBtnAction(MouseEvent event) throws ClassNotFoundException {
        try {
           Parent ProductDashboar= FXMLLoader.load(getClass().getResource("/Admin/AdminDashboard.fxml"));
           Scene scr = new Scene(ProductDashboar);
           Stage mp = (Stage) ((Node) event.getSource()).getScene().getWindow();
           mp.setScene(scr);
           mp.setTitle("Admin Dashboard");
           mp.show();

        } catch (Exception ex) {
            System.out.println("can't load");
        }
    }
}
