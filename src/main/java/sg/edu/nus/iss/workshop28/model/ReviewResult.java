package sg.edu.nus.iss.workshop28.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class ReviewResult {
    
    private String rating;
    private LocalDateTime timestamp;
    private List<Review> games;

    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public List<Review> getGames() {
        return games;
    }
    public void setGames(List<Review> games) {
        this.games = games;
    }
    
    public JsonObject toJSON() {
        
        JsonArray gamesArr = null;
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        
        for (Review r : getGames()) {
            arrBuilder.add(r.toJSON());
        }

        gamesArr = arrBuilder.build();
        
        return Json.createObjectBuilder()
                .add("rating", getRating())
                .add("timestamp", getTimestamp().toString())
                .add("games", gamesArr.toString())
                .build();
    }

    @Override
    public String toString() {
        return "ReviewResult [rating=" + rating + ", timestamp=" + timestamp + ", games=" + games + "]";
    }

}
