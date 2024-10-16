package com.example.invfinal;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Loginpage {

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    private Stage stage;
    private InventoryGUI inventoryGUI = new InventoryGUI();

    public void initialize(Stage stage) {
        this.stage = stage;

        // Handle login button click
        loginButton.setOnAction(e -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // For simplicity, we'll use fixed credentials here (you could modify this to connect to a database)
        if ("manager".equals(username) && "managerpass".equals(password)) {
            // Log in as manager
            inventoryGUI.startManagerView(stage,"manager");
        } else if ("customer1".equals(username) && "customer1pass".equals(password)) {
            // Log in as customer1 (Bob)
            inventoryGUI.startCustomerView(stage, "Bob");
        } else if ("customer2".equals(username) && "customer2pass".equals(password)) {
            // Log in as customer2 (Charlie)
            inventoryGUI.startCustomerView(stage, "Charlie");
        } else {
            // Invalid credentials, display an error
            System.out.println("Invalid username or password");
        }
    }
}
