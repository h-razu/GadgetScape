package employee;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import static employee.DashboardController.productCategory;
import static gadgetscape.GadgetScape.cartItemsContainer;
import static gadgetscape.LogInUIController.LoggedInName;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.All_Products;
import model.Cart_Products;

public class ProductsListController implements Initializable {

    @FXML
    private Label productNameLabel;
    @FXML
    private Label empNameLabel;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<All_Products> tableList;
    @FXML
    private TableColumn<All_Products, String> productID;
    @FXML
    private TableColumn<All_Products, String> productName;
    @FXML
    private TableColumn<All_Products, String> unitPrice;
    @FXML
    private TableColumn<All_Products, String> status;
    @FXML
    private TableColumn<All_Products, String> action;
    @FXML
    private Pane logOutBtnContainer;

    private boolean isVisible;
    ObservableList<All_Products> productList = FXCollections.observableArrayList();
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
    private TableColumn<Cart_Products, String> deleteCartItem;
    @FXML
    private Label quantityField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logOutBtnContainer.setVisible(false);
        isVisible = false;

        productNameLabel.setText(productCategory);

        try {
            loadData();
            loadCartTable();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //set name
        empNameLabel.setText(LoggedInName);
    }

    @FXML
    private void backBtnAction(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Stage gotoDashboard = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scn = new Scene(root);
        gotoDashboard.setScene(scn);
        gotoDashboard.setTitle("All Products");
        gotoDashboard.show();
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
    private void searchAction(MouseEvent event) throws ClassNotFoundException, SQLException {

        productList.clear();
        tableList.getItems().removeAll();
        tableList.setItems(productList);
        String search_Value = searchField.getText();

        if (!(search_Value.isEmpty())) {
            Connection connection;

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";
            connection = DriverManager.getConnection(connectionUrl);

            String query = "SELECT Product_ID, Product_Name, Price, Product_Status FROM Products WHERE Product_Name LIKE '%" + search_Value + "%' AND Category='" + productCategory + "'";

            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while (rs.next()) {
                productList.add(new All_Products(rs.getInt("Product_ID"), rs.getString("Product_Name"), rs.getString("Product_Status"), rs.getFloat("Price")));
            }

            productID.setCellValueFactory(new PropertyValueFactory<>("productID"));
            productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
            unitPrice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
            status.setCellValueFactory(new PropertyValueFactory<>("productStatus"));

            productName.setCellFactory(param -> {
                return new TableCell<All_Products, String>() {
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

            //add cell of button action
            Callback<TableColumn<All_Products, String>, TableCell<All_Products, String>> cellWithBtn = (TableColumn<All_Products, String> param) -> {

                final TableCell<All_Products, String> cell = new TableCell<All_Products, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                            setText(null);

                        } else {

                            FontAwesomeIconView infoIcon = new FontAwesomeIconView(FontAwesomeIcon.INFO);
                            FontAwesomeIconView cartIcon = new FontAwesomeIconView(FontAwesomeIcon.CART_PLUS);

                            infoIcon.setStyle(
                                    " -fx-cursor: hand ;"
                                    + "-glyph-size:35px;"
                                    + "-fx-fill:#646464;"
                            );
                            cartIcon.setStyle(
                                    " -fx-cursor: hand ;"
                                    + "-glyph-size:35px;"
                                    + "-fx-fill:#00E676;"
                            );

                            infoIcon.setOnMouseClicked((MouseEvent event) -> {
                                try {
                                    All_Products data = tableList.getSelectionModel().getSelectedItem();

                                    int id = data.getProductID();

                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductInfo.fxml"));
                                    Parent root = loader.load();
                                    ProductInfoController info = loader.getController();
                                    try {
                                        info.displayInfoFromDB(id);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    Stage productInfo = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                    Scene scn = new Scene(root);
                                    productInfo.setScene(scn);
                                    productInfo.setTitle("Products");
                                    productInfo.show();

                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }

                            });

                            cartIcon.setOnMouseClicked((MouseEvent event) -> {
                                quantityField.setText("1");

                                All_Products data = tableList.getSelectionModel().getSelectedItem();

                                if (data.getProductStatus().equalsIgnoreCase("Out Of Stock")) {

                                    Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                                    errorAlert.setHeaderText("Out Of Stock");
                                    errorAlert.setContentText("This Product is not available.....");
                                    errorAlert.showAndWait();

                                } else {

                                    Cart_Products cart_Item = new Cart_Products();
                                    cart_Item.setName(data.getProductName());
                                    cart_Item.setPrice(data.getProductPrice());
                                    cart_Item.setQuantity(1);
                                    cart_Item.setProductID(data.getProductID());

                                    cartItemsContainer.add(cart_Item);
                                    loadCartTable();
                                }
                            });

                            HBox managebtn = new HBox(infoIcon, cartIcon);
                            managebtn.setStyle("-fx-alignment:center");
                            managebtn.setSpacing(15);

                            setGraphic(managebtn);

                            setText(null);
                        }
                    }
                };

                return cell;
            };
            action.setCellFactory(cellWithBtn);

            tableList.setItems(productList);
            connection.close();
        } else {
            loadData();
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

    void loadData() throws ClassNotFoundException, SQLException {

        //connect to database
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String URL = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";
        Connection connection = DriverManager.getConnection(URL);

        String query = "SELECT Product_ID, Product_Name, Price, Product_Status FROM Products WHERE Category='" + productCategory + "'";

        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(query);

        while (rs.next()) {
            productList.add(new All_Products(rs.getInt("Product_ID"), rs.getString("Product_Name"), rs.getString("Product_Status"), rs.getFloat("Price")));
        }

        productID.setCellValueFactory(new PropertyValueFactory<>("productID"));
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        unitPrice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        status.setCellValueFactory(new PropertyValueFactory<>("productStatus"));

        productName.setCellFactory(param -> {
            return new TableCell<All_Products, String>() {
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

        //add cell of button action
        Callback<TableColumn<All_Products, String>, TableCell<All_Products, String>> cellWithBtn = (TableColumn<All_Products, String> param) -> {

            final TableCell<All_Products, String> cell = new TableCell<All_Products, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        FontAwesomeIconView infoIcon = new FontAwesomeIconView(FontAwesomeIcon.INFO);
                        FontAwesomeIconView cartIcon = new FontAwesomeIconView(FontAwesomeIcon.CART_PLUS);

                        infoIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:35px;"
                                + "-fx-fill:#646464;"
                        );
                        cartIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:35px;"
                                + "-fx-fill:#00E676;"
                        );

                        infoIcon.setOnMouseClicked((MouseEvent event) -> {
                            try {
                                All_Products data = tableList.getSelectionModel().getSelectedItem();

                                int id = data.getProductID();

                                FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductInfo.fxml"));
                                Parent root = loader.load();
                                ProductInfoController info = loader.getController();
                                try {
                                    info.displayInfoFromDB(id);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                Stage productInfo = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                Scene scn = new Scene(root);
                                productInfo.setScene(scn);
                                productInfo.setTitle("Products");
                                productInfo.show();

                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                        });

                        cartIcon.setOnMouseClicked((MouseEvent event) -> {
                            quantityField.setText("1");

                            All_Products data = tableList.getSelectionModel().getSelectedItem();

                            if (data.getProductStatus().equalsIgnoreCase("Out Of Stock")) {

                                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                                errorAlert.setHeaderText("Out Of Stock");
                                errorAlert.setContentText("This Product is not available.....");
                                errorAlert.showAndWait();

                            } else {

                                Cart_Products cart_Item = new Cart_Products();
                                cart_Item.setName(data.getProductName());
                                cart_Item.setPrice(data.getProductPrice());
                                cart_Item.setQuantity(1);
                                cart_Item.setProductID(data.getProductID());

                                cartItemsContainer.add(cart_Item);
                                loadCartTable();
                            }
                        });

                        HBox managebtn = new HBox(infoIcon, cartIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        managebtn.setSpacing(15);

                        setGraphic(managebtn);

                        setText(null);
                    }
                }

            };

            return cell;
        };
        action.setCellFactory(cellWithBtn);

        tableList.setItems(productList);
        connection.close();

    }

    @FXML
    private void decBtnAction(MouseEvent event) throws ClassNotFoundException, SQLException {
        changeQuantity("decrement");
    }

    @FXML
    private void incBtnAction(MouseEvent event) throws ClassNotFoundException, SQLException {
        changeQuantity("increment");
    }

    void loadCartTable() {

        cartTable.getItems().clear();

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
                            loadCartTable();
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

        deleteCartItem.setCellFactory(cellWithDeleteBtn);
        cartTable.setItems(cartItemList);
    }

    @FXML
    private void proceedBtnAction(ActionEvent event) throws IOException {

        if (cartItemsContainer.isEmpty()) {
            Alert errorAlert = new Alert(AlertType.INFORMATION);
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
        loadCartTable();
    }

    void changeQuantity(String value) throws ClassNotFoundException, SQLException {

        if (cartTable.getSelectionModel().getSelectedItem() != null) {

            int index = cartTable.getSelectionModel().getSelectedIndex();
            Cart_Products product = cartItemsContainer.get(index);

            int availableQuantity=0;
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
            loadCartTable();

        } else {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("Please Choose An Item");
            a.show();
        }
    }
}
