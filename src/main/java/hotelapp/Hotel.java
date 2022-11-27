package hotelapp;
import com.google.gson.annotations.SerializedName;

/**
 * Class representing a hotel.
 */
public class Hotel {
    @SerializedName(value = "f")
    private String hotelName;

    @SerializedName(value="ad")
    private String address;

    @SerializedName(value="id")
    private String hotelID;

    @SerializedName(value="ci")
    private String city;

    @SerializedName(value="pr")
    private String state;


    private String lat;

    private String lon;


    /**
     * Constructor for Hotel.
     * @param hotelName name of the hotel
     * @param address address of the hotel
     * @param hotelID id of the hotel
     * @param city city of the hotel
     * @param state state of the hotel
     * @param lat latitude of the hotel
     * @param lon longitude of the hotel
     */

    public Hotel(String hotelName, String hotelID, String address, String city, String state, String lat, String lon) {
        this.hotelName = hotelName;
        this.hotelID = hotelID;
        this.address = address;
        this.city = city;
        this.state = state;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString() {
        String str = "";
        str += "********************" + System.lineSeparator();
        str += hotelName + ": " + hotelID + System.lineSeparator();
        str  += address + System.lineSeparator();
        str += city + ", " + state;
        return str;
    }


    /** Getters for the hotel class. */
    public String getHotelName() {
        return hotelName;
    }

    /**
     * Get the hotel ID.
     * @return hotel ID
     */
    public String getAddress() {
        return address;
    }


    /**
     * Get the hotel ID.
     * @return hotel ID
     */
    public String getHotelID() {
        return hotelID;
    }

    /** Get the city of the hotel. */
    public String getCity() {
        return city;
    }

    /** Get the state of the hotel. */
    public String getState() {
        return state;
    }

    /** Get the latitude of the hotel. */
    public String getLat() {
        return lat;
    }

    /** Get the longitude of the hotel. */
    public String getLon() {
        return lon;
    }

}



