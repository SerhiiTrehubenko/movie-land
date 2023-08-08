package com.tsa.movieland.service;

import com.tsa.movieland.dao.UserDao;
import com.tsa.movieland.dto.UserRegistration;
import com.tsa.movieland.entity.Credentials;
import com.tsa.movieland.entity.User;
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
    private final CredentialsService credentialsService;

    @Override
    public void save(UserRegistration userRegistration) {
        final int userId = userDao.save(userRegistration);
        Credentials credentials = createCredentials(userRegistration, userId);
        credentialsService.save(credentials);
    }

    private Credentials createCredentials(UserRegistration userRegistration, int userId) {
        return Credentials.builder()
                .userId(userId)
                .password(userRegistration.getPassword())
                .build();
    }

    @Override
    public User getUserByEmail(String email) {
        final User user = userDao.getUserByEmail(email);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("User with email:[%s] was not found".formatted(email));
        }
        final Credentials credentials = credentialsService.getCredentialsByUserId(user.getId());
        user.setCredentials(credentials);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.getUserByEmail(username);
    }
}
