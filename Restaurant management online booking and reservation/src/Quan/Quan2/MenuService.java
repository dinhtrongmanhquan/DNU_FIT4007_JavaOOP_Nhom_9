package Quan.Quan2;

import Quan.Menu.MenuItem;

import java.util.List;
import java.util.stream.Collectors;

public class MenuService {
    private MenuRepository repo;
    public MenuService(MenuRepository repo){ this.repo=repo; }

    public List<MenuItem> getAll(){ return repo.getAll(); }
    public void add(MenuItem m){ repo.add(m); }
    public void remove(String id) throws MenuItemNotFoundException{
        if(repo.find(id)==null) throw new MenuItemNotFoundException("MenuItem "+id+" not found");
        repo.remove(id);
    }
    public MenuItem find(String id) throws MenuItemNotFoundException{
        MenuItem m=repo.find(id);
        if(m==null) throw new MenuItemNotFoundException("MenuItem "+id+" not found");
        return m;
    }
    public List<MenuItem> searchByName(String keyword){
        return repo.getAll().stream().filter(m->m.getName().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
    }
}
