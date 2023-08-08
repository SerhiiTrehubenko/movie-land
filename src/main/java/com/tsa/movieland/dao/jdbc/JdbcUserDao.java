package com.tsa.movieland.dao.jdbc;

import com.tsa.movieland.dao.UserDao;
import com.tsa.movieland.dao.jdbc.mapper.UserMapper;
import com.tsa.movieland.dto.UserRegistration;
import com.tsa.movieland.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcUserDao implements UserDao {

    private final String getUserIdByEmail = "SELECT user_id FROM users WHERE user_email = ?";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final UserMapper userMapper;

    @Override
    public int save(UserRegistration userRegistration) {
        final String email = userRegistration.getEmail();

        String saveUser = "INSERT INTO users (user_firstname, user_lastname, user_nickname, user_email) " +
                "VALUES (:firstname, :lastname, :nickname, :email)";
        return namedParameterJdbcTemplate.execute(
                saveUser,
                getParamMap(userRegistration),
                createCallback(email)
        ).orElseThrow(() -> new UsernameNotFoundException("user with email: [%s] not found".formatted(email)));
    }

    private Map<String, String> getParamMap(UserRegistration userRegistration) {
        return Map.of(
                "firstname", userRegistration.getFirstName(),
                "lastname", userRegistration.getLastName(),
                "nickname", userRegistration.getNickname(),
                "email", userRegistration.getEmail()
        );
    }

    private PreparedStatementCallback<Optional<Integer>> createCallback(String email) {
        return ps -> {
            ps.executeUpdate();
            final Connection connection = ps.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement(getUserIdByEmail);
            try (connection; preparedStatement) {
                preparedStatement.setString(1, email);
                final ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return Optional.of(resultSet.getInt("user_id"));
                }
                return Optional.empty();
            }
        };
    }

    @Override
    public User getUserByEmail(String email) {
        String findByEmail = "SELECT user_id, user_firstname, user_lastname, user_nickname, user_email FROM users WHERE user_email = :email";

        return namedParameterJdbcTemplate.queryForObject(findByEmail,
                Map.of("email", email),
                userMapper);
    }
}
