package com.tsa.movieland.dao;

import com.tsa.movieland.dao.mapper.GenreMapper;
import com.tsa.movieland.entity.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class DefaultGenreDao implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAll() {
        String queryFindAll = "SELECT * FROM genres";
        return jdbcTemplate.query(queryFindAll, new GenreMapper());
    }
}
