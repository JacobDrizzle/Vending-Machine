package Main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Windows.Window;

public class SignUp extends Scene {
	private Main vendingMachine;
	
    public SignUp(Main vendingMachine) {
        super(new VBox(10), 750, 650);
        this.vendingMachine = vendingMachine;
        Window window = new Window();
        
        VBox signUpLayout = (VBox) this.getRoot();
        Text signUpText = new Text("Vending Machine SignUp");
        signUpText.getStyleClass().add("order-text");
        
        // Scale the scene and objects within to window size
        signUpLayout.prefWidthProperty().bind(signUpLayout.widthProperty());
        signUpLayout.prefHeightProperty().bind(signUpLayout.heightProperty());
        signUpLayout.getStylesheets().add(getClass().getResource("../Styles.css").toExternalForm());
        signUpLayout.getStyleClass().add("signUp-Box");
        
        // Create grid pane
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Create form components
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter 4-digit PIN");
        passwordField.setOnMouseClicked(e -> {
            // Reset border color when the user clicks on the field
            passwordField.getStyleClass().remove("error");
        });
        
        DatePicker dobPicker = new DatePicker();
        dobPicker.setPromptText("Select date of birth");
        
        dobPicker.setEditable(false); 
        dobPicker.setOnMouseClicked(e -> {
            // Reset border color when the user clicks on the field
            dobPicker.getStyleClass().remove("error");
        });
        
        ComboBox<String> genderComboBox = new ComboBox<>();
        genderComboBox.getItems().addAll("Male", "Female", "Other");
        genderComboBox.setPromptText("Select gender");

        TextField emailField = new TextField();
        emailField.setPromptText("Enter email address");
        
        emailField.setOnMouseClicked(e -> {
            // Reset border color when the user clicks on the field
            emailField.getStyleClass().remove("error");
        });
        
        TextField mobileField = new TextField();
        mobileField.setPromptText("Enter mobile number");

        TextArea addressArea = new TextArea();
        addressArea.setPromptText("Enter address (up to 3 lines)");
        
        // Create the country ComboBox
        ComboBox<String> countryBox = new ComboBox<>();
        countryBox.setPromptText("Select country");
        loadText(countryBox, "Countries.txt");
        countryBox.setPromptText("Enter country");
        countryBox.setOnKeyPressed(event -> handleComboBoxKeyPress(event, countryBox));
        
        
        // Create the county ComboBox
        Label countyLabel = new Label("County:");
        ComboBox<String> countyBox = new ComboBox<>();
        countyBox.setPromptText("Select county");
        loadText(countyBox, "Counties.txt");
        countyBox.setOnKeyPressed(event -> handleComboBoxKeyPress(event, countyBox));
        
        // If Ireland is selected in the country box add the county box to the scene
        countryBox.setOnAction(event -> {
            String selectedCountry = countryBox.getValue();
            if ("Ireland".equals(selectedCountry)) {
                grid.add(countyLabel, 0, 9);
                grid.add(countyBox, 1, 9);
            } else {
                grid.getChildren().removeAll(countyLabel, countyBox);
            }
        });
        
        CheckBox termsCheckBox = new CheckBox("I accept the terms and conditions");
        
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        
        // When the user clicks the username field after inputing invalid details
        // reset the style of all element
        usernameField.setOnMouseClicked(e -> {
            // Reset border color when the user clicks on the field
            usernameField.getStyleClass().remove("error");
            passwordField.getStyleClass().remove("error");
            dobPicker.getStyleClass().remove("error");
            emailField.getStyleClass().remove("error");
            termsCheckBox.getStyleClass().remove("error");
        });
        
        
        // Add components to the grid
        grid.add(new Label("Username:*"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("PIN:*"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Date of Birth:*"), 0, 3);
        grid.add(dobPicker, 1, 3);
        grid.add(new Label("Gender:*"), 0, 4);
        grid.add(genderComboBox, 1, 4);
        grid.add(new Label("Email:*"), 0, 5);
        grid.add(emailField, 1, 5);
        grid.add(new Label("Mobile Number:"), 0, 6);
        grid.add(mobileField, 1, 6);
        grid.add(new Label("Address:"), 0, 7);
        grid.add(addressArea, 1, 7);
        grid.add(new Label("Country:"), 0, 8);
        grid.add(countryBox, 1, 8);
        grid.add(termsCheckBox, 0, 10, 2, 1);

        VBox gridBox = new VBox(10, signUpText, grid );
        gridBox.getStyleClass().add("signUp-textBox");
        
        gridBox.setAlignment(Pos.CENTER);
        signUpLayout.setAlignment(Pos.CENTER);
        
        Button signUpButton = new Button("Sign Up");
        
        // Sign up button functionality
        // Store the users details and validate, if details are incorrect add error styling
        // and set the prompt text to alert the user of invalid details
        signUpButton.setOnAction(e -> {
        	
        	// local variables to store the entered details
            String username = usernameField.getText();
            String password = passwordField.getText();
            String email = emailField.getText();
            LocalDate dob = dobPicker.getValue();
            
            // Validate input
            // if username is less than 3 letters alert the user and set error styles
            if (username.length() < 3) {
            	usernameField.setPromptText("Username must be at least 3 characters");
                usernameField.getStyleClass().add("error"); // Add a CSS class for styling
            }
            
            // if username exists in the DB alert the user
            if (usernameExists(username)) {
            	usernameField.setPromptText("Username already exists");
                usernameField.getStyleClass().add("error"); // Add a CSS class for styling
            }
            
            // if password length is not 4 char long set error styles and alert the user
            if (password.length() != 4) {
            	passwordField.setPromptText("PIN must be exactly 4 digits");
                passwordField.getStyleClass().add("error");
            }
            
            // if email is incorrect format alert the user
            if (!isValidEmail(email)) {
            	emailField.setPromptText("Invalid email format");
                emailField.getStyleClass().add("error");
            }
            
            // if age is less than 18 prompt the user they must be 18+
            if (!is18OrOlder(dob)) {
            	dobPicker.setPromptText("Must be 18 years or older");
                dobPicker.getStyleClass().add("error");
            }
            
            // if the terms & conditions are not selected alert the user
            if(!termsCheckBox.isSelected()) {
            	termsCheckBox.setText("Please agree to the terms & conditions");
                termsCheckBox.getStyleClass().add("error");
            }
            
            // if all details are valid save to the usersDB.txt file and open a window 
            // alerting the user of a successful registration
            if (isValidInput(username, password, email, dob)&& saveToDatabase(username, password)) {
            	window.openWindow("Registration success", "Congratulations Registration was successful!\n\t\t Please log in to proceed", "Close");
            	vendingMachine.showHomePage();
            } 
        });
        
        // Back butto to send the user back to home page
        Button backButton = new Button("Back to Home");
        backButton.setOnAction(e -> {
        	// Switch to the home scene
        	vendingMachine.showHomePage();
        });
        
        // Box to display button on the scene
        HBox buttonBox = new HBox(10, signUpButton, backButton);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);
        buttonBox.getStyleClass().add("signup-buttons");
        
        // Box to hold all elements in the scene
        VBox signUpBox = new VBox(10, gridBox, buttonBox);
        signUpBox.getStyleClass().add("signUp"); 
        
        signUpLayout.getChildren().addAll(signUpBox);
    }
    
    // Validates user details if successful returns true
    private boolean isValidInput(String username, String password, String email, LocalDate dob) {
        // Return true if the input is valid, otherwise return false.
    	return username.length() >= 3 && password.length() == 4 && isValidEmail(email) && is18OrOlder(dob);
    }
    
    // method to load text from the countrys and counties text files.
    private void loadText(ComboBox<String> comboBox, String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                comboBox.getItems().add(line);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception properly in your application
        }
    }
    
    // method to highlight countries that start with the letter pressed
    private void handleComboBoxKeyPress(KeyEvent event, ComboBox<String> comboBox) {
        if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
            // If the pressed key is a letter or a digit, search and select the item starting with that character
            String key = event.getText();
            if (key != null && !key.isEmpty()) {
                for (String item : comboBox.getItems()) {
                    if (item.toLowerCase().startsWith(key.toLowerCase())) {
                        comboBox.getSelectionModel().select(item);

                        // Scroll to the selected item
                        scrollToSelected(comboBox);

                        break;
                    }
                }
            }
        }
    }
    
    // method to scroll to the selected item in the ComboBox
    private void scrollToSelected(ComboBox<String> comboBox) {
        ListView<?> list = (ListView<?>) ((ComboBoxListViewSkin<?>) comboBox.getSkin()).getPopupContent();
        list.scrollTo(Math.max(0, comboBox.getSelectionModel().getSelectedIndex()));
    }
    // Check if a username already exists in the usersDB.txt file 
    private boolean usernameExists(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader("usersDB.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1) {
                    String existingUsername = parts[0].trim();
                    if (username.equals(existingUsername)) {
                        return true; // Username already exists
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception properly in your application
        }
        return false; // Username does not exist
    }
    
    // Validates the users email against a regex
    private boolean isValidEmail(String email) {
    	// email validation logic using a regular expression
    	final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
    	Pattern pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

    	Matcher matcher = pattern.matcher(email);
    	return matcher.matches();
    }
    
    // Method to validate the users DOB 
    private boolean is18OrOlder(LocalDate dob) {
    	if (dob == null) {
    		System.out.println("Please enter a date of birth!");
    		return false;
    	} else {
	        LocalDate currentDate = LocalDate.now();
	        Period age = Period.between(dob, currentDate);
	        return age.getYears() >= 18;
    	}
    }

    // Save user data to usersDB.txt
    private boolean saveToDatabase(String username, String password) {
        try {
        	 // Check if the username already exists
            if (usernameExists(username)) {
                System.out.println("Username already exists. Please choose another one.");
                return false;
            }
        	BufferedWriter writer = new BufferedWriter(new FileWriter("usersDB.txt", true));
            writer.write(username + "," + password + "," + 0.0 + "," + 0.0 + "," + false);
            writer.newLine();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred while saving user data. Please try again.");
            return false;
        }
    }
}