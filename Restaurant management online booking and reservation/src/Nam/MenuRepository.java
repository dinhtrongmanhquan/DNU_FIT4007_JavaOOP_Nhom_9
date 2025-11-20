package Nam;

import Quan.Menu.MenuItem;
import Quan.Menu.Food;
import Quan.Menu.Drink;
import java.io.*;
import java.util.*;

public class MenuRepository {
    private List<MenuItem> items = new ArrayList<>();
    private final String file = "data/menu.csv";

    public List<MenuItem> getAll() { return items; }
    public void add(MenuItem m) { items.add(m); }
    public void remove(String id) { items.removeIf(x->x.getId().equals(id)); }
    public MenuItem find(String id) { return items.stream().filter(x->x.getId().equals(id)).findFirst().orElse(null); }

    public void load() {
        items.clear();
        try {
            for (String[] r : CsvUtil.readAll(file)) {
                if (r.length < 5) continue;
                String id = r[0], name = r[1], type = r[2], price = r[3], disc = r[4];
                double p = Double.parseDouble(price);
                double d = Double.parseDouble(disc);
                if ("Food".equalsIgnoreCase(type)) items.add(new Food(id, name, p, d));
                else items.add(new Drink(id, name, p, d));
            }
        } catch (IOException e) {}
    }

    public void save() {
        List<String[]> rows = new ArrayList<>();
        for (MenuItem m: items) rows.add(new String[]{m.getId(), m.getName(), m.getType(), String.valueOf(m.getPrice()/(1-m.getDiscount())), String.valueOf(m.getDiscount())});
        try { CsvUtil.writeAll(file, rows); } catch (IOException e) { System.err.println("Save menu error: "+e.getMessage()); }
    }
}
