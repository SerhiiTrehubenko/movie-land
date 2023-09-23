package com.tsa.movieland.dao.jdbc;

import com.tsa.movieland.common.RatingRequest;
import com.tsa.movieland.context.JdbcDao;
import com.tsa.movieland.dao.RatingDao;
import com.tsa.movieland.dao.jdbc.mapper.AvgRatingMapper;
import com.tsa.movieland.common.AvgRating;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.*;

@RequiredArgsConstructor
@JdbcDao
public class JdbcRatingDao implements RatingDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final AvgRatingMapper avgRatingDtoMapper;

    @Override
    public Iterable<AvgRating> fidAllAvgRatingsGroupedMovie() {
        String query = "SELECT movie_id, AVG(movie_raring) avg_rating," +
                " COUNT(movie_id) AS user_votes FROM movies_ratings" +
                " GROUP BY movie_id ORDER BY movie_id;";
        return namedParameterJdbcTemplate.query(query, avgRatingDtoMapper);
    }

    @Override
    public void saveBuffer(Iterator<RatingRequest> iterator) {
        String query = "INSERT INTO movies_ratings (movie_id, user_id, movie_raring) " +
                "VALUES (:movieId, (SELECT user_id FROM users where user_email=:userEmail), :rating) " +
                "ON CONFLICT ON CONSTRAINT compound_id DO UPDATE SET movie_raring = :rating;";

        Map<String, Object>[] preparedParams = getParams(iterator);

        namedParameterJdbcTemplate.batchUpdate(query, preparedParams);
    }

    @Override
    public void saveBuffer(Set<RatingRequest> avgRatings) {
        throw new NotImplementedException();
    }

    private Map<String, Object>[] getParams(Iterator<RatingRequest> iterator) {
        List<Map<String, Object>> params = new ArrayList<>();
        while (iterator.hasNext()) {
            RatingRequest movieRequest = iterator.next();
            params.add(Map.of("movieId", movieRequest.getMovieId(), "userEmail", movieRequest.getUserEmail(), "rating", movieRequest.getRating()));
            iterator.remove();
        }

        @SuppressWarnings("unchecked")
        Map<String, Object>[] preparedParams = params.toArray(new Map[0]);
        return preparedParams;
    }
}
