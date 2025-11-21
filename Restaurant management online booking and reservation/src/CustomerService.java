package Quan.Quan2;

import Quan.Quan3.CustomerNotFoundException;
import Quan.Quan1.Customer;
import Nam.CustomerRepository;
import java.util.List;

public class CustomerService {
    private CustomerRepository repo;
    public CustomerService(CustomerRepository repo){ this.repo=repo; }

    public List<Customer> getAll(){ return repo.getAll(); }
    public void add(Customer c){ repo.add(c); }
    public Customer find(String id) throws CustomerNotFoundException {
        Customer c=repo.find(id);
        if(c==null) throw new CustomerNotFoundException("Customer "+id+" not found");
        return c;
    }

    public void edit(String id, String newName, String newPhone, int newAge) throws CustomerNotFoundException {
        Customer customer = find(id); // Sử dụng find() để kiểm tra sự tồn tại

        customer.setName(newName);
        customer.setPhone(newPhone);
        customer.setAge(newAge);

        repo.save();
    }
}