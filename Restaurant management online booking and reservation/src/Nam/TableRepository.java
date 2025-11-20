package Nam;

import Quan.Quan1.Table;
import Quan.Quan1.StandardTable;
import Quan.Quan1.VipTable;
import java.io.*;
import java.util.*;

public class TableRepository {
    private List<Table> tables = new ArrayList<>();
    private final String file = "data/tables.csv";

    public List<Table> getAll() { return tables; }
    public void add(Table t) { tables.add(t); }
    public void remove(String id) { tables.removeIf(t -> t.getId().equals(id)); }
    public Table find(String id) { return tables.stream().filter(t->t.getId().equals(id)).findFirst().orElse(null); }

    public void load() {
        tables.clear();
        try {
            List<String[]> rows = CsvUtil.readAll(file);
            for (String[] r: rows) {
                if (r.length < 4) continue;
                String id = r[0], type = r[1], seats = r[2], status = r[3];
                int s = Integer.parseInt(seats);
                if ("VIP".equalsIgnoreCase(type)) tables.add(new VipTable(id, s, Table.Status.valueOf(status)));
                else tables.add(new StandardTable(id, s, Table.Status.valueOf(status)));
            }
        } catch (IOException e) { /* ignore */ }
    }

    public void save() {
        List<String[]> rows = new ArrayList<>();
        for (Table t: tables) {
            rows.add(new String[]{t.getId(), (t instanceof VipTable) ? "VIP":"STD", String.valueOf(t.getSeats()), t.getStatus().name()});
        }
        try { CsvUtil.writeAll(file, rows); } catch (IOException e) { System.err.println("Save tables error: "+e.getMessage()); }
    }
}

