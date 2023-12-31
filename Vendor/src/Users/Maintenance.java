package Users;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Main.Main;
import Orders.Orders;
import Windows.Window;

public class Maintenance extends User {
	Main vendingMachine = new Main();
	Orders orders = new Orders(vendingMachine);
    Window window = new Window();
    
    public Maintenance(String username, String password) {
        super(username, password, 999.0, 0.0, false);
    }
    
    @Override
    public void generateMaintenanceReport() {
        // Overridden implementation for maintenance users
        generateRepairReport();
    }
    
    public void generateRepairReport() {
    	LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String Date = currentDateTime.format(formatter);
        int linesFromBottom = 1;        
        
    	try {
    		BufferedReader br = new BufferedReader(new FileReader("ErrorReport.txt"));
            String line;
            StringBuilder lastErrorDescription = new StringBuilder();

            // Read each line until the end of the file
            while ((line = br.readLine()) != null) {
                // Add each line to lastErrorDescription
                lastErrorDescription.append(line).append("\n");
            }

            // Split the content into lines
            String[] lines = lastErrorDescription.toString().split("\n");
            String errorDescription = "";
            
            // Check if there are enough lines in the file
            if (lines.length >= linesFromBottom) {
                // Extract the error description (1 line from the bottom)
            	errorDescription = lines[lines.length - linesFromBottom].trim();
                System.out.println("Last Error Description: " + errorDescription);
            } else {
                System.out.println("Failed getting error description.");
            }
    		
        	BufferedWriter writer = new BufferedWriter(new FileWriter("RepairReport.txt", true));
        	writer.write("\n\t\t======== REPAIR REPORT ========\n");
            writer.write("\t\t==== " + Date + " =====\n\n");
            writer.write("\t\t\tRepaired " + errorDescription + "\n\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            window.openWindow("Report Failed", "Failed to save repair report.\nPlease try again", "Close");
            System.out.println("Error generating repair report");
        }
    	
    }
    @Override
	public void change(double earnings) {
		releaseChange(earnings);
	}
	
    // Release change function for maintenance
    public void releaseChange(double earnings) {
    	// Check if there is no change to dispense
    	if (earnings == 0) {
    		System.out.println("No change to dispense.");
    	} else {
    		// Define the available coins in the machine
    		List<Double> coins = new ArrayList<>();    		
    		coins.add(0.05);
    		coins.add(0.10);
    	    coins.add(0.20);
    	    coins.add(0.50);
    	    coins.add(1.00);
    	    coins.add(2.00);
    	    
    	    // Initialize a memoization map to store calculated results
    	    Map<Long, Integer> memo = new HashMap<>();
    	    
    	    // Calculate the minimum number of coins needed to dispense change
            long minCoins = minChange((long) earnings, coins, memo);

            if (minCoins != -1) {
            	// Display the result
                String result = "The minimum amount of coins for €" + earnings + " : " + minCoins;
                window.openWindow("Change collected", result, "Close");
                
                // Update vending machine earnings and total earnings
                vendingMachine.setLifeTimeEarnings(earnings);
                vendingMachine.setTotalEarnings(-earnings);
                
                // Update UI with total earnings information
                orders.updateEarningsLabel(vendingMachine.getTotalEarnings());
                System.out.println("Total Earnings: " + vendingMachine.getTotalEarnings() + ".");
            } else {
            	// Unable to make change for the given amount
                System.out.println("Cannot make change for the given amount.");
            }
        }
    }
    
	// method to calculate the minimum number of coins needed for change
    private long minChange(long amount, List<Double> coins, Map<Long, Integer> memo) {
    	
    	// Base case: amount is 0, no coins needed
		if(amount == 0) {
			return 0;
		}
		
		// Base case: amount is negative, indicating invalid state
		if(amount < 0) {
			return -1;
		}
		
		// Check if the result is already memoized
		if (memo.containsKey(amount)) {
            return memo.get(amount);
        }
		
		// Initialize minCoins to track the minimum number of coins
		long minCoins = -1;
		
		// Iterate through each coin denomination
		for(double coin : coins) {
			// Calculate the remaining amount after subtracting the current coin
			long subAmount = (long) (amount - coin);
			
			// Recursively calculate the minimum coins for the remaining amount
			long subCoins = minChange(subAmount, coins, memo);
			
			// Update minCoins if a valid solution is found
			if(subCoins != -1) {
				long numCoins = subCoins + 1;
				if(numCoins < minCoins || minCoins == -1) {
					minCoins = numCoins;
				}
			}
		}
		
		// Memoize the result for the current amount
		memo.put(amount, (int) minCoins);
		
		return minCoins;
	}
}