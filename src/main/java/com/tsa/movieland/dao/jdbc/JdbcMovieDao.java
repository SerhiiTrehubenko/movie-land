package com.tsa.movieland.dao.jdbc;

import com.tsa.movieland.dao.MovieDao;
import com.tsa.movieland.dao.jdbc.mapper.MovieMapper;
import com.tsa.movieland.util.SortDirection;
import com.tsa.movieland.util.SortField;
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
//  I use concatenation here because I want to reuse FIND_ALL template in other queries
//  for avoiding COPYING of query String
    @Override
    public Iterable<Movie> findAll() {
        String query = FIND_ALL + ";";

        return namedParameterJdbcTemplate.query(query, mapper);
    }

    @Override
    public Iterable<Movie> findRandom() {
        String query = FIND_ALL + " ORDER BY random() LIMIT :randomNumber;";
        var parameters = new MapSqlParameterSource("randomNumber", randomNumber);
        return namedParameterJdbcTemplate.query(query, parameters, mapper);
    }
//  The same as findAll();
    @Override
    public Iterable<Movie> findByGenreId(int genreId) {
        String query = BY_GENRE + ";";
        var parameters = new MapSqlParameterSource("genreId", genreId);
        return namedParameterJdbcTemplate.query(query, parameters, mapper);
    }

    //    I can't rid of concatenation because when I inject parameters through any JdbcTemplate
//    it wraps any string into 'string' as a result query has wrong format and Exception is thrown
//    example: ORDER BY movie_rating ASC; --> works
//             ORDER BY 'movie_rating' 'ASC'; --> does not work
//    By the way when [String column, String direction] appear hear they come from process of Http
//    parameters parsing where they undergo converting to ENUMS
    @Override
    public Iterable<Movie> findAllSorted(String column, String direction) {
        checkOnFormat(column, direction);
        String query = FIND_ALL + " ORDER BY %s %s;".formatted(column, direction);

        return namedParameterJdbcTemplate.query(query, mapper);
    }
//  The same as findAllSorted();
    @Override
    public Iterable<Movie> findByGenreIdSorted(int genreId, String column, String direction) {
        checkOnFormat(column, direction);
        String query = BY_GENRE + " ORDER BY %s %s;".formatted(column, direction);
        var parameters = new MapSqlParameterSource("genreId", genreId);
        return namedParameterJdbcTemplate.query(query, parameters, mapper);
    }

    private void checkOnFormat(String column, String direction) {
        if (!checkColumnName(column) && !checkDirectionName(direction)) {
            throw new RuntimeException("Provided column name: [%s] or sorting direction: [%s] has wrong format".formatted(column, direction));
        }
    }

    private boolean checkColumnName(String column) {
        for (SortField field : SortField.values()) {
            if (field.getColumnName().equals(column)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDirectionName(String direction) {
        for (SortDirection field : SortDirection.values()) {
            if (field.toString().equals(direction)) {
                return true;
            }
        }
        return false;
    }
}
