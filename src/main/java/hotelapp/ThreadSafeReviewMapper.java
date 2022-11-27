package hotelapp;

import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/** extends ReviewMapper to make it thread safe using ReentrantReadWrite Lock by overriding functions of the parent class. */
public class ThreadSafeReviewMapper extends ReviewMapper {

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public ThreadSafeReviewMapper() {
        super();
    }

    /** delete a review from the map */
    public void deleteReview(String hotelId, String reviewId) {
        lock.writeLock().lock();
        try {
            super.deleteReview(hotelId, reviewId);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /** given a list of reviews add them to the map */
    public void addReviews(List<Review> reviews) {
        lock.writeLock().lock();
        try {
            super.addReviews(reviews);
        } finally {
            lock.writeLock().unlock();
        }
    }


    /** given a hotelId, return the review */
    public TreeSet<Review> printReviews(String hotelID) {
        lock.readLock().lock();
        try {
            return super.printReviews(hotelID);
        } finally {
            lock.readLock().unlock();
        }
    }


    /** given a hotelId, return a boolean */
    public boolean containsHotel(String hotelId) {
        lock.readLock().lock();
        try {
            return super.containsHotel(hotelId);
        } finally {
            lock.readLock().unlock();
        }
    }


    /** given a hotelId, return the review object */
    public TreeSet<Review> getReviewObject(String hotelId) {
        lock.readLock().lock();
        try {
            return super.getReviewObject(hotelId);
        } finally {
            lock.readLock().unlock();
        }
    }


    /** given a hotelID and a review object adds the review to the map */
    public void addNewReview(String hotelId, Review review) {
        lock.writeLock().lock();
        try {
            super.addNewReview(hotelId, review);
        } finally {
            lock.writeLock().unlock();
        }
    }


}
