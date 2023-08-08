package com.tsa.movieland.service;

import com.tsa.movieland.entity.Credentials;

public interface CredentialsService {
    void save(Credentials credentials);
    Credentials getCredentialsByUserId(int userId);
}
