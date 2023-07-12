package com.tsa.movieland.dao;

import com.tsa.movieland.entity.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MovieDaoITest {

    @Autowired
    MovieDao movieDao;

    @Test
    void shouldReturnListOfMovies() {
        Iterable<Movie> movies = movieDao.findAll();

        assertNotNull(movies);

        long numberMovies = StreamSupport.stream(movies.spliterator(), false).count();
        assertEquals(25, numberMovies);
    }

    @Test
    void shouldReturnThreeRandomMovies() {
        Iterable<Movie> movies = movieDao.findRandom();

        assertNotNull(movies);

        long numberMovies = StreamSupport.stream(movies.spliterator(), false).count();
        assertEquals(3, numberMovies);
    }

    @Test
    void shouldReturnMoviesByGenre() {
        Iterable<Movie> movies = movieDao.findByGenreId(2);

        assertNotNull(movies);

        long numberMovies = StreamSupport.stream(movies.spliterator(), false).count();
        assertEquals(7, numberMovies);
    }
}
