package dev.ngb.issues_logging_app.api.resource;

import dev.ngb.issues_logging_app.api.endpoint.IssueEndpoint;
import dev.ngb.issues_logging_app.application.dto.issue.IssueSearchCriteria;
import dev.ngb.issues_logging_app.application.dto.issue.IssueSearchResponse;
import dev.ngb.issues_logging_app.application.service.IssueService;
import dev.ngb.issues_logging_app.common.factory.ResponseFactory;
import dev.ngb.issues_logging_app.common.model.ApiResult;
import dev.ngb.issues_logging_app.common.model.PageData;
import dev.ngb.issues_logging_app.domain.entity.Issue;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IssueResource implements IssueEndpoint {

    private final IssueService issueService;

    public IssueResource(IssueService issueService) {
        this.issueService = issueService;
    }

    @Override
    public ApiResult<PageData<IssueSearchResponse>> search(
            String keyword,
            Issue.Status status,
            List<Integer> tagIds,
            List<Integer> categoryIds,
            Integer projectId,
            Pageable pageable
    ) {
        IssueSearchCriteria criteria = IssueSearchCriteria.builder()
                .keyword(keyword)
                .status(status)
                .tagIds(tagIds)
                .categoryIds(categoryIds)
                .projectId(projectId)
                .build();
        PageData<IssueSearchResponse> issuesPage = issueService.searchIssues(criteria, pageable);
        return ResponseFactory.createResultResponse(issuesPage);
    }
}
