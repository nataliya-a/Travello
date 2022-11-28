package hotelapp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadSafeHotelData extends HotelData {

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public ThreadSafeHotelData() {
        super();
    }

    /** returns an hotelMap */
    public TreeMap<String, Hotel> getHotelsMap() {
        lock.readLock().lock();
        try {
            return super.getHotelsMap();
        } finally {
            lock.readLock().unlock();
        }
    }

    /** given a list of hotels, add them to the map */

    public void hotelMapperNew(List<Hotel> hotels) {
        lock.writeLock().lock();
        try {
            super.hotelMapperNew(hotels);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /** given a hotelId, return the hotel */
    public String getHotelDataNew(String strHotelId) {
        lock.readLock().lock();
        try {
            return super.getHotelDataNew(strHotelId);
        } finally {
            lock.readLock().unlock();
        }
    }

    /** given a hotelId, return the hotel object */
    public Hotel getHotelObjectNew(String strHotelId) {
        lock.readLock().lock();
        try {
            return super.getHotelObjectNew(strHotelId);
        } finally {
            lock.readLock().unlock();
        }
    }

    /** return a list of all the hotels */
    public List<Hotel> getHotels() {
        lock.readLock().lock();
        try {
            return super.getHotels();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * @param hotelId  hotel id
     * @param checkIn  check in date
     * @param checkOut check out date
     * @return
     */
    @Override
    public boolean canBook(String hotelId, LocalDate checkIn, LocalDate checkOut) {
        lock.readLock().lock();
        try {
            return super.canBook(hotelId, checkIn, checkOut);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * @param hotelId hotel id
     * @param checkIn check in date
     * @param checkOut check out date
     * @param userId user id
     */
    @Override
    public void book(String hotelId, LocalDate checkIn, LocalDate checkOut, String userId) {
        lock.writeLock().lock();
        try {
            super.book(hotelId, checkIn, checkOut, userId);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * @param hotelId  hotel id
     * @return a list of all the dates for a given hotel
     */
    @Override
    public ArrayList<LocalDate> getAllBookedDates(String hotelId) {
        lock.readLock().lock();
        try {
            return super.getAllBookedDates(hotelId);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * @param hotelId  hotel id
     * @param userId user id
     * @return a list of all the dates for a given hotel and user
     */
    @Override
    public ArrayList<LocalDate> getUserBookedDates(String hotelId, String userId) {
        lock.readLock().lock();
        try {
            return super.getUserBookedDates(hotelId, userId);
        } finally {
            lock.readLock().unlock();
        }
    }
}
