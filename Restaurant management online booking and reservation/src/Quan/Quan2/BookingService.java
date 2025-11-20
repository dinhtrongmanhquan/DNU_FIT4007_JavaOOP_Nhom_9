package Quan.Quan2;

import Quan.Quan1.Booking;
import Nam.BookingRepository1;
import Quan.Quan3.TableAlreadyBookedException;

import java.util.List;

public class BookingService {
    private BookingRepository1 repo;
    public BookingService(BookingRepository1 repo){ this.repo=repo; }

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
