package dev.ngb.issues_logging_app.domain.repository;

import dev.ngb.issues_logging_app.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);
}
