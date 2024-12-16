package dev.ngb.issues_logging_app.domain.repository;

import dev.ngb.issues_logging_app.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String username);
}
