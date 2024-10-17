package com.example.invfinal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    private ObservableList<Product> products;

    // Constructor
    public Inventory() {
        this.products = FXCollections.observableArrayList();
    }

    // Add a new product
    public void addProduct(Product product) {
        products.add(product);
    }

    // Get the list of products
    public ObservableList<Product> getProducts() {
        return products;
    }

    // Generate statistics or other functionalities can be added here
    public void generateStatistics() {
        // Example: print total inventory value
        double totalValue = products.stream()
                .mapToDouble(product -> product.getQuantity() * product.getPrice())
                .sum();
        System.out.println("Total Inventory Value: $" + totalValue);
    }

    public void orderProduct(int productId, int quantity, Customer customer) {
        for (Product product : products) {
            if (product.getId() == productId) {
                if (product.getQuantity() >= quantity) {
                    product.setQuantity(product.getQuantity() - quantity);
                    System.out.println(customer.name + " successfully ordered " + quantity + " of " + product.getName());
                } else {
                    System.out.println("Insufficient stock for product: " + product.getName());
                }
                return;
            }
        }
        System.out.println("Product not found.");
    }

}
