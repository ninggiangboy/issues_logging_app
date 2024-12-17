package dev.ngb.issues_logging_app.domain.repository;

import dev.ngb.issues_logging_app.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    boolean existsByName(String name);

    Set<Tag> findAllByIdIn(Set<Integer> integers);
}
