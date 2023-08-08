package com.tsa.movieland.dao;

import com.tsa.movieland.entity.Review;
import com.tsa.movieland.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcReviewDaoITest extends DaoBaseTest {

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
}