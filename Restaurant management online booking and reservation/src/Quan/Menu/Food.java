package Quan.Menu;

public class Food extends MenuItem {
    public Food(String id, String name, double price, double discount) {
        super(id, name, price, discount);
    }

    public String getType() {
        return "Food";
    }
}
