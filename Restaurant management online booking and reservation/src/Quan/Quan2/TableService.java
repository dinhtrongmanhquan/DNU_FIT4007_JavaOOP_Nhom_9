package Quan.Quan2;

import Quan.Quan1.Table;
import Quan.Quan3.TableNotFoundException;

import java.util.List;

public class TableService {
    private TableRepository repo;
    public TableService(TableRepository repo){ this.repo=repo; }
    public List<Table> getAll(){ return repo.getAll(); }
    public void add(Table t){ repo.add(t); }
    public void remove(String id) throws TableNotFoundException{
        if(repo.find(id)==null) throw new TableNotFoundException("Table "+id+" not found");
        repo.remove(id);
    }
    public Table find(String id) throws TableNotFoundException{
        Table t=repo.find(id);
        if(t==null) throw new TableNotFoundException("Table "+id+" not found");
        return t;
    }

    public void load() {

    }
}
