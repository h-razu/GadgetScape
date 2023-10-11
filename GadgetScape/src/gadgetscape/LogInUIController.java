package gadgetscape;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class LogInUIController implements Initializable {

    @FXML
    private JFXTextField Email;
    @FXML
    private JFXPasswordField userPassword;

    Connection con = null;
    @FXML
    private JFXTabPane LogInRole;
    @FXML
    private JFXButton EmployeeLogin;
    @FXML
    private JFXPasswordField AdminPassword;
    @FXML
    private JFXTextField AdminEmailText;
    @FXML
    private ImageView UserEmailNullError;
    @FXML
    private ImageView UserPassNullError;
    @FXML
    private ImageView AdminEmailNullError;
    @FXML
    private ImageView AdminPassNullError;

    public static String LoggedInName;
    @FXML
    private ImageView EmailPIc;
    @FXML
    private ImageView PasswordPic;
    @FXML
    private ImageView AdminEmailPic;
    @FXML
    private ImageView AdminPasswordPic;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void EmployeeLoginBtnAction(ActionEvent event) {
        String Role;
        if (!Email.getText().equals("") && !userPassword.getText().equals("")) {
            System.out.println(Email.getText());
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";
                con = DriverManager.getConnection(connectionUrl);

                String sql = "select * from Employee where Email='" + Email.getText() + "' AND Passwords='" + userPassword.getText() + "'";

                Statement stm = con.createStatement();

                ResultSet rs = stm.executeQuery(sql);

                if (rs.next()) {
                    Role = rs.getString("Roles");
                    if (Role.equalsIgnoreCase("Salesman")) {
                        LoggedInName = rs.getString("EmpName");
                        Parent SalesmanDashboard = FXMLLoader.load(getClass().getResource("/employee/Dashboard.fxml"));
                        Scene scr = new Scene(SalesmanDashboard);
                        Stage mp = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        mp.setScene(scr);
                        mp.setTitle("Salesman Dashboard");
                        mp.show();

                    } else {
                        LoggedInName = rs.getString("EmpName");
                        Parent ManagerDashboard = FXMLLoader.load(getClass().getResource("/Manager/ManagerDashboard.fxml"));
                        Scene scr = new Scene(ManagerDashboard);
                        Stage mp = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        mp.setScene(scr);
                        mp.setTitle("Manager Dashboard");
                        mp.show();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please Insert Correct Data");
                }
                userPassword.setText("");
                Email.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex);
                ex.printStackTrace();
            }
        } else {
            try {
                Image image = new Image(getClass().getResourceAsStream("/images/error-icon-14-256.png"));
                System.out.println("Saber");
                if (Email.getText().equals("")) {
                    UserEmailNullError.setImage(image);
                }
                if (!Email.getText().equals("")) {
                    UserEmailNullError.setImage(null);
                }
                if (userPassword.getText().equals("")) {
                    UserPassNullError.setImage(image);
                }
                if (!userPassword.getText().equals("")) {
                    UserPassNullError.setImage(null);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "No Image Found!");
            }

        }
    }

    @FXML
    private void AdminLoginBtn(ActionEvent event) {
        if (!AdminEmailText.getText().equals("") && !AdminPassword.getText().equals("")) {
            try {

                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";
                con = DriverManager.getConnection(connectionUrl);

                String sql = "select * from Admins where Email='" + AdminEmailText.getText() + "' AND Passwords='" + AdminPassword.getText() + "'";

                Statement stm = con.createStatement();

                ResultSet rs = stm.executeQuery(sql);
                if (rs.next()) {
                    LoggedInName = rs.getString("AdminName");

                    Parent AdminDashboard = FXMLLoader.load(getClass().getResource("/Admin/AdminDashboard.fxml"));
                    Scene scr = new Scene(AdminDashboard);
                    Stage mp = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    mp.setScene(scr);
                    mp.setTitle("Admin Dashboard");
                    mp.show();
                } else {
                    JOptionPane.showMessageDialog(null, "Please Insert Correct Data");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex);
                ex.printStackTrace();
            }

        } else {
            try {
                Image image = new Image(getClass().getResourceAsStream("/images/error-icon-14-256.png"));
                if (AdminEmailText.getText().equals("")) {
                    AdminEmailNullError.setImage(image);
                }
                if (!AdminEmailText.getText().equals("")) {
                    AdminEmailNullError.setImage(null);
                }
                if (AdminPassword.getText().equals("")) {
                    AdminPassNullError.setImage(image);
                }
                if (!AdminPassword.getText().equals("")) {
                    AdminPassNullError.setImage(null);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "No Image Found!");
            }

        }
    }

}
