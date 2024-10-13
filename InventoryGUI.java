import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InventoryGUI extends Application {
    private Inventory inventory = new Inventory();

    @Override
    public void start(Stage primaryStage) {
        ListView<String> productList = new ListView<>();

        Button viewProductsButton = new Button("View Products");
        viewProductsButton.setOnAction(e -> {
            productList.getItems().clear();
            for (Product product : inventory.getProducts()) {
                productList.getItems().add(product.toString());
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(viewProductsButton, productList);

        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Inventory Management System");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
