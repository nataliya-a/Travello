package hotelapp;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Class representing a hotel. */
public class Review {
    @SerializedName(value="hotelId")
    private String hotelID;

    private String reviewText;

    private String userNickname;

    private String title;

    @SerializedName(value="reviewId")
    private String reviewID;

    private String ratingOverall;

    @SerializedName(value="reviewSubmissionTime")
    private LocalDate reviewSubmissionTime;  //"reviewSubmissionTime":"2016-08-16T18:38:29Z"



    /**
     * Constructor for Review.
     * @param hotelID id of the hotel
     * @param reviewText text of the review
     * @param userNickname nickname of the user
     * @param title title of the review
     * @param reviewID id of the review
     * @param ratingOverall rating of the review
     * @param reviewSubmissionTime time of the review
     */
    public Review(String hotelID, String reviewText, String userNickname, String title, String reviewID, String ratingOverall, String reviewSubmissionTime) {
        this.hotelID = hotelID;
        this.reviewText = reviewText;
        this.userNickname = userNickname.length() == 0 ? "Anonymous" : userNickname;
        this.title = title;
        this.reviewID = reviewID;
        this.ratingOverall = ratingOverall;
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        this.reviewSubmissionTime = LocalDate.parse(reviewSubmissionTime, formatter);
    }



    @Override
    public String toString() {
        String str = "";
        str += "Review by " + userNickname + " on " + reviewSubmissionTime;
        str += System.lineSeparator() + "Rating: " + ratingOverall;
        str += System.lineSeparator() + "ReviewId: " + reviewID;
        str += System.lineSeparator() + title;
        str += System.lineSeparator() + reviewText;
        return str;
    }


    /** Getters for the review class. */
    public String getHotelID() {
        return hotelID;
    }

    /**
     * Get the review text.
     * @return review text
     */
    public String getReviewID() {
        return reviewID;
    }

    /**
     * Get the review text.
     * @return review text
     */
    public String getReviewText() { return reviewText; }

    /**
     * Get the review text.
     * @return review text
     */
    public LocalDate getReviewSubmissionTime() {
        return reviewSubmissionTime;
    }

    /**
     * Get the review title.
     * @return review title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the review UserNickname.
     * @return review UserNickname
     */

    public String getUserNickname() {
        return userNickname;
    }

    /**
     * Get the review ratingOverall.
     * @return review ratingOverall
     */
    public String getRatingOverall() {
        return ratingOverall;
    }
}

