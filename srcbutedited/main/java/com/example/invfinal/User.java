package com.example.invfinal;

abstract class User {
    protected String name;

    public User(String name) {
        this.name = name;
    }

    public abstract void viewProducts(Inventory inventory);
}

class Manager extends User {
    public Manager(String name) {
        super(name);
    }

    public void addProduct(Inventory inventory, Product product) {
        inventory.addProduct(product);
        System.out.println("Product added by Manager: " + name);
    }

    @Override
    public void viewProducts(Inventory inventory) {
        System.out.println("Manager " + name + " is viewing all products:");
        inventory.getProducts().forEach(System.out::println);
    }

    // Additional Manager-specific functionalities can be added here if needed
}

class Customer extends User {
    public Customer(String name) {
        super(name);
    }

    @Override
    public void viewProducts(Inventory inventory) {
        System.out.println("Customer " + name + " is viewing available products:");
        inventory.getProducts().forEach(System.out::println);
    }

    public void placeOrder(Inventory inventory, int productId, int quantity) {
        inventory.orderProduct(productId, quantity, this);
    }
}
