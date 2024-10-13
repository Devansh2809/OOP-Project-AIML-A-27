public class OrderThread extends Thread {
    private Inventory inventory;
    private Customer customer;
    private int productId;
    private int quantity;

    public OrderThread(Inventory inventory, Customer customer, int productId, int quantity) {
        this.inventory = inventory;
        this.customer = customer;
        this.productId = productId;
        this.quantity = quantity;
    }

    @Override
    public void run() {
        customer.placeOrder(inventory, productId, quantity);
    }
}
