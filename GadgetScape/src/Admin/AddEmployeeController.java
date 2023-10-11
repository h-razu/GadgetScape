package Admin;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Rbkar
 */
public class AddEmployeeController implements Initializable {

    @FXML
    private AnchorPane Rootpane;
    @FXML
    private JFXTextField NidTxtField;
    @FXML
    private JFXTextField empPhoneTxtField;
    @FXML
    private JFXButton DoneBtn;
    @FXML
    private JFXTextField empEmailTxtField;
    @FXML
    private Pane SalesManPane;
    @FXML
    private JFXTextField SMworkingTxtField;
    @FXML
    private JFXTextField SMrateTxtField;
    @FXML
    private JFXTextField managerRateTxtField;
    @FXML
    private JFXTextField managerPeriodTxtField;
    @FXML
    private JFXTextField empNameTxtField;
    @FXML
    private JFXComboBox<String> empRolesComboBox;
    @FXML
    private Pane ManagerPane;

    ObservableList<String> rolesList = FXCollections.observableArrayList("Manager", "Salesman");
    @FXML
    private JFXTextField empAddressTxtField;
    @FXML
    private JFXTextField empPassTxtField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        empRolesComboBox.setItems(rolesList);
        ManagerPane.setVisible(false);
        SalesManPane.setVisible(false);
    }

    @FXML
    private void rolesFieldAction(ActionEvent event) {

        String roleName = empRolesComboBox.getValue();
        if (roleName.equalsIgnoreCase("Manager")) {
            ManagerPane.setVisible(true);
            SalesManPane.setVisible(false);
        }
        if (roleName.equalsIgnoreCase("SalesMan")) {
            SalesManPane.setVisible(true);
            ManagerPane.setVisible(false);
        }
    }

    @FXML
    private void DoneBtnAction(ActionEvent event) throws ClassNotFoundException, SQLException {

        Connection conn;

        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";
        conn = DriverManager.getConnection(connectionUrl);
        boolean emailFormat = false;
        String email = empEmailTxtField.getText();
        String phone = empPhoneTxtField.getText();

        if (!(empRolesComboBox.getValue() == null) && !(NidTxtField.getText().isEmpty()) && !(empPhoneTxtField.getText().isEmpty())
                && !(empEmailTxtField.getText().isEmpty()) && !(empNameTxtField.getText().isEmpty()) && !(empAddressTxtField.getText().isEmpty())
                && !(empPassTxtField.getText().isEmpty())) {

            ArrayList<Integer> existingID = new ArrayList<Integer>();
            int employeeID;

            //check emp_ID for duplicate value
            String getEmpID = "SELECT Emp_ID FROM Employee";

            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(getEmpID);

            while (rs.next()) {
                existingID.add(rs.getInt("Emp_ID"));
            }

            while (true) {
                employeeID = generateRandomEmpID();

                if (!existingID.contains(employeeID)) {
                    break;
                }
            }

            emailFormat = Pattern.matches("[^.\\s][a-z0-9\\.\\_]{3,64}@([a-z]){2,50}.[a-z]{2,}", email);

            if (emailFormat && phone.length() == 11) {

                String sql = "INSERT INTO Employee (Emp_ID,EmpName,Email,Phone,Passwords,NID,Addresses,Roles)"
                        + "Values(" + employeeID + ",'" + empNameTxtField.getText() + "','" + empEmailTxtField.getText() + "','"
                        + empPhoneTxtField.getText() + "','" + empPassTxtField.getText() + "','" + NidTxtField.getText() + "','" + empAddressTxtField.getText() + "','"
                        + empRolesComboBox.getValue() + "')";
                System.out.println(sql);
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sql);

                if (empRolesComboBox.getValue().equalsIgnoreCase("Manager") && !(managerRateTxtField.getText().isEmpty()) && !(managerPeriodTxtField.getText().isEmpty())) {
                    String query = "INSERT INTO Manager (Emp_ID,Period,Rate)"
                            + "Values(" + employeeID + "," + Float.parseFloat(managerPeriodTxtField.getText()) + "," + Float.parseFloat(managerRateTxtField.getText()) + ")";

                    Statement stmt2 = conn.createStatement();
                    stmt2.executeUpdate(query);
                    conn.close();

                    Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                    a.setContentText("Add Employee Success");
                    a.show();

                } else if (empRolesComboBox.getValue().equalsIgnoreCase("Salesman") && !(SMworkingTxtField.getText().isEmpty()) && !(SMrateTxtField.getText().isEmpty())) {
                    String query = "INSERT INTO Salesman (Emp_ID,WorkingHours,Rate)"
                            + "Values(" + employeeID + "," + Float.parseFloat(SMworkingTxtField.getText()) + "," + Float.parseFloat(SMrateTxtField.getText()) + ")";
                    Statement stmt2 = conn.createStatement();
                    stmt2.executeUpdate(query);
                    conn.close();

                    Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                    a.setContentText("Add Employee Success");
                    a.show();
                } else {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("No Employee Found");
                    a.show();
                }

            } else {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setContentText("Please Insert Correct Information");
                a.show();
            }

        } else {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("Please Insert All The Information");
            a.show();
        }

    }

    int generateRandomEmpID() {
        //random employee id generate
        int rand = ThreadLocalRandom.current().nextInt(100, 999);
        String randomNum = Integer.toString(rand);

        SimpleDateFormat formatter = new SimpleDateFormat("yyMM");
        Date currDate = Calendar.getInstance().getTime();
        String date = formatter.format(currDate);

        String num = date + randomNum;
        int employeeID = Integer.parseInt(num);
        System.out.println(employeeID);
        return employeeID;
    }

}
