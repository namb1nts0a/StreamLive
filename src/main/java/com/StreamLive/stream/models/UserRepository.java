package com.StreamLive.stream.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findBySessionId(String sessionId);
    Optional<User> findByUsername(String username);
}
