package hotelapp;//package jdbc.simpleRegistrationLoginWithoutSessions;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;
import java.util.*;

/**
 * A class that handles all database operations.
 */
public class DatabaseHandler {

    private static DatabaseHandler dbHandler = new DatabaseHandler("database.properties"); // singleton pattern
    private Properties config; // a "map" of properties
    private String uri = null; // uri to connect to mysql using jdbc
    private Random random = new Random(); // used in password  generation

    /**
     * DataBaseHandler is a singleton, we want to prevent other classes
     * from creating objects of this class using the constructor
     */
    private DatabaseHandler(String propertiesFile){
        this.config = loadConfigFile(propertiesFile);
        this.uri = "jdbc:mysql://"+ config.getProperty("hostname") + "/" + config.getProperty("username") + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    }

    /**
     * Returns the instance of the database handler.
     * @return instance of the database handler
     */
    public static DatabaseHandler getInstance() {
        return dbHandler;
    }

    /**
     * Loads the properties file.
     * @param propertyFile the name of the properties file
     * @return a "map" of properties
     */

    public Properties loadConfigFile(String propertyFile) {
        Properties config = new Properties();
        try (FileReader fr = new FileReader(propertyFile)) {
            config.load(fr);
        }
        catch (IOException e) {
            System.out.println(e);
        }

        return config;
    }

    /**
     * Creates a new table in the database.
     */
    public void createTable() {
        Statement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            statement = dbConnection.createStatement();
            statement.executeUpdate(PreparedStatements.CREATE_USER_TABLE);
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }


    /**
     * Returns the hex encoding of a byte array.
     *
     * @param bytes - byte array to encode
     * @param length - desired length of encoding
     * @return hex encoded byte array
     */
    public static String encodeHex(byte[] bytes, int length) {
        BigInteger bigint = new BigInteger(1, bytes);
        String hex = String.format("%0" + length + "X", bigint);

        assert hex.length() == length;
        return hex;
    }

    /**
     * Calculates the hash of a password and salt using SHA-256.
     *
     * @param password - password to hash
     * @param salt - salt associated with user
     * @return hashed password
     */
    public static String getHash(String password, String salt) {
        String salted = salt + password;
        String hashed = salted;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salted.getBytes());
            hashed = encodeHex(md.digest(), 64);
        }
        catch (Exception ex) {
            System.out.println(ex);
        }

        return hashed;
    }

    /**
     * Registers a new user, placing the username, password hash, and
     * salt into the database.
     *
     * @param newuser - username of new user
     * @param newpass - password of new user
     */
    public void registerUser(String newuser, String newpass) {
        // Generate salt
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);

        String usersalt = encodeHex(saltBytes, 32); // salt
        String passhash = getHash(newpass, usersalt); // hashed password
        System.out.println(usersalt);

        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.REGISTER_SQL);
                statement.setString(1, newuser);
                statement.setString(2, passhash);
                statement.setString(3, usersalt);
                statement.executeUpdate();
                statement.close();
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }

/**
     * Checks if a user and the password they entered match the one in database.
     *
     * @param username - username to check
     * @param password - password to check
     * @return true if user exists, false otherwise
     */
    public boolean authenticateUser(String username, String password) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {

            statement = connection.prepareStatement(PreparedStatements.AUTH_SQL);
            String usersalt = getSalt(connection, username);
            String passhash = getHash(password, usersalt);
            statement.setString(1, username);
            statement.setString(2, passhash);
            ResultSet results = statement.executeQuery();
            boolean flag = results.next();
            return flag;
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    /**
     * Gets the salt for a specific user.
     *
     * @param connection - active database connection
     * @param user - which user to retrieve salt for
     * @return salt for the specified user or null if user does not exist
     * @throws SQLException if any issues with database connection
     */
    private String getSalt(Connection connection, String user) {
        String salt = null;
        try (PreparedStatement statement = connection.prepareStatement(PreparedStatements.SALT_SQL)) {
            statement.setString(1, user);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                salt = results.getString("usersalt");
                return salt;
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return salt;
    }


    /**
     * Checks if a user exists in the database.
     */
    public boolean checkUser(String username) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {

            statement = connection.prepareStatement(PreparedStatements.CHECK_USERNAME_SQL);
            statement.setString(1, username);
            ResultSet results = statement.executeQuery();
            boolean flag = results.next();
            return flag;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if the table exists in the database.
     */

    public int checkTable(String tableName) {
        PreparedStatement statement;
        int count = 0;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {

            statement = connection.prepareStatement(PreparedStatements.CHECK_SQL_TABLE);
            statement.setString(1, tableName);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                count = Integer.parseInt(results.getString("count(*)"));
            }
            return count;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new hotels table in the database.
     */

    public void createHotelsTable() {
        Statement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = dbConnection.createStatement();
            statement.executeUpdate(PreparedStatements.CREATE_HOTELS_TABLE);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Creates a new reviews table in the database.
     */
    public void createReviewsTable() {
        Statement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = dbConnection.createStatement();
            statement.executeUpdate(PreparedStatements.CREATE_REVIEWS_TABLE);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Inserts a new hotel into the hotels table.
     */

    //String hotelName, String hotelID, String address, String city, String state, String lat, String lon
    public void insertHotel(String hotelId, String hotelName, String address, String city, String state, String lat, String lon) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(PreparedStatements.INSERT_HOTEL_SQL);
            statement.setString(1, hotelId);
            statement.setString(2, hotelName);
            statement.setString(3, address);
            statement.setString(4, city);
            statement.setString(5, state);
            statement.setString(6, lat);
            statement.setString(7, lon);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }


    /**
     * Inserts a new review into the reviews table.
     */
    public void insertReview(String hotelId, String reviewId, String reviewTitle, String reviewText, String username, String reviewRating, String reviewDate) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(PreparedStatements.INSERT_REVIEW_SQL);
            statement.setString(1, hotelId);
            statement.setString(2, reviewId);
            statement.setString(3, reviewTitle);
            statement.setString(4, reviewText);
            statement.setString(5, username);
            statement.setString(6, reviewRating);
            statement.setString(7, reviewDate);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }


        /**
         * Checks if the table is empty.
         */
    public int checkHotelsTableEmpty() {
        PreparedStatement statement;
        int count = -1;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {

            statement = connection.prepareStatement(PreparedStatements.CHECK_HOTELS_TABLE_EMPTY);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                count = Integer.parseInt(results.getString("count(*)"));
            }
            return count;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int checkReviewsTableEmpty() {
        PreparedStatement statement;
        int count = -1;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {

            statement = connection.prepareStatement(PreparedStatements.CHECK_REVIEWS_TABLE_EMPTY);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                count = Integer.parseInt(results.getString("count(*)"));
            }
            return count;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the hotelName from the hotels table.
     */
    public Map<String, String> getHotelNames(String keyword) {
        keyword = "%" + keyword.toUpperCase() + "%";
        System.out.println(keyword);
        Map<String, String> matches = new HashMap<>();
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {

            statement = connection.prepareStatement(PreparedStatements.GET_HOTEL_SQL);
            statement.setString(1, keyword);
            System.out.println(statement);
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                matches.put(results.getString("hotelId"), results.getString("hotelName"));

            }
            return matches;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the details of hotels matching the hotelID.
     */
    public Map<String, String> getHotelDetails(String hotelId) {
        Map<String, String> details = new HashMap<>();
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {

            statement = connection.prepareStatement(PreparedStatements.GET_HOTEL_DETAILS_SQL);
            statement.setString(1, hotelId);
            System.out.println(statement);
            ResultSet results = statement.executeQuery();

            if (results.next()) {
                details.put("hotelID", results.getString("hotelID"));
                details.put("hotelName", results.getString("hotelName"));
                details.put("address", results.getString("address"));
                details.put("city", results.getString("city"));
                details.put("state", results.getString("state"));
                details.put("lat", results.getString("lat"));
                details.put("lon", results.getString("lon"));
            }
            return details;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the reviews of hotels matching the hotelID.
     */
    public List<Map<String, String>> getHotelReviews(String hotelId) {
        List<Map<String, String>> reviews = new ArrayList<>();
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {

            statement = connection.prepareStatement(PreparedStatements.GET_HOTEL_REVIEWS_SQL);
            statement.setString(1, hotelId);
            System.out.println(statement);
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                Map<String, String> review = new HashMap<>();
                review.put("hotelID", results.getString("hotelID"));
                review.put("reviewID", results.getString("reviewID"));
                review.put("reviewTitle", results.getString("title"));
                review.put("reviewText", results.getString("reviewText"));
                review.put("username", results.getString("username"));
                review.put("reviewRating", results.getString("ratings"));
                review.put("reviewDate", results.getString("reviewSubmissionTime"));
                reviews.add(review);
            }
            return reviews;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the review if the hotelID and username matches.
     */
    public void updateReview(String reviewTitle, String reviewText, String reviewRating, String hotelId, String username) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(PreparedStatements.UPDATE_REVIEW_SQL);
            statement.setString(1, reviewTitle);
            statement.setString(2, reviewText);
            statement.setString(3, reviewRating);
            statement.setString(4, hotelId);
            statement.setString(5, username);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Deletes the review if the hotelID and username matches.
     */
    public void deleteReview(String reviewId) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(PreparedStatements.DELETE_REVIEW_SQL);
            statement.setString(1, reviewId);
            System.out.println(statement);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Inserts the expedia link into the user's expedia table.
     */
    public void insertExpediaTable(String username, String expediaLink) {
        if (linkExistsForUser(username, expediaLink)) return;

        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(PreparedStatements.INSERT_EXPEDIA_SQL);
            statement.setString(1, username);
            statement.setString(2, expediaLink);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }


    /** Clear the expedia link history for a given user. */
    public void clearExpediaHistory(String username) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(PreparedStatements.DELETE_EXPEDIA_LINKS_FOR_USERNAME);
            statement.setString(1, username);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /** Get the expedia link history for a given user. */
    public List<String> getExpediaLinksForUser(String username) {
        List<String> links = new ArrayList<>();
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(PreparedStatements.GET_EXPEDIA_LINKS_FOR_USERNAME);
            statement.setString(1, username);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                links.add(results.getString("link"));
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return links;
    }

    /**
     * Check if the link already exists for user;
     *
     */
    public boolean linkExistsForUser(String username, String expediaLink) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(PreparedStatements.EXPEDIA_LINK_EXIST_FOR_USERNAME);
            statement.setString(1, username);
            statement.setString(2, expediaLink);
            System.out.println(statement);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                int count = Integer.parseInt(results.getString("count(*)"));
                if (count > 0) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return false;
    }


    /**
     * Creates the expedia table.
     */
    public void createExpediaTable() {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(PreparedStatements.CREATE_EXPEDIA_TABLE);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    /** Create the favhotels table. */
    public void createFavHotelsTable() {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(PreparedStatements.CREATE_FAV_HOTEL_TABLE);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /** Get a list of hotels that the username favored. */
    public List<Hotel> getFavHotels(String username) {
        List<Hotel> favHotels = new ArrayList<>();
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(PreparedStatements.GET_FAV_HOTELS_FOR_USERNAME);
            statement.setString(1, username);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                String hotelId = results.getString("hotelID");
                Map<String, String> hotelDetails = getHotelDetails(hotelId);
                Hotel hotel = new Hotel(
                        hotelDetails.get("hotelName"),
                        hotelDetails.get("hotelID"),
                        hotelDetails.get("address"),
                        hotelDetails.get("city"),
                        hotelDetails.get("state"),
                        hotelDetails.get("lat"),
                        hotelDetails.get("lon")
                        );
                favHotels.add(hotel);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return favHotels;
    }

    /** Insert a favorite hotel for a user. */
    public void insertFavHotel(String username, String hotelId) {
        if (favHotelExistsForUser(username, hotelId)) return;

        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(PreparedStatements.INSERT_FAV_HOTEL_SQL);
            statement.setString(1, username);
            statement.setString(2, hotelId);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /** Clear all favored hotels. */
    public void clearFavHotelHistory(String username) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(PreparedStatements.DELETE_FAV_HOTELS_FOR_USERNAME);
            statement.setString(1, username);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }


    /** Check if the favorite hotel already exists for user. */
    public boolean favHotelExistsForUser(String username, String hotelId) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(PreparedStatements.FAV_HOTEL_EXIST_FOR_USERNAME);
            statement.setString(1, username);
            statement.setString(2, hotelId);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                int count = Integer.parseInt(results.getString("count(*)"));
                if (count > 0) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return false;
    }


}

