package dev.ngb.issues_logging_app.api.endpoint;

import dev.ngb.issues_logging_app.application.dto.issue.IssueSearchResponse;
import dev.ngb.issues_logging_app.common.model.ApiResult;
import dev.ngb.issues_logging_app.common.model.PageData;
import dev.ngb.issues_logging_app.domain.entity.Issue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/issues")
public interface IssueEndpoint {
    @GetMapping("/search")
    ApiResult<PageData<IssueSearchResponse>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Issue.Status status,
            @RequestParam(required = false) List<Integer> tagIds,
            @RequestParam(required = false) List<Integer> categoryIds,
            @RequestParam(required = false) Integer projectId,
            @PageableDefault Pageable pageable
    );
}
