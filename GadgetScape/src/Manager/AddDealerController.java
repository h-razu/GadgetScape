/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manager;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author 88016
 */
public class AddDealerController implements Initializable {

    @FXML
    private JFXTextField CompanyNameTxtField;
    @FXML
    private JFXTextField PhoneTxtField;
    @FXML
    private JFXTextArea AddressTxtField;
    @FXML
    private JFXButton DoneBtn;
    @FXML
    private JFXTextField EmailTextField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void DoneBtnAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        String cn = CompanyNameTxtField.getText();
        String phone = PhoneTxtField.getText();
        String eml = EmailTextField.getText();
        String adrs = AddressTxtField.getText();
        boolean CompanyNameFormat = false, EmailFormat = false, PhoneFormat = false, AdrsFormat = false;
        CompanyNameFormat = Pattern.matches("([^.\\s]([a-zA-Z\\.]){1,}\\s[a-zA-Z]{1,}\\s?([a-zA-Z]{1,})\\s?([a-zA-Z]{1,})?)", cn);
        EmailFormat = Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", eml);
        PhoneFormat = Pattern.matches("(^([+]{1}[8]{2}|0088)?(01){1}[3-9]{1}\\d{8})$", phone);
        AdrsFormat = Pattern.matches("[\\w\\.\\s\\/\\-\\,]{5,50}", adrs);

        if (!(CompanyNameTxtField.getText().isEmpty()) && !(PhoneTxtField.getText().isEmpty())
                && !(AddressTxtField.getText().isEmpty()) && !(EmailTextField.getText().isEmpty())) {
            if (CompanyNameFormat && EmailFormat && AdrsFormat && PhoneFormat) {
                Connection conn;
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";
                conn = DriverManager.getConnection(connectionUrl);

                //insert data in table
                String sql = "INSERT INTO Dealers (Company_Name,Phone,Email,Addresses)"
                        + "Values('" + CompanyNameTxtField.getText() + "','" + PhoneTxtField.getText() + "','" + EmailTextField.getText() + "','"
                        + AddressTxtField.getText() + "')";
                
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sql);

                try {
                    Parent ProductDashboar = FXMLLoader.load(getClass().getResource("/Manager/ManagerDashboard.fxml"));
                    Scene scr = new Scene(ProductDashboar);
                    Stage mp = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    mp.setScene(scr);
                    mp.setTitle("Manager Dashboard");
                    mp.show();

                } catch (Exception ex) {
                    System.out.println("can't load");
                }
            } else if (!CompanyNameFormat) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Name format is not correct!");
                a.show();
            } else if (!PhoneFormat) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Phone number format is not correct!");
                a.show();
            } else if (!EmailFormat) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Email format is not correct!");
                a.show();
            } else if (!AdrsFormat) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Address format is not correct!");
                a.show();
            }

        } else {

            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("Please Insert All The Information");
            a.show();
        }

    }

}
