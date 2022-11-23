package hotelapp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that parses the review data and stores the data in a data structure.
 */
public class HotelParser { // HotelData

    /**
     * Parses the hotel data and stores the data in a data structure.
     * @param filePath
     * @return List of hotels
     */
    public List<Hotel> parseHotelDataNew(String filePath) {
        Gson gson = new Gson();
        List<Hotel> hotelsNew = new ArrayList<>();
        try (JsonReader reader = new JsonReader(new FileReader(filePath))) {

            JsonObject jo = gson.fromJson(reader, JsonObject.class);
            JsonArray jsonArr = jo.getAsJsonArray("sr");
            for (int i = 0; i < jsonArr.size(); i++) {
                JsonObject job = (JsonObject) jsonArr.get(i);
                JsonObject ll = (JsonObject) job.get("ll");
                String hotelName = job.get("f").getAsString();
                String hotelID = job.get("id").getAsString();
                String address = job.get("ad").getAsString();
                String city = job.get("ci").getAsString();
                String state = job.get("pr").getAsString();
                String lat = ll.get("lat").getAsString();
                String lon = ll.get("lng").getAsString();
                Hotel hotel = new Hotel(hotelName, hotelID, address, city, state, lat, lon);
                hotelsNew.add(hotel);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return hotelsNew;
    }
}


