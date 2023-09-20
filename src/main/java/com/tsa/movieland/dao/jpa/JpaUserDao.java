package com.tsa.movieland.dao.jpa;

import com.tsa.movieland.context.JpaDao;
import com.tsa.movieland.dao.UserDao;
import com.tsa.movieland.security.entity.User;
import com.tsa.movieland.mapper.UserMapper;
import com.tsa.movieland.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@JpaDao
@RequiredArgsConstructor
public class JpaUserDao implements UserDao {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserEntityByEmail(email).map(userMapper::toUser).orElseThrow();
    }
}
