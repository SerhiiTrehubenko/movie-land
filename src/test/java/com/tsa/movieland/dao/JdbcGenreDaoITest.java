package com.tsa.movieland.dao;

import com.tsa.movieland.dao.jdbc.JdbcGenreDao;
import com.tsa.movieland.entity.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class JdbcGenreDaoITest extends DaoBaseTest {
    @Autowired
    private JdbcGenreDao genreDao;

    @Test
    void ShouldReturnListOfGenres() {

        Iterable<Genre> genres = genreDao.findAll();

        assertNotNull(genres);

        long genreNumber = StreamSupport.stream(genres.spliterator(), false).count();
        assertEquals(15, genreNumber);
    }

    @Test
    void shouldReturnGenresByMovieId() {
        final List<Genre> genres = (List<Genre>) genreDao.findByMovieId(1121);

        assertEquals(1, genres.size());
        assertEquals(15, genres.get(0).getId());
        assertEquals("вестерн", genres.get(0).getName());
    }
}
