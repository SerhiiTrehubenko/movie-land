package com.tsa.movieland.dao;

import com.tsa.movieland.dto.UserRegistration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class JdbcUserDaoTest extends DaoBaseTest {

    @Autowired
    UserDao userDao;

    @Test
    void save() {
        final UserRegistration userRegistration = UserRegistration.builder()
                .firstName("test")
                .lastName("test")
                .nickname("test")
                .email("test")
                .build();
        final int id = userDao.save(userRegistration);

        assertEquals(1000015, id);
    }
}