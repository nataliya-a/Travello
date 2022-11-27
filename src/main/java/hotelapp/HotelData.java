package hotelapp;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class HotelData {

    private final TreeMap<String, Hotel> hotelsMap = new TreeMap<>();


    /** given a list of hotels, add them to the map */
    public void hotelMapperNew(List<Hotel> hotels) {
        for (Hotel hotel : hotels) {
            String hotelId = hotel.getHotelID();
            hotelsMap.put(hotelId, hotel);
        }
    }

    /** given a hotelId, returns the hotel object*/

    public Hotel getHotelObjectNew(String strHotelId) {
        return hotelsMap.get(strHotelId);
    }

    /** adds all hotels to a list and returns it */
    public List<Hotel> getHotels() {
        List<Hotel> hotelNames = new ArrayList<>();
        for (String key : hotelsMap.keySet()) {
            Hotel hotel = hotelsMap.get(key);
            hotelNames.add(hotel);

        }
        return hotelNames;
    }

    /** returns an hotelMap */
    public TreeMap<String, Hotel> getHotelsMap() {
        return hotelsMap;
    }

    /** given a hotelId, return the String for hotel */
    public String getHotelDataNew(String strHotelId) {
        return hotelsMap.get(strHotelId).toString();
    }


}
