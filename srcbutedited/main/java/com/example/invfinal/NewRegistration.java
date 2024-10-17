package com.example.invfinal;

public class NewRegistration {

    private static final int MAX_USERS = 10; // Maximum allowed users (both customers and managers)
    private User[] users;                    // Array to store User objects
    private int userCount;                   // Keeps track of registered users

    // Constructor
    public NewRegistration() {
        users = new User[MAX_USERS];          // Initialize the array
        userCount = 0;                        // Initially no users registered
    }

    // Method to register a new user
    public String register(String name, String password, String reenterPassword, String role) {
        if (userCount >= MAX_USERS) {
            return "Registration failed: Maximum number of users reached.";
        }

        if (!password.equals(reenterPassword)) {
            return "Registration failed: Passwords do not match.";
        }

        if (isNameTaken(name)) {
            return "Registration failed: Username already taken.";
        }

        // Create new user based on the role
        User newUser;
        if (role.equalsIgnoreCase("manager")) {
            newUser = new Manager(name);
        } else if (role.equalsIgnoreCase("customer")) {
            newUser = new Customer(name);
        } else {
            return "Registration failed: Invalid role specified.";
        }

        // Add new user to the array
        users[userCount] = newUser;
        userCount++;

        return "Registration successful: Welcome, " + name + "!";
    }

    // Helper method to check if the username is already taken
    private boolean isNameTaken(String name) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].name.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    // Optional: Method to print all registered users (for testing)
    public void printAllUsers() {
        System.out.println("Registered Users:");
        for (int i = 0; i < userCount; i++) {
            System.out.println((i + 1) + ". " + users[i].name + " (" + users[i].getClass().getSimpleName() + ")");
        }
    }

    // Method to get the registered users
    public User[] getUsers() {
        return users;
    }
}
