package sg.edu.nus.iss.workshop28.model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class Game {
    
    private Integer gid;
    private String name;
    private Integer year;
    private Integer ranking;
    private Integer users_rated;
    private String url;
    private String image;
    private LocalDateTime timestamp;
    private String[] reviews;
    private String average;

    public String getAverage() {
        return average;
    }
    public void setAverage(String average) {
        this.average = average;
    }
    public Integer getGid() {
        return gid;
    }
    public void setGid(Integer gid) {
        this.gid = gid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }
    public Integer getRanking() {
        return ranking;
    }
    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }
    public Integer getUsers_rated() {
        return users_rated;
    }
    public void setUsers_rated(Integer users_rated) {
        this.users_rated = users_rated;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public String[] getReviews() {
        return reviews;
    }
    public void setReviews(String[] reviews) {
        this.reviews = reviews;
    }
    
    public static Game createFromDoc(Document d) {
        Game game = new Game();
        List<String> reviewsArr = (List<String>)d.get("reviews");
        game.setReviews(reviewsArr.toArray(new String[0]));
        
        game.setGid(d.getInteger("gid"));
        game.setName(d.getString("name"));
        game.setYear(d.getInteger("year"));
        game.setRanking(d.getInteger("ranking"));
        if (d.containsKey("average")) {
            game.setAverage(d.getString("average"));
        } else {
            game.setAverage(null);
        }
        game.setUsers_rated(d.getInteger("users_rated"));
        game.setUrl(d.getString("url"));
        game.setImage(d.getString("image"));
        game.setTimestamp(LocalDateTime.now());
        
        return game;
    }

    public JsonObject toJSON() {
        JsonArrayBuilder reviewsArrBuilder = Json.createArrayBuilder();
        for (String review : this.getReviews()) {
            reviewsArrBuilder.add(review);
        }
        JsonArray reviewsArray = reviewsArrBuilder.build();
        
        return Json.createObjectBuilder()
                .add("game_id", getGid())
                .add("name", getName())
                .add("year", getYear())
                .add("rank", getRanking())
                .add("average", (getAverage() != null)
                    ? getAverage().toString() : "N/A")
                .add("users_rated", (getUsers_rated() != null)
                    ? getUsers_rated().toString() : "N/A")
                .add("url", getUrl())
                .add("thumbnail", getImage())
                .add("reviews", reviewsArray)
                .add("timestamp", getTimestamp().toString())
                .build();
    }

    @Override
    public String toString() {
        return "Game [gid=" + gid + ", name=" + name + ", year=" + year + ", ranking=" + ranking + ", users_rated="
                + users_rated + ", url=" + url + ", image=" + image + ", timestamp=" + timestamp + ", reviews="
                + Arrays.toString(reviews) + ", average=" + average + "]";
    }
    
}
