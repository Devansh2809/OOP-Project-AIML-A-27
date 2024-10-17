package com.example.invfinal;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class NewRegistrationController {

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField reenterPasswordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Button registerButton;

    private Stage stage;

    // Instance of the registration system
    private NewRegistration registrationSystem = new NewRegistration(); // Assuming you have a system to handle registrations

    @FXML
    private void initialize() {
        // Set up the roles for the combo box (only allowing "Customer" role)
        roleComboBox.getItems().addAll("Customer");

        // Add action listener to Register button
        registerButton.setOnAction(event -> handleRegistration());
    }

    private void handleRegistration() {
        String name = nameField.getText();
        String password = passwordField.getText();
        String reenteredPassword = reenterPasswordField.getText();
        String role = roleComboBox.getValue();

        // Basic validation
        if (role == null || !"Customer".equals(role)) {
            showAlert("Please select a valid role (Customer only).");
            return;
        }

        // Check if passwords match
        if (!password.equals(reenteredPassword)) {
            showAlert("Passwords do not match. Please re-enter.");
            return;
        }

        // Call the registration system to register the user
        String result = registrationSystem.register(name, password, reenteredPassword, role);

        if ("Registration successful".equals(result)) {
            // After successful registration, redirect to the login page
            showAlert("Registration successful! Redirecting to login page...");
            redirectToLoginPage();
        } else {
            // Handle registration failure
            showAlert(result);
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Registration");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to redirect to the login page after registration
    private void redirectToLoginPage() {
        try {
            // Load the login page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("loginpage.fxml"));
            Parent root = loader.load();

            // Get the controller for the login page and set the stage
            Loginpage loginController = loader.getController();
            loginController.initialize(stage); // Pass the same stage back to the login controller

            // Set the login page scene
            Scene loginScene = new Scene(root);
            stage.setScene(loginScene);
            stage.setTitle("Login Page");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to set the stage, called when navigating to the registration page
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setLoginStage(Stage stage) {

    }
}
