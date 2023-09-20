package com.tsa.movieland.dao.jdbc.mapper;

import com.tsa.movieland.context.JdbcMapper;
import com.tsa.movieland.dto.CountryDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@JdbcMapper
public class CountryMapper implements RowMapper<CountryDto> {
    @Override
    public CountryDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return CountryDto.builder()
                .id(rs.getInt("country_id"))
                .name(rs.getString("country_name"))
                .build();
    }
}
