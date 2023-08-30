package com.tsa.movieland.dao.jdbc;

import com.tsa.movieland.common.RatingRequest;
import com.tsa.movieland.dao.RatingDao;
import com.tsa.movieland.dao.jdbc.mapper.AvgRatingMapper;
import com.tsa.movieland.common.AvgRating;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class JdbcRatingDao implements RatingDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final AvgRatingMapper avgRatingDtoMapper;

    @Override
    public Iterable<AvgRating> fidAllAvgRatingsGroupedMovie() {
        String query = "SELECT movie_id, AVG(movie_raring) avg_rating," +
                " ARRAY_AGG((SELECT u.user_email FROM users u WHERE u.user_id = mr.user_id)) AS user_emails," +
                " ARRAY_AGG(movie_raring) AS user_votes FROM movies_ratings mr" +
                " GROUP BY movie_id ORDER BY movie_id;";
        return namedParameterJdbcTemplate.query(query, avgRatingDtoMapper);
    }

    @Override
    public void saveBuffer(Set<Map.Entry<String, List<RatingRequest>>> entrySet) {
        String query = "INSERT INTO movies_ratings (movie_id, user_id, movie_raring) " +
                "VALUES (:movieId, (SELECT user_id FROM users where user_email=:userEmail), :rating) " +
                "ON CONFLICT ON CONSTRAINT compound_id DO UPDATE SET movie_raring = :rating;";

        List<Map<String, Object>> params = new ArrayList<>();
        entrySet.forEach(entry -> params.addAll(createParams(entry)));
        @SuppressWarnings("unchecked")
        Map<String, Object>[] preparedParams = params.toArray(new Map[0]);

        namedParameterJdbcTemplate.batchUpdate(query, preparedParams);
    }

    private List<Map<String, Object>> createParams(Map.Entry<String, List<RatingRequest>> entry) {
        String userEmail = entry.getKey();
        List<RatingRequest> ratingRequests = entry.getValue();
        int size = ratingRequests.size();
        List<Map<String, Object>> params = new ArrayList<>(size);
        for (RatingRequest movieRequest : ratingRequests) {
            params.add(Map.of("movieId", movieRequest.getMovieId(), "userEmail", userEmail, "rating", movieRequest.getRating()));
        }
        return params;
    }
}
