package com.tsa.movieland.dao;

import com.tsa.movieland.dao.mapper.GenreMapper;
import com.tsa.movieland.entity.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcGenreDao implements GenreDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;

    @Override
    public Iterable<Genre> findAll() {
        String queryFindAll = "SELECT genre_id, genre_name FROM genres";
        return jdbcTemplate.query(queryFindAll, genreMapper);
    }
}
