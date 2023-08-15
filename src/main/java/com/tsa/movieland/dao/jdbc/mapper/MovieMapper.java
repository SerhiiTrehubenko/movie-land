package com.tsa.movieland.dao.jdbc.mapper;

import com.tsa.movieland.context.JdbcMapper;
import com.tsa.movieland.entity.Movie;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@JdbcMapper
public class MovieMapper implements ResultSetExtractor<Movie> {
    @Override
    public Movie extractData(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }
        return Movie.builder()
                .id(rs.getInt("movie_id"))
                .nameRussian(rs.getString("movie_rus_name"))
                .nameNative(rs.getString("movie_native_name"))
                .yearOfRelease(rs.getInt("movie_release_year"))
                .description(rs.getString("movie_description"))
                .price(rs.getDouble("movie_price"))
                .build();
    }
}
