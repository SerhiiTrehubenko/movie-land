package com.tsa.movieland.service;

import com.tsa.movieland.dao.UserDao;
import com.tsa.movieland.security.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService, UserDetailsService {

    private final UserDao userDao;

    @Override
    public User getUserByEmail(String email) {
        final User user = userDao.getUserByEmail(email);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("User with email:[%s] was not found".formatted(email));
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.getUserByEmail(username);
    }
}
