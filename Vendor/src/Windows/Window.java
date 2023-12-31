package Windows;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Window {
	// Opens an alert window to prompt the user
    public void openWindow(String title, String label, String buttonText) {
        // Create a new stage for the window
        Stage windowStage = new Stage();
        windowStage.getIcons().add(new Image(getClass().getResourceAsStream("../logo.png")));
        windowStage.initModality(Modality.APPLICATION_MODAL);
        
        windowStage.setTitle(title);
        Label windowLabel = new Label(label);
        windowLabel.getStyleClass().add("window-text");
        Button confirmButton = new Button(buttonText);

        confirmButton.setOnAction(e -> {
        	windowStage.close();
        });
        VBox windowLayout = new VBox(10);
        windowLayout.getChildren().addAll(windowLabel, confirmButton);
        windowLayout.setAlignment(Pos.CENTER);
        windowLayout.getStyleClass().add("window");

        // Set the scene for the new stage
        Scene windowScene = new Scene(windowLayout, 300, 150);
        windowScene.getStylesheets().add(getClass().getResource("../Styles.css").toExternalForm());
        windowStage.setScene(windowScene);
        windowStage.show();
    }
}
