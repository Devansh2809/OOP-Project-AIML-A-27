import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Product class
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
        return name + " (" + type + "), ₹" + price + " [Qty: " + quantity + "]";
    }
}

// Order class
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

// Main Application class
public class InventoryManagementSystem extends Application {
    private ConcurrentHashMap<String, Product> inventory = new ConcurrentHashMap<>();
    private List<Order> orders = new ArrayList<>();
    private TextArea notificationArea = new TextArea();
    private Label totalProductsLabel = new Label("Total Products: 0");
    private List<Order> currentCart = new ArrayList<>(); // For storing current orders

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Inventory Management System");

        // Root layout
        BorderPane root = new BorderPane();
        VBox menuLayout = new VBox(10);
        menuLayout.setPadding(new Insets(10));

        // Buttons for manager, customer, and statistics interfaces
        Button managerButton = new Button("Manager");
        Button customerButton = new Button("Customer");
        Button statisticsButton = new Button("Statistics");

        managerButton.setMaxWidth(Double.MAX_VALUE);
        customerButton.setMaxWidth(Double.MAX_VALUE);
        statisticsButton.setMaxWidth(Double.MAX_VALUE);

        menuLayout.getChildren().addAll(managerButton, customerButton, statisticsButton);
        root.setCenter(menuLayout);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Manager Interface
        managerButton.setOnAction(e -> showManagerInterface(primaryStage));
        // Customer Interface
        customerButton.setOnAction(e -> showCustomerInterface(primaryStage));
        // Statistics Interface
        statisticsButton.setOnAction(e -> showStatisticsInterface(primaryStage));
    }

    // Manager Interface
    private void showManagerInterface(Stage stage) {
        BorderPane managerRoot = new BorderPane();
        VBox managerPanel = new VBox(10);
        managerPanel.setPadding(new Insets(10));

        Label productIdLabel = new Label("Product ID:");
        TextField productIdField = new TextField();

        Label productNameLabel = new Label("Product Name:");
        TextField productNameField = new TextField();

        Label productQuantityLabel = new Label("Quantity:");
        TextField productQuantityField = new TextField();

        Label productPriceLabel = new Label("Price:");
        TextField productPriceField = new TextField();

        Label productTypeLabel = new Label("Type (good/cargo):");
        TextField productTypeField = new TextField();

        Label shipmentModeLabel = new Label("Mode of Shipment (land/sea):");
        TextField shipmentModeField = new TextField();

        Label shipmentDurationLabel = new Label("Shipment Duration:");
        TextField shipmentDurationField = new TextField();

        Button addProductButton = new Button("Add Product");
        Button backButton = new Button("Back to Menu");

        managerPanel.getChildren().addAll(productIdLabel, productIdField, productNameLabel, productNameField, productQuantityLabel, productQuantityField,
                productPriceLabel, productPriceField, productTypeLabel, productTypeField, shipmentModeLabel, shipmentModeField,
                shipmentDurationLabel, shipmentDurationField, addProductButton, backButton);

        managerRoot.setCenter(managerPanel);

        // Notification section
        VBox notificationPanel = new VBox(10);
        notificationPanel.setPadding(new Insets(10));
        Label notificationLabel = new Label("Notifications:");
        notificationArea.setEditable(false);
        notificationPanel.getChildren().addAll(notificationLabel, notificationArea, totalProductsLabel);
        managerRoot.setRight(notificationPanel);

        Scene managerScene = new Scene(managerRoot, 800, 600);
        stage.setScene(managerScene);

        // Add product action
        addProductButton.setOnAction(e -> {
            String id = productIdField.getText();
            String name = productNameField.getText();
            int quantity;
            double price;
            String type = productTypeField.getText();
            String modeOfShipment = shipmentModeField.getText();
            String shipmentDuration = shipmentDurationField.getText();

            try {
                quantity = Integer.parseInt(productQuantityField.getText());
                price = Double.parseDouble(productPriceField.getText());
            } catch (NumberFormatException ex) {
                notificationArea.appendText("Invalid quantity or price!\n");
                return;
            }

            Product product = new Product(id, name, quantity, price, type, modeOfShipment, shipmentDuration);
            inventory.put(id, product);
            updateTotalProducts();
            notificationArea.appendText("Product Added: " + product + "\n");

            // Clear fields after adding
            productIdField.clear();
            productNameField.clear();
            productQuantityField.clear();
            productPriceField.clear();
            productTypeField.clear();
            shipmentModeField.clear();
            shipmentDurationField.clear();
        });

        // Back button action
        backButton.setOnAction(e -> {
            start(stage);
        });
    }

    // Customer Interface
    private void showCustomerInterface(Stage stage) {
        BorderPane customerRoot = new BorderPane();
        VBox customerPanel = new VBox(10);
        customerPanel.setPadding(new Insets(10));

        Label productIdLabel = new Label("Enter Product ID to Order:");
        TextField productIdField = new TextField();
        Label quantityLabel = new Label("Quantity:");
        TextField quantityField = new TextField();
        Label areaLabel = new Label("Enter Your Area:");
        TextField areaField = new TextField();
        Label cityLabel = new Label("Enter Your City:");
        TextField cityField = new TextField();
        Button addToCartButton = new Button("Add to Cart");
        Button checkoutButton = new Button("Checkout");
        TextArea cartDetailsArea = new TextArea();
        cartDetailsArea.setEditable(false);
        Button backButton = new Button("Back to Menu");

        customerPanel.getChildren().addAll(productIdLabel, productIdField, quantityLabel, quantityField, areaLabel, areaField, cityLabel, cityField, addToCartButton, checkoutButton, cartDetailsArea, backButton);

        Scene customerScene = new Scene(customerRoot, 600, 400);
        customerRoot.setCenter(customerPanel);
        stage.setScene(customerScene);

        // Add to cart action
        addToCartButton.setOnAction(e -> {
            String productId = productIdField.getText();
            int orderQuantity;

            try {
                orderQuantity = Integer.parseInt(quantityField.getText());
            } catch (NumberFormatException ex) {
                cartDetailsArea.appendText("Invalid quantity input!\n");
                return;
            }

            String area = areaField.getText();
            String city = cityField.getText();
            LocalDate orderDate = LocalDate.now(); // Get current date

            if (inventory.containsKey(productId)) {
                Product product = inventory.get(productId);
                if (product.getQuantity() >= orderQuantity) {
                    product.setQuantity(product.getQuantity() - orderQuantity); // Update product quantity in inventory
                    currentCart.add(new Order(productId, area, city, orderQuantity, orderDate)); // Add to current cart
                    cartDetailsArea.appendText("Added to Cart: " + product.getName() + " x" + orderQuantity + "\n");
                    notificationArea.appendText("Order Placed: " + product.getName() + " x" + orderQuantity + "\n");

                    // Check for low stock notification
                    if (product.getQuantity() < 5) {
                        notificationArea.appendText(product.getName() + " is low on stock! Only " + product.getQuantity() + " left.\n");
                    }
                } else {
                    cartDetailsArea.appendText("Insufficient stock for: " + product.getName() + ". Only " + product.getQuantity() + " left.\n");
                }
            } else {
                cartDetailsArea.appendText("Product not found!\n");
            }

            // Clear fields after adding to cart
            productIdField.clear();
            quantityField.clear();
        });

        // Checkout action
        checkoutButton.setOnAction(e -> {
            if (currentCart.isEmpty()) {
                cartDetailsArea.appendText("Your cart is empty!\n");
                return;
            }

            // Create a new scene for checkout details
            BorderPane checkoutRoot = new BorderPane();
            VBox checkoutPanel = new VBox(10);
            checkoutPanel.setPadding(new Insets(10));

            StringBuilder checkoutDetails = new StringBuilder("Checkout Details:\n");
            double totalAmount = 0.0;
            Map<String, List<Order>> shipmentMap = new HashMap<>();

            for (Order order : currentCart) {
                Product product = inventory.get(order.getProductId());
                totalAmount += product.getPrice() * order.getQuantity();

                // Calculate expected delivery date
                String shipmentDuration = product.getShipmentDuration();
                int days = 0;

                if (shipmentDuration.contains("hour")) {
                    days = Integer.parseInt(shipmentDuration.split(" ")[0]) / 24; // Convert hours to days
                } else if (shipmentDuration.contains("day")) {
                    days = Integer.parseInt(shipmentDuration.split(" ")[0]);
                }

                LocalDate expectedDeliveryDate = LocalDate.now().plusDays(days);
                
                // Group orders by mode of shipment
                shipmentMap.putIfAbsent(product.getModeOfShipment(), new ArrayList<>());
                shipmentMap.get(product.getModeOfShipment()).add(order);

                checkoutDetails.append("Product: ").append(product.getName())
                        .append(", Quantity: ").append(order.getQuantity())
                        .append(", Price: ₹").append(product.getPrice() * order.getQuantity())
                        .append(", Expected Delivery Date: ").append(expectedDeliveryDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append("\n");
            }

            checkoutDetails.append("Total Amount: ₹").append(totalAmount).append("\n");

            // Fetch the mode of shipment from the first product in the cart
            for (Map.Entry<String, List<Order>> entry : shipmentMap.entrySet()) {
                checkoutDetails.append("\nMode of Shipment: ").append(entry.getKey().substring(0, 1).toUpperCase())
                        .append(entry.getKey().substring(1).toLowerCase()).append("\n");
                for (Order order : entry.getValue()) {
                    Product product = inventory.get(order.getProductId());
                    // Calculate expected delivery date
                    String shipmentDuration = product.getShipmentDuration();
                    int days = 0;

                    if (shipmentDuration.contains("hour")) {
                        days = Integer.parseInt(shipmentDuration.split(" ")[0]) / 24; // Convert hours to days
                    } else if (shipmentDuration.contains("day")) {
                        days = Integer.parseInt(shipmentDuration.split(" ")[0]);
                    }

                    LocalDate expectedDeliveryDate = LocalDate.now().plusDays(days);
                    checkoutDetails.append(" - Product: ").append(product.getName())
                            .append(", Quantity: ").append(order.getQuantity())
                            .append(", Expected Delivery Date: ").append(expectedDeliveryDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append("\n");
                }
            }

            // Display checkout details in the new scene
            TextArea checkoutDetailsArea = new TextArea(checkoutDetails.toString());
            checkoutDetailsArea.setEditable(false);
            checkoutPanel.getChildren().addAll(new Label("Checkout Summary:"), checkoutDetailsArea);
            Button finishButton = new Button("Finish");
            checkoutPanel.getChildren().add(finishButton);
            checkoutRoot.setCenter(checkoutPanel);

            Scene checkoutScene = new Scene(checkoutRoot, 600, 400);
            stage.setScene(checkoutScene);

            // Finish button action
            finishButton.setOnAction(event -> {
                currentCart.clear(); // Clear cart after checkout
                notificationArea.appendText("Checkout Completed!\n");
                stage.setScene(customerScene); // Return to customer scene
            });

            // Clear previous cart details
            cartDetailsArea.clear();
            areaField.clear();
            cityField.clear();
        });

        // Back button action
        backButton.setOnAction(e -> {
            start(stage);
        });
    }

    // Statistics Interface
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

        // Product statistics
        Label productStatsLabel = new Label("Product Stock:");
        statisticsPanel.getChildren().add(productStatsLabel);

        // Calculate and display statistics on the page load
        int totalGoods = 0;
        int totalCargo = 0;

        for (Product product : inventory.values()) {
            if (product.getType().equalsIgnoreCase("good")) {
                totalGoods += product.getQuantity();
                statisticsPanel.getChildren().add(new Label(product.toString())); // Show goods with remaining stock
            } else if (product.getType().equalsIgnoreCase("cargo")) {
                totalCargo += product.getQuantity();
                statisticsPanel.getChildren().add(new Label(product.toString())); // Show cargo with remaining stock
            }
        }

        remainingGoodsLabel.setText("Remaining Goods: " + totalGoods);
        remainingCargoLabel.setText("Remaining Cargo: " + totalCargo);

        // Back button action
        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(e -> {
            start(stage);
        });

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