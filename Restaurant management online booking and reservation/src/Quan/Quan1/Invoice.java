package Quan.Quan1;

import Quan.Quan1.InvoiceLine;

import java.time.LocalDateTime;
import java.util.*;

public class Invoice {
    private String id;
    private String tableId;
    private String customerName;
    private LocalDateTime createdAt;
    private double surcharge;
    private double discount;
    private List<InvoiceLine> lines = new ArrayList<>();

    public Invoice(String id,String tableId,String customerName,String createdAtStr,double surcharge,double discount){
        this.id=id; this.tableId=tableId; this.customerName=customerName;
        this.createdAt=LocalDateTime.parse(createdAtStr);
        this.surcharge=surcharge; this.discount=discount;
    }

    public String getId(){ return id; }
    public String getTableId(){ return tableId; }
    public String getCustomerName(){ return customerName; }
    public LocalDateTime getCreatedAt(){ return createdAt; }
    public double getSurcharge(){ return surcharge; }
    public double getDiscount(){ return discount; }

    public List<InvoiceLine> getLines(){ return lines; }
    public void addLine(InvoiceLine line){ lines.add(line); }

    public double total(){
        double t=0;
        for(InvoiceLine l: lines) t+=l.getTotal();
        return t+surcharge - discount;
    }
}
