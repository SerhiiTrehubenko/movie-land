package com.tsa.movieland.repository;

import com.tsa.movieland.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@com.tsa.movieland.context.JpaRepository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    @EntityGraph(value = "UserEntity.credentials",
            type = EntityGraph.EntityGraphType.LOAD)
    Optional<UserEntity> findUserEntityByEmail(String email);
}
