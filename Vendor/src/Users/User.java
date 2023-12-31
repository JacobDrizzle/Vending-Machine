package Users;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class User {
    private String username;
    private String password;
    private double credit;
    private double totalSpend;
    private boolean cardDetails;
    
    public User(String username, String password, double credit, double totalSpend, boolean cardDetails) {
        this.username = username;
        this.password = password;
        this.credit = credit;
        this.totalSpend = totalSpend;
        this.cardDetails = cardDetails;
    }

    public String getUsername() {
        return username;
    }
    
    public void generateMaintenanceReport() {

    }
    
    public void change(double earnings) {

    }
    
    public String getPassword() {
        return password;
    }

    public double getCredit() {
        return credit;
    }
    
    public boolean getCardDetails() {
        return cardDetails;
    }
    
    public double getTotalSpend() {
        return totalSpend;
    }
    
    // method to update the userDB file
    public void updateFile() {
    	 try {
             File file = new File("usersDB.txt");
             File tempFile = new File("tempUsersDB.txt");

             BufferedReader br = new BufferedReader(new FileReader(file));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));

             String line;
             while ((line = br.readLine()) != null) {
                 String[] parts = line.split(",");
                 if (parts.length == 5) { // Format is "username,password,credit,totalSpend"
                     String currentUsername = parts[0].trim();

                     if (currentUsername.equals(username)) {
                         // Update the credit for the matching user
                         bw.write(currentUsername + "," + parts[1].trim() + "," + credit + "," + totalSpend + "," + cardDetails);
                     } else {
                         // Keep the other users' records unchanged
                         bw.write(line);
                     }
                     bw.newLine();
                 }
             }
             br.close();
             bw.close();

             // Replace the original file with the updated file
             if (!file.delete()) {
                 System.out.println("Failed to delete the original file.");
             }

             // Rename the temporary file to the original file
             if (!tempFile.renameTo(file)) {
                 System.out.println("Failed to rename the temporary file.");
             }
         } catch (IOException e) {
             e.printStackTrace();
             System.out.println("Failed to save: usersDB.txt");
         }
    }
    
    public void setTotalSpend(double amount) {
    	this.totalSpend += amount;
    	updateFile();
    }

    public void setCredit(double amount) {
        this.credit = amount;
        updateFile();
    }
    
    public void setCardDetails(boolean details) {
    	this.cardDetails = details;
    	updateFile();
    }

    public void deductCredit(double amount) {
        credit -= amount;
    }

    public void addCredit(double amount) {
        credit += amount;
    }
}