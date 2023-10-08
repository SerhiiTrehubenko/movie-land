package com.tsa.movieland.dao.jpa;

import com.tsa.movieland.context.JpaDao;
import com.tsa.movieland.dao.UserDao;
import com.tsa.movieland.entity.UserEntity;
import com.tsa.movieland.dao.jpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@JpaDao
@RequiredArgsConstructor
public class JpaUserDao implements UserDao {

    private final UserRepository userRepository;
    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findUserEntityByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email:[%s] was not found".formatted(email)));
    }
}
