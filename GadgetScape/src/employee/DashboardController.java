package employee;

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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class DashboardController implements Initializable {

    @FXML
    private Label empNameLabel;
    @FXML
    private Pane logOutBtnContainer;
    private boolean isVisible;
    public static String productCategory;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // for log out button
        logOutBtnContainer.setVisible(false);
        isVisible = false;

        //set name label
        empNameLabel.setText(LoggedInName);
    }

    @FXML
    private void profileContainerAction(MouseEvent event) {
        if (!isVisible) {
            logOutBtnContainer.setVisible(true);
            isVisible = true;
        } else {
            logOutBtnContainer.setVisible(false);
            isVisible = false;
        }
    }

    @FXML
    private void laptopAction(MouseEvent event) throws IOException {

        gotoProductList("Laptop", event);
    }

    @FXML
    private void monitorAction(MouseEvent event) throws IOException {
        gotoProductList("Monitor", event);
    }

    @FXML
    private void keyboardAction(MouseEvent event) throws IOException {
        gotoProductList("Keyboard", event);
    }

    @FXML
    private void mouseAction(MouseEvent event) throws IOException {
        gotoProductList("Mouse", event);
    }

    @FXML
    private void gpuAction(MouseEvent event) throws IOException {
        gotoProductList("GPU", event);
    }

    @FXML
    private void processorAction(MouseEvent event) throws IOException {
        gotoProductList("Processor", event);
    }

    @FXML
    private void storageAction(MouseEvent event) throws IOException {
        gotoProductList("RAM & SSD", event);
    }

    @FXML
    private void soundAction(MouseEvent event) throws IOException {
        gotoProductList("Sound System", event);
    }

    @FXML
    private void logOutAction(ActionEvent event) throws IOException {
        Parent home = FXMLLoader.load(getClass().getResource("/gadgetscape/LogInUI.fxml"));
        Stage root = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scn = new Scene(home);
        root.setScene(scn);
        root.setTitle("HOME");
        root.show();
    }

    void gotoProductList(String labelName, MouseEvent event) throws IOException {

        productCategory = labelName;

        Parent home = FXMLLoader.load(getClass().getResource("ProductsList.fxml"));
        Stage productList = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scn = new Scene(home);
        productList.setScene(scn);
        productList.setTitle("All Products");
        productList.show();

        /*  FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductsContainer.fxml"));
        Parent root = loader.load();

        ProductsContainerController productContainerController = loader.getController();
        productContainerController.displayLabel(labelName);*/
    }

}
