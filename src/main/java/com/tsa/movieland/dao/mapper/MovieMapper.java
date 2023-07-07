package com.tsa.movieland.dao.mapper;

import com.tsa.movieland.entity.Movie;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;


public class MovieMapper implements RowMapper<Movie> {
    @Override
    public Movie mapRow(ResultSet rs, int rowNum) throws SQLException {
        Object posters = rs.getArray("posters").getArray();
        return new Movie(rs.getInt("movie_id"),
                rs.getString("movie_rus_name"),
                rs.getString("movie_en_name"),
                rs.getInt("movie_release_year"),
                rs.getDouble("movie_rating"),
                rs.getDouble("movie_price"),
                Arrays.asList((String[]) posters));
    }
}
