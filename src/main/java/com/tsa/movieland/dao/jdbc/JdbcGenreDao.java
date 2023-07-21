package com.tsa.movieland.dao.jdbc;

import com.tsa.movieland.dao.GenreDao;
import com.tsa.movieland.dao.jdbc.mapper.GenreMapper;
import com.tsa.movieland.entity.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

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
}
