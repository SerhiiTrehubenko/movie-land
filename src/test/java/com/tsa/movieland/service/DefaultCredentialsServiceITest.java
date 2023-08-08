package com.tsa.movieland.service;

import com.tsa.movieland.common.Role;
import com.tsa.movieland.dao.DaoBaseTest;
import com.tsa.movieland.entity.Credentials;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class DefaultCredentialsServiceITest extends DaoBaseTest {
    @Autowired
    DefaultCredentialsService service;

    @Test
    void shouldSaveCredentials() {
        int lengthOfEncryptedPassword = 60;
        int userId = 1000001;
        final Credentials credentials = Credentials.builder()
                .userId(userId)
                .password("password")
                .build();

        service.save(credentials);

        final Credentials credentialsByUserId = service.getCredentialsByUserId(userId);

        assertNotNull(credentialsByUserId);
        assertEquals(userId, credentialsByUserId.getUserId());
        assertNotNull(credentialsByUserId.getPassword());
        assertEquals(lengthOfEncryptedPassword, credentialsByUserId.getPassword().length());
        assertEquals(Role.USER, credentialsByUserId.getRole());
    }
}