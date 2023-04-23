package sg.edu.nus.iss.workshop28.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.workshop28.model.Game;
import sg.edu.nus.iss.workshop28.model.Review;
import sg.edu.nus.iss.workshop28.model.ReviewResult;
import sg.edu.nus.iss.workshop28.service.BoardGameService;

@RestController
@RequestMapping(path="/game")
public class BoardGameRestController {
    
    @Autowired
    private BoardGameService bgSvc;

    @GetMapping(path="/{gameId}/reviews")
    public ResponseEntity<String> getHistoricalGameReviews(@PathVariable String gameId) {
        
        Optional<Game> r = this.bgSvc.aggregateGameReviews(gameId);

        if (r.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("Game with game id -> %s not found".formatted(gameId));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(r.get().toJSON().toString());
    }

    @GetMapping(path="/highest")
    public ResponseEntity<String> getHighestGameReviewsByUser(@RequestParam String user, String rating) {

        rating = "highest";
        List<Review> highestList = this.bgSvc.aggregateHighestLowestGameReviewsByUser(user, rating);
        ReviewResult rr = new ReviewResult();
        rr.setRating(rating);
        rr.setTimestamp(LocalDateTime.now());
        rr.setGames(highestList);

        if (highestList == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("User of name %s not found".formatted(user));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(rr.toJSON().toString());
    }

    @GetMapping(path="/lowest")
    public ResponseEntity<String> getLowestGameReviewsByUser(@RequestParam String user, String rating) {

        rating = "lowest";
        List<Review> lowestList = this.bgSvc.aggregateHighestLowestGameReviewsByUser(user, rating);
        ReviewResult rr = new ReviewResult();
        rr.setRating(rating);
        rr.setTimestamp(LocalDateTime.now());
        rr.setGames(lowestList);

        if (lowestList == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("User of name %s not found".formatted(user));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(rr.toJSON().toString());
    }
}
