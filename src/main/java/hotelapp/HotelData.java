package hotelapp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

public class HotelData {

    private final TreeMap<String, Hotel> hotelsMap = new TreeMap<>();

    private final Map<String, Map<LocalDate, Set<String>>> bookedDates = new HashMap<>();


    /**
     * Check if can book a room for a given hotelId.
     * @param hotelId hotel id
     * @param checkIn check in date
     * @param checkOut check out date
     * @return true if the room is available, false otherwise
     */
    public boolean canBook(String hotelId, LocalDate checkIn, LocalDate checkOut) {

        Map<LocalDate, Set<String>> hotelBookedDates = bookedDates.get(hotelId);
        if (hotelBookedDates == null) {
            return true;
        }
        for (LocalDate date = checkIn; date.isBefore(checkOut.plusDays(1)); date = date.plusDays(1)) {
            if (hotelBookedDates.containsKey(date)) {
                if (hotelBookedDates.get(date).size() >= 3) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Book a room for a given hotelId.
     * @param hotelId hotel id
     * @param checkIn check in date
     * @param checkOut check out date
     * @param userId user id
     */
    public void book(String hotelId, LocalDate checkIn, LocalDate checkOut, String userId) {
        if (!hotelsMap.containsKey(hotelId)) {
            return;
        }
        Map<LocalDate, Set<String>> hotelBookedDates = bookedDates.get(hotelId);
        if (hotelBookedDates == null) {
            hotelBookedDates = new HashMap<>();
            bookedDates.put(hotelId, hotelBookedDates);
        }
        for (LocalDate date = checkIn; date.isBefore(checkOut.plusDays(1)); date = date.plusDays(1)) {
            if (!hotelBookedDates.containsKey(date)) {
                hotelBookedDates.put(date, new HashSet<>());
            }
            hotelBookedDates.get(date).add(userId);
        }
    }


    /** given a list of hotels, add them to the map */
    public void hotelMapperNew(List<Hotel> hotels) {
        for (Hotel hotel : hotels) {
            String hotelId = hotel.getHotelID();
            hotelsMap.put(hotelId, hotel);
        }
    }

    public void addHotelToDB(List<Hotel> hotels) {
        DatabaseHandler db = DatabaseHandler.getInstance();
        for (Hotel hotel : hotels) {
            String hotelId = hotel.getHotelID();
            String hotelName = hotel.getHotelName();
            String address = hotel.getAddress();
            String city = hotel.getCity();
            String state = hotel.getState();
            String lat = hotel.getLat();
            String lon = hotel.getLon();
            db.insertHotel(hotelId, hotelName, address, city, state, lat, lon);
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

    /** given a hotelId, return the all booked dates */
    public ArrayList<LocalDate> getAllBookedDates(String hotelId) {
        Map<LocalDate, Set<String>> hotelBookedDates = bookedDates.get(hotelId);
        if (hotelBookedDates == null) {
            return new ArrayList<>();
        }
        System.out.println(hotelBookedDates);
        return new ArrayList<>(hotelBookedDates.keySet());

    }

    /**  given a hotelId and userId, return the all booked dates */
    public ArrayList<LocalDate> getUserBookedDates(String hotelId, String userId) {
        Map<LocalDate, Set<String>> hotelBookedDates = bookedDates.get(hotelId);
        if (hotelBookedDates == null) {
            return new ArrayList<>();
        }

        ArrayList<LocalDate> bookedDates = new ArrayList<>();
        for (LocalDate date : hotelBookedDates.keySet()) {
            if (hotelBookedDates.get(date).contains(userId)) {
                bookedDates.add(date);
            }
        }
        bookedDates.sort(Comparator.reverseOrder());
        return bookedDates;
    }




}
