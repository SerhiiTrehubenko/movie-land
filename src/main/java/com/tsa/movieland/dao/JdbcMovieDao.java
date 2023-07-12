package com.tsa.movieland.dao;

import com.tsa.movieland.dao.mapper.MovieMapper;
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
            "movie_rating, movie_price, posters FROM movies_expand";

    private final static String BY_GENRE = "SELECT movie.movie_id, movie_rus_name, movie_native_name, movie_release_year, " +
            "                movie_rating, movie_price, posters " +
            "FROM movies_expand movie  " +
            "JOIN movie_genres genre  " +
            "ON movie.movie_id = genre.movie_id " +
            "WHERE genre.genre_id = :genreId";

    @Value("${movie.number.random}")
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
                "            movie_rating, movie_price, posters FROM movies_expand " +
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
    public Iterable<Movie> findByGenreIdSortPrice(int genreId, String direction) {
        String query = BY_GENRE + " ORDER BY movie_price %s;".formatted(direction);
        var parameters = setParameters(genreId);
        return jdbcTemplate.query(query, parameters, mapper);
    }

    @Override
    public Iterable<Movie> findByGenreIdSortRating(int genreId, String direction) {
        String query = BY_GENRE + " ORDER BY movie_rating %s;".formatted(direction);
        var parameters = setParameters(genreId);
        return jdbcTemplate.query(query, parameters, mapper);
    }

    @Override
    public Iterable<Movie> findAllSortPrice(String direction) {
        String query = FIND_ALL + " ORDER BY movie_price %s;".formatted(direction);

        return jdbcTemplate.query(query, mapper);
    }

    @Override
    public Iterable<Movie> findAllSortRating(String direction) {
        String query = FIND_ALL + " ORDER BY movie_rating %s;".formatted(direction);

        return jdbcTemplate.query(query, mapper);
    }
}
