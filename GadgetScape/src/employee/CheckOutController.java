package employee;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import static gadgetscape.GadgetScape.cartItemsContainer;
import static gadgetscape.LogInUIController.LoggedInName;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import model.Cart_Products;
import model.Customer;

public class CheckOutController implements Initializable {

    @FXML
    private Label empNameLabel;
    @FXML
    private Pane logOutBtnContainer;
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
    @FXML
    private Label totalPrice;
    @FXML
    private Label discountPrice;
    @FXML
    private TextField customerSearchText;
    @FXML
    private TableView<Customer> customer;
    @FXML
    private TableColumn<Customer, String> customerName;
    @FXML
    private TableColumn<Customer, String> customerPhone;
    @FXML
    private Label customerNameText;
    @FXML
    private Label points;
    @FXML
    private HBox discountHbox;
    @FXML
    private RadioButton discountRB;
    @FXML
    private Button proceedBtn;

    private boolean isVisible;
    ObservableList<Cart_Products> cartItemList = FXCollections.observableArrayList();
    ObservableList<Customer> customerInfo = FXCollections.observableArrayList();

    float subTotal = 0;
    int availablePoint = 0;
    int choosenCustomerID = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // for log out button
        logOutBtnContainer.setVisible(false);
        isVisible = false;

        //set name label
        empNameLabel.setText(LoggedInName);

        //set current total price
        float Price = 0;
        Cart_Products p;
        for (int i = 0; i < cartItemsContainer.size(); i++) {
            p = cartItemsContainer.get(i);
            Price = Price + p.getPrice();
        }
        totalPrice.setText(Float.toString(Price));

        //initialize sub total
        subTotal = Price;

        //load cart table
        displayCartTable();

        //disable discount RB
        discountRB.setDisable(true);
        //initially disable the discount price container
        discountHbox.setVisible(false);

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
    private void decBtnAction(MouseEvent event) throws ClassNotFoundException, SQLException {
        changeQuantity("decrement");
    }

    @FXML
    private void incBtnAction(MouseEvent event) throws ClassNotFoundException, SQLException {
        changeQuantity("increment");

    }

    @FXML
    private void submitBtnAction(ActionEvent event) throws ClassNotFoundException, SQLException {

        if (!customerNameText.getText().isEmpty() && cartItemsContainer.size() > 0) {

            ArrayList<Integer> existingTransID = new ArrayList<Integer>();
            int transID;

            Connection connection;

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";
            connection = DriverManager.getConnection(connectionUrl);

            //check trans_ID for duplicate value
            String getTransID = "SELECT Trans_ID FROM Purchase";

            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(getTransID);

            while (rs.next()) {
                existingTransID.add(rs.getInt("Trans_ID"));
            }

            while (true) {
                transID = generateRandomTransactionID();

                if (!existingTransID.contains(transID)) {
                    break;
                }
            }

            //insert value into purchase table
            String insertIntoPurchaseTableSQL = "INSERT INTO Purchase (Trans_ID, Cus_ID, totalAmount)"
                    + "Values(" + transID + "," + choosenCustomerID + "," + subTotal + ")";

            Statement stmt = connection.createStatement();
            stmt.executeUpdate(insertIntoPurchaseTableSQL);

            //update points in customer table
            int rewardPoints = (int) (subTotal * 0.05);
            int totalPoints = availablePoint + rewardPoints;

            String updateCustomerPoints = "UPDATE Customer set Points=" + totalPoints + "where Cus_ID='" + choosenCustomerID + "'";
            stmt.executeUpdate(updateCustomerPoints);

            //per transaction
            Cart_Products p;
            for (int i = 0; i < cartItemsContainer.size(); i++) {
                p = cartItemsContainer.get(i);
                int id = p.getProductID();
                int quantity = p.getQuantity();

                String insertIntoPerTrans = "INSERT INTO perTrans (Product_ID, quantity,Trans_ID)"
                        + "Values(" + id + "," + quantity + "," + transID + ")";

                stmt.executeUpdate(insertIntoPerTrans);

                //update quantity
                String getCurrentQuantity = "SELECT Quantity FROM Products WHERE Product_ID=" + id;

                ResultSet rs2 = stmt.executeQuery(getCurrentQuantity);

                int currentQuantity = 0;

                if (rs2.next()) {
                    currentQuantity = rs2.getInt(("Quantity"));
                }
                int newQuantity = currentQuantity - quantity;

                String updateQuantitySQL;

                if (newQuantity == 0) {
                    updateQuantitySQL = "UPDATE Products set Quantity=" + newQuantity + " , Product_Status= 'Out Of Stock' where Product_ID=" + id;
                } else {
                    updateQuantitySQL = "UPDATE Products set Quantity=" + newQuantity + "where Product_ID=" + id;
                }

                stmt.executeUpdate(updateQuantitySQL);
            }

            connection.close();

            //clear fields
            cartItemsContainer.clear();
            cartItemsContainer = new ArrayList<>();
            displayCartTable();

            customerInfo.clear();
            customer.getItems().removeAll();
            quantityField.setText("1");
            totalPrice.setText("");
            discountHbox.setVisible(false);
            customerSearchText.setText("");
            customerNameText.setText("");
            points.setText("");
            discountRB.setDisable(true);

            Alert errorAlert = new Alert(Alert.AlertType.CONFIRMATION);
            errorAlert.setHeaderText("Purchase Information");
            errorAlert.setContentText("Products Purchase Successful....");
            errorAlert.showAndWait();

        } else {

            Alert errorAlert = new Alert(Alert.AlertType.WARNING);
            errorAlert.setHeaderText("No Customer or Empty Cart");
            errorAlert.setContentText("Please Choose A Customer or Add Some Item In The Cart...");
            errorAlert.showAndWait();

        }
    }

    @FXML
    private void customerSearchAction(ActionEvent event) throws ClassNotFoundException, SQLException {

        customerInfo.clear();
        customer.getItems().removeAll();
        customer.setItems(customerInfo);
        String search_Value = customerSearchText.getText();

        if (!(search_Value.isEmpty())) {
            Connection connection;

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=GadgetScape;";
            connection = DriverManager.getConnection(connectionUrl);

            String query = "SELECT * FROM Customer WHERE Cus_Name LIKE '%" + search_Value + "%' OR Phone LIKE '%" + search_Value + "%'";

            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while (rs.next()) {
                customerInfo.add(new Customer(rs.getInt("Cus_ID"), rs.getString("Cus_Name"), rs.getString("Phone"), rs.getInt("Points")));
            }

            customerName.setCellValueFactory(new PropertyValueFactory<>("name"));
            customerPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

            customer.setItems(customerInfo);
            connection.close();
        }
    }

    @FXML
    private void chooseCustomerAction(ActionEvent event) {
        if (customer.getSelectionModel().getSelectedItem() != null) {

            Customer data = customer.getSelectionModel().getSelectedItem();
            String name = data.getName();
            int point = data.getPoints();

            //initialize available point
            availablePoint = point;
            choosenCustomerID = data.getId();

            customerNameText.setText(name);
            points.setText(Integer.toString(point));

            //disable of enable point radio button according to price
            if (subTotal >= 3000 && point > 0) {
                discountRB.setDisable(false);
            } else {
                discountRB.setDisable(true);
            }

        } else {
            JOptionPane.showMessageDialog(null, "Please Select a Customer");
        }
    }

    @FXML
    private void AddCustomerAction(ActionEvent event) throws IOException {
        Pane view = new FXMLLoader().load(getClass().getResource("AddCustomer.fxml"));

        Stage stage = new Stage();
        stage.setScene(new Scene(view));
        stage.show();
    }

    @FXML
    private void redeemPointsAction(ActionEvent event) {

        float price = Float.parseFloat(totalPrice.getText().trim()); //1600
        float maxDiscount = (float) (price * 0.10); //600

        int currentPoint = Integer.parseInt(points.getText().trim());

        if (discountRB.isSelected()) {

            if (currentPoint > 0) {

                float discountAmount = (float) ((float) currentPoint * 0.01);  //per 100 point = 1 taka

                if (discountAmount > maxDiscount) { //1000>600
                    subTotal = price - maxDiscount; //1600-600
                    availablePoint = (int) (currentPoint - (maxDiscount * 100)); //
                } else {
                    subTotal = price - discountAmount;
                    availablePoint = 0;
                }

                discountHbox.setVisible(true);
                discountPrice.setText(Float.toString(subTotal));
            } else {
                subTotal = price;
            }

        } else {

            subTotal = price;
            availablePoint = currentPoint;
            discountHbox.setVisible(false);
        }

    }

    //control after quantity change
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

            float Price = 0;
            Cart_Products p;
            for (int i = 0; i < cartItemsContainer.size(); i++) {
                p = cartItemsContainer.get(i);
                Price = Price + p.getPrice();
            }
            totalPrice.setText(Float.toString(Price));

            displayCartTable();

            //disable of enable point radio button according to price
            if (Price >= 3000 && !customerNameText.getText().isEmpty()) {
                discountRB.setDisable(false);
            } else {
                discountRB.setDisable(true);
            }

            //initially disable the discount price container
            discountHbox.setVisible(false);
            subTotal = Price;

        } else {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("Please Choose An Item");
            a.show();
        }
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

                            float Price = 0;
                            Cart_Products p;
                            for (int i = 0; i < cartItemsContainer.size(); i++) {
                                p = cartItemsContainer.get(i);
                                Price = Price + p.getPrice();
                            }
                            totalPrice.setText(Float.toString(Price));

                            if (cartItemsContainer.isEmpty()) {
                                proceedBtn.setDisable(true);
                                discountRB.setDisable(true);
                                discountHbox.setVisible(false);
                            }

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

    int generateRandomTransactionID() {
        int rand = ThreadLocalRandom.current().nextInt(100, 999);
        String randomNum = Integer.toString(rand);

        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
        Date currDate = Calendar.getInstance().getTime();
        String date = formatter.format(currDate);

        String num = date + randomNum;
        int id = Integer.parseInt(num);

        return id;
    }

}
