package com.example.invfinal;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InventoryManagementSystem {
    public static void main(String[] args) {
        // Initialize inventory
        Inventory inventory = new Inventory();
        Scanner scanner = new Scanner(System.in);
        ExecutorService executorService = Executors.newFixedThreadPool(5); // to manage concurrent requests

        // Initialize Manager and Customers
        Manager manager = new Manager("Alice");
        Customer customer1 = new Customer("Bob");
        Customer customer2 = new Customer("Charlie");

        // Add default products to inventory
        manager.addProduct(inventory, new Product(1, "Laptop", "good", 50, 1200));
        manager.addProduct(inventory, new Product(2, "Car", "cargo", 10, 25000));

        boolean exit = false;

        while (!exit) {
            System.out.println("\nLogin as:");
            System.out.println("1. Manager");
            System.out.println("2. Customer");
            System.out.println("3. Exit");
            int userType = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (userType) {
                case 1: // Manager
                    boolean managerExit = false;
                    while (!managerExit) {
                        System.out.println("\nManager Menu: ");
                        System.out.println("1. Add product");
                        System.out.println("2. View products");
                        System.out.println("3. View statistics");
                        System.out.println("4. Logout");

                        int managerChoice = scanner.nextInt();
                        scanner.nextLine(); // consume newline

                        switch (managerChoice) {
                            case 1:
                                // Manager adds a product
                                System.out.print("Enter product ID: ");
                                int id = scanner.nextInt();
                                scanner.nextLine(); // consume newline
                                System.out.print("Enter product name: ");
                                String name = scanner.nextLine();
                                System.out.print("Enter product type (good/cargo): ");
                                String type = scanner.nextLine();
                                System.out.print("Enter product quantity: ");
                                int quantity = scanner.nextInt();
                                System.out.print("Enter product price: ");
                                double price = scanner.nextDouble();

                                manager.addProduct(inventory, new Product(id, name, type, quantity, price));
                                break;

                            case 2:
                                // Manager views all products
                                manager.viewProducts(inventory);
                                break;

                            case 3:
                                // Manager views statistics
                                inventory.generateStatistics();
                                break;

                            case 4:
                                // Logout
                                managerExit = true;
                                System.out.println("Logging out...");
                                break;

                            default:
                                System.out.println("Invalid choice, try again.");
                        }
                    }
                    break;

                case 2: // Customer
                    boolean customerExit = false;
                    while (!customerExit) {
                        System.out.println("Choose customer (1: Bob, 2: Charlie): ");
                        int customerChoice = scanner.nextInt();
                        scanner.nextLine(); // consume newline
                        Customer currentCustomer = (customerChoice == 1) ? customer1 : customer2;

                        System.out.println("\nCustomer Menu: ");
                        System.out.println("1. View products");
                        System.out.println("2. Place order");
                        System.out.println("3. Logout");

                        int customerOption = scanner.nextInt();
                        scanner.nextLine(); // consume newline

                        switch (customerOption) {
                            case 1:
                                // Customer views products
                                currentCustomer.viewProducts(inventory);
                                break;

                            case 2:
                                // Customer places an order
                                System.out.print("Enter product ID to order: ");
                                int productId = scanner.nextInt();
                                System.out.print("Enter quantity to order: ");
                                int orderQuantity = scanner.nextInt();

                                // Use threading for concurrency
                                executorService.submit(new OrderThread(inventory, currentCustomer, productId, orderQuantity));
                                break;

                            case 3:
                                // Logout
                                customerExit = true;
                                System.out.println("Logging out...");
                                break;

                            default:
                                System.out.println("Invalid choice, try again.");
                        }
                    }
                    break;

                case 3:
                    exit = true;
                    executorService.shutdown();
                    System.out.println("Exiting system...");
                    break;

                default:
                    System.out.println("Invalid input! Please choose a valid option.");
            }
        }

        scanner.close();
    }
}
