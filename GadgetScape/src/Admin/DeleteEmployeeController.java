package Admin;

import model.EmployeeInfo;
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
public class DeleteEmployeeController implements Initializable {

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
    private TableColumn<EmployeeInfo, String> empRoleCol;
    @FXML
    private JFXTextArea searchTxtField;
    @FXML
    private ImageView searchBtn;
    @FXML
    private Button deleteBtn;

    private ObservableList<EmployeeInfo> data;
    @FXML
    private TableView<EmployeeInfo> deleteEmpInfoTable;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            databaseRecord();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DeleteEmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DeleteEmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void searchBtnAction(MouseEvent event) throws SQLException, ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";
        Connection conn = DriverManager.getConnection(connectionUrl);

        deleteEmpInfoTable.getItems().clear();
        String SearchValue = searchTxtField.getText();

        String SearchQuery = "Select *from Employee where Emp_ID LIKE '%" + SearchValue + "%' OR Phone LIKE '%" + SearchValue + "%'"
                + " OR Email LIKE '%" + SearchValue + "%' OR EmpName LIKE '%" + SearchValue + "%' OR Roles LIKE '%" + SearchValue + "%'";
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
            empRoleCol.setCellValueFactory(new PropertyValueFactory<>("Role"));
            deleteEmpInfoTable.setItems(data);

        } catch (SQLException ex) {

            System.out.println("Error " + ex.getMessage());
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("Please input correct data");
            a.show();
        }
    }

    @FXML
    private void deleteBtnAction(MouseEvent event) throws ClassNotFoundException {

        try {

            if (deleteEmpInfoTable.getSelectionModel().getSelectedItem() != null) {
                EmployeeInfo data = deleteEmpInfoTable.getSelectionModel().getSelectedItem();
                int empID = data.getEmpID();
                String role = data.getRole();

                Connection con;

                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";
                con = DriverManager.getConnection(connectionUrl);

                if (role.equalsIgnoreCase("Manager")) {
                    String sql = "DELETE from Manager where Emp_ID=" + empID + "";
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(sql);
                    deleteEmpInfoTable.getItems().remove(data);
                    System.out.println("SuccessfulI");
                } else {
                    String sql = "DELETE from Salesman where Emp_ID=" + empID + "";
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(sql);
                    deleteEmpInfoTable.getItems().remove(data);
                    System.out.println("SuccessfulI");
                }
                String sql = "DELETE from Employee where Emp_ID=" + empID + "";
                Statement stmt = con.createStatement();
                stmt.executeUpdate(sql);

            } else {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setContentText("No data selected");
                a.show();
            }
        } catch (SQLException ex) {
            System.out.println("Error:" + ex);

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
            empRoleCol.setCellValueFactory(new PropertyValueFactory<>("Role"));
            deleteEmpInfoTable.setItems(data);

            con.close();

        } catch (SQLException ex) {

            System.out.println("Error" + ex.getMessage());
        }
    }

}
