package hotelapp;

public class PreparedStatements {
    /** Prepared Statements  */
    /** For creating the users table */
    public static final String CREATE_USER_TABLE =
            "CREATE TABLE users (" +
                    "userid INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(32) NOT NULL UNIQUE, " +
                    "password CHAR(64) NOT NULL, " +
                    "usersalt CHAR(32) NOT NULL);";

    /** Used to insert a new user into the database. */
    public static final String REGISTER_SQL =
            "INSERT INTO users (username, password, usersalt) " +
                    "VALUES (?, ?, ?);";

    /** Used to retrieve the salt associated with a specific user. */
    public static final String SALT_SQL =
            "SELECT usersalt FROM users WHERE username = ?";

    /** Used to authenticate a user. */
    public static final String AUTH_SQL =
            "SELECT username FROM users " +
                    "WHERE username = ? AND password = ?";

    /** Used to check if the username already exists in the database. */
    public static final String CHECK_USERNAME_SQL =
            "SELECT username FROM users " +
                    "WHERE username = ?";


    /** Used to create the hotels table. */
    public static final String CREATE_HOTELS_TABLE =
            "CREATE TABLE hotels (" +
                    "hotelID VARCHAR(225) PRIMARY KEY, " +
                    "hotelName VARCHAR(225) NOT NULL, " +
                    "address VARCHAR(225) NOT NULL, " +
                    "city VARCHAR(128) NOT NULL, " +
                    "state CHAR(2) NOT NULL, " +
                    "lat VARCHAR(25), " +
                    "lon VARCHAR(25));";


    /** Used to insert a new hotel into the hotels table. */
    public static final String INSERT_HOTEL_SQL =
            "INSERT INTO hotels " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);";


    /** Used to check if a table already exists in the database. */
    public static final String CHECK_SQL_TABLE =
            "SELECT count(*) " +
                    "FROM information_schema.tables " +
                    "WHERE table_schema = 'user018' " +
                    "AND table_name = ?";

    /** Create the favhotels table. */
    public static final String CREATE_FAV_HOTEL_TABLE =
            "CREATE TABLE favhotels (" +
                    "id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(225), " +
                    "hotelID VARCHAR(125));";

    /** Used to create the reviews table. */
    public static final String CREATE_REVIEWS_TABLE =
            "CREATE TABLE reviews (" +
                    "id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                    "hotelID VARCHAR(125), " +
                    "reviewID VARCHAR(225), " +
                    "title VARCHAR(225), " +
                    "reviewText VARCHAR(3000), " +
                    "username VARCHAR(225), " +
                    "ratings VARCHAR(25), " +
                    "reviewSubmissionTime VARCHAR(225));";


    /** Used to insert a new review into the reviews table. */
    public static final String INSERT_REVIEW_SQL =
            "INSERT INTO reviews (hotelID, reviewID, title, reviewText, username, ratings, reviewSubmissionTime) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);";


    /** Used to check if the existing hotels table is empty. */
    public static final String CHECK_HOTELS_TABLE_EMPTY =
            "SELECT count(*) " +
                    "FROM hotels;";

    /** Used to check if the existing reviews table is empty. */
    public static final String CHECK_REVIEWS_TABLE_EMPTY =
            "SELECT count(*) " +
                    "FROM reviews;";


    /**
     * Gets the matching hotel from the hotels table.
     */
    //select hotelName from hotels where upper(hotelName) LIKE '%?%';
    public static final String GET_HOTEL_SQL =
            "SELECT hotelID, hotelName " +
                    "FROM hotels " +
                    "WHERE upper(hotelName) LIKE ?;";

    /**
     * Gets the details of hotels matching the hotelID.
     */
    public static final String GET_HOTEL_DETAILS_SQL =
            "SELECT * " +
                    "FROM hotels " +
                    "WHERE hotelID = ?;";

    /**
     * Gets the reviews of hotels matching the hotelID and order by reviewSubmissionTime.
     */
    public static final String GET_HOTEL_REVIEWS_SQL =
            "SELECT * " +
                    "FROM reviews " +
                    "WHERE hotelID = ? " +
                    "ORDER BY reviewSubmissionTime DESC;";

    /**
     * Updates the review if the hotelID and username matches.
     */
    public static final String UPDATE_REVIEW_SQL =
            "UPDATE reviews " +
                    "SET title = ?, reviewText = ?, ratings = ? " +
                    "WHERE hotelID = ? AND username = ?;";

    /**
     * Deletes the review if the hotelID and username matches.
     */
    public static final String DELETE_REVIEW_SQL =
            "DELETE FROM reviews " +
                    "WHERE reviewID = ?;";

    /**
     * Gets the review from reviews when the hotelID and username matches.
     */
    public static final String GET_REVIEW_SQL =
            "SELECT * " +
                    "FROM reviews " +
                    "WHERE hotelID = ? AND username = ?;";

    /**
     * Creates a new table for the user's expedia links.
     */
    public static final String CREATE_EXPEDIA_TABLE =
            "CREATE TABLE expedia (" +
                    "id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(225), " +
                    "link VARCHAR(520));";

    /**
     * Inserts the expedia link into the user's expedia table.
     */
    public static final String INSERT_EXPEDIA_SQL =
            "INSERT INTO expedia (username, link) " +
                    "VALUES (?, ?);";

    /**
     * Gets the expedia links from the user's expedia table.
     */
    public static final String EXPEDIA_LINK_EXIST_FOR_USERNAME =
            "SELECT count(*) " +
                    "FROM expedia " +
                    "WHERE username = ? AND link = ?;";


    /** Delete all links for user under expedia table. */
    public static final String DELETE_EXPEDIA_LINKS_FOR_USERNAME =
            "DELETE FROM expedia " +
                    "WHERE username = ?;";


    /** Gets all links for user under expedia table. */
    public static final String GET_EXPEDIA_LINKS_FOR_USERNAME =
            "SELECT link " +
                    "FROM expedia " +
                    "WHERE username = ?;";

    /** Gets all the fav hotels for user under favhotels table. */
    public static final String GET_FAV_HOTELS_FOR_USERNAME =
            "SELECT favhotels.hotelID, hotels.hotelName FROM favhotels INNER JOIN hotels ON favhotels.hotelID=hotels.hotelID " +
                    "WHERE favhotels.username = ?;";

    /** Inserts the fav hotel into the user's favhotels table. */
    public static final String INSERT_FAV_HOTEL_SQL =
            "INSERT INTO favhotels (username, hotelID) " +
                    "VALUES (?, ?);";

    /** Checks if the fav hotel already exists in the user's favhotels table. */
    public static final String FAV_HOTEL_EXIST_FOR_USERNAME =
            "SELECT count(*) " +
                    "FROM favhotels " +
                    "WHERE username = ? AND hotelID = ?;";

    /** Delete all links for user under favhotesl table. */
    public static final String DELETE_FAV_HOTELS_FOR_USERNAME =
            "DELETE FROM favhotels " +
                    "WHERE username = ?;";

}
