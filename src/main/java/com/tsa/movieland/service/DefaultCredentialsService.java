package com.tsa.movieland.service;

import com.tsa.movieland.common.Role;
import com.tsa.movieland.dao.UserCredentialsDao;
import com.tsa.movieland.entity.Credentials;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultCredentialsService implements CredentialsService {

    private final PasswordEncoder passwordEncoder;
    private final UserCredentialsDao userCredentialsDao;

    @Override
    public void save(Credentials credentials) {
        String encryptedPassword = passwordEncoder.encode(credentials.getPassword());
        credentials.setPassword(encryptedPassword);
        credentials.setRole(Role.USER);

        userCredentialsDao.save(credentials);
    }

    @Override
    public Credentials getCredentialsByUserId(int userId) {
        return userCredentialsDao.findByUserId(userId);
    }
}
