import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InventoryGUI extends Application {
    private Inventory inventory = new Inventory();
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Override
    public void start(Stage primaryStage) {
        // Main layout
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(15));

        // Product list view for displaying products
        ListView<String> productList = new ListView<>();
        Button viewProductsButton = new Button("View Products");
        viewProductsButton.setOnAction(e -> {
            productList.getItems().clear();
            for (Product product : inventory.getProducts()) {
                productList.getItems().add(product.toString());
            }
        });

        // Customer order section
        Label orderLabel = new Label("Place an Order:");
        TextField productIdField = new TextField();
        productIdField.setPromptText("Product ID");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        Button placeOrderButton = new Button("Place Order");
        placeOrderButton.setOnAction(e -> {
            int productId = Integer.parseInt(productIdField.getText());
            int quantity = Integer.parseInt(quantityField.getText());

            // Simulate customer order with threading
            Customer customer = new Customer("User"); // You could prompt for the customer name dynamically
            executorService.submit(new OrderThread(inventory, customer, productId, quantity));

            // Clear fields
            productIdField.clear();
            quantityField.clear();
        });

        // Manager section to add products
        Label addProductLabel = new Label("Add Product:");
        TextField addProductIdField = new TextField();
        addProductIdField.setPromptText("Product ID");
        TextField addProductNameField = new TextField();
        addProductNameField.setPromptText("Product Name");
        TextField addProductTypeField = new TextField();
        addProductTypeField.setPromptText("Product Type (good/cargo)");
        TextField addProductQuantityField = new TextField();
        addProductQuantityField.setPromptText("Quantity");
        TextField addProductPriceField = new TextField();
        addProductPriceField.setPromptText("Price");

        Button addProductButton = new Button("Add Product");
        addProductButton.setOnAction(e -> {
            int productId = Integer.parseInt(addProductIdField.getText());
            String productName = addProductNameField.getText();
            String productType = addProductTypeField.getText();
            int quantity = Integer.parseInt(addProductQuantityField.getText());
            double price = Double.parseDouble(addProductPriceField.getText());

            Product product = new Product(productId, productName, productType, quantity, price);
            Manager manager = new Manager("Manager");  // You can prompt for the manager's name dynamically
            manager.addProduct(inventory, product);

            // Clear fields
            addProductIdField.clear();
            addProductNameField.clear();
            addProductTypeField.clear();
            addProductQuantityField.clear();
            addProductPriceField.clear();
        });

        // View statistics button for Manager
        Button viewStatsButton = new Button("View Statistics");
        viewStatsButton.setOnAction(e -> {
            inventory.generateStatistics();  // Display stats in console for simplicity
        });

        // Layout for placing an order
        HBox orderLayout = new HBox(10, productIdField, quantityField, placeOrderButton);
        HBox addProductLayout = new HBox(10, addProductIdField, addProductNameField, addProductTypeField, addProductQuantityField, addProductPriceField, addProductButton);

        mainLayout.getChildren().addAll(viewProductsButton, productList, orderLabel, orderLayout, addProductLabel, addProductLayout, viewStatsButton);

        Scene scene = new Scene(mainLayout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Inventory Management System");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
