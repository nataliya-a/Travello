package hotelapp;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ReviewParser {

    private final List<String> paths = new ArrayList<>();

    private final List<Review> reviews = new ArrayList<>();

    /**
     * Method to parse a single review file
     * @param path the path to the directory containing the review files
     */

    public List<Review> parseSingleReviewFile(String path) {
        try (FileReader br = new FileReader(path)) {
            JsonParser parser = new JsonParser();
            JsonObject jo = (JsonObject) parser.parse(br);
            JsonObject reviewDetails = jo.get("reviewDetails").getAsJsonObject();
            JsonObject reviewCollection = reviewDetails.get("reviewCollection").getAsJsonObject();
            JsonArray jsonArr = reviewCollection.getAsJsonArray("review");
            for (JsonElement elem : jsonArr) {
                JsonObject reviewObj = elem.getAsJsonObject();
                String hotelId = reviewObj.get("hotelId").getAsString();
                String reviewId = reviewObj.get("reviewId").getAsString();
                String ratingOverall = reviewObj.get("ratingOverall").getAsString();
                String title = reviewObj.get("title").getAsString();
                String reviewText = reviewObj.get("reviewText").getAsString();
                String userNickname = reviewObj.get("userNickname").getAsString();
                String reviewSubmissionTime = reviewObj.get("reviewSubmissionTime").getAsString();
                Review r = new Review(hotelId, reviewText, userNickname, title, reviewId, ratingOverall, reviewSubmissionTime);
                reviews.add(r);
            }
        }catch (IOException e){
            System.out.println("Could not read the file: " + e);
        }


        return reviews;
    }

    /**
     * Method to get the path of a single file in a directory
     * @param directory the path to the directory containing the review files
     */

    public List<String> filePath(String directory) {

        Path p = Paths.get(directory);

        try(DirectoryStream<Path> pathsInDir = Files.newDirectoryStream(p)) {
            for (Path path : pathsInDir) {
                if (path.toString().endsWith(".json")) {
                    paths.add(path.toString());
                    //parseJson(path.toString());
                } else {
                    filePath(path.toString());
                }
            }
        } catch (IOException e) {
            System.out.println("Can not open directory: " + directory);
        }
        return paths;
    }



    public List<Review> parseReviewsFiles(List<String> paths) {
        for (String path : paths) {
            try (FileReader br = new FileReader(path)) {
                JsonParser parser = new JsonParser();
                JsonObject jo = (JsonObject) parser.parse(br);
                JsonObject reviewDetails = jo.get("reviewDetails").getAsJsonObject();
                JsonObject reviewCollection = reviewDetails.get("reviewCollection").getAsJsonObject();
                JsonArray jsonArr = reviewCollection.getAsJsonArray("review");
                for (JsonElement elem : jsonArr) {
                    JsonObject reviewObj = elem.getAsJsonObject();
                    String hotelId = reviewObj.get("hotelId").getAsString();
                    String reviewId = reviewObj.get("reviewId").getAsString();
                    String ratingOverall = reviewObj.get("ratingOverall").getAsString();
                    String title = reviewObj.get("title").getAsString();
                    String reviewText = reviewObj.get("reviewText").getAsString();
                    String userNickname = reviewObj.get("userNickname").getAsString();
                    String reviewSubmissionTime = reviewObj.get("reviewSubmissionTime").getAsString();
                    Review r = new Review(hotelId, reviewText, userNickname, title, reviewId, ratingOverall, reviewSubmissionTime);
                    reviews.add(r);
                }
            }catch (IOException e){
                System.out.println("Could not read the file: " + e);
            }

        }
        return reviews;
    }
}
