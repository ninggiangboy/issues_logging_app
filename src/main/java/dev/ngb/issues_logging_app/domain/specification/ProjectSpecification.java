package dev.ngb.issues_logging_app.domain.specification;

import dev.ngb.issues_logging_app.common.util.ListUtils;
import dev.ngb.issues_logging_app.domain.entity.Project;
import dev.ngb.issues_logging_app.domain.entity.Project_;
import dev.ngb.issues_logging_app.domain.entity.User;
import dev.ngb.issues_logging_app.domain.entity.User_;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public class ProjectSpecification {

    public static Specification<Project> hasIdIn(List<Integer> ids) {
        return (root, query, cb) -> {
            if (ListUtils.isEmpty(ids)) return null;
            return cb.in(root.get(Project_.ID)).value(ids);
        };
    }

    public static Specification<Project> hasId(Integer id) {
        return (root, query, cb) -> cb.equal(root.get(Project_.ID), id);
    }

    public static Specification<Project> hasMemberId(UUID memberId) {
        return (root, query, cb) -> {
            Join<Project, User> membersJoin = root.join(Project_.MEMBERS);
            return cb.equal(membersJoin.get(User_.ID), memberId);
        };
    }
}
