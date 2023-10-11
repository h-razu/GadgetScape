package Admin;

import static Admin.UpdateEmployeeInfoController.empID;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Rbkar
 */
public class UpdateEmployeeInfoPopUpController implements Initializable {

    @FXML
    private TextField NewPhoneEmp;
    @FXML
    private JFXButton DoneBtn;
    @FXML
    private JFXTextArea NewAddressEmp;
    @FXML
    private AnchorPane updateEmpPopUpInfo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void DoneBtnAction(MouseEvent event) throws ClassNotFoundException, SQLException {

        Connection con;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";
        con = DriverManager.getConnection(connectionUrl);
        Statement stmt = con.createStatement();

        if (!(NewPhoneEmp.getText().isEmpty()) && !(NewAddressEmp.getText().isEmpty())) {
            String sql = "update Employee set Phone='" + NewPhoneEmp.getText() + "' , Addresses= '" + NewAddressEmp.getText() + "' where Emp_ID='" + empID + "'";
            stmt.executeUpdate(sql);

            updateEmpPopUpInfo.getScene().getWindow().hide();
        } else if (!(NewPhoneEmp.getText().isEmpty()) && NewAddressEmp.getText().isEmpty()) {
            String sql = "update Employee set Phone='" + NewPhoneEmp.getText() + "' where Emp_ID='" + empID + "'";
            stmt.executeUpdate(sql);

            updateEmpPopUpInfo.getScene().getWindow().hide();
        } else if (!(NewAddressEmp.getText().isEmpty()) && NewPhoneEmp.getText().isEmpty()) {
            String sql = "update Employee set Addresses='" + NewAddressEmp.getText() + "' where Emp_ID='" + empID + "'";
            stmt.executeUpdate(sql);

            updateEmpPopUpInfo.getScene().getWindow().hide();
        } else {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("No data inserted");
            a.show();
        }
    }
}
