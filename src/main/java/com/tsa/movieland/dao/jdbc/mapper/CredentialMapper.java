package com.tsa.movieland.dao.jdbc.mapper;

import com.tsa.movieland.common.Role;
import com.tsa.movieland.context.JdbcMapper;
import com.tsa.movieland.entity.Credentials;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@JdbcMapper
public class CredentialMapper implements RowMapper<Credentials> {
    @Override
    public Credentials mapRow(ResultSet rs, int rowNum) throws SQLException {
        Role role = Role.valueOf(rs.getString("role"));
        return Credentials.builder()
                .userId(rs.getInt("user_id"))
                .password(rs.getString("md5_password"))
                .role(role)
                .build();
    }
}
