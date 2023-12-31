package Main;
import Users.Guest;
import Users.Maintenance;
import Windows.FAQWindow;
import Windows.ReportErrorWindow;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;

public class Home extends Scene {
	
    public Home(Main vendingMachine) {
        super(new VBox(10), 600, 450);
        VBox homeLayout = (VBox) this.getRoot();
        FAQWindow faqWindow = new FAQWindow();
        ReportErrorWindow errorWindow = new ReportErrorWindow();
        
        homeLayout.prefWidthProperty().bind(homeLayout.widthProperty());
        homeLayout.prefHeightProperty().bind(homeLayout.heightProperty());
        
        // Adding CSS styles to the scene
        homeLayout.getStylesheets().add(getClass().getResource("../Styles.css").toExternalForm());
        homeLayout.getStyleClass().add("home-scene");
        
        // Button for reporting errors
        Button reportButton = new Button("!");
        reportButton.getStyleClass().add("report-button");
        reportButton.setOnAction(e -> {errorWindow.reportErrorWindow(vendingMachine);});
        
        // REPORT TOOLTIP
        Tooltip tooltip = new Tooltip("Report an Error!");
        Tooltip.install(reportButton, tooltip);
        
        
        Button helpButton = new Button("?");
        helpButton.getStyleClass().add("report-button");
        helpButton.setOnAction(e -> {faqWindow.openFAQWindow();});
        
        // FAQ TOOLTIP
        Tooltip helpTooltip = new Tooltip("FAQ's");
        Tooltip.install(helpButton, helpTooltip);
        HBox helpBox = new HBox(5,reportButton, helpButton);
        helpBox.setAlignment(Pos.TOP_RIGHT);
        
        // Setting the welcome text in the home scene
        Text welcomeText = new Text("Welcome to the Vending Machine App!");
        welcomeText.getStyleClass().add("welcome-text");
        
        HBox welcomeBox = new HBox(10, welcomeText);
        welcomeBox.setAlignment(Pos.CENTER);
        welcomeBox.getStyleClass().add("welcome-box");
        
        // Initializing the scene buttons
        Button signUpButton = new Button("Sign Up");
        Button loginButton = new Button("Log in");
        Button guestButton = new Button("Continue as Guest");
        Button repairButton = new Button("Maintenance");
        
        homeLayout.setPadding(new Insets(10));
        
        // Brings the user to the sign up scene
        signUpButton.setOnAction(e -> {
            vendingMachine.showSignUpPage();
        });
        
        // Brings the user to the login scene
        loginButton.setOnAction(e -> {
            vendingMachine.showLoginPage();
        });
        
        // logs the user in as a guest
        guestButton.setOnAction(e -> {
        	Guest guestUser = new Guest();
        	vendingMachine.login(guestUser);
        });
        
        // Logs the user in as maintenance
        repairButton.setOnAction(e -> {
        	Maintenance maintenanceUser = new Maintenance("Maintenance", "");
        	vendingMachine.login(maintenanceUser);
        });
       
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(signUpButton, loginButton, guestButton, repairButton);
        
        
        
        homeLayout.getChildren().addAll(helpBox, welcomeBox,  buttonBox);
    }
}