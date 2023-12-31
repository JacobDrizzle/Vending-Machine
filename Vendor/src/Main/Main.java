package Main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import Orders.Orders;
import Users.User;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	  private Stage primaryStage; // Initialize the PrimaryStage
	  private Home homeScene; // Import the HomeScene
	  private Login loginScene; // Import the LoginScene
	  private SignUp signUpScene; // Import the SignupScene
	  private Orders orderScene; // Import the OrderScene
	  private User loggedInUser; // Stores the logged-in user
	  private boolean loggedIn = false; // Checks for logged in user
	  private double totalEarnings; // Keeps track of the machine earnings 
	  private double lifeTimeEarnings; // Keeps track of the machine earnings taken by maintenance

	    @Override
	    public void start(Stage primaryStage) {
	        this.primaryStage = primaryStage;
	        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../logo.png")));
	        // Initialize the scenes
	        loginScene = new Login(this);
	        homeScene = new Home(this);
	        signUpScene = new SignUp(this);
	        orderScene = new Orders(this);
	        try {
	            BufferedReader br = new BufferedReader(new FileReader("MachineStatus.txt"));
	            String line;
	            while ((line = br.readLine()) != null) {
	                String[] parts = line.split(":");
	                if (parts.length == 2) {
	                    String part = parts[0].trim();
	                    String status = parts[1].trim();
	                    System.out.println(part + ":" + status); // Debug output
	                }
	            }
	            br.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        // Set the initial scene to the home page
	        showHomePage();

	        primaryStage.setTitle("Vending Machine App");
	        primaryStage.show();
	    }
	    
	    // returns the user
	    public boolean isLoggedIn() {
	        return loggedIn;
	    }
	    
	    // method to log in as a user and proceed to orders scene
	    public boolean login(User user) {
	        loggedIn = true;
	        loggedInUser = user; // Store the logged-in user
	        orderScene = new Orders(this); // Initialize the Orders scene after setting the user
	        showOrderPage();
	        return loggedIn;
	    }
	    
	    // method to continue to the orders scene as a guest
	    public void continueAsGuest() {
	        loggedIn = false;
	        loggedInUser = null; // Clear the logged-in user
	        orderScene = new Orders(this); // Initialize the Orders scene with no user
	        showOrderPage();
	    }
	    
	    // method to get currently logged in user
	    public User getLoggedInUser() {
	        return loggedInUser;
	    }
	    
	    // method to log the user out
	    public void logout() {
	        loggedIn = false;
	        System.out.println(loggedInUser.getUsername() + " Logged out!");
	        loggedInUser = null; // Clear the logged-in user
	    }
	    
	    // method to load the login scene
	    public void showLoginPage() {
	        primaryStage.setScene(loginScene);
	    }
	    
	    // method to load the orders scene
	    public void showOrderPage() {
	    	 orderScene.setUser(loggedInUser);
	         primaryStage.setScene(orderScene); 
	    }
	    
	    // method to load the home scene
	    public void showHomePage() {
	        primaryStage.setScene(homeScene);
	    }
	    
	    // method to load the sign up scene
	    public void showSignUpPage() {
	        primaryStage.setScene(signUpScene);
	    }
	    
	    // Method to read data from a given text file
	    public String readFromFile(String fileName, String Line) {
	    	try {
	            BufferedReader reader = new BufferedReader(new FileReader(fileName));
	            String line;
	            while ((line = reader.readLine()) != null) {
	                if (line.startsWith(Line)) {
	                    return line.substring(Line.length());
	                }
	            }
	            reader.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	            System.out.println("Error reading from " + fileName);
	            return null;
	        }
	    	 return null;
	    }
	    
	    // method to get the total earnings withdraw by maintenance
	    public double getLifeTimeEarnings() {
	    	String response = readFromFile("MachineStatus.txt", "LIFETIME-EARNINGS:");
	    	return response != null ? Double.parseDouble(response) : 0.0;
	    }
	    
	    // method to get the total earnings ready for maintenance to withdraw
	    public double getTotalEarnings() {
	    	String response = readFromFile("MachineStatus.txt", "EARNINGS:");
	    	return response != null ? Double.parseDouble(response) : 0.0;
	    }
	    
	    // method to check if machine is out of order
	    public boolean getMachineStatus() {
	    	String response = readFromFile("MachineStatus.txt", "ERROR-STATUS:");
	    	return response != null ? Boolean.parseBoolean(response) : false;
	    }
	    
	    // method returns the amount of errors the machine has had in its lifetime
	    public int getMachineErrorCount() {
	        String response = readFromFile("MachineStatus.txt", "ERROR-COUNT:");
	        return response != null ? Integer.parseInt(response) : 0;
	    }
	    
	    // method to write data to a file
	    public <T> void writeToFile(String fileName, String Line, T value) {
	        try {
    	        BufferedReader reader = new BufferedReader(new FileReader(fileName));
    	        StringBuilder fileContents = new StringBuilder();
    	        String line;
    	        while ((line = reader.readLine()) != null) {
    	            if (line.startsWith(Line)) {
    	                line = Line + value;
    	            }
    	            fileContents.append(line).append("\n");
    	        }
    	        reader.close();

    	        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
    	        writer.write(fileContents.toString());
    	        writer.close();
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	        System.out.println("Error writing to " + fileName);
    	    }
	    }
	    
	    // method to set total earnings ready to be withdrawn by maintenance
	    public void setTotalEarnings(double amount) {
	        double earnings = getTotalEarnings();
	        earnings += amount;
	        writeToFile("MachineStatus.txt", "EARNINGS:", earnings);
	        System.out.println("Total Earnings: " + this.totalEarnings);
	    }
	    
	    // method to set the total earnings withdrawn by maintenance
	    public void setLifeTimeEarnings(double amount) {
	        double earnings = getLifeTimeEarnings() + amount;
	        writeToFile("MachineStatus.txt", "LIFETIME-EARNINGS:", earnings);
	        System.out.println("LIFETIME-EARNINGS: " + this.lifeTimeEarnings);
	    }

	    // method to set the total amount of errors the machine has encountered 
	    public void setMachineErrorCount() {
	    	int count = getMachineErrorCount();
	    	count++;
	    	writeToFile("MachineStatus.txt", "ERROR-COUNT:", count);
	    }
	    
	    // method to set the machine status
	    public void setMachineStatus(boolean status) {
	    	writeToFile("MachineStatus.txt", "ERROR-STATUS:", status);
	    }
	    
	    // method to set the last service date
	    public void setLastService(String date) {
	    	writeToFile("MachineStatus.txt", "LAST-SERVICE:", date);
	    }
	    
	    // Starts the application
	    public static void main(String[] args) {
	        launch(args);
	    }
	}