package Quan.Quan2;

import Quan.Quan1.Invoice;
import Nam.InvoiceRepository;
import java.util.List;

public class InvoiceService {
    private InvoiceRepository repo;
    public InvoiceService(InvoiceRepository repo){ this.repo=repo; }

    public void addInvoice(Invoice i){ repo.add(i); }
    public List<Invoice> getAll(){ return repo.getAll(); }
}
