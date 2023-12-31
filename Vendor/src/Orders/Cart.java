package Orders;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Drinks> items; // List to store Drinks selected by the user

    public Cart() {
        items = new ArrayList<>();
    }
    
    // method to add drinks to the users cart
    public void addToCart(Drinks drink) {
        items.add(drink);
        System.out.println("Added " + drink.getName() + " to cart!" );
    }
    
    // method to remove items from the users cart
    public void removeFromCart(Drinks drink) {
        items.remove(drink);
    }
    
    // method to return items in the users cart
    public List<Drinks> getItems() {
        return items;
    }
    
    // method to clear the users cart
    public void clearCart() {
        items.clear();
    }
}