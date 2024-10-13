public class InventoryManagementSystem {
    public static void main(String[] args) {
        // Initialize inventory
        Inventory inventory = new Inventory();

        // Create manager and customers
        Manager manager = new Manager("Alice");
        Customer customer1 = new Customer("Bob");
        Customer customer2 = new Customer("Charlie");

        // Add products to inventory
        manager.addProduct(inventory, new Product(1, "Laptop", "good", 50, 1200));
        manager.addProduct(inventory, new Product(2, "Car", "cargo", 10, 25000));

        // Manager views all products
        manager.viewProducts(inventory);

        // Customers placing orders concurrently
        OrderThread order1 = new OrderThread(inventory, customer1, 1, 2);
        OrderThread order2 = new OrderThread(inventory, customer2, 1, 3);
        order1.start();
        order2.start();

        try {
            order1.join();
            order2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final inventory status
        manager.viewProducts(inventory);
    }
}
