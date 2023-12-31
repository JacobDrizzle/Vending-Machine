package Main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import Users.Guest;
import Users.User; 

public class Login extends Scene {
	
	 private Main vendingMachine;
	 
	 public Login(Main vendingMachine) {
	   
		super(new VBox(10),650, 550);
	    this.vendingMachine = vendingMachine;
	    
        VBox loginLayout = (VBox) this.getRoot();
        loginLayout.getStylesheets().add(getClass().getResource("../Styles.css").toExternalForm());
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.getStyleClass().add("login-scene");
        
        Text loginText = new Text("Vending Machine Login");
        loginText.getStyleClass().add("order-text");
        VBox textBox = new VBox(5, loginText);
        textBox.setAlignment(Pos.CENTER);
        
        Text usernameLabel = new Text("Username:");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setFocusTraversable(true);
        
        
        Text passwordLabel = new Text("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        
        usernameField.setOnMouseClicked(e -> {
            // Reset border color when the user clicks on the field
            usernameField.getStyleClass().remove("error");
            usernameField.setPromptText("");
            passwordField.getStyleClass().remove("error");
            passwordField.setPromptText("");
        });
        
        Button loginButton = new Button("Login");
        Button guestButton = new Button("Continue as Guest");
        Button backButton = new Button("Back to Home");
        
        HBox buttonRow = new HBox(10,loginButton,guestButton, backButton);
        
        buttonRow.getStyleClass().add("login-buttons");
        buttonRow.setAlignment(Pos.BOTTOM_CENTER);
        
        backButton.setOnAction(e -> {vendingMachine.showHomePage();});
        
        loginLayout.setPadding(new Insets(10));
        
        // Login button functionality
        // store the entered username and password and validates the credentials with the 
        // isValidUser method if a user is returned then we proceed with logging the user in
        // and opening the order scene
        // If credentials are invalid alert the user
        loginButton.setOnAction(e -> {
            String enteredUsername = usernameField.getText();
            String enteredPassword = passwordField.getText();

            // Implement login logic
            User user = isValidUser(enteredUsername, enteredPassword);
            if (user != null) {
                loginSuccessful(user);
            } else {
                // if authentication failed alert the user via promptText and adding styles to the input fields
            	usernameField.setText("");
            	usernameField.setPromptText("Incorrect Username");
            	usernameField.getStyleClass().add("error");
            	passwordField.setText("");
            	passwordField.setPromptText("Or Password");
            	passwordField.getStyleClass().add("error");
            	usernameField.setFocusTraversable(true);
                System.out.println("Login failed. Invalid credentials.");
            }
        });

        // Guest button Functionality
        // If a user does not want to log in they can proceed as a guest
        guestButton.setOnAction(e -> {
        	Guest guestUser = new Guest();
        	vendingMachine.login(guestUser);
        });
        
        VBox loginBox = new VBox(10, textBox, usernameLabel, usernameField, passwordLabel, passwordField, buttonRow);
        loginBox.getStyleClass().add("loginBox"); 
        loginLayout.getChildren().addAll(loginBox);
    }
	 
	 // Validates the user exists in the usersDB.txt file and returns the user
	private User isValidUser(String enteredUsername, String enteredPassword) {
	    	try {
	            BufferedReader br = new BufferedReader(new FileReader("usersDB.txt"));
	            String line;
	            while ((line = br.readLine()) != null) {
	                String[] parts = line.split(",");
	                if (parts.length == 5) {
	                    String username = parts[0].trim();
	                    String password = parts[1].trim();
	                    
	                    double credit = Double.parseDouble(parts[2].trim());
	                    double totalSpend = Double.parseDouble(parts[3].trim());
	                    
	                    boolean cardDetails = Boolean.parseBoolean(parts[4].trim());
	                    System.out.println("Read from file: " + username + " | " + password + " | " + credit + " | " + totalSpend + " | " + cardDetails); // Debug output
	                    
	                    if (enteredUsername.equals(username) && enteredPassword.equals(password)) {
	                        br.close();
	                        return new User(username, password, credit, totalSpend, cardDetails);
	                    	}
	                	}
	            	}
	            br.close();
	            
	        	} catch (IOException e) {
	       e.printStackTrace();
	    }
	   return null; // User not found
	 }
	 
	 
	 // loads the order scene if loging is successful
	 private void loginSuccessful(User user) {
	        vendingMachine.login(user); // Pass the logged-in user to VendingMachine  
	    }
}