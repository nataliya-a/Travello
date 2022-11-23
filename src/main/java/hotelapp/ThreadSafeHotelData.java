package hotelapp;

import hotelapp.Hotel;
import hotelapp.HotelData;

import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadSafeHotelData extends HotelData {

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();


    /** given a list of hotels, add them to the map */
    public String getHotelData(String strHotelId) {
        lock.readLock().lock();
        try {
            return super.getHotelData(strHotelId);
        } finally {
            lock.readLock().unlock();
        }
    }



    /** given a list of hotels, add them to the map */
    public void addHotels(Hotel[] hotels) {
        lock.writeLock().lock();
        try {
            super.addHotels(hotels);
        } finally {
            lock.writeLock().unlock();
        }
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


}
