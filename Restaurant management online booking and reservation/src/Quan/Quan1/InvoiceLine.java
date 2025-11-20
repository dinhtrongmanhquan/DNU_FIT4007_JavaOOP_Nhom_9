package Quan.Quan1;

import Quan.Menu.MenuItem;

public class InvoiceLine {
    private MenuItem item;
    private int quantity;

    public InvoiceLine(MenuItem item,int quantity){ this.item=item; this.quantity=quantity; }
    public MenuItem getItem(){ return item; }
    public int getQuantity(){ return quantity; }
    public double getTotal(){ return item.getPrice()*quantity*(1-item.getDiscount()); }
}
