package sg.edu.nus.iss.workshop28.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.workshop28.model.Game;
import sg.edu.nus.iss.workshop28.model.Review;
import sg.edu.nus.iss.workshop28.repository.BoardGameRepository;

@Service
public class BoardGameService {
    
    @Autowired
    private BoardGameRepository bgRepo;

    public Optional<Game> aggregateGameReviews(String gameId) {
        return this.bgRepo.aggregateGameReviews(gameId);
    }

    public List<Review> aggregateHighestLowestGameReviewsByUser(String user, String rating) {
        return this.bgRepo.aggregateHighestLowestGameReviewsByUser(user, rating);
    }
}
