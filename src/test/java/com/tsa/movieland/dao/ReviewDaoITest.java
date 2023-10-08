package com.tsa.movieland.dao;

import com.tsa.movieland.CommonContainer;
import com.tsa.movieland.dto.UserDto;
import com.tsa.movieland.entity.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReviewDaoITest extends CommonContainer {

    @Autowired
    ReviewDao reviewDao;

    @Test
    void shouldReturnReviewsByMovieId() {
        List<Review> reviews = reviewDao.findByMovieId(1121);
        assertEquals(1, reviews.size());
        assertEquals("Для воскресного вечернего просмотра подходит по всем критериям.",
                reviews.get(0).getText());

        UserDto userDto = reviews.get(0).getUser();
        assertNotNull(userDto);
        assertEquals(1000005, userDto.getId());
        assertEquals("exodus", userDto.getNickname());
    }

    @Test
    void shouldAddNewReview() {
        int userId = 1000003;
        int movieId = 1121;
        String textTest = "review TEST";
        String existedText = "Для воскресного вечернего просмотра подходит по всем критериям.";

        Review reviewTest = Review.builder()
                .reviewId(Review.ReviewId.builder().movieId(movieId).userId(userId).build())
                .text(textTest)
                .recordTime(new Timestamp(System.currentTimeMillis()))
                .build();
        reviewDao.save(reviewTest);

        List<Review> reviews = reviewDao.findByMovieId(movieId);
        assertEquals(2, reviews.size());

        assertEquals(existedText, reviews.get(0).getText());
        assertEquals(textTest, reviews.get(1).getText());
    }
}