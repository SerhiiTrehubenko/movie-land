package com.tsa.movieland.dao.jdbc;

import com.tsa.movieland.context.JdbcDao;
import com.tsa.movieland.dao.UserDao;
import com.tsa.movieland.dao.jdbc.mapper.UserMapper;
import com.tsa.movieland.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;

@RequiredArgsConstructor
@JdbcDao
public class JdbcUserDao implements UserDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final UserMapper userMapper;

    @Override
    public User getUserByEmail(String email) {
        String findByEmail = "SELECT user_id, user_firstname, user_lastname, user_nickname, user_email FROM users WHERE user_email = :email";

        return namedParameterJdbcTemplate.queryForObject(findByEmail,
                Map.of("email", email),
                userMapper);
    }
}
