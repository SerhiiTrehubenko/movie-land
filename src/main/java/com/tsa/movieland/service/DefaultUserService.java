package com.tsa.movieland.service;

import com.tsa.movieland.dao.UserDao;
import com.tsa.movieland.mapper.UserMapper;
import com.tsa.movieland.security.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultUserService implements UserService, UserDetailsService {

    private final UserDao userDao;
    private final UserMapper userMapper;

    @Override
    public User getUserByEmail(String email) {
        return userMapper.toUser(userDao.getUserByEmail(email));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.getUserByEmail(username);
    }
}
