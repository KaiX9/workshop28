use boardgames;

    db.games.aggregate([
        {
            $match: { gid:18 }
        },
        {
            $lookup: {  from:"reviews",
                        localField:"gid",
                        foreignField:"gid",
                        as:"reviewsDocs" }
        },
        {
            $project: {_id: -1, gid: 1,
                name: 1, year: 1, ranking: 1, users_rated: 1, url: 1, image: 1,
                reviews: {
                    $map: {
                        input: "$reviewsDocs",
                        as: "review",
                        in: { $concat: ["/review/", "$$review.c_id"] }
                    }
                }, timestamp: "$$NOW" }
        },
    ]) 

    db.reviews.aggregate([
        {$match: {"user": "hibikir"}},
        {$sort: {"rating": -1}},
        {$limit: 1}
    ]);
    db.reviews.aggregate([
        {
            $match: {
                "user": "hibikir",
                "rating": maxRating
            }
        },
        {
            $lookup: {
                from: "games",
                localField: "gid",
                foreignField: "gid",
                as: "gameReviews"
            }
        },
        {
            $project: {
                _id: 1,
                c_id: 1,
                user: 1,
                rating: 1,
                c_text: 1,
                gid: 1,
                game_name: {$arrayElemAt: ["$gameReviews.name", 0]}
            }
        }
    ]);