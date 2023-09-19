package com.tsa.movieland.repository;

import com.tsa.movieland.common.RatingRequest;
import com.tsa.movieland.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@com.tsa.movieland.context.JpaRepository
public interface RatingRepository extends JpaRepository<Rating, Rating.PrimaryKey> {

    @Query(value = "SELECT movie_id movieId, AVG(movie_raring) currentAvg," +
            " COUNT(movie_id) AS userVotes FROM movies_ratings" +
            " GROUP BY movie_id ORDER BY movie_id;", nativeQuery = true)
    List<AverageRating> getAllAvgRating();

    @Query(value = "INSERT INTO movies_ratings (movie_id, user_id, movie_raring) " +
            "VALUES (:#{#ratingRequest.getMovieId()}, (SELECT user_id FROM users where user_email=:#{#ratingRequest.getUserEmail()}), :#{#ratingRequest.getRating()}) " +
            "ON CONFLICT ON CONSTRAINT compound_id DO UPDATE SET movie_raring = :#{#ratingRequest.getRating()}", nativeQuery = true)
    @Modifying
    void saveRatingRequest(@Param("ratingRequest") RatingRequest ratingRequest);

    interface AverageRating {
        Integer getMovieId();
        Double getCurrentAvg();
        Integer getUserVotes();
    }
}
