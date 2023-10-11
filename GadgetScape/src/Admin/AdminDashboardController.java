package Admin;

import model.EmployeeInfo;
import com.jfoenix.controls.JFXButton;
import static gadgetscape.LogInUIController.LoggedInName;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Rbkar
 */
public class AdminDashboardController implements Initializable {

    @FXML
    private JFXButton EmpInfoBtn;
    @FXML
    private JFXButton AddEmpBtn;
    @FXML
    private JFXButton UpdateEmpBtn;
    @FXML
    private JFXButton DeleteEmpBtn;
    @FXML
    private JFXButton logOutBtn;
    private TableView<EmployeeInfo> empInfoTable;
    @FXML
    private BorderPane adminDashRoot;
    @FXML
    private Label infoText;
    @FXML
    private JFXButton RefreshEmpBtn;
    @FXML
    private Label adminNameLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            Pane view = new FXMLLoader().load(getClass().getResource("/Admin/EmployeeInfo.fxml"));
            adminDashRoot.setCenter(view);
            infoText.setText("Employee Info");
        } catch (Exception ex) {
            System.out.println("can't load");
        }

        adminNameLabel.setText(LoggedInName);
    }

    @FXML
    private void EmpInfoBtnAction(MouseEvent event) {
        try {
            Pane view = new FXMLLoader().load(getClass().getResource("/Admin/EmployeeInfo.fxml"));
            adminDashRoot.setCenter(view);
            infoText.setText("Employee Info");
        } catch (Exception ex) {
            System.out.println("can't load");
        }
    }

    @FXML
    private void AddEmpBtnAction(MouseEvent event) {
        try {
            Pane view = new FXMLLoader().load(getClass().getResource("/Admin/AddEmployee.fxml"));
            adminDashRoot.setCenter(view);
            infoText.setText("Add Employee");
        } catch (Exception ex) {
            System.out.println("can't load");
        }
    }

    @FXML
    private void UpdateEmpBtnAction(MouseEvent event) {
        try {
            Pane view = new FXMLLoader().load(getClass().getResource("/Admin/updateEmployeeInfo.fxml"));
            adminDashRoot.setCenter(view);
            infoText.setText("Update Employee Info");
        } catch (Exception ex) {
            System.out.println("can't load");
        }
    }

    @FXML
    private void DeleteEmpBtnAction(MouseEvent event) throws ClassNotFoundException {

        try {
            Pane view = new FXMLLoader().load(getClass().getResource("/Admin/DeleteEmployee.fxml"));
            adminDashRoot.setCenter(view);
            infoText.setText("Delete Employee");
        } catch (Exception ex) {
            System.out.println("can't load");
        }
    }

    @FXML
    private void logOutBtnAction(MouseEvent event) throws IOException {
        Parent home = FXMLLoader.load(getClass().getResource("/gadgetscape/LogInUI.fxml"));
        Stage root = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scn = new Scene(home);
        root.setScene(scn);
        root.setTitle("HOME");
        root.show();
    }

    @FXML
    private void RefreshEmpBtnAction(MouseEvent event) throws SQLException, ClassNotFoundException, IOException {
        try {
            Pane view = new FXMLLoader().load(getClass().getResource("/Admin/EmployeeInfo.fxml"));
            adminDashRoot.setCenter(view);
            infoText.setText("Employee Info");
        } catch (Exception ex) {
            System.out.println("can't load");
        }
    }
}
