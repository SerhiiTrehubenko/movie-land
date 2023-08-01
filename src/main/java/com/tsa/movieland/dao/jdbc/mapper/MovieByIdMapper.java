package com.tsa.movieland.dao.jdbc.mapper;

import com.tsa.movieland.context.JdbcMapper;
import com.tsa.movieland.dto.MovieByIdDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

@JdbcMapper
public class MovieByIdMapper implements RowMapper<MovieByIdDto> {
    @Override
    public MovieByIdDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        Object posters = rs.getArray("movie_posters").getArray();
        return MovieByIdDto.builder()
                .id(rs.getInt("movie_id"))
                .nameRussian(rs.getString("movie_rus_name"))
                .nameNative(rs.getString("movie_native_name"))
                .yearOfRelease(rs.getInt("movie_release_year"))
                .description(rs.getString("movie_description"))
                .rating(rs.getDouble("movie_rating"))
                .price(rs.getDouble("movie_price"))
                .picturePath(Arrays.asList((String[]) posters))
                .build();
    }
}
