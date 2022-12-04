package hotelapp;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

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

        dbTables();
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        HotelParser hp = new HotelParser();
        List<Hotel> hotels = hp.parseHotelDataNew(argsMap.get("-hotels"));

        hd.hotelMapperNew(hotels);
        if (dbHandler.checkHotelsTableEmpty() == 0) {
            System.out.println("Inserting hotels into database...");
            hd.addHotelToDB(hotels);
        }

        ReviewParser rp = new ReviewParser();
        List<String> paths = rp.filePath(argsMap.get("-reviews"));
        List<Review> reviews = rp.parseReviewsFiles(paths);
        System.out.println(reviews.size());
        rm.addReviews(reviews);
        if (dbHandler.checkReviewsTableEmpty() == 0) {
            System.out.println("Inserting reviews into database...");
            rm.addReviewsToDB(reviews);
        }





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


    /**
     * Creates the database tables if they don't exist already.
     */
    public static void dbTables() {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        if (dbHandler.checkTable("hotels") != 1) {
            dbHandler.createHotelsTable();
        }
        if (dbHandler.checkTable("reviews") != 1) {
            dbHandler.createReviewsTable();
        }
        if (dbHandler.checkTable("expedia") != 1) {
            dbHandler.createExpediaTable();
        }
        if (dbHandler.checkTable("favhotels") != 1) {
            dbHandler.createFavHotelsTable();
        }
    }



}
