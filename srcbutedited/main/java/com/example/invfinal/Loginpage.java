package com.example.invfinal;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Loginpage {

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton; // Register button

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    private Stage stage;
    private InventoryGUI inventoryGUI = new InventoryGUI(); // Instance of InventoryGUI to start views

    // Initialize method to set the stage
    public void initialize(Stage stage) {
        this.stage = stage;

        // Handle login button click
        loginButton.setOnAction(e -> handleLogin());

        // Handle register button click to open the registration page
        registerButton.setOnAction(e -> openRegistrationPage());
    }

    // Handle login based on user credentials
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Check credentials for admin (manager)
        if ("adminlogin".equals(username) && "pass".equals(password)) {
            // Log in as manager (admin)
            inventoryGUI.startManagerView(new Stage(), "Admin");
        }
        // Check credentials for customer (Bob)
        else if ("bob".equals(username) && "boblogin".equals(password)) {
            // Log in as customer (Bob)
            inventoryGUI.startCustomerView(new Stage(), "Bob");
        }
        else {
            // Invalid credentials, display an error
            System.out.println("Invalid username or password");
        }

        // Clear fields after login attempt
        clearFields();
    }

    // Open the registration page
    private void openRegistrationPage() {
        try {
            // Load the registration FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newregistration.fxml"));
            Parent root = loader.load();

            // Get the controller of the registration page
            NewRegistrationController registrationController = loader.getController();
            registrationController.setLoginStage(stage); // Pass the login stage to return after registration

            // Create a new scene for the registration page
            Scene registrationScene = new Scene(root);

            // Switch to the registration page
            stage.setScene(registrationScene);
            stage.setTitle("New Registration");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Clear username and password fields after login attempt
    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
    }
}
