package com.tsa.movieland.dao.jdbc;

import com.tsa.movieland.context.JdbcDao;
import com.tsa.movieland.dao.ReviewDao;
import com.tsa.movieland.dao.jdbc.mapper.ReviewUserMapper;
import com.tsa.movieland.dto.AddReviewRequest;
import com.tsa.movieland.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;

@RequiredArgsConstructor
@JdbcDao
public class JdbcReviewDao implements ReviewDao {

    private final ReviewUserMapper reviewUserMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Iterable<ReviewDto> findByMovieId(int movieId) {

        String queryReviews = "SELECT user_id, user_nickname, movie_comment " +
                "FROM review_with_user_by_movie_id " +
                "WHERE movie_id = :movieId;";
        return namedParameterJdbcTemplate.query(
                queryReviews,
                Map.of("movieId", movieId),
                reviewUserMapper
        );
    }

    @Override
    public void save(int userId, AddReviewRequest reviewRequest) {
        String saveReviewQuery = "INSERT INTO movie_reviews (movie_id, user_id, movie_comment) VALUES (:movieId, :userId, :text)";
        namedParameterJdbcTemplate
                .update(saveReviewQuery, Map.of(
                        "movieId", reviewRequest.getMovieId(),
                        "userId", userId,
                        "text", reviewRequest.getText()
                ));
    }
}
