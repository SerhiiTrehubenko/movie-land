package com.tsa.movieland.dao;

import com.tsa.movieland.dao.mapper.MovieMapper;
import com.tsa.movieland.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DefaultMovieDao implements MovieDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Movie> findAll() {
        String query = "SELECT * FROM movies_expand;";

        return jdbcTemplate.query(query, new MovieMapper());
    }

    @Override
    public List<Movie> findThreeRandom() {
        String query = "SELECT * FROM movies_expand " +
                "ORDER BY random() " +
                "LIMIT 3;";

        return jdbcTemplate.query(query, new MovieMapper());
    }

    @Override
    public List<Movie> findByGenreId(int genreId) {
        String query = "SELECT * " +
                "FROM movies_expand movie  " +
                "JOIN movie_genres genre  " +
                "ON movie.movie_id = genre.movie_id " +
                "WHERE genre.genre_id = :genreId;";

        return namedParameterJdbcTemplate.query(query,
                new MapSqlParameterSource()
                .addValue("genreId", genreId), new MovieMapper());
    }
}
