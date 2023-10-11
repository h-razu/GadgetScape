
package Manager;

import static Manager.UpdateProductInfoController.productID;
import static Manager.UpdateProductInfoController.productQuantity;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.layout.AnchorPane;

public class UpdateProductPopUpController implements Initializable {

    @FXML
    private JFXTextField updatePriceTxtField;
    @FXML
    private JFXButton doneBtn;
    @FXML
    private JFXTextField updateQuantityTxtField;
    @FXML
    private AnchorPane UpdatePopUp;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void doneBtnAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        int quantity;
        Connection con;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";
        con = DriverManager.getConnection(connectionUrl);
        Statement stmt = con.createStatement();
        try{
            if (!(updatePriceTxtField.getText().isEmpty()) && !(updateQuantityTxtField.getText().isEmpty())) {
           quantity = Integer.parseInt(updateQuantityTxtField.getText())+ productQuantity ;
            
            String sql = "update Products set Price=" + updatePriceTxtField.getText() + " , Quantity= " + quantity + ", Product_Status= 'In Stock' where Product_ID=" + productID + "";
            stmt.executeUpdate(sql);

            UpdatePopUp.getScene().getWindow().hide();
        }
        else if(updatePriceTxtField.getText().isEmpty() && !(updateQuantityTxtField.getText().isEmpty())){
        quantity = Integer.parseInt(updateQuantityTxtField.getText())+ productQuantity ;    
        String sql = "update Products set  Quantity= " + quantity + ", Product_Status= 'In Stock' where Product_ID=" + productID + "";
        stmt.executeUpdate(sql);

        UpdatePopUp.getScene().getWindow().hide();
        }
        else if(updateQuantityTxtField.getText().isEmpty() && !(updatePriceTxtField.getText().isEmpty())){
            
        float price = Float.parseFloat(updatePriceTxtField.getText());    
        String sql = "update Products set  Price= " + price + " where Product_ID=" + productID + "";
        stmt.executeUpdate(sql);
     
        stmt.executeUpdate(sql);

        UpdatePopUp.getScene().getWindow().hide();
        }
        else
        {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("No data Inserted");
            a.show();
        }
        }
        catch(Exception e){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Please Insert Correct Data! Only Digits Are Allowed.");
            a.show();
        }
    }
}
