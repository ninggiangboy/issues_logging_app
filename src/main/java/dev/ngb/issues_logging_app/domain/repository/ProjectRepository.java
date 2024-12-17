package dev.ngb.issues_logging_app.domain.repository;

import dev.ngb.issues_logging_app.domain.entity.Project;
import dev.ngb.issues_logging_app.domain.entity.Project_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, Integer>, JpaSpecificationExecutor<Project> {

    @Query("SELECT p.id FROM Project p JOIN p.members m WHERE m.id = :userId")
    List<Integer> findAllProjectIdsByMemberId(UUID userId);

    @EntityGraph(attributePaths = {Project_.MEMBERS})
    Optional<Project> findOne(Specification<Project> projectSpecification);

    default Optional<Project> findOneWithMembers(Specification<Project> projectSpecification) {
        return findOne(projectSpecification);
    }

    @EntityGraph(attributePaths = {Project_.MEMBERS})
    Optional<Project> findOneById(Integer id);

    default Optional<Project> findOneByIdWithMembers(Integer id) {
        return findOneById(id);
    }
}
