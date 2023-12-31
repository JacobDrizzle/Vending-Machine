package Windows;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FAQWindow {
    public void openFAQWindow() {
        // Create a new stage for the FAQ window
        Stage faqWindow = new Stage();
        faqWindow.initModality(Modality.APPLICATION_MODAL);
        faqWindow.setTitle("FAQS");
        faqWindow.getIcons().add(new Image(FAQWindow.class.getResourceAsStream("../logo.png")));
        
        Text faqText = new Text("Vending Machine FAQ'S");
        faqText.getStyleClass().add("faq-lable");
        
        VBox innerBox = new VBox(10, faqText);
        innerBox.setAlignment(Pos.CENTER);
        // Create the content for the FAQ window using VBox
        VBox faqLayout = new VBox(5);

        // Create and add each question and answer pair
        addQuestionAnswerPair(innerBox, "Q. Do I need an account to purchase drinks?", "A. You do not need an account to purchase drinks");
        addQuestionAnswerPair(innerBox, "Q. Does the machine give change to guests?", "A. The machine does not give change to guest users");
        addQuestionAnswerPair(innerBox, "Q. How can I report an error if the machine is broken?", "A. To report an error, please press the ! button");

        Button closeButton = new Button("Close");

        closeButton.setOnAction(e -> {
            faqWindow.close();
        });

        HBox buttonBox = new HBox(15, closeButton);
        buttonBox.setAlignment(Pos.CENTER);
        
        innerBox.getStyleClass().add("faqBox"); 
        innerBox.getChildren().addAll(buttonBox);
        
        faqLayout.getChildren().addAll(innerBox);
        faqLayout.setAlignment(Pos.CENTER);
        
        faqLayout.setPadding(new Insets(10));
        faqLayout.getStyleClass().add("window");

        // Set the scene for the new stage
        Scene faqScene = new Scene(faqLayout, 400, 300);
        faqScene.getStylesheets().add(FAQWindow.class.getResource("../Styles.css").toExternalForm());
        faqWindow.setScene(faqScene);

        // Show the FAQ window
        faqWindow.show();
    }


    private void addQuestionAnswerPair(VBox layout, String question, String answer) {
        Label questionLabel = new Label(question);
        questionLabel.getStyleClass().add("faq-text");
        VBox pairLayout = new VBox(5);
        Label answerLabel = new Label(answer);
        answerLabel.getStyleClass().add("faq-text");
        answerLabel.setVisible(false); // Initially hide the answer

        // Toggle the visibility of the answer when the question is clicked
        questionLabel.setOnMouseClicked(e -> {
        	if(answerLabel.isVisible()) {
        		pairLayout.getChildren().remove(answerLabel);
        		answerLabel.setVisible(!answerLabel.isVisible());
        	}else {
	            pairLayout.getChildren().add(answerLabel);
	            answerLabel.setVisible(!answerLabel.isVisible());
        	}
        });
        pairLayout.getChildren().add(questionLabel);
        pairLayout.getStyleClass().add("faq-text");
        pairLayout.setPadding(new Insets(5));

        layout.getChildren().add(pairLayout);
    }
}