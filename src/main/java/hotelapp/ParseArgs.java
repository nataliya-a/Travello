package hotelapp;

import java.util.HashMap;
import java.util.List;

/** ParseArgs class parses the command line arguments and returns the values. */
public class ParseArgs {
    private final static HashMap<String, String> argsMap = new HashMap<>();
    private final static ThreadSafeHotelData hd = new ThreadSafeHotelData();
    private final static ThreadSafeReviewMapper rm = new ThreadSafeReviewMapper();

    /** parse the command line arguments and return the values. */
    public static void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            argsMap.put(args[i], args[i + 1]);
        }

        HotelParser hp = new HotelParser();
        List<Hotel> hotels = hp.parseHotelDataNew(argsMap.get("-hotels"));

        hd.hotelMapperNew(hotels);


        ReviewParser rp = new ReviewParser();
        List<String> paths = rp.filePath(argsMap.get("-reviews"));
        List<Review> reviews = rp.parseReviewsFiles(paths);
        rm.addReviews(reviews);

    }

    /**
     * Returns the hotel data.
     * @return the hotel data
     */
    public static ThreadSafeHotelData getHotelData() {
        return hd;
    }

    /**
     * Returns the review mapper.
     * @return the review mapper
     */
    public static ThreadSafeReviewMapper getReviewMapper() {
        return rm;
    }


}
