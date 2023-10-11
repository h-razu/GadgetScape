package Admin;

import model.EmployeeInfo;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
public class EmployeeInfoController implements Initializable {

    @FXML
    private TableView<EmployeeInfo> empInfoTable;
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
    private ImageView searchBtn;
    @FXML
    private JFXTextArea searchTxtField;

    private ObservableList<EmployeeInfo> data;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            // TODO
            //show employee information
            databaseRecord();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EmployeeInfoController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeInfoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void searchBtnAction(MouseEvent event) throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";
        Connection conn = DriverManager.getConnection(connectionUrl);

        empInfoTable.getItems().clear();
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

            empInfoTable.setItems(data);

        } catch (SQLException ex) {

            System.out.println("Error" + ex.getMessage());
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("Please input correct data");
            a.show();
        }
    }

    private void databaseRecord() throws ClassNotFoundException, SQLException {

        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";

        Connection con = DriverManager.getConnection(connectionUrl);

        data = FXCollections.observableArrayList();

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

            empInfoTable.setItems(data);

            con.close();

        } catch (SQLException ex) {

            System.out.println("Error" + ex.getMessage());
        }
    }
}
