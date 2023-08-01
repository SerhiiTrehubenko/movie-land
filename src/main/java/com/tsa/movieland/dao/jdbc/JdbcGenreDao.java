package com.tsa.movieland.dao.jdbc;

import com.tsa.movieland.dao.GenreDao;
import com.tsa.movieland.dao.jdbc.mapper.GenreMapper;
import com.tsa.movieland.entity.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcGenreDao implements GenreDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final GenreMapper genreMapper;

    @Override
    public Iterable<Genre> findAll() {
        String queryFindAll = "SELECT genre_id, genre_name FROM genres ORDER BY genre_id;";
        return namedParameterJdbcTemplate.query(queryFindAll, genreMapper);
    }

    @Override
    public Iterable<Genre> findByMovieId(int movieId) {
        String queryGenres = "SELECT genre_id, genre_name FROM genres_by_movie_id " +
                "WHERE movie_id = :movieId";
        return namedParameterJdbcTemplate.query(
                queryGenres,
                Map.of("movieId", movieId),
                genreMapper
        );
    }
}
