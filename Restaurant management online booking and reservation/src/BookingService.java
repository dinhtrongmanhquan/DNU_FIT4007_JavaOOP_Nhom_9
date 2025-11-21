package Quan.Quan2;

import Quan.Quan1.Booking;
import Nam.BookingRepository;
import Quan.Quan3.TableAlreadyBookedException;

import java.util.List;

public class BookingService {
    private BookingRepository repo;
    public BookingService(BookingRepository repo){ this.repo=repo; }

    public List<Booking> getAll(){ return repo.getAll(); }

    public void addBooking(Booking b) throws TableAlreadyBookedException {
        for(Booking exist: repo.getAll()){
            if(exist.getTableId().equals(b.getTableId()) && exist.getDate().equals(b.getDate())
                    && exist.getTime().equals(b.getTime())){
                throw new TableAlreadyBookedException("Table already booked at "+b.getDate()+" "+b.getTime());
            }
        }
        repo.add(b);
    }
}
