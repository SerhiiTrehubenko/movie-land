package com.tsa.movieland.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.tsa.movieland.dao.jdbc.JdbcGenreDao;
import com.tsa.movieland.entity.Genre;
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
@DataSet(value = "datasets/genres/dataset-genres.json",
        cleanAfter = true, cleanBefore = true,
        skipCleaningFor = "flyway_schema_history"
)
public class JdbcGenreDaoITest {
    @Autowired
    private JdbcGenreDao genreDao;

    @Test
    void ShouldReturnListOfGenres() {

        Iterable<Genre> genres = genreDao.findAll();

        assertNotNull(genres);

        long genreNumber = StreamSupport.stream(genres.spliterator(), false).count();
        assertEquals(15, genreNumber);
    }
}
