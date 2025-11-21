package Quan.Quan2;

import Quan.Quan1.Table;
import Quan.Quan3.TableNotFoundException;
import Nam.TableRepository;
import java.util.List;

public class TableService {
    private TableRepository repo;
    public TableService(TableRepository repo){ this.repo=repo; }

    public List<Table> getAll(){ return repo.getAll(); }
    public void add(Table t){ repo.add(t); }

    public Table find(String id) throws TableNotFoundException{
        Table t = repo.find(id);
        if(t == null) throw new TableNotFoundException("Table " + id + " not found");
        return t;
    }

    public void remove(String id) throws TableNotFoundException{
        if(find(id) == null) throw new TableNotFoundException("Table "+id+" not found");
        repo.remove(id);
    }

    public void edit(String id, int newSeats, Table.Status newStatus) throws TableNotFoundException{
        Table t = find(id);

        t.setStatus(newStatus);

        repo.save();
    }

    public void load() {
    }
}