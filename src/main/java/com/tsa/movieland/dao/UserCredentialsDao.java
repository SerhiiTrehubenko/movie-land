package com.tsa.movieland.dao;

import com.tsa.movieland.entity.Credentials;

public interface UserCredentialsDao {
    void save(Credentials credentials);

    Credentials findByUserId(int userId);
}
