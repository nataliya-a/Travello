package hotelapp;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class HotelData {

    private static final TreeMap<String, Hotel> hotelsMap = new TreeMap<>();

    private final TreeMap<String, Hotel> hotelsMapNew = new TreeMap<>();


    /** given a list of hotels, add them to the map */

    public void addHotels(Hotel[] hotels) {
        for (Hotel hotel : hotels) {
            String hotelId = hotel.getHotelID();
            hotelsMap.put(hotelId, hotel);
        }
    }

    /** given a list of hotels, add them to the map */

    public void hotelMapperNew(List<Hotel> hotels) {
        for (Hotel hotel : hotels) {
            String hotelId = hotel.getHotelID();
            hotelsMapNew.put(hotelId, hotel);
        }
    }

    /** given a hotelId, return the hotel */



    /** given a hotelId, return the hotel */

    public Hotel getHotelObjectNew(String strHotelId) {
        return hotelsMapNew.get(strHotelId);
    }


    /** given a hotelId, return the hotel */

    public String getHotelData(String strHotelId) {
        if (hotelsMap.containsKey(strHotelId)) {
            return hotelsMap.get(strHotelId).toString();
        }
        else {
            return "ID does not exist";
        }
    }


    public List<Hotel> getHotels() {
        List<Hotel> hotelNames = new ArrayList<>();
        for (String key : hotelsMap.keySet()) {
            Hotel hotel = hotelsMap.get(key);
            hotelNames.add(hotel);

        }
        return hotelNames;
    }


    public TreeMap<String, Hotel> getHotelsMap() {
        return hotelsMapNew;
    }

    public String getHotelDataNew(String strHotelId) {
        return hotelsMapNew.get(strHotelId).toString();
    }




}
