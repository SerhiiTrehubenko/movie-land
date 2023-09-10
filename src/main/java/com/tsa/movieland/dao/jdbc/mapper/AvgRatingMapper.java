package com.tsa.movieland.dao.jdbc.mapper;

import com.tsa.movieland.context.JdbcMapper;
import com.tsa.movieland.common.AvgRating;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

@JdbcMapper
public class AvgRatingMapper implements RowMapper<AvgRating> {
    @Override
    public AvgRating mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {

        return AvgRating.builder()
                .movieId(rs.getInt("movie_id"))
                .currentAvg(rs.getDouble("avg_rating"))
                .userVotes(rs.getInt("user_votes"))
                .build();
    }
}
