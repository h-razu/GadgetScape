package Manager;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
 * @author Rbkar
 */
public class AddProductController implements Initializable {

    @FXML
    private JFXComboBox<String> productCategoryCombo;
    @FXML
    private JFXTextField productNameTxtField;
    @FXML
    private JFXTextField productPriceTxtField;
    @FXML
    private JFXTextArea productDetailsTxtField;
    @FXML
    private JFXButton DoneBtn;
    ObservableList<String> categoryList = FXCollections.observableArrayList("Laptop", "Monitor", "Keyboard", "Mouse", "GPU", "Processor", "RAM & SSD", "Sound System");
    @FXML
    private JFXTextField QuantityTextField;
    @FXML
    private JFXTextField ImgTextField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        productCategoryCombo.setItems(categoryList);
    }

    @FXML
    private void productCategoryAction(ActionEvent event) {
    }

    @FXML
    private void DoneBtnAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        String pn = productNameTxtField.getText();
        String price = productPriceTxtField.getText();
        String quantity = QuantityTextField.getText();
        String details = productDetailsTxtField.getText();
        boolean ProductNameFormat = false, PriceFormat = false, QuantityFormat = false, DetailsFormat = false;
        ProductNameFormat = Pattern.matches("[\\w\\.\\s\\/\\-\\,]{1,300}", pn);
        PriceFormat = Pattern.matches("^(?:-(?:[1-9](?:\\d{0,2}(?:,\\d{3})+|\\d*))|(?:0|(?:[1-9](?:\\d{0,2}(?:,\\d{3})+|\\d*))))(?:.\\d+|)$", price);
        QuantityFormat = Pattern.matches("^[0-9]+$", quantity);
        

        if (!(productCategoryCombo.getValue() == null) && !(productNameTxtField.getText().isEmpty()) && !(productPriceTxtField.getText().isEmpty())
                && !(productDetailsTxtField.getText().isEmpty()) && !(QuantityTextField.getText().isEmpty()) && !(ImgTextField.getText().isEmpty())) {
            if (ProductNameFormat && PriceFormat && QuantityFormat) {

                Connection conn;

                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";
                conn = DriverManager.getConnection(connectionUrl);

                //insert data in table
                String sql = "INSERT INTO Products (Product_Name,ImageURL,Price,Quantity,Details,Category,Product_Status)"
                        + "Values('" + productNameTxtField.getText() + "','" + ImgTextField.getText() + "'," + productPriceTxtField.getText() + ","
                        + QuantityTextField.getText() + ",'" + productDetailsTxtField.getText() + "','" + productCategoryCombo.getValue() + "',' In Stock ')";
                System.out.println(sql);
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

            } else if (!ProductNameFormat) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Name format is not correct!");
                a.show();
            } else if (!PriceFormat) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Price format is not correct!");
                a.show();
            } else if (!QuantityFormat) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Quantity format is not correct!");
                a.show();
            } 
        } else {

            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("Please Insert All The Information");
            a.show();
        }

    }
    
}
