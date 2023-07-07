package com.tsa.movieland.dao;

import com.tsa.movieland.entity.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MovieDaoITest {

    @Autowired
    MovieDao movieDao;

    @Test
    void shouldReturnListOfMovies() {
        List<Movie> movies = movieDao.findAll();

        assertNotNull(movies);
        assertEquals(25, movies.size());
    }

    @Test
    void shouldReturnThreeRandomMovies() {
        List<Movie> movies = movieDao.findThreeRandom();

        assertNotNull(movies);
        assertEquals(3, movies.size());
    }

    @Test
    void shouldReturnMoviesByGenre() {
        List<Movie> movies = movieDao.findByGenreId(2);

        assertNotNull(movies);
        assertEquals(7, movies.size());
    }
}
