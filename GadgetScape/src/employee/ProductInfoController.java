package employee;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import static employee.DashboardController.productCategory;
import static gadgetscape.GadgetScape.cartItemsContainer;
import static gadgetscape.LogInUIController.LoggedInName;
import java.io.IOException;
import static java.lang.String.valueOf;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Cart_Products;

public class ProductInfoController implements Initializable {

    @FXML
    private Label productName;
    @FXML
    private Label productPrice;
    @FXML
    private Label productDetails;
    @FXML
    private Label productQuantity;
    @FXML
    private ImageView productImage;
    @FXML
    private Label empNameLabel;
    @FXML
    private Pane logOutBtnContainer;
    @FXML
    private Label productNameLabel;

    private boolean isVisible;
    int productID;

    ObservableList<Cart_Products> cartItemList = FXCollections.observableArrayList();

    @FXML
    private TableView<Cart_Products> cartTable;
    @FXML
    private TableColumn<Cart_Products, String> chooseProductName;
    @FXML
    private TableColumn<Cart_Products, String> chooseProductQuantity;
    @FXML
    private TableColumn<Cart_Products, String> chooseProductPrice;
    @FXML
    private TableColumn<Cart_Products, String> chooseProductDelete;
    @FXML
    private Label quantityField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // for log out button
        logOutBtnContainer.setVisible(false);
        isVisible = false;

        productNameLabel.setText(productCategory);
        displayCartTable();

        empNameLabel.setText(LoggedInName);
    }

    @FXML
    private void addToCartAction(ActionEvent event) {

        int quantity = Integer.parseInt(productQuantity.getText().trim());

        if (quantity > 0) {
            Cart_Products cart_Item = new Cart_Products();
            cart_Item.setName(productName.getText().trim());
            cart_Item.setPrice(Float.parseFloat(productPrice.getText().trim()));
            cart_Item.setQuantity(1);
            cart_Item.setProductID(productID);

            cartItemsContainer.add(cart_Item);
            System.out.println(cartItemsContainer.size());
            displayCartTable();
        }
        else{
            Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
            errorAlert.setHeaderText("Out Of Stock");
            errorAlert.setContentText("This Product is not available.....");
            errorAlert.showAndWait();
        }
    }

    @FXML
    private void backBtnAction(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductsList.fxml"));
        Parent root = loader.load();

        Stage productsList = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scn = new Scene(root);
        productsList.setScene(scn);
        productsList.setTitle("Products");
        productsList.show();
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
    private void logOutAction(ActionEvent event) throws IOException {
        Parent home = FXMLLoader.load(getClass().getResource("/gadgetscape/LogInUI.fxml"));
        Stage root = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scn = new Scene(home);
        root.setScene(scn);
        root.setTitle("HOME");
        root.show();
    }

    void displayInfoFromDB(int value) throws ClassNotFoundException, SQLException {

        productID = value;
        //connect to database
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String URL = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";
        Connection connection = DriverManager.getConnection(URL);

        String query = "SELECT Product_Name, Price, Quantity, Details, ImageURL  FROM Products WHERE Product_ID=" + value;
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(query);

        if (rs.next()) {
            productName.setText(rs.getString("Product_Name"));
            productPrice.setText(valueOf(rs.getFloat("Price")));
            productQuantity.setText(valueOf(rs.getInt("Quantity")));

            Image image = new Image(getClass().getResourceAsStream(rs.getString("ImageURL")));
            productImage.setImage(image);

            String details = rs.getString(("Details"));
            for (String Line : details.split("/n", 0)) {

                productDetails.setText(productDetails.getText() + System.lineSeparator() + Line);
            }
        }
    }

    @FXML
    private void decBtnAction(MouseEvent event) throws ClassNotFoundException, SQLException {
        changeQuantity("decrement");
    }

    @FXML
    private void incBtnAction(MouseEvent event) throws ClassNotFoundException, SQLException {
        changeQuantity("increment");
    }

    void displayCartTable() {
        cartTable.getItems().clear();
        System.out.println(cartItemsContainer.size());

        if (cartItemsContainer.size() > 0) {
            for (Cart_Products item : cartItemsContainer) {
                cartItemList.add(new Cart_Products(item.getProductID(), item.getName(), item.getQuantity(), item.getPrice()));
            }
        }

        chooseProductQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        chooseProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        chooseProductName.setCellValueFactory(new PropertyValueFactory<>("name"));

        chooseProductName.setCellFactory(param -> {
            return new TableCell<Cart_Products, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        Text text = new Text(item);
                        text.setStyle("-fx-text-alignment:justify;");
                        text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(35));
                        setGraphic(text);
                    }
                }
            };
        });

        //delete icon
        Callback<TableColumn<Cart_Products, String>, TableCell<Cart_Products, String>> cellWithDeleteBtn = (TableColumn<Cart_Products, String> param) -> {

            final TableCell<Cart_Products, String> cell = new TableCell<Cart_Products, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        FontAwesomeIconView deleteFromCart = new FontAwesomeIconView(FontAwesomeIcon.TRASH);

                        deleteFromCart.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:35px;"
                                + "-fx-fill:#ff0000;"
                        );

                        deleteFromCart.setOnMouseClicked((MouseEvent event) -> {
                            quantityField.setText("1");

                            int index = cartTable.getSelectionModel().getSelectedIndex();
                            Cart_Products product = cartItemsContainer.get(index);
                            cartItemsContainer.remove(index);
                            displayCartTable();

                        });

                        HBox deleteBtn = new HBox(deleteFromCart);
                        deleteBtn.setStyle("-fx-alignment:center");
                        deleteBtn.setSpacing(15);

                        setGraphic(deleteBtn);

                        setText(null);
                    }
                }

            };

            return cell;
        };

        chooseProductDelete.setCellFactory(cellWithDeleteBtn);

        cartTable.setItems(cartItemList);
    }

    @FXML
    private void proceedBtnAction(ActionEvent event) throws IOException {
        if (cartItemsContainer.isEmpty()) {
            Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
            errorAlert.setHeaderText("Empty Cart");
            errorAlert.setContentText("No item in the cart");
            errorAlert.showAndWait();
        } else {
            Parent home = FXMLLoader.load(getClass().getResource("CheckOut.fxml"));
            Stage root = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scn = new Scene(home);
            root.setScene(scn);
            root.setTitle("Check Out");
            root.show();
        }
    }

    @FXML
    private void cartClearAction(ActionEvent event) {
        cartItemsContainer.clear();
        cartItemsContainer = new ArrayList<>();
        displayCartTable();
    }

    void changeQuantity(String value) throws ClassNotFoundException, SQLException {

        if (cartTable.getSelectionModel().getSelectedItem() != null) {

            int index = cartTable.getSelectionModel().getSelectedIndex();
            Cart_Products product = cartItemsContainer.get(index);

            int availableQuantity = 0;
            float unitPrice = 0;

            //get quantity and price from DB
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String URL = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";
            Connection connection = DriverManager.getConnection(URL);

            String query = "SELECT Quantity, Price FROM Products WHERE Product_ID=" + product.getProductID();
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(query);

            if (rs.next()) {
                availableQuantity = rs.getInt("Quantity");
                unitPrice = rs.getFloat("Price");
            }

            int cartQuantity = product.getQuantity();
            quantityField.setText(Integer.toString(cartQuantity));

            if (value.equalsIgnoreCase("Increment")) {
                if (cartQuantity < availableQuantity) {
                    cartQuantity = cartQuantity + 1;
                    quantityField.setText(Integer.toString(cartQuantity));
                } else {
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setContentText("Out Of Stock!!!");
                    a.show();
                }

            } else {
                if (cartQuantity > 1) {
                    cartQuantity = cartQuantity - 1;
                    quantityField.setText(Integer.toString(cartQuantity));
                }
            }
            product.setQuantity(cartQuantity);
            product.setPrice(unitPrice * cartQuantity);
            displayCartTable();

        } else {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("Please Choose An Item");
            a.show();
        }
    }
}
