package dev.ngb.issues_logging_app.domain.specification;

import dev.ngb.issues_logging_app.common.util.ListUtils;
import dev.ngb.issues_logging_app.common.util.StringUtils;
import dev.ngb.issues_logging_app.domain.entity.Category_;
import dev.ngb.issues_logging_app.domain.entity.Issue;
import dev.ngb.issues_logging_app.domain.entity.Issue_;
import dev.ngb.issues_logging_app.domain.entity.Project_;
import dev.ngb.issues_logging_app.infrastructure.database.SqlFunction;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.List;

public class IssueSpecification {

    public static Specification<Issue> hasCategoryIdIn(List<Integer> ids) {
        return (root, query, cb) -> {
            if (ListUtils.isEmpty(ids)) return null;
            return root.get(Issue_.CATEGORY).get(Category_.ID).in(ids);
        };
    }

    public static Specification<Issue> hasProjectIdIn(List<Integer> ids) {
        return (root, query, cb) -> {
            if (ListUtils.isEmpty(ids)) return null;
            return root.get(Issue_.PROJECT).get(Project_.ID).in(ids);
        };
    }

    public static Specification<Issue> hasProjectId(Integer projectId) {
        return (root, query, cb) -> {
            if (projectId == null) return null;
            return cb.equal(root.get(Issue_.PROJECT).get(Project_.ID), projectId);
        };
    }

    public static Specification<Issue> notHasProjectId() {
        return (root, query, cb) -> cb.isNull(root.get(Issue_.PROJECT));
    }

    public static Specification<Issue> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            if (StringUtils.isBlank(keyword)) return null;

            return cb.isTrue(
                    cb.function(
                            SqlFunction.TSVECTOR_MATCH,
                            Boolean.class,
                            root.get(Issue_.TITLE),
                            cb.literal(keyword)
                    )
            );
        };
    }

    public static Specification<Issue> hasCreatedDateBetween(Instant from, Instant to) {
        return (root, query, cb)
                -> cb.between(root.get(Issue_.CREATED_AT), from, to);
    }

    public static Specification<Issue> hasStatus(Issue.Status status) {
        return (root, query, cb)
                -> cb.equal(root.get(Issue_.STATUS), status);
    }

    public static Specification<Issue> hasStatusNotIn(List<Issue.Status> statuses) {
        return (root, query, cb)
                -> cb.not(root.get(Issue_.STATUS).in(statuses));
    }
}
