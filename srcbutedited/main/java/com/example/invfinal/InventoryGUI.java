package com.example.invfinal;

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

    }

    // Method to start the customer view
    public void startCustomerView(Stage stage, String customerName) {
        VBox customerLayout = new VBox(15);
        customerLayout.setPadding(new Insets(15));

        Customer customer = new Customer(customerName);

        // List view for showing available products to the customer
        ListView<String> customerProductList = new ListView<>();
        Button viewProductsButton = new Button("View Products");
        viewProductsButton.setOnAction(e -> {
            customerProductList.getItems().clear();
            for (Product product : inventory.getProducts()) {
                customerProductList.getItems().add(product.toString());
            }
        });

        // Fields for placing an order
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
            executorService.submit(new OrderThread(inventory, customer, productId, quantity));

            productIdField.clear();
            quantityField.clear();
        });

        // Layout for placing orders
        HBox orderLayout = new HBox(10, productIdField, quantityField, placeOrderButton);

        customerLayout.getChildren().addAll(viewProductsButton, customerProductList, orderLabel, orderLayout);

        Scene customerScene = new Scene(customerLayout, 600, 400);
        stage.setScene(customerScene);
        stage.setTitle("Customer View - " + customerName);
        stage.show();
    }

    // Method to start the manager view
    public void startManagerView(Stage stage, String managerName) {
        VBox managerLayout = new VBox(15);
        managerLayout.setPadding(new Insets(15));

        Manager manager = new Manager(managerName);

        // List view for showing all products in inventory
        ListView<String> managerProductList = new ListView<>();
        Button viewProductsButton = new Button("View Products");
        viewProductsButton.setOnAction(e -> {
            managerProductList.getItems().clear();
            for (Product product : inventory.getProducts()) {
                managerProductList.getItems().add(product.toString());
            }
        });

        // Fields for adding a new product
        Label addProductLabel = new Label("Add Product:");
        TextField productIdField = new TextField();
        productIdField.setPromptText("Product ID");
        TextField productNameField = new TextField();
        productNameField.setPromptText("Product Name");
        TextField productTypeField = new TextField();
        productTypeField.setPromptText("Product Type (good/cargo)");
        TextField productQuantityField = new TextField();
        productQuantityField.setPromptText("Quantity");
        TextField productPriceField = new TextField();
        productPriceField.setPromptText("Price");

        Button addProductButton = new Button("Add Product");
        addProductButton.setOnAction(e -> {
            int productId = Integer.parseInt(productIdField.getText());
            String productName = productNameField.getText();
            String productType = productTypeField.getText();
            int quantity = Integer.parseInt(productQuantityField.getText());
            double price = Double.parseDouble(productPriceField.getText());

            Product product = new Product(productId, productName, productType, quantity, price);
            manager.addProduct(inventory, product);

            productIdField.clear();
            productNameField.clear();
            productTypeField.clear();
            productQuantityField.clear();
            productPriceField.clear();

            // Refresh the product list view
            viewProductsButton.fire();
        });

        // Layout for adding products
        HBox addProductLayout = new HBox(10, productIdField, productNameField, productTypeField, productQuantityField, productPriceField, addProductButton);

        managerLayout.getChildren().addAll(viewProductsButton, managerProductList, addProductLabel, addProductLayout);

        Scene managerScene = new Scene(managerLayout, 600, 400);
        stage.setScene(managerScene);
        stage.setTitle("Manager View - " + managerName);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
