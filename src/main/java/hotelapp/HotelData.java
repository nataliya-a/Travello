package hotelapp;

import java.util.List;
import java.util.TreeMap;

public class HotelData {
    //private static final HashMap<String, String> hotelsMap = new HashMap<>();

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

    public Hotel getHotelObject(String strHotelId) {
        return hotelsMap.get(strHotelId);
    }


    /** given a hotelId, return the hotel */

    public Hotel getHotelObjectNew(String strHotelId) {
        return hotelsMapNew.get(strHotelId);
    }


    /** given a hotelId, return the hotel */

    public String getHotelData(String strHotelId) {
        if (hotelsMap.containsKey(strHotelId)) {
            //System.out.println(hotelsSet.get(strHotelId));
            return hotelsMap.get(strHotelId).toString();
        }
        else {
            //System.out.println("ID does not exist");
            return "ID does not exist";
        }
    }

    /** returns all hotel IDs */
    public String[] getAllHotelIDs() {
        String[] keys = new String[hotelsMap.keySet().size()];
        int i = 0;
        for (String key : hotelsMap.keySet()) {
            keys[i] = key;
            i++;
        }
        return keys;

    }


    public TreeMap<String, Hotel> getHotelsMap() {
        return hotelsMapNew;
    }

    public String getHotelDataNew(String strHotelId) {
        return hotelsMapNew.get(strHotelId).toString();
    }

    public static List<Hotel> trigerHotelData() {
        HotelParser hp = new HotelParser();
        List<Hotel> hotels = hp.parseHotelDataNew("input/hotels/hotels.json");
        return hotels;
//        ThreadSafeHotelData hd = new ThreadSafeHotelData();
//        hd.hotelMapperNew(hotels);

    }

}
