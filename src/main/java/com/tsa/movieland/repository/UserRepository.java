package com.tsa.movieland.repository;

import com.tsa.movieland.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@com.tsa.movieland.context.JpaRepository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findUserEntityByEmail(String email);
}
