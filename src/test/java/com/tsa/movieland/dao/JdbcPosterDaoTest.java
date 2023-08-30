package com.tsa.movieland.dao;

import com.tsa.movieland.CommonContainer;
import com.tsa.movieland.dao.jdbc.JdbcPosterDao;
import com.tsa.movieland.dto.PosterDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcPosterDaoTest extends CommonContainer {

    @Autowired
    JdbcPosterDao posterDao;

    @Test
    void shouldSavePoster() {
        int movieId = 1125;
        String existPosterLink = " https://images-na.ssl-images-amazon.com/images/M/MV5BMTY3OTI5NDczN15BMl5BanBnXkFtZTcwNDA0NDY3Mw@@._V1._SX140_CR0,0,140,209_.jpg";
        String newPosterLink = "https://t4.ftcdn.net/jpg/00/97/58/97/360_F_97589769_t45CqXyzjz0KXwoBZT9PRaWGHRk5hQqQ.jpg";

        posterDao.add(movieId, newPosterLink);

        final List<PosterDto> posterDtos = (List<PosterDto>) posterDao.findPosterByMovieId(movieId);
        assertEquals(2, posterDtos.size());
        assertEquals(existPosterLink, posterDtos.get(0).getLink());
        assertEquals(newPosterLink, posterDtos.get(1).getLink());
    }

    @Test
    void shouldUpdatePosterByMovieIdWhenMovieHasOnePoster() {
        int movieId = 1125;
        String newPosterLinkWithPosterNumber = "https://t4.ftcdn.net/jpg/00/97/58/97/360_F_97589769_t45CqXyzjz0KXwoBZT9PRaWGHRk5hQqQ.jpg; 0";

        String oldPosterLink = " https://images-na.ssl-images-amazon.com/images/M/MV5BMTY3OTI5NDczN15BMl5BanBnXkFtZTcwNDA0NDY3Mw@@._V1._SX140_CR0,0,140,209_.jpg";
        String newPosterLink = "https://t4.ftcdn.net/jpg/00/97/58/97/360_F_97589769_t45CqXyzjz0KXwoBZT9PRaWGHRk5hQqQ.jpg";

        posterDao.update(movieId, newPosterLinkWithPosterNumber);

        final List<PosterDto> posterDtos = (List<PosterDto>) posterDao.findPosterByMovieId(movieId);
        assertEquals(1, posterDtos.size());
        assertEquals(newPosterLink, posterDtos.get(0).getLink());
        assertNotEquals(oldPosterLink, posterDtos.get(0).getLink());
    }

    @Test
    void shouldUpdatePosterByMovieIdWhenMovieHasMoreThenOnePosters() {
        int movieId = 1125;
        String newPosterLinkWithPosterNumber = "https://images.unsplash.com/photo-1525609004556-c46c7d6cf023?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NXx8Y2Fyc3xlbnwwfHwwfHx8MA%3D%3D&w=1000&q=80; 0";
        String newPoster = "https://images.unsplash.com/photo-1525609004556-c46c7d6cf023?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NXx8Y2Fyc3xlbnwwfHwwfHx8MA%3D%3D&w=1000&q=80";

        String oldPosterLinkFirst = " https://images-na.ssl-images-amazon.com/images/M/MV5BMTY3OTI5NDczN15BMl5BanBnXkFtZTcwNDA0NDY3Mw@@._V1._SX140_CR0,0,140,209_.jpg";
        String newPosterLinkSecond = "https://t4.ftcdn.net/jpg/00/97/58/97/360_F_97589769_t45CqXyzjz0KXwoBZT9PRaWGHRk5hQqQ.jpg";

        posterDao.add(movieId, newPosterLinkSecond);

        posterDao.update(movieId, newPosterLinkWithPosterNumber);

        final List<PosterDto> posterDtos = (List<PosterDto>) posterDao.findPosterByMovieId(movieId);
        assertEquals(2, posterDtos.size());
        assertNotEquals(oldPosterLinkFirst, posterDtos.get(0).getLink());

        assertEquals(newPoster, posterDtos.get(0).getLink());
        assertEquals(newPosterLinkSecond, posterDtos.get(1).getLink());
    }
}