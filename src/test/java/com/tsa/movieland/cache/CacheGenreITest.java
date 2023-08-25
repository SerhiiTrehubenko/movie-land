package com.tsa.movieland.cache;

import com.tsa.movieland.cache.CachedGenreDao;
import com.tsa.movieland.dao.DaoBaseTest;
import com.tsa.movieland.entity.Genre;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled
public class CacheGenreITest extends DaoBaseTest {

    @Autowired
    private CachedGenreDao cachedGenreDao;

    @Test
    void shouldFillCacheWithGenresNames() {
        Iterable<Genre> genres = cachedGenreDao.findAll();

        assertNotNull(genres);
        long genreNamesNumber = StreamSupport.stream(genres.spliterator(), false).count();
        assertEquals(15, genreNamesNumber);
    }
}
