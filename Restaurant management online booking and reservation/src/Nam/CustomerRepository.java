package Nam;


import Quan.Quan1.Customer;
import java.io.*;
import java.util.*;

public class CustomerRepository {
    private List<Customer> customers = new ArrayList<>();
    private final String file = "data/customers.csv";

    public List<Customer> getAll() { return customers; }
    public void add(Customer c) { customers.add(c); }
    public void remove(String id) { customers.removeIf(c->c.getId().equals(id)); }
    public Customer find(String id) { return customers.stream().filter(c->c.getId().equals(id)).findFirst().orElse(null); }

    public void load() {
        customers.clear();
        try {
            for (String[] r : CsvUtil.readAll(file)) {
                if (r.length < 4) continue;
                String id=r[0], name=r[1], phone=r[2], age=r[3];
                customers.add(new Customer(id,name,phone,Integer.parseInt(age)));
            }
        } catch (IOException e) {}
    }

    public void save() {
        List<String[]> rows = new ArrayList<>();
        for (Customer c : customers) rows.add(new String[]{c.getId(),c.getName(),c.getPhone(),String.valueOf(c.getAge())});
        try { CsvUtil.writeAll(file, rows); } catch (IOException e) { System.err.println("Save customers error: "+e.getMessage()); }
    }
}
