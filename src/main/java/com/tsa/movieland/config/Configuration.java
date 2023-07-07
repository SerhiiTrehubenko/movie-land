package com.tsa.movieland.config;

import com.tsa.movieland.cache.GenreCache;
import com.tsa.movieland.dao.GenreDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Timer;

@org.springframework.context.annotation.Configuration
@PropertySource("classpath:application.yml")
public class Configuration {

    private final static int FORE_HOUR_GENRE_REFRESH = 4 * 3600000;
    private final static int ENDLESS_CYCLE = 1;
    @Value("${spring.datasource.driver-class-name}")
    private String driver;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String userName;
    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
       var dataSource = new DriverManagerDataSource();
       dataSource.setDriverClassName(driver);
       dataSource.setUrl(url);
       dataSource.setUsername(userName);
       dataSource.setPassword(password);

       return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public GenreCache genreCache(GenreDao genreDao) {
        GenreCache genreCache = new GenreCache(genreDao);
        Timer timer = new Timer("GenreCache");
        timer.schedule(genreCache, ENDLESS_CYCLE, FORE_HOUR_GENRE_REFRESH);
        return genreCache;
    }
}
