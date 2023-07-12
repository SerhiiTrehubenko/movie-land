package com.tsa.movieland.dao;

import com.tsa.movieland.entity.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class JdbcGenreDaoITest {
    @Autowired
    private GenreDao genreDao;

    @Test
    void ShouldReturnListOfGenres() {

        Iterable<Genre> genres = genreDao.findAll();

        assertNotNull(genres);

        long genreNumber = StreamSupport.stream(genres.spliterator(), false).count();
        assertEquals(15, genreNumber);
    }
}
