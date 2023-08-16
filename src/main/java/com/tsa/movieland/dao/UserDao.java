package com.tsa.movieland.dao;

import com.tsa.movieland.entity.User;

public interface UserDao {
    User getUserByEmail(String email);
}
