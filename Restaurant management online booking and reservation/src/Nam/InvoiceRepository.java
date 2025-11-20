package Nam;

import Quan.Quan1.Invoice;
import java.io.*;
import java.util.*;

public class InvoiceRepository {
    private List<Invoice> invoices = new ArrayList<>();
    private final String file = "data/invoices.csv";

    public List<Invoice> getAll() { return invoices; }
    public void add(Invoice i) { invoices.add(i); }
    public void remove(String id) { invoices.removeIf(i->i.getId().equals(id)); }
    public Invoice find(String id) { return invoices.stream().filter(i->i.getId().equals(id)).findFirst().orElse(null); }

    public void load() {
        invoices.clear();
        try {
            for (String[] r : CsvUtil.readAll(file)) {
                // Simple CSV: id, tableId, customerName, createdAt, surcharge, discount
                if (r.length<6) continue;
                invoices.add(new Invoice(r[0],r[1],r[2],r[3],Double.parseDouble(r[4]),Double.parseDouble(r[5])));
            }
        } catch (IOException e) {}
    }

    public void save() {
        List<String[]> rows = new ArrayList<>();
        for (Invoice i: invoices) rows.add(new String[]{i.getId(),i.getTableId(),i.getCustomerName(),
                i.getCreatedAt().toString(), String.valueOf(i.getSurcharge()), String.valueOf(i.getDiscount())});
        try { CsvUtil.writeAll(file, rows); } catch (IOException e) { System.err.println("Save invoices error: "+e.getMessage()); }
    }
}

