package com.tsa.movieland.config;

import com.tsa.movieland.cache.GenreCache;
import com.tsa.movieland.dao.GenreDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

@org.springframework.context.annotation.Configuration
@EnableScheduling
@PropertySource("classpath:application.yml")
public class Configuration {

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public GenreCache genreCache(GenreDao genreDao) {
        GenreCache genreCache = new GenreCache(genreDao);
        genreCache.run();
        return genreCache;
    }
}
