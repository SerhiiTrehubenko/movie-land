package com.tsa.movieland.dao.jdbc.mapper;

import com.tsa.movieland.context.JdbcMapper;
import com.tsa.movieland.dto.PosterDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@JdbcMapper
public class PosterMapper implements RowMapper<PosterDto> {
    @Override
    public PosterDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return PosterDto.builder()
                .movieId(rs.getInt("movie_id"))
                .link(rs.getString("poster_link"))
                .build();
    }
}
