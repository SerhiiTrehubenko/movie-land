package com.tsa.movieland.dao.jpa.repository;

import com.tsa.movieland.entity.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<Credentials, Integer> {
    Optional<Credentials> findCredentialsByUserId(int userId);
}
