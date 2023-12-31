package Windows;

import Main.Main;
import Orders.Orders;
import Users.Guest;
import Users.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TextFormatter.Change;


public class AddCreditWindow {
	private boolean warningDisplayed = false;
	// Opens a credit window allowing the user to add credit to their account
    public void addCredit(User user, Main vendingMachine, Orders orders, Window window) {
        // Create a new stage for the Credit window
    	if (vendingMachine.getMachineStatus()){
    		window.openWindow("Out Of Service", "The machine is currently out of service", "Close");
    	} else {
	        Stage addCreditStage = new Stage();
	        addCreditStage.initModality(Modality.APPLICATION_MODAL);
	        addCreditStage.setTitle("Add Credit");
	        addCreditStage.getIcons().add(new Image(getClass().getResourceAsStream("../logo.png")));
	        
	        // Create the content for the Add Credit window
	        Label amountLabel = new Label("Enter the credit amount:");
	        TextField amountField = new TextField();
	        
	        amountField.setTextFormatter(new TextFormatter<>(this::filter));
	        amountLabel.getStyleClass().add("window-label");
	       
	        Button confirmButton = new Button("Confirm");
	        
	        Button backButton = new Button("Back");
	        backButton.setOnAction(e -> {
	        	addCreditStage.close();
	        });
	        
	        Label warningLabel = new Label();
	        warningLabel.getStyleClass().add("warning-label");
	        if( user instanceof Guest) {
	        	warningLabel.setText("Credit amount should be €1.80 or more");
	        } else {
	        	warningLabel.setText("Credit amount should be €10.00 or more");
	        }
	       
	
	        amountField.textProperty().addListener((observable, oldValue, newValue) -> {
	            try {
	                double creditAmount = Double.parseDouble(newValue);
	                if (creditAmount >= 1.80 && user instanceof Guest && !warningDisplayed) {
	                	warningDisplayed = true;
	                    window.openWindow("Warning!", "Warning this machine does not give change!", "Close");
	                } 
	            } catch (NumberFormatException e) {
	            	System.out.println("Invalid input");
	            }
	        });
	        
	        confirmButton.setOnAction(e -> {
	        	String amountText = amountField.getText();
	        	System.out.println("amountText:" + amountText);
	            // Process the credit input, deduct credit from the user, and close the window
	            if (amountText.isEmpty()) {
	                System.out.println("Please enter a valid credit amount");
	                amountField.setPromptText("Invalid amount");
	                // Optionally display an error message to the user
	                return;
	            }
	            warningDisplayed = false;
	            // get the amount of credit from the textField
	            double creditAmount = Double.parseDouble(amountText);
	            System.out.println("creditAmount:" + creditAmount);
	            
	            if (user.getUsername() != "Guest" && creditAmount < 10.0) {
	            	System.out.println("Invalid credit amount");
	            	return;
	            } else if (user.getUsername() == "Guest" && creditAmount < 1.8) {	            	
	            	System.out.println("Invalid credit amount");
	            	return;
	            }
	            
		        double Credit = user.getCredit() + creditAmount;
		        user.setCredit(Credit);
		                    
		        orders.updateCreditLabel(user.getCredit());
		        addCreditStage.close();
		                    
		        System.out.println(user.getUsername() + " Credit = €" + user.getCredit());
	        });
	
	        // Create a layout for the Add Credit window
	        VBox creditLayout = new VBox(10);
	        creditLayout.getStyleClass().add("window");
	        HBox buttonBox = new HBox(10, confirmButton, backButton);
	        
	        buttonBox.setAlignment(Pos.CENTER);
	        creditLayout.getChildren().addAll(warningLabel, amountLabel, amountField, buttonBox);
	        creditLayout.setAlignment(Pos.CENTER);
	        
	        creditLayout.setPadding(new Insets(10));
	        // Set the scene for the new stage
	        Scene creditScene = new Scene(creditLayout, 260, 210);
	        creditScene.getStylesheets().add(getClass().getResource("../Styles.css").toExternalForm());
	        addCreditStage.setScene(creditScene);
	
	        // Show the Add Credit window
	        addCreditStage.show();
    	}
    }
    private Change filter(Change change) {
        String newText = change.getControlNewText();
        if (newText.matches("^\\d*\\.?\\d*$")) {
            return change;
        }
        return null;
    }
    
}
