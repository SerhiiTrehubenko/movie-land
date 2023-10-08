package com.tsa.movieland.dao.jpa.repository;

import com.tsa.movieland.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    @EntityGraph(value = "UserEntity.credentials",
            type = EntityGraph.EntityGraphType.LOAD)
    Optional<UserEntity> findUserEntityByEmail(String email);
}
