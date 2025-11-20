package Quan.Quan1;

public class VipTable extends Table {
    public VipTable(String id, int seats, Table.Status status) {
        super(id, seats, status);
    }

    public double getSurcharge() {
        return (double)50000.0F;
    }
}
