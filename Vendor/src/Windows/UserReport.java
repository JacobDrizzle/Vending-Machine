package Windows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UserReport {
    public void userReportWindow() {
        // Create a new stage for the Report window
        Stage reportWindow = new Stage();
        reportWindow.initModality(Modality.APPLICATION_MODAL);
        reportWindow.setTitle("User Report");
        reportWindow.getIcons().add(new Image(getClass().getResourceAsStream("../logo.png")));

        int totalUsers = getTotalUsers();
        Label totalUsersLabel = new Label("Total Users: " + totalUsers);
        HBox headerBox = new HBox(200, totalUsersLabel);
        headerBox.setAlignment(Pos.CENTER);
        
        Label userNameLabel = new Label("Username: ");
        Label pinLabel = new Label("PIN: ");
        Label creditLabel = new Label("Credit: ");
        Label totalSpendLabel = new Label("Total Spend: ");
        Label cardDetailsLabel = new Label("Card Details: ");

        Label[] labels = {userNameLabel, pinLabel, creditLabel, totalSpendLabel, cardDetailsLabel};

        totalUsersLabel.getStyleClass().add("report-window-header");
        for (Label label : labels) {
            label.getStyleClass().add("report-window-text");
        }

        Button closeButton = new Button("Close");

        closeButton.setOnAction(e -> {
            reportWindow.close();
        });

        // Create a layout for the Report window
        VBox reportLayout = new VBox(10);
        HBox lableBox = new HBox(15);
        HBox buttonBox = new HBox(10, closeButton);
        buttonBox.setAlignment(Pos.CENTER);

        lableBox.getChildren().addAll(userNameLabel, pinLabel, creditLabel, totalSpendLabel, cardDetailsLabel);
        lableBox.setAlignment(Pos.CENTER);
        lableBox.setPadding(new Insets(10));
        reportLayout.getStyleClass().add("window");
        reportLayout.getChildren().addAll(headerBox,lableBox,buttonBox);
        // Set the scene for the new stage
        Scene reportScene = new Scene(reportLayout, 450, 250);
        reportScene.getStylesheets().add(getClass().getResource("../Styles.css").toExternalForm());
        reportWindow.setScene(reportScene);

        // Read and display user data
        readUserReport(userNameLabel, pinLabel, creditLabel, totalSpendLabel, cardDetailsLabel);
        // Show the Report window
        reportWindow.show();
    }
    
    // method to get total number of users with an account
    private int getTotalUsers() {
        int totalUsers = 0;

        try (BufferedReader br = new BufferedReader(new FileReader("usersDB.txt"))) {
            while (br.readLine() != null) {
                totalUsers++;
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception properly in your application
        }

        return totalUsers;
    }
    
    // reads in data from usersDB.txt file
    private void readUserReport(Label userNameLabel, Label pinLabel, Label creditLabel, Label totalSpendLabel, Label cardDetailsLabel) {
        try (BufferedReader reader = new BufferedReader(new FileReader("usersDB.txt"))) {

            // Display labels only once at the top
            userNameLabel.setText("Username: ");
            pinLabel.setText("PIN: ");
            creditLabel.setText("Credit: ");
            totalSpendLabel.setText("Total Spend: ");
            cardDetailsLabel.setText("Card Details: ");

            String line;
            while ((line = reader.readLine()) != null) {
                String[] userAttributes = line.split(",");

                // Append user details on each line
                userNameLabel.setText(userNameLabel.getText() + "\n" + userAttributes[0]);
                pinLabel.setText(pinLabel.getText() + "\n" + userAttributes[1]);
                creditLabel.setText(creditLabel.getText() + "\n" + userAttributes[2]);
                totalSpendLabel.setText(totalSpendLabel.getText() + "\n" + userAttributes[3]);
                cardDetailsLabel.setText(cardDetailsLabel.getText() + "\n" + userAttributes[4]);
            }

        } catch (IOException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
            e.printStackTrace(); // Handle the exception properly in your application
        }
    }
}