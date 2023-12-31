package com.tsa.movieland.dao;

import com.tsa.movieland.common.RatingRequest;
import com.tsa.movieland.CommonContainer;
import com.tsa.movieland.dao.jdbc.JdbcRatingDao;
import com.tsa.movieland.common.AvgRating;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JdbcRatingDaoTest extends CommonContainer {

    @Autowired
    private JdbcRatingDao jdbcRatingDao;

    @Test
    void shouldSaveEntrySetAsRatingsBuffer() {
        int movieId1111 = 10;
        int movieId1112 = 11;
        int movieId1122 = 21;
        int movieId1123 = 22;

        List<AvgRating> ratingsBeforeInsertion = (List<AvgRating>) jdbcRatingDao.fidAllAvgRatingsGroupedMovie();
        assertEquals(25, ratingsBeforeInsertion.size());
        assertEquals(8.60, ratingsBeforeInsertion.get(movieId1111).getCurrentAvg());
        assertEquals(7.90, ratingsBeforeInsertion.get(movieId1112).getCurrentAvg());
        assertEquals(7.70, ratingsBeforeInsertion.get(movieId1122).getCurrentAvg());
        assertEquals(7.60, ratingsBeforeInsertion.get(movieId1123).getCurrentAvg());

        Set<RatingRequest> ratings = new HashSet<>(ratingsOfDennis());
        ratings.addAll(ratingsOfTravis());
        jdbcRatingDao.saveBuffer(ratings.iterator());
        List<AvgRating> ratingsAfterInsertion = (List<AvgRating>) jdbcRatingDao.fidAllAvgRatingsGroupedMovie();
        assertEquals(25, ratingsAfterInsertion.size());
        assertEquals(6.30, ratingsAfterInsertion.get(movieId1111).getCurrentAvg());
        assertEquals(5.45, ratingsAfterInsertion.get(movieId1112).getCurrentAvg());
        assertEquals(6.35, ratingsAfterInsertion.get(movieId1122).getCurrentAvg());
        assertEquals(7.30, ratingsAfterInsertion.get(movieId1123).getCurrentAvg());
    }

    Set<RatingRequest> ratingsOfDennis() {
        String dennisEmail = "dennis.craig82@example.com";
        RatingRequest ratingFirst = RatingRequest.builder()
                .userEmail(dennisEmail)
                .movieId(1122)
                .rating(5)
                .build();
        RatingRequest ratingSecond = RatingRequest.builder()
                .userEmail(dennisEmail)
                .movieId(1123)
                .rating(7)
                .build();
        return Set.of(ratingFirst, ratingSecond);
    }

    Set<RatingRequest> ratingsOfTravis() {
        String travisEmail = "travis.wright36@example.com";
        RatingRequest ratingFirst = RatingRequest.builder()
                .userEmail(travisEmail)
                .movieId(1111)
                .rating(4)
                .build();
        RatingRequest ratingSecond = RatingRequest.builder()
                .userEmail(travisEmail)
                .movieId(1112)
                .rating(3)
                .build();
        return Set.of(ratingFirst, ratingSecond);
    }
}