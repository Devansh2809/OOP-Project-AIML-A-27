import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class Product {
    private String id;
    private String name;
    private int quantity;
    private double price;
    private String type; // "good" or "cargo"
    private String modeOfShipment; // "land" or "sea"
    private String shipmentDuration; // e.g., "5 days"

    Product(String id, String name, int quantity, double price, String type, String modeOfShipment, String shipmentDuration) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
        this.modeOfShipment = modeOfShipment;
        this.shipmentDuration = shipmentDuration;
    }

    String getId() { return id; }
    String getName() { return name; }
    int getQuantity() { return quantity; }
    double getPrice() { return price; }
    String getType() { return type; }
    String getModeOfShipment() { return modeOfShipment; }
    String getShipmentDuration() { return shipmentDuration; }

    void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return name + " (" + type + "), Rs." + price + " [Qty: " + quantity + "]";
    }
}

class Order {
    private String productId;
    private String area;
    private String city;
    private int quantity;
    private LocalDate orderDate;

    Order(String productId, String area, String city, int quantity, LocalDate orderDate) {
        this.productId = productId;
        this.area = area;
        this.city = city;
        this.quantity = quantity;
        this.orderDate = orderDate;
    }

    String getProductId() { return productId; }
    String getCity() { return city; }
    int getQuantity() { return quantity; }
    LocalDate getOrderDate() { return orderDate; }
}

public class InventoryManagementSystem extends Application {
    private final List<Product> inventory = Collections.synchronizedList(new ArrayList<>());
    private final List<Order> orders = Collections.synchronizedList(new ArrayList<>());
    private final TextArea notificationArea = new TextArea();
    private final Label totalProductsLabel = new Label("Total Products: 0");
    private final List<Order> currentCart = Collections.synchronizedList(new ArrayList<>());

    private static final String MANAGER_USERNAME = "admin";
    private static final String MANAGER_PASSWORD = "password";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Inventory Management System");
        showLoginInterface(primaryStage);
    }

    private void showLoginInterface(Stage stage) {
        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(10));
        Label userLabel = new Label("Username:");
        TextField userField = new TextField();
        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();
        Button loginButton = new Button("Login");
        Button backToMainButton = new Button("Back to Main");

        loginLayout.getChildren().addAll(userLabel, userField, passLabel, passField, loginButton, backToMainButton);
        Scene loginScene = new Scene(loginLayout, 300, 200);

        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();

            if (MANAGER_USERNAME.equals(username) && MANAGER_PASSWORD.equals(password)) {
                showManagerInterface(stage);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Invalid Username or Password");
                alert.show();
            }
        });

        backToMainButton.setOnAction(e -> showMainMenu(stage));
        stage.setScene(loginScene);
        stage.show();
    }

    private void showMainMenu(Stage stage) {
        BorderPane root = new BorderPane();
        VBox menuLayout = new VBox(10);
        menuLayout.setPadding(new Insets(10));

        Button managerButton = new Button("Manager");
        Button customerButton = new Button("Customer");
        Button statisticsButton = new Button("Statistics");

        managerButton.setMaxWidth(Double.MAX_VALUE);
        customerButton.setMaxWidth(Double.MAX_VALUE);
        statisticsButton.setMaxWidth(Double.MAX_VALUE);

        menuLayout.getChildren().addAll(managerButton, customerButton, statisticsButton);
        root.setCenter(menuLayout);

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();

        managerButton.setOnAction(e -> showLoginInterface(stage));
        customerButton.setOnAction(e -> showCustomerInterface(stage));
        statisticsButton.setOnAction(e -> showStatisticsInterface(stage));
    }

    private void showManagerInterface(Stage stage) {
        BorderPane managerRoot = new BorderPane();
        VBox managerPanel = new VBox(10);
        managerPanel.setPadding(new Insets(10));

        TextField productIdField = new TextField();
        TextField productNameField = new TextField();
        TextField productQuantityField = new TextField();
        TextField productPriceField = new TextField();
        TextField productTypeField = new TextField();
        TextField shipmentModeField = new TextField();
        TextField shipmentDurationField = new TextField();
        Button addProductButton = new Button("Add Product");
        Button backButton = new Button("Back to Menu");

        managerPanel.getChildren().addAll(
            new Label("Product ID:"), productIdField,
            new Label("Product Name:"), productNameField,
            new Label("Quantity:"), productQuantityField,
            new Label("Price:"), productPriceField,
            new Label("Type (good/cargo):"), productTypeField,
            new Label("Mode of Shipment (land/sea):"), shipmentModeField,
            new Label("Shipment Duration:"), shipmentDurationField,
            addProductButton, backButton
        );

        managerRoot.setCenter(managerPanel);

        VBox notificationPanel = new VBox(10);
        notificationPanel.setPadding(new Insets(10));
        notificationPanel.getChildren().addAll(new Label("Notifications:"), notificationArea, totalProductsLabel);
        managerRoot.setRight(notificationPanel);

        Scene managerScene = new Scene(managerRoot, 800, 600);
        stage.setScene(managerScene);

        addProductButton.setOnAction(e -> {
            Thread thread = new Thread(() -> {
                synchronized (inventory) {
                    try {
                        String id = productIdField.getText();
                        String name = productNameField.getText();
                        int quantity = Integer.parseInt(productQuantityField.getText());
                        double price = Double.parseDouble(productPriceField.getText());
                        String type = productTypeField.getText();
                        String modeOfShipment = shipmentModeField.getText();
                        String shipmentDuration = shipmentDurationField.getText();

                        Product product = new Product(id, name, quantity, price, type, modeOfShipment, shipmentDuration);
                        inventory.add(product);

                        Platform.runLater(() -> {
                            updateTotalProducts();
                            notificationArea.appendText("Product Added: " + product + "\n");
                        });
                    } catch (NumberFormatException ex) {
                        Platform.runLater(() -> notificationArea.appendText("Invalid input!\n"));
                    }
                }
            });
            thread.start();
        });

        backButton.setOnAction(e -> showMainMenu(stage));
    }

  private void showCustomerInterface(Stage stage) {
    BorderPane customerRoot = new BorderPane();
    VBox customerPanel = new VBox(10);
    customerPanel.setPadding(new Insets(10));

    TextField productIdField = new TextField();
    TextField quantityField = new TextField();
    TextField areaField = new TextField();
    TextField cityField = new TextField();
    Button addToCartButton = new Button("Add to Cart");
    Button checkoutButton = new Button("Checkout");
    TextArea cartDetailsArea = new TextArea();
    cartDetailsArea.setEditable(false);
    Button backButton = new Button("Back to Menu");

    customerPanel.getChildren().addAll(
        new Label("Enter Product ID to Order:"), productIdField,
        new Label("Quantity:"), quantityField,
        new Label("Enter Your Area:"), areaField,
        new Label("Enter Your City:"), cityField,
        addToCartButton, checkoutButton, cartDetailsArea, backButton
    );

    Scene customerScene = new Scene(customerRoot, 600, 400);
    customerRoot.setCenter(customerPanel);
    stage.setScene(customerScene);

    // Add to Cart Button Action
    addToCartButton.setOnAction(e -> {
        Thread thread = new Thread(() -> {
            synchronized (inventory) {
                String productId = productIdField.getText();
                int orderQuantity;

                try {
                    orderQuantity = Integer.parseInt(quantityField.getText());
                } catch (NumberFormatException ex) {
                    Platform.runLater(() -> cartDetailsArea.appendText("Invalid quantity input!\n"));
                    return;
                }

                Product product = findProductById(productId);
                if (product != null && product.getQuantity() >= orderQuantity) {
                    product.setQuantity(product.getQuantity() - orderQuantity);
                    Order order = new Order(productId, areaField.getText(), cityField.getText(), orderQuantity, LocalDate.now());
                    currentCart.add(order);

                    Platform.runLater(() -> cartDetailsArea.appendText("Added to Cart: " + product.getName() + " x" + orderQuantity + "\n"));
                } else {
                    Platform.runLater(() -> cartDetailsArea.appendText("Insufficient stock or product not found!\n"));
                }
            }
        });
        thread.start();
    });

    // Checkout Button Action with Order Confirmation
    checkoutButton.setOnAction(e -> {
        double totalAmount = 0;
        StringBuilder confirmationMessage = new StringBuilder("Order Confirmation:\n");

        for (Order order : currentCart) {
            Product product = findProductById(order.getProductId());
            if (product != null) {
                double itemTotal = product.getPrice() * order.getQuantity();
                totalAmount += itemTotal;

                // Calculate expected delivery date
                int duration = Integer.parseInt(product.getShipmentDuration().replaceAll("[^0-9]", ""));
                LocalDate deliveryDate = order.getOrderDate().plusDays(duration);

                // Append details to confirmation message
                confirmationMessage.append("Product: ").append(product.getName())
                    .append("\nQuantity: ").append(order.getQuantity())
                    .append("\nMode of Shipment: ").append(product.getModeOfShipment())
                    .append("\nExpected Delivery Date: ").append(deliveryDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                    .append("\nItem Total: Rs. ").append(String.format("%.2f", itemTotal))
                    .append("\n\n");
            }
        }

        confirmationMessage.append("Total Amount: Rs. ").append(String.format("%.2f", totalAmount)).append("\n");
        
        // Display order confirmation
        Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION);
        confirmationAlert.setTitle("Order Confirmation");
        confirmationAlert.setHeaderText("Your Order Details:");
        confirmationAlert.setContentText(confirmationMessage.toString());
        confirmationAlert.showAndWait();

        Platform.runLater(() -> notificationArea.appendText("Checkout Completed!\n"));
        currentCart.clear();
    });

    backButton.setOnAction(e -> showMainMenu(stage));
}

    private Product findProductById(String productId) {
        synchronized (inventory) {
            for (Product product : inventory) {
                if (product.getId().equals(productId)) {
                    return product;
                }
            }
        }
        return null;
    }

  private void showStatisticsInterface(Stage stage) {
    BorderPane statisticsRoot = new BorderPane();
    VBox statisticsPanel = new VBox(10);
    statisticsPanel.setPadding(new Insets(10));

    Label statisticsLabel = new Label("Inventory Statistics");
    statisticsPanel.getChildren().add(statisticsLabel);
    statisticsRoot.setCenter(statisticsPanel);

    // Remaining goods and cargo
    Label remainingGoodsLabel = new Label();
    Label remainingCargoLabel = new Label();
    statisticsRoot.setBottom(new VBox(10, remainingGoodsLabel, remainingCargoLabel));
    remainingGoodsLabel.setPadding(new Insets(10));
    remainingCargoLabel.setPadding(new Insets(10));

    Label productStatsLabel = new Label("Product Stock:");
    statisticsPanel.getChildren().add(productStatsLabel);

    // Thread to calculate statistics in the background
    Thread statsThread = new Thread(() -> {
        int[] totalGoods = {0};  
        int[] totalCargo = {0};  

        synchronized (inventory) {
            for (Product product : inventory) {
                if (product.getType().equalsIgnoreCase("good")) {
                    totalGoods[0] += product.getQuantity();
                } else if (product.getType().equalsIgnoreCase("cargo")) {
                    totalCargo[0] += product.getQuantity();
                }

                // Update UI for each product (on the JavaFX thread), including Product ID
                Product currentProduct = product;
                Platform.runLater(() -> 
                    statisticsPanel.getChildren().add(new Label("ID: " + currentProduct.getId() + " - " + currentProduct.toString()))
                );

                // Check for low stock and generate notification
                if (product.getQuantity() < 5) {  // threshold set to 5
                    Platform.runLater(() -> 
                        notificationArea.appendText("Low Stock Alert: " + product.getName() + " is running low!\n")
                    );
                }
            }
        }

        Platform.runLater(() -> {
            remainingGoodsLabel.setText("Remaining Goods: " + totalGoods[0]);
            remainingCargoLabel.setText("Remaining Cargo: " + totalCargo[0]);
        });
    });

    statsThread.start();

    Button backButton = new Button("Back to Menu");
    backButton.setOnAction(e -> showMainMenu(stage));

    statisticsPanel.getChildren().add(backButton);
    Scene statisticsScene = new Scene(statisticsRoot, 600, 400);
    stage.setScene(statisticsScene);
}


    private void updateTotalProducts() {
        totalProductsLabel.setText("Total Products: " + inventory.size());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
