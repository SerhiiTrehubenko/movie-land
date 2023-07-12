package com.tsa.movieland.dao.mapper;

import com.tsa.movieland.entity.Movie;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

@Component
public class MovieMapper implements RowMapper<Movie> {
    @Override
    public Movie mapRow(ResultSet rs, int rowNum) throws SQLException {
        Object posters = rs.getArray("posters").getArray();
        return Movie.builder()
                .id(rs.getInt("movie_id"))
                .nameRussian(rs.getString("movie_rus_name"))
                .nameNative(rs.getString("movie_native_name"))
                .yearOfRelease(rs.getInt("movie_release_year"))
                .rating(rs.getDouble("movie_rating"))
                .price(rs.getDouble("movie_price"))
                .picturePath(Arrays.asList((String[]) posters))
                .build();
    }
}
