package com.tsa.movieland.service;

import com.tsa.movieland.entity.User;

public interface UserService {

    User getUserByEmail(String email);
}
