package com.tsa.movieland.dao.jpa;

import com.tsa.movieland.context.JpaDao;
import com.tsa.movieland.dao.UserCredentialsDao;
import com.tsa.movieland.entity.Credentials;
import com.tsa.movieland.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;

@JpaDao
@RequiredArgsConstructor
public class JpaCredentialsDao implements UserCredentialsDao {

    private final CredentialRepository credentialRepository;

    @Override
    public void save(Credentials credentials) {
        credentialRepository.save(credentials);
    }

    @Override
    public Credentials findByUserId(int userId) {
        return credentialRepository.findCredentialsByUserId(userId).orElseThrow();
    }
}
