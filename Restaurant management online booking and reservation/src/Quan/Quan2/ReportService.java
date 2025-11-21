package Quan.Quan2;

import Nam.InvoiceRepository;
import Quan.Quan1.Invoice;
import Quan.Quan1.InvoiceLine;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class ReportService {
    private InvoiceRepository invoiceRepo;

    public ReportService(InvoiceRepository invoiceRepo) {
        this.invoiceRepo = invoiceRepo;
    }

    public double getTotalRevenue(LocalDate startDate, LocalDate endDate) {
        return invoiceRepo.getAll().stream()
                .filter(i -> {
                    LocalDate invoiceDate = i.getCreatedAt().toLocalDate();
                    return !invoiceDate.isBefore(startDate) && !invoiceDate.isAfter(endDate);
                })
                .mapToDouble(Invoice::total)
                .sum();
    }

    public Map<Month, Double> getMonthlyRevenue(int year) {
        return invoiceRepo.getAll().stream()
                .filter(i -> i.getCreatedAt().getYear() == year)
                .collect(Collectors.groupingBy(
                        i -> i.getCreatedAt().getMonth(),
                        Collectors.summingDouble(Invoice::total)
                ));
    }

    public Map<String, Integer> getTopSellingItems(int limit) {
        Map<String, Integer> itemCounts = new HashMap<>();

        for (Invoice invoice : invoiceRepo.getAll()) {
            for (InvoiceLine line : invoice.getLines()) {
                String itemName = line.getItem().getName();
                int quantity = line.getQuantity();
                itemCounts.put(itemName, itemCounts.getOrDefault(itemName, 0) + quantity);
            }
        }

        return itemCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}