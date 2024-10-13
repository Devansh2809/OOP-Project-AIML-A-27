import java.util.ArrayList;
import java.util.List;

class Inventory {
    private List<Product> products = new ArrayList<>();

    // Add product to the inventory
    public void addProduct(Product product) {
        products.add(product);
    }

    // Display all products
    public void displayProducts() {
        for (Product product : products) {
            System.out.println(product);
        }
    }

    // Order a product (with concurrency handling)
    public synchronized void orderProduct(int productId, int quantity, Customer customer) {
        for (Product product : products) {
            if (product.getId() == productId) {
                if (product.getQuantity() >= quantity) {
                    product.setQuantity(product.getQuantity() - quantity);
                    System.out.println(customer.name + " ordered " + quantity + " of " + product.getName());
                } else {
                    System.out.println("Sorry, only " + product.getQuantity() + " items available in stock.");
                }
                return;
            }
        }
        System.out.println("Product not found.");
    }
}
