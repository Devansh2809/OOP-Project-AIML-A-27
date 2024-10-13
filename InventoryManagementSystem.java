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

        // Main loop for user interaction
        boolean exit = false;
        while (!exit) {
            System.out.println("\nChoose an option: ");
            System.out.println("1. Manager: Add product");
            System.out.println("2. Manager: View products");
            System.out.println("3. Manager: View statistics");
            System.out.println("4. Customer: View products");
            System.out.println("5. Customer: Place order");
            System.out.println("6. Exit");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    // Manager adds a product
                    System.out.print("Enter product ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline
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
                    // Customer views products
                    System.out.println("Choose customer (1: Bob, 2: Charlie): ");
                    int customerChoice = scanner.nextInt();
                    Customer currentCustomer = (customerChoice == 1) ? customer1 : customer2;
                    currentCustomer.viewProducts(inventory);
                    break;

                case 5:
                    // Customer places an order
                    System.out.println("Choose customer (1: Bob, 2: Charlie): ");
                    customerChoice = scanner.nextInt();
                    currentCustomer = (customerChoice == 1) ? customer1 : customer2;

                    System.out.print("Enter product ID to order: ");
                    int productId = scanner.nextInt();
                    System.out.print("Enter quantity to order: ");
                    int orderQuantity = scanner.nextInt();

                    // Use threading for concurrency
                    executorService.submit(new OrderThread(inventory, currentCustomer, productId, orderQuantity));
                    break;

                case 6:
                    // Exit the system
                    exit = true;
                    executorService.shutdown();
                    System.out.println("Exiting system...");
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }

        scanner.close();
    }
}
