package com.tsa.movieland.dao;

import com.tsa.movieland.security.entity.User;

public interface UserDao {
    User getUserByEmail(String email);
}
