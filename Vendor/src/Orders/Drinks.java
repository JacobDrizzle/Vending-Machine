package Orders;

import Windows.Window;

public class Drinks {
    private String name;
    private double price;
    private int quantity;
    private boolean restocked = false;
    
    Window window = new Window();
    
    public Drinks(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
    
    public void decreaseQuantity() {
        if (quantity > 0) {
            quantity--;
        }
        restocked = false;
    }
    
    public boolean isRestocked() {
        return restocked;
    }
    
    // If the drink stock is getting low or empty open a window alerting the user
    public void drinkStock() {
    	if (quantity <= 5 && quantity > 1) {	
	    	String msg = "Only "+ (quantity - 1)+ " " +  name + " left time for restock!";
	    	window.openWindow("Stock Low", msg, "Close");
	    	System.out.println("Only "+ (quantity -1) + " " +  name + " left time for restock!");
    	}
    	
    	if(quantity == 1) {
    		String msg = "No "+ name + " Report to maintenance.";
    		System.out.println(msg);
    		window.openWindow("Stock Low", msg, "Close");
    	}
    }
    
    // method for maintenance to return a string alerting the user of drinks stock 
    public String restock(int amount) {
    	String response = "";
        if (quantity == 20) {
            System.out.println("Sorry stock is full!");
            response = "Sorry stock is full!";
            return response;
        } else if (quantity + amount > 20) {
        	System.out.println("Cant add that many!");
        	response = "Cant add that many!";
        	return response;
        } else {
        	quantity += amount; // Reset quantity to the maximum value
            System.out.println(name + ": Restocked " + quantity + " Remaining");
            response = (name + ": Restocked " + quantity + " Remaining");
            restocked = true;
            return response;
        }
    }
}