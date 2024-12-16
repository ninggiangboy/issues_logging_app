package dev.ngb.issues_logging_app.domain.repository;

import dev.ngb.issues_logging_app.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    boolean existsByName(String name);
}
