package com.tsa.movieland.service;

import com.tsa.movieland.dto.UserRegistration;
import com.tsa.movieland.entity.User;

public interface UserService {
    void save(UserRegistration userRegistration);

    User getUserByEmail(String email);
}
