package com.tsa.movieland.dao;

import com.tsa.movieland.dto.UserRegistration;
import com.tsa.movieland.entity.User;

public interface UserDao {
    int save(UserRegistration userRegistration);

    User getUserByEmail(String email);
}
