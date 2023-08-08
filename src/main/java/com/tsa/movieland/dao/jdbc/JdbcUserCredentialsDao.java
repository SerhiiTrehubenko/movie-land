package com.tsa.movieland.dao.jdbc;

import com.tsa.movieland.dao.UserCredentialsDao;
import com.tsa.movieland.dao.jdbc.mapper.CredentialMapper;
import com.tsa.movieland.entity.Credentials;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcUserCredentialsDao implements UserCredentialsDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final CredentialMapper credentialMapper;

    @Override
    public void save(Credentials credentials) {
        String saveQuery = "INSERT INTO user_credentials (user_id, md5_password, role) " +
                "VALUES (:userId, :password, :role);";
        namedParameterJdbcTemplate
                .update(saveQuery,
                        Map.of(
                                "userId", credentials.getUserId(),
                                "password", credentials.getPassword(),
                                "role", credentials.getRole().name()
                        ));
    }

    @Override
    public Credentials findByUserId(int userId) {
        String findByUserId = "SELECT user_id, md5_password, role FROM user_credentials " +
                "WHERE user_id = :userId";
        return namedParameterJdbcTemplate.queryForObject(findByUserId, Map.of("userId", userId), credentialMapper);
    }
}
