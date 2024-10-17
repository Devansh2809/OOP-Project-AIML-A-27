package com.example.invfinal;

public class Product {
    private int id;
    private String name;
    private String type; // good or cargo
    private int quantity;
    private double price;

    // Constructor
    public Product(int id, String name, String type, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return id + ": " + name + " | Type: " + type + " | Qty: " + quantity + " | $" + price;
    }
}
