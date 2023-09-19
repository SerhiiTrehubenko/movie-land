package com.tsa.movieland.cache;

import com.tsa.movieland.PostConstructContainer;
import com.tsa.movieland.entity.GenreEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CacheGenreITest extends PostConstructContainer {

    @Autowired
    private CachedGenreDao cachedGenreDao;

    @Test
    void shouldFillCacheWithGenresNames() {
        Iterable<GenreEntity> genres = cachedGenreDao.findAll();

        assertNotNull(genres);
        long genreNamesNumber = StreamSupport.stream(genres.spliterator(), false).count();
        assertEquals(15, genreNamesNumber);
    }
}
