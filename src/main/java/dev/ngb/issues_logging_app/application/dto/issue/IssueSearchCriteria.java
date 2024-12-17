package dev.ngb.issues_logging_app.application.dto.issue;

import dev.ngb.issues_logging_app.domain.entity.Issue;
import lombok.Builder;

import java.util.List;

@Builder
public record IssueSearchCriteria(
        String keyword,
        Issue.Status status,
        List<Integer> tagIds,
        List<Integer> categoryIds,
        Integer projectId
) {
}
