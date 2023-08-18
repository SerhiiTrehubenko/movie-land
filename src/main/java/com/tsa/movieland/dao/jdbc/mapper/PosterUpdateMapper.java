package com.tsa.movieland.dao.jdbc.mapper;

import com.tsa.movieland.context.JdbcMapper;
import com.tsa.movieland.entity.PosterUpdate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@JdbcMapper
public class PosterUpdateMapper implements RowMapper<PosterUpdate> {
    @Override
    public PosterUpdate mapRow(ResultSet rs, int rowNum) throws SQLException {
        return PosterUpdate.builder()
                .movieId(rs.getInt("movie_id"))
                .link(rs.getString("poster_link"))
                .recordTime(rs.getTimestamp("poster_record_time"))
                .build();
    }
}
