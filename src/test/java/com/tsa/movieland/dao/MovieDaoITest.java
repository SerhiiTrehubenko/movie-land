package com.tsa.movieland.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.tsa.movieland.entity.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DBRider
@ActiveProfiles("test")
@DataSet(value = {
        "datasets/users/dataset-users.json",
        "datasets/genres/dataset-genres.json",
        "datasets/movies/dataset-movies.json",
        "datasets/jointable/dataset-movies_genres.json",
        "datasets/jointable/dataset-movies_ratings.json",
        "datasets/posters/dataset-posters.json",
},
        cleanAfter = true, cleanBefore = true,
        skipCleaningFor = "flyway_schema_history"
)
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
