package hotelapp;

import java.util.*;

/**
 * This class that gets, adds, deletes, and updates reviews in reviewsMap.
 *
 */
public class ReviewMapper {
    private final HashMap<String, TreeSet<Review>> reviewsMap = new HashMap<>();

    /** given a list of reviews add them to the map */
    public void addReviews(List<Review> reviews) {
        for (Review r : reviews) {
            String reviewText = r.getReviewText();
            reviewText.replaceAll("\\p{Punct}", "");
            // add reviews to the corresponding hotel
            if (r.getHotelID() != null) {
                reviewsMap.computeIfAbsent(r.getHotelID(), k -> new TreeSet<>(
                                // sort by date by the most recent; if equal, sort by reviewID in increasing order
                                (r1, r2) -> {
                                    if (r2.getReviewSubmissionTime().compareTo(r1.getReviewSubmissionTime()) == 0) {
                                        return r1.getReviewID().compareTo(r2.getReviewID());
                                    }
                                    return r2.getReviewSubmissionTime().compareTo(r1.getReviewSubmissionTime());
                                }
                        ))
                        .add(r);
            }
        }
    }

    public void addReviewsToDB(List<Review> reviews) {
        DatabaseHandler db = DatabaseHandler.getInstance();
        for (Review r : reviews) {
            String reviewText = r.getReviewText();
            reviewText.replaceAll("\\p{Punct}", "");
            String hotelID = r.getHotelID();
            String reviewID = r.getReviewID();
            String reviewTitle = r.getTitle();
            String reviewSubmissionTime = String.valueOf(r.getReviewSubmissionTime());
            String username = r.getUserNickname();
            String rating = String.valueOf(r.getRatingOverall());
            if (reviewID.equals("")) {
                reviewID = UUID.randomUUID().toString();
            }
            db.insertReview(hotelID, reviewID, reviewTitle, reviewText, username, rating, reviewSubmissionTime);
        }
    }


    /** given a hotelId, return the review */
    public TreeSet<Review> printReviews(String hotelID) {
        return reviewsMap.get(hotelID);
    }


    /** given a hotelId, returns true if the hotel has reviews */
    public boolean containsHotel(String hotelId) {
        return reviewsMap.containsKey(hotelId);
    }


    /** given a hotelId, return the set of reviews */
    public TreeSet<Review> getReviewObject(String hotelId) {
        return reviewsMap.get(hotelId);
    }


    /** delete a review from the map if the reviewID matches */
    public void deleteReview(String hotelId, String reviewId) {
        reviewsMap.get(hotelId).removeIf(r -> r.getReviewID().equals(reviewId));
    }

    /** given a hotelID and a review object adds the review to the map */
    public void addNewReview(String hotelID, Review review) {
        reviewsMap.get(hotelID).add(review);
    }


}


