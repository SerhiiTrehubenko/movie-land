package com.tsa.movieland.dao;

import com.tsa.movieland.entity.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class DefaultGenreDaoITest {
    @Autowired
    private GenreDao genreDao;

    @Test
    void ShouldReturnListOfGenres() {

        List<Genre> genres = genreDao.findAll();

        assertNotNull(genres);
        assertEquals(15, genres.size());
    }
}
