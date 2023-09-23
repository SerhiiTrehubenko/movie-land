package com.tsa.movieland.service;

import com.tsa.movieland.common.Role;
import com.tsa.movieland.dao.UserCredentialsDao;
import com.tsa.movieland.entity.Credentials;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultCredentialsService implements CredentialsService {

    private final PasswordEncoder passwordEncoder;
    private final UserCredentialsDao userCredentialsDao;

    @Override
    @Transactional
    public void save(Credentials credentials) {
        String encryptedPassword = passwordEncoder.encode(credentials.getPassword());
        credentials.setPassword(encryptedPassword);
        credentials.setRole(Role.USER);

        userCredentialsDao.save(credentials);
    }

    @Override
    @Transactional(readOnly = true)
    public Credentials getCredentialsByUserId(int userId) {
        return userCredentialsDao.findByUserId(userId);
    }
}
