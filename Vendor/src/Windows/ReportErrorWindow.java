package Windows;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Main.Main;

public class ReportErrorWindow {
	private ObservableList<String> errorOptions = FXCollections.observableArrayList(
    	    "Out of drinks",
    	    "Won't take coins",
    	    "Other"
    	);
	
	 public void reportErrorWindow(Main vendingMachine) {
	        // Create a new stage for the Add Credit window
	        Stage reportWindow = new Stage();
	        Window window = new Window();
	        reportWindow.initModality(Modality.APPLICATION_MODAL);
	        reportWindow.setTitle("Report Eerror");
	        reportWindow.getIcons().add(new Image(getClass().getResourceAsStream("../logo.png")));
	        // Create the content for the Add Credit window
	        Label reportLabel = new Label("Describe the error you encounted");
	        reportLabel.getStyleClass().add("window-label");
	        
	        Button reportButton = new Button("Report");
	        Button closeButton = new Button("close");
	        
	        ComboBox<String> errorBox = new ComboBox<>(errorOptions);
	        
	        TextField errorTextField = new TextField();
	        
	        errorTextField.setPromptText("Describe the error");
	        errorTextField.getStyleClass().add("report");
	        
	        // Initially hide the TextField for "Other" option
	        errorTextField.setVisible(false);

	        errorBox.setOnAction(e -> {
	            if ("Other".equals(errorBox.getValue())) {
	                errorTextField.setVisible(true);
	            } else {
	                errorTextField.setVisible(false);
	            }
	        });
	        
	        reportButton.setOnAction(e -> {
	        								String selectedError = errorBox.getValue();
	        								if(selectedError != null){
	        									String errorDescription = selectedError.equals("Other") ? errorTextField.getText() : selectedError;
		        								boolean status = true;
		        								window.openWindow("Report sent!", "Report has been sent to maintenance,\n\t    machine is out of service!", "Close");
		        						        // Update the machine status based on the report
		        						        vendingMachine.setMachineStatus(status);
		        						        vendingMachine.setMachineErrorCount();
		        								generateErrorReport(errorDescription);
		        								reportWindow.close();
	        									}
	        								});
	        
	        closeButton.setOnAction(e -> {reportWindow.close();});
	        
	        // Create a layout for the Add Credit window
	        VBox reportLayout = new VBox(10);
	        HBox buttonBox = new HBox(10, reportButton, closeButton);
	        buttonBox.setAlignment(Pos.CENTER);
	        
	        reportLayout.getChildren().addAll(reportLabel, errorBox, buttonBox, errorTextField );
	        reportLayout.setAlignment(Pos.CENTER);
	        reportLayout.setPadding(new Insets(10));
	        reportLayout.getStyleClass().add("window");
	        
	        // Set the scene for the new stage
	        Scene reportScene = new Scene(reportLayout, 400, 250);
	        reportScene.getStylesheets().add(getClass().getResource("../Styles.css").toExternalForm());
	        reportWindow.setScene(reportScene);

	        // Show the Add Credit window
	        reportWindow.show();
	    }
	 
	 	// Generates an error report for the ErrorReport.txt file
	    private void generateErrorReport(String errorText) {
	    	LocalDateTime currentDateTime = LocalDateTime.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        
	        String Date = currentDateTime.format(formatter);
	    	try {
	        	BufferedWriter writer = new BufferedWriter(new FileWriter("ErrorReport.txt", true));
	        	writer.write("\n\t\t======== ERROR REPORT ========\n");
	            writer.write("\t\t==== " + Date + " =====\n\n");
	            writer.write("\t\t\t  " + errorText + "\n\n");
	            writer.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    	
	    }
}
