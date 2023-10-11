package gadgetscape;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Cart_Products;

public class GadgetScape extends Application {

    public static List<Cart_Products> cartItemsContainer;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LogInUI.fxml"));
        Scene scn = new Scene(root);
        primaryStage.setScene(scn);
        primaryStage.setTitle("Dashboard");
        primaryStage.show();
    }

    public static void main(String[] args) {
        cartItemsContainer = new ArrayList<>();
        launch(args);
    }

}
