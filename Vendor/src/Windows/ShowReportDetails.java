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

public class ShowReportDetails {
	public void reportDetailsWindow() {
        // Create a new stage for the Add Credit window
        Stage reportWindow = new Stage();
        reportWindow.initModality(Modality.APPLICATION_MODAL);
        reportWindow.setTitle("Machine Status Report");
        reportWindow.getIcons().add(new Image(getClass().getResourceAsStream("../logo.png")));

        // labels to display machine details from text file
        Label txtLabel1 = new Label("");
        Label txtLabel2 = new Label("");
        Label txtLabel3 = new Label("");
        Label txtLabel4 = new Label("");
        Label txtLabel5 = new Label("");
        
        txtLabel1.getStyleClass().add("report-window-text");
        txtLabel2.getStyleClass().add("report-window-text");
        txtLabel3.getStyleClass().add("report-window-text");
        txtLabel4.getStyleClass().add("report-window-text");
        txtLabel5.getStyleClass().add("report-window-text");
        
        Button closeButton = new Button("close");

        closeButton.setOnAction(e -> {reportWindow.close();});
        
        // Create a layout for the Add Credit window
        VBox reportLayout = new VBox(10);
        HBox buttonBox = new HBox(10, closeButton);
        buttonBox.setAlignment(Pos.CENTER);
        
        reportLayout.getChildren().addAll(txtLabel1, txtLabel2, txtLabel3, txtLabel4, txtLabel5, buttonBox);
        reportLayout.setAlignment(Pos.CENTER);
        reportLayout.setPadding(new Insets(10));
        reportLayout.getStyleClass().add("window");
        
        // Set the scene for the new stage
        Scene reportScene = new Scene(reportLayout, 350, 225);
        reportScene.getStylesheets().add(getClass().getResource("../Styles.css").toExternalForm());
        reportWindow.setScene(reportScene);
        
        // Read and display machine report data
        readMachineReport(txtLabel1, txtLabel2, txtLabel3, txtLabel4, txtLabel5);
        // Show the Add Credit window
        reportWindow.show();
    }
 
 	// reads in data from MachineStatus.txt file
	private void readMachineReport(Label txtLabel1, Label txtLabel2, Label txtLabel3, Label txtLabel4, Label txtLabel5) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("MachineStatus.txt"));

            // Read data from the file and update labels
            String line;
            int labelIndex = 1;
            while ((line = reader.readLine()) != null && labelIndex <= 6) {
                switch (labelIndex) {
                    case 1:
                        txtLabel1.setText(line);
                        break;
                    case 2:
                        txtLabel2.setText(line);
                        break;
                    case 3:
                        txtLabel3.setText(line);
                        break;
                    case 4:
                        txtLabel4.setText(line);
                        break;
                    case 5:
                        txtLabel5.setText(line);
                        break;
                }
                labelIndex++;
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
