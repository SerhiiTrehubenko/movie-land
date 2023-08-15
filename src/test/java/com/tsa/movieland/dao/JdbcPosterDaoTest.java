package com.tsa.movieland.dao;

import com.tsa.movieland.dao.jdbc.JdbcPosterDao;
import com.tsa.movieland.entity.Poster;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcPosterDaoTest extends DaoBaseTest {

    @Autowired
    JdbcPosterDao posterDao;

    @Test
    void shouldSavePoster() {
        int movieId = 1125;
        String existPosterLink = " https://images-na.ssl-images-amazon.com/images/M/MV5BMTY3OTI5NDczN15BMl5BanBnXkFtZTcwNDA0NDY3Mw@@._V1._SX140_CR0,0,140,209_.jpg";
        String newPosterLink = "https://t4.ftcdn.net/jpg/00/97/58/97/360_F_97589769_t45CqXyzjz0KXwoBZT9PRaWGHRk5hQqQ.jpg";

        posterDao.addPoster(movieId, newPosterLink);

        final List<Poster> posters = (List<Poster>) posterDao.findPosterByMovieId(movieId);
        assertEquals(2, posters.size());
        assertEquals(existPosterLink, posters.get(0).getLink());
        assertEquals(newPosterLink, posters.get(1).getLink());
    }
}