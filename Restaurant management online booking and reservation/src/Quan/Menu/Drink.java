package Quan.Menu;

public class Drink extends MenuItem {
    public Drink(String id, String name, double price, double discount) {
        super(id, name, price, discount);
    }

    public String getType() {
        return "Drink";
    }
}
