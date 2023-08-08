package com.tsa.movieland.dao.jdbc.mapper;

import com.tsa.movieland.context.JdbcMapper;
import com.tsa.movieland.entity.Review;
import com.tsa.movieland.dto.UserDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@JdbcMapper
public class ReviewUserMapper implements RowMapper<Review> {
    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserDto userDto = UserDto.builder()
                .id(rs.getInt("user_id"))
                .nickname(rs.getString("user_nickname"))
                .build();
        return Review.builder()
                .user(userDto)
                .text(rs.getString("movie_comment"))
                .build();
    }
}
