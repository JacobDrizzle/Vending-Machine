package Orders;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import Main.Main;
import Users.Guest;
import Users.Maintenance;
import Users.User;
import Windows.FAQWindow;
import Windows.Window;
import Windows.CardDetailsWindow;
import Windows.AddCreditWindow;
import Windows.ReportErrorWindow;
import Windows.ShowReportDetails;
import Windows.UserReport;

public class Orders extends Scene {
    private VBox drinkList;
    private Cart cart;
    private Main vendingMachine;
    private Label cartLabel;
    private Label totalCostLabel;
    private Label creditLabel;
    private Label earningsLabel;
    private Label userLabel;
    private Label[] drinksLabels = new Label[4];
    private User user;
    private Drinks[] drinkArray = new Drinks[4];
    private double Earnings;
    private Map<String, Integer> drinkCounts = new HashMap<>();

    // START OF SCENE
    
    public Orders(Main vendingMachine) {
        super(new VBox(10), 650, 420);
        
        // Initializing Scene elements
        this.vendingMachine = vendingMachine;
        FAQWindow faqWindow = new FAQWindow();
        Window window = new Window();
        AddCreditWindow creditWindow = new AddCreditWindow();
        ReportErrorWindow errorWindow = new ReportErrorWindow();
        CardDetailsWindow detailsWindow = new CardDetailsWindow();
        ShowReportDetails reportDetails = new ShowReportDetails();
        UserReport userReport = new UserReport();
        
        this.user = vendingMachine.getLoggedInUser();
        cart = new Cart(); 
        setUser(user);
        HBox infoLabels = new HBox(10);
        VBox headerBox = new VBox(10);
        
        // Checks for a user and loads appropriate elements to the scene
        if (user != null) {
        	userLabel = new Label();
        	userLabel.setText("User: " + user.getUsername());
        	headerBox.getChildren().add(userLabel);
            if (user instanceof Guest) {
                // Guest user logic
                System.out.println("Guest user is logged in.");
                creditLabel = new Label("Total Credit: €" + user.getCredit());
            } else {
                // Regular user logic
            	double credit =  user.getCredit();
            	
            	// If user a user has saved card details && credit is below €2 opens an alert to warn the user of low credit
            	if (credit < 2 && user.getCardDetails()) {
            		window.openWindow("Credit Low", "\t\tWelcome " + user.getUsername() + "!\nCredit below €2, Please add credit", "Close");
            	}
            	
            	String formattedCredit = String.format("%.2f",credit);
                System.out.println("Logged in as: " + this.user.getUsername() + " Credit: " + this.user.getCredit() + " TotalSpent: " + this.user.getTotalSpend());
                creditLabel = new Label("Total Credit: €" + formattedCredit);
            }
        } else {
            System.out.println("Logged in as: Maintenance" );
            creditLabel = new Label("");
            // Handle the case w
        }
         
        VBox orderLayout = (VBox) this.getRoot();
        
        orderLayout.prefWidthProperty().bind(orderLayout.widthProperty());
        orderLayout.prefHeightProperty().bind(orderLayout.heightProperty());
        orderLayout.getStylesheets().add(getClass().getResource("../Styles.css").toExternalForm());
        orderLayout.getStyleClass().add("order-scene");
        orderLayout.setPadding(new Insets(10));
        
        cartLabel = new Label("Cart: 0 items"); // Initialize the cartLabel
        totalCostLabel = new Label("Total Cost: €0.00"); // Initialize the total cost label 

        // Button for reporting errors
        Button reportButton = new Button("!");
        reportButton.getStyleClass().add("report-button");
        reportButton.setOnAction(e -> {errorWindow.reportErrorWindow(vendingMachine);});
        
        // REPORT TOOLTIP
        Tooltip tooltip = new Tooltip("Report an Error!");
        Tooltip.install(reportButton, tooltip);
        
        // Button for FAQ'S
        Button helpButton = new Button("?");
        helpButton.getStyleClass().add("report-button");
        helpButton.setOnAction(e -> {faqWindow.openFAQWindow();});
        HBox helpBox = new HBox(5,reportButton, helpButton);
        helpBox.setAlignment(Pos.TOP_RIGHT);
        
        // FAQ TOOLTIP
        Tooltip helpTooltip = new Tooltip("FAQ's");
        Tooltip.install(helpButton, helpTooltip);
        
        // Format the total earnings for display in the label
        String formattedEarnings = String.format("%.2f",vendingMachine.getTotalEarnings());
        
        // Labels to display the total earnings and total users for Maintenance
        earningsLabel = new Label("Total Earnings: €" + formattedEarnings);
        
        Button maintenanceReportButton = new Button("Status Report!");
        maintenanceReportButton.setOnAction(e -> {reportDetails.reportDetailsWindow();});

        
        Button userReportButton = new Button("User Report!");
        userReportButton.setOnAction(e -> {userReport.userReportWindow();});
        // Adding elements to the scene for either Maintenance or normal Users
        if (user instanceof Maintenance) {
        	infoLabels.getChildren().addAll(userReportButton, earningsLabel, maintenanceReportButton);
        } else  {
        	infoLabels.getChildren().addAll(cartLabel, totalCostLabel, creditLabel);
        }
        // Adding styles to the infoLabels
        infoLabels.getStyleClass().add("order-labels");
        infoLabels.setAlignment(Pos.TOP_CENTER);
        
        drinkList = new VBox(10);
        
        // Read drink properties from text file and add them to the drinkArray
        try (BufferedReader br = new BufferedReader(new FileReader("Drinks.txt"))) {
            String line;
            int index = 0;

            while ((line = br.readLine()) != null && index < drinkArray.length) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0].trim();
                    double price = Double.parseDouble(parts[1].trim());
                    int quantity = Integer.parseInt(parts[2].trim());

                    drinkArray[index] = new Drinks(name, price, quantity);
                    index++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Function to add the drinks to the scene
        addDrinksToScene();
        
        // Button to send the user to the home page
        Button backButton = new Button("Back to Home");
        backButton.setOnAction(e -> {
        	if (user instanceof Guest) 
        		vendingMachine.setTotalEarnings(user.getCredit());
        	
            vendingMachine.showHomePage();
            vendingMachine.logout();
        });
        
        // Initialize the checkout button
        Button checkoutButton = new Button();
        
        // If the machine is out of order change the text of the button
        if (vendingMachine.getMachineStatus()){
    		checkoutButton.setText("Requires maintenance"); // Update the button text
    		checkoutButton.setDisable(true); // Disable the button
    	} else {
    		checkoutButton.setText("Checkout");
    	}
        
        // Checkout button functionality
        // If cart is empty open a window asking the user to add items to the cart.
        // else calculate the total cost of all items in the cart and checks if payment was successful
        // if payment is not successful open a window to alert user of Insufficient balance.
        checkoutButton.setOnAction(e -> {
							            if (cart.getItems().isEmpty()) {
							                // Display an error message because the cart is empty
							                System.out.println("Cart is empty. Please select drinks to order.");
							                window.openWindow("Cart empty", "Please add items to your cart", "Continue");
							            } else {
							                double totalCost = calculateTotalCost();
							                boolean paymentSuccess;
							                paymentSuccess = checkCredit(totalCost);
							                if(!paymentSuccess) {
							                	window.openWindow("Payment Declined", "Insufficent Ballance, Please top up", "Continue");
							                } else {
							                	handlePayment(totalCost, window);
							                	// Update the data file with the new quantities
						                        updateDrinkDataFile();
							                }
							            }
							        });
        
        //Button to allow the user to add credit to the machine
        Button creditButton = new Button("Add Credit");
        
        // Credit Button Functionality.
        // If user has card details saved continue with adding credit else open a window to input card details.
        creditButton.setOnAction(e -> {if (user.getCardDetails()) {
        								creditWindow.addCredit(user, vendingMachine, this, window);
								        } else {
								        	detailsWindow.addCardDetails(user, window);
								        }});
        
        // Button for Maintenance to restock drinks
        Button restockButton = new Button("Restock");
        restockButton.setOnAction(e -> openRestockWindow());
        
        // Dispense change button for maintenance
        Button changeButton = new Button("Dispense change");
        changeButton.setOnAction(e -> {
        								if(vendingMachine.getTotalEarnings() == 0) {
        									window.openWindow("No Change", "No change to dispense", "Close");
        								} else {
        									user.change(vendingMachine.getTotalEarnings());
        									
        								}
        });
        
        // Repair button for maintenance 
        Button repairButton = new Button("Repair!");
        
        // Repair Button Functionality
        // if the vending machine status == true the vending machine is out of order
        // clicking this button will reset the machine status(Fix the machine)
        // otherwise display an alert saying the machine does not need maintenance
        repairButton.setOnAction(e -> {
        								if(vendingMachine.getMachineStatus()) {
        										vendingMachine.setMachineStatus(false);
        										user.generateMaintenanceReport();
        										
        										LocalDateTime currentDateTime = LocalDateTime.now();
        								        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        								        
        								        String Date = currentDateTime.format(formatter);
        										vendingMachine.setLastService(Date);
        										window.openWindow("Repair Success", "The machine has been repaired", "Close");
        									}else {
        										window.openWindow("Nothing to repair", "The machine does not need servicing", "Close");
        								}});
        

        // Initialize a HBox to place the various buttons in the scene
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getStyleClass().add("order-row");
        
        // Initialize Header text and place it in a box
        Text headerText = new Text();
        headerBox.getChildren().add(headerText);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.getStyleClass().add("order-text");
        
        // Add elements to the scene depending on which user is logged in
        if (user instanceof Maintenance) { 
        	buttonBox.getChildren().addAll(repairButton, restockButton, changeButton, backButton);
        	headerText.setText("Drink Stock:");
        } else {
        	buttonBox.getChildren().addAll(checkoutButton, creditButton, backButton);
        	headerText.setText("Select a drink to order:");
        }
        
        // Parent box that takes in all scene elements
        VBox orderBox = new VBox(10,infoLabels, headerBox, drinkList, buttonBox);
        VBox.setMargin(orderBox, new Insets(20, 40, 0, 0));
        orderBox.setAlignment(Pos.CENTER);
        
        orderBox.getStyleClass().add("order-box");
        orderLayout.getChildren().addAll(helpBox, orderBox);
    }
    
    // END OF SCENE
    
    // START OF METHODS
    
    // Opens the window so maintenance can restock the machine
    private void openRestockWindow() {
        // Create a new stage for the Restock window
        Stage restockStage = new Stage();
        restockStage.getIcons().add(new Image(getClass().getResourceAsStream("../logo.png")));
        restockStage.initModality(Modality.APPLICATION_MODAL);
        restockStage.setTitle("Restock");
        
        // Create the content for the Restock window
        Label amountLabel = new Label("Choose a drink to restock:");
        amountLabel.getStyleClass().add("window-label");
        
        ToggleGroup group = new ToggleGroup();
        VBox radioButtonsContainer = new VBox(10); // Container for radio buttons
        radioButtonsContainer.getStyleClass().add("window-radio");

        // For each drink in the drinkArray create a radio button for each and add them to the scene
        for (int i = 0; i < drinkArray.length; i++) {
            if (drinkArray[i] != null) {
                String drinkInfo = drinkArray[i].getName() + " - €" + drinkArray[i].getPrice() + " (" + drinkArray[i].getQuantity() + " in stock)";
                RadioButton button = new RadioButton(drinkInfo);
                button.getStyleClass().add("window-text");
                button.setToggleGroup(group);
                radioButtonsContainer.getChildren().add(button); // Add radio buttons to the container
            }
        }
        
        // Text field for maintenance to enter how many drinks to restock
        TextField amountField = new TextField();
        amountField.setPromptText("Enter an amount to restock");
        amountField.setTextFormatter(new TextFormatter<>(this::filter));
        
        // Button to close the restock window 
        Button closeButton = new Button("close");
	    closeButton.setOnAction(e -> {restockStage.close();;});
	    
	    // Button to restock the selected drink
	    Button restockButton = new Button("Restock!");
	    
	    // Restock Button Functionality
	    // If the AmountField is not empty then store the amount text and convert to an integer,
	    // If there is a radio button selected Loop through the drinks array and check for the selected drink.
	    // Then use the drink.restock() method to validate if the restock is successful, this returns a String.
	    // Open a window to alert the user using the String response from the drink.restock() method.
	    // If the AmountField is empty set the prompt text in the amount field to alert the user.
        restockButton.setOnAction(e -> {
            // Get the text from the amountField
            String amountText = amountField.getText();

            if (!amountText.isEmpty()) {
                int restockAmount = Integer.parseInt(amountText);
                RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();

                if (selectedRadioButton != null) {
                    String selectedDrinkInfo = selectedRadioButton.getText();

                    for (int i = 0; i < drinkArray.length; i++) {
                        if (drinkArray[i] != null && selectedDrinkInfo.contains(drinkArray[i].getName())) {
                            // Increase the quantity of the selected drink
                            String response = drinkArray[i].restock(restockAmount);
                            amountField.setText("");
                            amountField.setPromptText(response);
                            // Update the data file with the new quantities
                            updateDrinkDataFile();

                            // update the display of the drink in the UI
                            updateDrinksLabel(drinkArray[i]);
                            Window window = new Window();
                            window.openWindow("Drink Restocked", response, "Close");
                            break;
                        }
                    }
                }
            } else {
            	// if amountField is empty set the prompt text to alert the user
                System.out.println("Please enter the amount to restock.");
                amountField.setPromptText("Please enter the amount to restock.");
            }
        });

        // Create a layout for the Restock window
        VBox restockLayout = new VBox(10);
        HBox buttonBox = new HBox(15, restockButton,closeButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getStyleClass().add("restock-buttons");
        
        restockLayout.getChildren().addAll(amountLabel, radioButtonsContainer, amountField, buttonBox); // Add the radio buttons container
        restockLayout.setAlignment(Pos.CENTER);
        
        restockLayout.setPadding(new Insets(10));
        restockLayout.getStyleClass().add("window");
        // Set the scene for the new stage
        Scene restockScene = new Scene(restockLayout, 350, 300);
        restockScene.getStylesheets().add(getClass().getResource("../Styles.css").toExternalForm());
        restockStage.setScene(restockScene);

        // Show the Restock window
        restockStage.show();
    }
    
    // Method to allow the user to enter only whole numbers when restocking drinks
    private Change filter(Change change) {
        String newText = change.getControlNewText();
        if (newText.matches("\\d*")) {
            return change;
        }
        return null;
    }
    
    // Calculates the total cost of the users cart
    private double calculateTotalCost() {
        double totalCost = 0.0;
        for (Drinks drink : cart.getItems()) {
        	if (user instanceof Guest) {
        		totalCost += drink.getPrice();
        	}else {
        		totalCost += drink.getPrice() - 0.10;
        	}
        }
        return totalCost;
    }
    
    private boolean checkCredit(double totalCost) {
    	 if (user.getCredit() >= totalCost) {
    		 return true;
    	 }
    	return false;
    }
    
    // Payment logic
    private boolean handlePayment(double totalCost, Window window) {
    	if (cart.getItems().isEmpty()) {
            // Error handling for empty cart
            System.out.println("Cart is empty. Please select drinks to order.");
            
            return false;
        } else {
            // Handle payment from their account
            String payment = "Payment for €" + totalCost +" successful.\n   Please take your drinks!";
            window.openWindow("Payment Success", payment, "Close");
            
            // Update session earnings for EarningsReport
            this.Earnings += totalCost;
                	
            // Clear the cart if payment is successful
            cart.clearCart();
                    
            // Update total earnings for vending machine
            vendingMachine.setTotalEarnings(totalCost);
                    
            // Get user credit, update their total spend, 
            double Credit = user.getCredit();
                    
            //deduct totalCost from user credit
            double remainder = Credit - totalCost;
                    
            // Update users total spend
            user.setTotalSpend(totalCost);
                    
            // Update user credit
            user.setCredit(remainder);
                    
            // Generate Earnings Report
            generateSalesReport();
                    
            // Update cart label to show 0 items
            updateCartLabel(); 
                    
            // Update total cost label to €0.00
            updateTotalCostLabel();
                    
            // Update credit label
            updateCreditLabel(user.getCredit());
                    
            // Return payment successful
            return true;
        }    
   }
    
    // Sets the active user for the machine
    public void setUser(User user) {
    	// Set user to logged in user
        this.user = user;
    }
    
    // Method to add drinks to the scene
    private void addDrinksToScene() {
    	// Create VBoxes for DrinkLabels
    	VBox vbox1 = new VBox(5);
    	VBox vbox2 = new VBox(5);
    	
    	// For each Drink in the drinkArray create labels for the scene
        for (int i = 0; i < drinkArray.length; i++) {
            Drinks drink = drinkArray[i];
            if (drink != null) {
            	if (user instanceof Guest) {
            		drinksLabels[i] = new Label(drink.getName() + " - €" + drink.getPrice() + " (" + drink.getQuantity() + " in stock)");
            	}else {
            		drinksLabels[i] = new Label(drink.getName() + " - €" + (drink.getPrice() - 0.10) + " (" + drink.getQuantity() + " in stock)");
            	}

                // Initialize the order button
                Button orderButton = new Button("Order");
                orderButton.getStyleClass().add("orderButton");
                
                // if machine is out of order set the button text and disable the button
                if(vendingMachine.getMachineStatus()) {
                	orderButton.setText("Out of Order");
                	orderButton.setDisable(true);
                }
                
                // Create a final variable for the lambda expression
                int finalI = i; 

                // Set the order button logic
                orderButton.setOnAction(e -> {
                    if (drink.getQuantity() > 0) {
                        drink.drinkStock();
                        
                        // Decrease the quantity in the drinkArray
                        for (int j = 0; j < drinkArray.length; j++) {
                            if (drinkArray[j] != null && drinkArray[j] == drink) {
                                drinkArray[j].decreaseQuantity();
                                break;
                            }
                        }
                        
                        // Increment the count in the map
                        String drinkName = drink.getName();
                        int currentCount = drinkCounts.getOrDefault(drinkName, 0);
                        drinkCounts.put(drinkName, currentCount + 1);
                        // if user is a guest add the drinks at full price(1.80). if user with account(1.70)
                        if (user instanceof Guest) {
                            drinksLabels[finalI].setText(drink.getName() + " - €" + drink.getPrice() + " (" + drink.getQuantity() + " in stock)");
                        } else {
                            drinksLabels[finalI].setText(drink.getName() + " - €" + (drink.getPrice() - 0.10) + " (" + drink.getQuantity() + " in stock)");
                        }
                        
                        // Add drink to the cart 
                        cart.addToCart(drink);
                        // Update cart to show newly added items
                        updateCartLabel();
                        // Update cart cost label
                        updateTotalCostLabel();
                    }else {
                        orderButton.setText("Out of Stock"); // Update the button text
                        orderButton.setDisable(true); // Disable the button
                    }
                });
                
                orderButton.setMinWidth(70);
                
                // Separate the drink labels
                if (i % 2 == 0) {
                	HBox hbox1 = new HBox(10, drinksLabels[i], orderButton);
                    vbox1.getChildren().addAll(hbox1);
                } else {
                	HBox hbox2 = new HBox(10, orderButton, drinksLabels[i]);
                	vbox2.getChildren().addAll(hbox2);
                }
                
                HBox hbox = new HBox(25, vbox1, vbox2);
                hbox.setAlignment(Pos.CENTER);
                hbox.getStyleClass().add("drink-box");

                drinkList.getChildren().add(hbox);
            }
        }
    }
    
    // Updates the drink file to keep track of stock
    private void updateDrinkDataFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Drinks.txt"))) {
            for (Drinks drink : drinkArray) {
                if (drink != null) {
                    writer.write(drink.getName() + "," + String.format("%.2f", drink.getPrice()) + "," + drink.getQuantity() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Updates the cart label
    private void updateCartLabel() {
        int itemCount = cart.getItems().size();
        cartLabel.setText("Cart: " + itemCount + " items");
    }
    
    // Update the drink labels
    private void updateDrinksLabel(Drinks drink) {
        for (int i = 0; i < drinkArray.length; i++) {
            if (drinkArray[i] != null && drinkArray[i] == drink) {
                drinksLabels[i].setText(drink.getName() + " - €" + drink.getPrice() + " (" + drink.getQuantity() + " in stock)");
                break;
            }
        }
    }
    
    // Update the credit label
    public void updateCreditLabel(double credit) {
       String formattedCredit = String.format("%.2f", credit);
       creditLabel.setText("Total Credit: €" + formattedCredit);
    }
    
    // Updates the earnings label
    public void updateEarningsLabel(double earnings) {
        String formattedEarnings = String.format("%.2f", earnings);
        earningsLabel.setText("Total Credit: €" + formattedEarnings);
     }

    // Updates the total cost label
    private void updateTotalCostLabel() {
        double totalCost = calculateTotalCost();
        totalCostLabel.setText("Total Cost: €" + String.format("%.2f", totalCost));
    }
    
    // Generates a report for the EarningsReport.txt file
    public void generateSalesReport() {
        // Calculate the count of each drink sold and store it in an array or map.

        double leftCredit, totalRevenue;
        // Calculate the total revenue generated through sales and unreturned change.
        double salesRevenue = Earnings;
        
        if (user instanceof Guest) {
        	leftCredit = user.getCredit();
        	totalRevenue = Earnings + leftCredit;
        } else {
        	leftCredit = 0;
        }
        int cartSize = cart.getItems().size();
        System.out.println("CART SIZE: " + cartSize);
        
        totalRevenue = Earnings + user.getCredit();
        
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        String Date = currentDateTime.format(formatter);
        String userName = user.getUsername();

        // save the report.
        saveReport(totalRevenue,salesRevenue, leftCredit, Date, userName, drinkCounts);
    }
    
    
    // Method to save the Earnings report
    private void saveReport(double totalRevenue, double saleRevenue, double creditRevenue, String date, String userName, Map<String, Integer> drinkCounts) {
    	double total = vendingMachine.getTotalEarnings();
    	String formattedTotal = String.format("%.2f", total);
    	
    	double sales = saleRevenue;
    	String formattedSales = String.format("%.2f", sales);
    	
    	double credit = creditRevenue;
    	String formattedCredit = String.format("%.2f", credit);
        try {
        	BufferedWriter writer = new BufferedWriter(new FileWriter("EarningsReport.txt", true));
        	writer.write("\n\t\t=== Vending Machine Report ===\n");
            writer.write("\t\t==== " + date + " =====\n");
            writer.write("\n\t\t======== DRINKS SOLD =========\n");
            
            writer.write("\n\t\t\tUser\t\t : " + userName +  "\n");
            
            for (Map.Entry<String, Integer> entry : drinkCounts.entrySet()) {
                writer.write("\t\t\t" + entry.getKey() + "\t\t : " + entry.getValue() + "\n");
            }

            writer.write("\n\t\t\tSales Revenue: €" + formattedSales);
            writer.write("\n\t\t\tCredit Revenue: €" + formattedCredit + "\n");
            writer.write("\n\t\t\tTotal Revenue: €" + formattedTotal + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}