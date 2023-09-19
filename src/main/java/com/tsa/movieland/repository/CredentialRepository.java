package com.tsa.movieland.repository;

import com.tsa.movieland.entity.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@com.tsa.movieland.context.JpaRepository
public interface CredentialRepository extends JpaRepository<Credentials, Integer> {
    Optional<Credentials> findCredentialsByUserId(int userId);
}
