package Quan.Quan1;

public abstract class Table {
    private String id;
    private int seats;
    private Status status;

    public Table(String id, int seats, Status status) {
        this.id = id;
        this.seats = seats;
        this.status = status;
    }

    public String getId() {
        return this.id;
    }

    public int getSeats() {
        return this.seats;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public abstract double getSurcharge();

    public static enum Status {
        AVAILABLE,
        BOOKED,
        OCCUPIED;
    }
}