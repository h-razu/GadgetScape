package employee;

import com.jfoenix.controls.JFXTextArea;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javax.swing.JOptionPane;

public class AddCustomerController implements Initializable {

    @FXML
    private TextField newCustomerName;
    @FXML
    private JFXTextArea newCustomerPhoneNo;
    @FXML
    private AnchorPane addCutomerPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void doneButtonAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        if (newCustomerName.getText().isEmpty() || newCustomerPhoneNo.getText().isEmpty()) {
            Alert errorAlert = new Alert(Alert.AlertType.WARNING);
            errorAlert.setHeaderText("Empty Text Field");
            errorAlert.setContentText("Please Fill All The Required Field");
            errorAlert.showAndWait();
        } else {

            Connection conn;

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";
            conn = DriverManager.getConnection(connectionUrl);

            String sql = "INSERT INTO Customer (Cus_Name, Phone, Points)"
                    + "Values('" + newCustomerName.getText().trim() + "','" + newCustomerPhoneNo.getText().trim() + "'," + 0 + ")";

            System.out.println(sql);

            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);

            conn.close();

            int value = JOptionPane.showConfirmDialog(null, "Add Completed", "Customer", JOptionPane.YES_NO_OPTION);

            if (value == JOptionPane.YES_OPTION) {

                addCutomerPane.getScene().getWindow().hide();
            }
        }
    }

}
