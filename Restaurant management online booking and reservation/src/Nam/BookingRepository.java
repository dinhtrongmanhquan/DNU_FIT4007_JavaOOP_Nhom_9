package Nam;

import Quan.Quan1.Booking;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BookingRepository {
    private List<Booking> bookings = new ArrayList<>();
    private final String file = "data/bookings.csv";
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public List<Booking> getAll() { return bookings; }
    public void add(Booking b) { bookings.add(b); }
    public void remove(String id) { bookings.removeIf(b->b.getId().equals(id)); }
    public Booking find(String id) { return bookings.stream().filter(b->b.getId().equals(id)).findFirst().orElse(null); }

    public void load() {
        bookings.clear();
        try {
            for (String[] r : CsvUtil1.readAll(file)) {
                if (r.length<6) continue;
                bookings.add(new Booking(r[0], r[1], r[2], r[3], r[4], r[5], Integer.parseInt(r[6])));
            }
        } catch (IOException e) {}
    }

    public void save() {
        List<String[]> rows = new ArrayList<>();
        for (Booking b: bookings) {
            rows.add(new String[]{b.getId(),b.getTableId(),b.getCustomerName(),b.getCustomerPhone(),
                    b.getDate().format(DATE_FMT), b.getTime().format(TIME_FMT), String.valueOf(b.getPax())});
        }
        try { CsvUtil1.writeAll(file, rows); } catch (IOException e) { System.err.println("Save bookings error: "+e.getMessage()); }
    }
}
