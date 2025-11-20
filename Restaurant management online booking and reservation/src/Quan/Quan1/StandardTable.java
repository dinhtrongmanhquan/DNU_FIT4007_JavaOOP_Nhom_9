package Quan.Quan1;

public class StandardTable extends Table {
    public StandardTable(String id, int seats, Table.Status status) {
        super(id, seats, status);
    }

    public double getSurcharge() {
        return (double)0.0F;
    }
}