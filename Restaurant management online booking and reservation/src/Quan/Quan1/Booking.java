package Quan.Quan1;

import java.time.LocalDate;
import java.time.LocalTime;

public class Booking {
    private String id;
    private String tableId;
    private String customerName;
    private String customerPhone;
    private LocalDate date;
    private LocalTime time;
    private int pax;

    public Booking(String id, String tableId, String customerName, String customerPhone,
                   String dateStr, String timeStr, int pax) {
        this.id=id; this.tableId=tableId; this.customerName=customerName; this.customerPhone=customerPhone;
        this.date=LocalDate.parse(dateStr);
        this.time=LocalTime.parse(timeStr);
        this.pax=pax;
    }

    public String getId(){ return id; }
    public String getTableId(){ return tableId; }
    public String getCustomerName(){ return customerName; }
    public String getCustomerPhone(){ return customerPhone; }
    public LocalDate getDate(){ return date; }
    public LocalTime getTime(){ return time; }
    public int getPax(){ return pax; }
}
