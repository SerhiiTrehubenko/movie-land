package com.tsa.movieland.dao.jdbc.callback;

import com.tsa.movieland.context.JdbcCallback;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@JdbcCallback
public class AddMovieCallback implements PreparedStatementCallback<Optional<Integer>> {
    private final static String GET_MOVIE_ID = "SELECT movie_id FROM movies WHERE movie_rus_name = ? AND movie_native_name = ?";

    private String nameRussian;
    private String nameNative;

    public AddMovieCallback() {
    }

    public AddMovieCallback(String nameRussian, String nameNative) {
        this.nameRussian = nameRussian;
        this.nameNative = nameNative;
    }

    public PreparedStatementCallback<Optional<Integer>> getInstance(String nameRussian, String nameNative) {
        return new AddMovieCallback(nameRussian, nameNative);
    }
    @Override
    public Optional<Integer> doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
        ps.executeUpdate();
        final Connection connection = ps.getConnection();
        final PreparedStatement preparedStatement = connection.prepareStatement(GET_MOVIE_ID);
        preparedStatement.setString(1, nameRussian);
        preparedStatement.setString(2, nameNative);
        final ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return Optional.of(resultSet.getInt("movie_id"));
        }
        return Optional.empty();
    }
}
