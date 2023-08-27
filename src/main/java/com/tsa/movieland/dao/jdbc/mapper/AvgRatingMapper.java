package com.tsa.movieland.dao.jdbc.mapper;

import com.tsa.movieland.context.JdbcMapper;
import com.tsa.movieland.common.AvgRating;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@JdbcMapper
public class AvgRatingMapper implements RowMapper<AvgRating> {
    @Override
    public AvgRating mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        Map<String, Double> userRatings = getUserRatings(rs);

        return AvgRating.builder()
                .movieId(rs.getInt("movie_id"))
                .currentAvg(rs.getDouble("avg_rating"))
                .userVotes(userRatings)
                .build();
    }

    private Map<String, Double> getUserRatings(ResultSet rs) throws SQLException {
        Map<String, Double> userRatings = new HashMap<>();
        String[] userEmails = (String[]) rs.getArray("user_emails").getArray();
        BigDecimal[] userVotes = (BigDecimal[]) rs.getArray("user_votes").getArray();
        for (int i = 0; i < userEmails.length; i++) {
            userRatings.put(userEmails[i], userVotes[i].doubleValue());
        }
        return userRatings;
    }
}
