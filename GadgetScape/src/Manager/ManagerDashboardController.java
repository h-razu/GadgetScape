package Manager;

import com.jfoenix.controls.JFXButton;
import static gadgetscape.LogInUIController.LoggedInName;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Rbkar
 */
public class ManagerDashboardController implements Initializable {

    @FXML
    private BorderPane adminDashRoot;
    @FXML
    private JFXButton productInfoBtn;
    @FXML
    private JFXButton addProductBtn;
    @FXML
    private JFXButton updateProductBtn;
    @FXML
    private JFXButton deleteProductBtn;
    @FXML
    private JFXButton addDealerBtn;
    @FXML
    private JFXButton deleteDealerBtn;
    @FXML
    private JFXButton logOutBtn;
    @FXML
    private JFXButton dealerInfoBtn;
    @FXML
    private Label ManagerName;
    @FXML
    private JFXButton RefreshBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ManagerName.setText(LoggedInName);
        try {
            Pane view = new FXMLLoader().load(getClass().getResource("/Manager/productInfo.fxml"));
            adminDashRoot.setCenter(view);
        } catch (Exception ex) {
            System.out.println("can't load");
        }
    }

    @FXML
    private void productInfoBtnAction(MouseEvent event) {
        try {
            Pane view = new FXMLLoader().load(getClass().getResource("/Manager/productInfo.fxml"));
            adminDashRoot.setCenter(view);
        } catch (Exception ex) {
            System.out.println("can't load");
        }
    }

    @FXML
    private void addProductBtnAction(MouseEvent event) {
        try {
            Pane view = new FXMLLoader().load(getClass().getResource("/Manager/AddProduct.fxml"));
            adminDashRoot.setCenter(view);
        } catch (Exception ex) {
            System.out.println("can't load");
        }
    }

    @FXML
    private void updateProductBtnAction(MouseEvent event) {
        try {
            Pane view = new FXMLLoader().load(getClass().getResource("/Manager/UpdateProductInfo.fxml"));
            adminDashRoot.setCenter(view);
        } catch (Exception ex) {
            System.out.println("can't load");
        }
    }

    @FXML
    private void deleteProductBtnAction(MouseEvent event) {
        try {
            Pane view = new FXMLLoader().load(getClass().getResource("/Manager/DeleteProduct.fxml"));
            adminDashRoot.setCenter(view);
        } catch (Exception ex) {
            System.out.println("can't load");
        }
    }

    @FXML
    private void addDealerBtnAction(MouseEvent event) {
        try {
            Pane view = new FXMLLoader().load(getClass().getResource("/Manager/AddDealer.fxml"));
            adminDashRoot.setCenter(view);
        } catch (Exception ex) {
            System.out.println("can't load");
        }
    }

    @FXML
    private void deleteDealerBtnAction(MouseEvent event) {
        try {
            Pane view = new FXMLLoader().load(getClass().getResource("/Manager/DeleteDealer.fxml"));
            adminDashRoot.setCenter(view);
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
    private void dealerInfoBtnAction(MouseEvent event) {
        try {
            Pane view = new FXMLLoader().load(getClass().getResource("/Manager/DealerInfo.fxml"));
            adminDashRoot.setCenter(view);
        } catch (Exception ex) {
            System.out.println("can't load");
        }
    }

    @FXML
    private static void RefreshBtnAction(ActionEvent event) {
       
    }

}
