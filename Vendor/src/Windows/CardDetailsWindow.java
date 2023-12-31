package Windows;

import Users.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CardDetailsWindow {
	// Opens an alert window if the users credit is low
    public void addCardDetails(User user, Window window) {
    	// Create a new stage for the Add Credit window
        Stage cardDetailsStage = new Stage();
        cardDetailsStage.initModality(Modality.APPLICATION_MODAL);
        cardDetailsStage.setTitle("Please Enter Card Details");
        cardDetailsStage.getIcons().add(new Image(getClass().getResourceAsStream("../logo.png")));
        // Create the content for the Add Credit window
        Label amountLabel = new Label("Add Card Details");
        amountLabel.getStyleClass().add("window-label");
        
        TextField cardNumberField = new TextField();
        cardNumberField.setPromptText("Card Number");

        TextField expiryDateField = new TextField();
        expiryDateField.setPromptText("Expiry Date (MM/YY)");

        TextField cvvField = new TextField();
        cvvField.setPromptText("CVV");

        Button saveButton = new Button("Save Details");
        Button closeButton = new Button("Close");
        
        HBox buttonBox = new HBox(10, saveButton, closeButton);
        buttonBox.getStyleClass().add("restock-buttons");
        buttonBox.setAlignment(Pos.CENTER);
        
        String cardNumberRegex = "\\d{16}"; // 16-digit card number
        String expiryDateRegex = "\\d{2}/\\d{2}"; // MM/YY format
        String cvvRegex = "\\d{3}"; // 3-digit CVV
        
        saveButton.setOnAction(e -> {
        	if(!cardNumberField.getText().matches(cardNumberRegex)) {
            	System.out.println("Invalid Card Number");
            	cardNumberField.setText("");
            	cardNumberField.setPromptText("Card Number must be 16 Digits");
            }
            
            if(!expiryDateField.getText().matches(expiryDateRegex)) {
            	System.out.println("Invalid card Expiry");
            	expiryDateField.setText("");
            	expiryDateField.setPromptText("Format should be MM/YY");
            }
            
            if(!cvvField.getText().matches(cvvRegex)) {
            	System.out.println("Invalid CVV");
            	cvvField.setText("");
            	cvvField.setPromptText("CVV Must be three digits");
            }
            
            if (cardNumberField.getText().matches(cardNumberRegex) && expiryDateField.getText().matches(expiryDateRegex) && cvvField.getText().matches(cvvRegex)) {
                // Assuming user is a class member
                user.setCardDetails(true);
                window.openWindow("Success", "Details Saved Successfully!", "Continue");
                cardDetailsStage.close();
            } else {
                System.out.println("Invalid card details. Please check and try again.");
            }
        });

        closeButton.setOnAction(e -> cardDetailsStage.close());

        // Create a layout for the Add Credit window
        VBox detailsLayout = new VBox(10);
        detailsLayout.getStyleClass().add("window");
        detailsLayout.getChildren().addAll(amountLabel, cardNumberField, expiryDateField, cvvField, buttonBox);
        detailsLayout.setAlignment(Pos.CENTER);
        detailsLayout.setPadding(new Insets(10));

        // Set the scene for the new stage
        Scene addCardDetailsScene = new Scene(detailsLayout, 350, 250);
        cardDetailsStage.setScene(addCardDetailsScene);
        addCardDetailsScene.getStylesheets().add(getClass().getResource("../Styles.css").toExternalForm());
        // Show the Add Credit window
        cardDetailsStage.show();
    }
}
