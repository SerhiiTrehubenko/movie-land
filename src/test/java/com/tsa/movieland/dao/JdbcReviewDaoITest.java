package com.tsa.movieland.dao;

import com.tsa.movieland.CommonContainer;
import com.tsa.movieland.dto.AddReviewRequest;
import com.tsa.movieland.entity.Review;
import com.tsa.movieland.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcReviewDaoITest extends CommonContainer {

    @Autowired
    ReviewDao reviewDao;

    @Test
    void shouldReturnReviewsByMovieId() {
        List<Review> reviews = (List<Review>) reviewDao.findByMovieId(1121);
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

        AddReviewRequest reviewTest = AddReviewRequest.builder().movieId(movieId).text(textTest).build();

        reviewDao.save(userId, reviewTest);

        final List<Review> reviews = (List<Review>) reviewDao.findByMovieId(movieId);
        assertEquals(2, reviews.size());

        assertEquals(textTest, reviews.get(0).getText());
        assertEquals(existedText, reviews.get(1).getText());
    }
}