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
    private String randomNumber;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final MovieMapper mapper;

    @Override
    public Iterable<Movie> findAll() {
        String query = FIND_ALL + ";";

        return jdbcTemplate.query(query, mapper);
    }

    @Override
    public Iterable<Movie> findRandom() {
        String query = "SELECT movie_id, movie_rus_name, movie_native_name, movie_release_year, " +
                "            movie_rating, movie_price, movie_posters FROM movies_with_posters " +
                "ORDER BY random() " +
                "LIMIT %s;".formatted(randomNumber);

        return jdbcTemplate.query(query, mapper);
    }

    @Override
    public Iterable<Movie> findByGenreId(int genreId) {
        String query = BY_GENRE + ";";
        var parameters = setParameters(genreId);
        return jdbcTemplate.query(query, parameters, mapper);
    }

    private MapSqlParameterSource setParameters(int genreId) {
        return new MapSqlParameterSource("genreId", genreId);
    }

    @Override
    public Iterable<Movie> findAllSorted(String column, String direction) {
        String query = FIND_ALL + " ORDER BY %s %s;".formatted(column, direction);

        return jdbcTemplate.query(query, mapper);
    }

    @Override
    public Iterable<Movie> findByGenreIdSorted(int genreId, String column, String direction) {
        String query = BY_GENRE + " ORDER BY %s %s;".formatted(column, direction);
        var parameters = setParameters(genreId);
        return jdbcTemplate.query(query, parameters, mapper);
    }
}
