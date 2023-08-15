package com.tsa.movieland.dao.jdbc.mapper;

import com.tsa.movieland.context.JdbcMapper;
import com.tsa.movieland.entity.Genre;
import com.tsa.movieland.entity.Poster;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@JdbcMapper
public class PosterMapper implements RowMapper<Poster> {
    @Override
    public Poster mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Poster.builder()
                .movieId(rs.getInt("movie_id"))
                .link(rs.getString("poster_link"))
                .build();
    }
}
