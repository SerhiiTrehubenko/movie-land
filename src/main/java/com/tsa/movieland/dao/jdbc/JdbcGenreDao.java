package com.tsa.movieland.dao.jdbc;

import com.tsa.movieland.context.JdbcDao;
import com.tsa.movieland.dao.GenreDao;
import com.tsa.movieland.dao.jdbc.mapper.GenreMapper;
import com.tsa.movieland.entity.GenreEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;

@RequiredArgsConstructor
@JdbcDao
public class JdbcGenreDao implements GenreDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final GenreMapper genreMapper;

    @Override
    public Iterable<GenreEntity> findAll() {
        String queryFindAll = "SELECT genre_id, genre_name FROM genres ORDER BY genre_id;";
        return namedParameterJdbcTemplate.query(queryFindAll, genreMapper);
    }

    @Override
    public Iterable<GenreEntity> findByMovieId(int movieId) {
        String queryGenres = "SELECT genre_id, genre_name FROM genres_by_movie_id " +
                "WHERE movie_id = :movieId";
        return namedParameterJdbcTemplate.query(
                queryGenres,
                Map.of("movieId", movieId),
                genreMapper
        );
    }
}
