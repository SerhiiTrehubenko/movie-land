package com.tsa.movieland.dao;

import com.tsa.movieland.entity.UserEntity;

public interface UserDao {
    UserEntity getUserByEmail(String email);
}
