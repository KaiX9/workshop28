package sg.edu.nus.iss.workshop28.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation.AddFieldsOperationBuilder;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.workshop28.model.Game;
import sg.edu.nus.iss.workshop28.model.Review;

@Repository
public class BoardGameRepository {
    
    @Autowired
    MongoTemplate mongoTemplate;

    public Optional<Game> aggregateGameReviews(String gameId) {
        // match against the game id -----> games collection
        MatchOperation matchGameId = Aggregation.match(
            Criteria.where("gid").is(Integer.parseInt(gameId)));
        // lookup is used to establish relationship reviews <-----> games col
        LookupOperation lookUpOps = Aggregation.lookup("reviews", 
            "gid", "gid", "reviewsDocs");

        ProjectionOperation projectOps = Aggregation.project("_id", "gid", "name", "year", 
            "ranking", "users_rated", "url", "image")
            .and(new AggregationExpression() {
                @Override
                public Document toDocument(AggregationOperationContext context) {
                    return new Document("$map",
                        new Document("input", "$reviewsDocs")
                        .append("as", "review")
                        .append("in", new Document("$concat"
                            , Arrays.asList("/review/", "$$review.c_id"))));
                }
            }).as("reviews");

        AddFieldsOperationBuilder a = Aggregation.addFields();
        a.addFieldWithValue("timestamp", LocalDateTime.now());
        AddFieldsOperation newFieldOps = a.build();

        Aggregation pipeline = Aggregation.newAggregation(matchGameId, lookUpOps, projectOps, newFieldOps);

        AggregationResults<Document> r = mongoTemplate.aggregate(pipeline, "games", Document.class);

        if (!r.iterator().hasNext()) {
            return Optional.empty();
        }

        Document doc = r.iterator().next();
        Game g = Game.createFromDoc(doc);
        return Optional.of(g);
    }

    public List<Review> aggregateHighestLowestGameReviewsByUser(String user, String rating) {

        try {
            int actualRating = 0;

            if (rating == "highest") {
                Aggregation maxRatingAggregation = Aggregation.newAggregation
                    (Aggregation.match(Criteria.where("user").is(user)),
                    Aggregation.sort(Sort.Direction.DESC, "rating"),
                    Aggregation.limit(1));
                AggregationResults<Document> maxRatingResults = mongoTemplate
                    .aggregate(maxRatingAggregation, "reviews", Document.class);
                actualRating = maxRatingResults.getUniqueMappedResult().getInteger("rating");
                System.out.println("Highest Rating: " + actualRating);
            } else if (rating == "lowest") {
                Aggregation minRatingAggregation = Aggregation.newAggregation
                    (Aggregation.match(Criteria.where("user").is(user)),
                    Aggregation.sort(Sort.Direction.ASC, "rating"),
                    Aggregation.limit(1));
                AggregationResults<Document> minRatingResults = mongoTemplate
                    .aggregate(minRatingAggregation, "reviews", Document.class);
                actualRating = minRatingResults.getUniqueMappedResult().getInteger("rating");
                System.out.println("Lowest Rating: " + actualRating);
            }
        
        MatchOperation mOp = Aggregation.match(Criteria.where("user").is(user)
            .and("rating").is(actualRating));
        
        LookupOperation lOp = Aggregation.lookup("games",
            "gid", "gid", "gameReviews");
        ProjectionOperation pOp = Aggregation.project("_id", "c_id", "user", "rating",
            "c_text", "gid").and(ArrayOperators.ArrayElemAt
            .arrayOf("gameReviews.name")
            .elementAt(0))
            .as("game_name");

        Aggregation pipeline = Aggregation.newAggregation(mOp, lOp,
            pOp);

        AggregationResults<Document> r = mongoTemplate.aggregate(pipeline, "reviews",
        Document.class);
        
        List<Document> reviews = r.getMappedResults();
        System.out.println(r.getMappedResults());
        List<Review> reviewList = new ArrayList<>();
        for (Document d : reviews) {
            Review re = Review.createFromDoc(d);
            reviewList.add(re);
        }
        
        return reviewList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to get fields from different collections if required
    
    // public String getGameName(String c_id) {

    //     Query q1 = new Query();
    //     q1.addCriteria(Criteria.where("c_id").is(c_id));
    //     q1.fields().include("gid");
    //     List<Review> r = mongoTemplate.find(q1, Review.class, "reviews");
    //     Integer gid = r.get(0).getGid();

    //     Query q2 = new Query();
    //     q2.addCriteria(Criteria.where("gid").is(gid));
    //     q2.fields().include("name");
    //     List<Game> g = mongoTemplate.find(q2, Game.class, "games");
    //     String name = g.get(0).getName();

    //     return name;
    // }

}
