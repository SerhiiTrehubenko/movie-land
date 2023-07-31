package com.tsa.movieland.dao.jdbc;

import com.tsa.movieland.dao.MovieDao;
import com.tsa.movieland.dao.jdbc.mapper.MovieMapper;
import com.tsa.movieland.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcMovieDao implements MovieDao {

    private final static String FIND_ALL = "SELECT movie_id, movie_rus_name, movie_native_name, movie_release_year, " +
            "movie_rating, movie_price, movie_posters FROM movies_with_posters";

    private final static String BY_GENRE = "SELECT movie.movie_id, movie_rus_name, movie_native_name, movie_release_year, " +
            "                movie_rating, movie_price, movie_posters " +
            "FROM movies_with_posters movie  " +
            "JOIN movies_genres m_g " +
            "ON movie.movie_id = m_g.movie_id " +
            "WHERE m_g.genre_id = :genreId";

    @Value("${number.movies.random}")
    private Integer randomNumber;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final MovieMapper mapper;

    @Override
    public Iterable<Movie> findAll() {

        return namedParameterJdbcTemplate.query(FIND_ALL, mapper);
    }

    @Override
    public Iterable<Movie> findRandom() {
        String query = FIND_ALL + " ORDER BY random() LIMIT :randomNumber;";
        var parameters = new MapSqlParameterSource("randomNumber", randomNumber);
        return namedParameterJdbcTemplate.query(query, parameters, mapper);
    }

    @Override
    public Iterable<Movie> findByGenreId(int genreId) {
        var parameters = new MapSqlParameterSource("genreId", genreId);
        return namedParameterJdbcTemplate.query(BY_GENRE, parameters, mapper);
    }

    @Override
    public Iterable<Movie> findAll(String column, String direction) {
        String query = FIND_ALL + " ORDER BY %s %s;".formatted(column, direction);

        return namedParameterJdbcTemplate.query(query, mapper);
    }

    @Override
    public Iterable<Movie> findByGenreId(int genreId, String column, String direction) {
        String query = BY_GENRE + " ORDER BY %s %s;".formatted(column, direction);
        var parameters = new MapSqlParameterSource("genreId", genreId);
        return namedParameterJdbcTemplate.query(query, parameters, mapper);
    }
}
