package dev.ngb.issues_logging_app.application.service;

import dev.ngb.issues_logging_app.application.dto.issue.IssueCreateRequest;
import dev.ngb.issues_logging_app.application.dto.issue.IssueSearchCriteria;
import dev.ngb.issues_logging_app.application.dto.issue.IssueSearchResponse;
import dev.ngb.issues_logging_app.common.model.PageData;
import org.springframework.data.domain.Pageable;

public interface IssueService {
    PageData<IssueSearchResponse> searchIssues(IssueSearchCriteria criteria, Pageable page);

//    void createIssue(IssueCreateRequest request);
}
