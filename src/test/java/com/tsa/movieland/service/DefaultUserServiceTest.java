package com.tsa.movieland.service;

import com.tsa.movieland.common.Role;
import com.tsa.movieland.dao.DaoBaseTest;
import com.tsa.movieland.dto.UserRegistration;
import com.tsa.movieland.entity.Credentials;
import com.tsa.movieland.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class DefaultUserServiceTest extends DaoBaseTest {

    @Autowired
    DefaultUserService service;

    @Test
    void shouldSaveUser() {
        int userId = 1000015;
        int lengthOfEncryptedPassword = 60;
        String value = "test";
        String email = "test@email.com";
        final UserRegistration user = UserRegistration.builder()
                .firstName(value)
                .lastName(value)
                .nickname(value)
                .email(email)
                .password(value)
                .build();

        service.save(user);

        final User userByEmail = service.getUserByEmail(email);

        assertNotNull(userByEmail);
        assertEquals(userId, userByEmail.getId());
        assertEquals(value, userByEmail.getFirstName());
        assertEquals(value, userByEmail.getLastName());
        assertEquals(value, userByEmail.getNickname());
        assertEquals(email, userByEmail.getEmail());

        final Credentials credentialsByUserId = userByEmail.getCredentials();

        assertNotNull(credentialsByUserId);
        assertEquals(userId, credentialsByUserId.getUserId());
        assertNotNull(credentialsByUserId.getPassword());
        assertEquals(lengthOfEncryptedPassword, credentialsByUserId.getPassword().length());
        assertEquals(Role.USER, credentialsByUserId.getRole());
    }
}