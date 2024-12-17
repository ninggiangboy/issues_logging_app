package dev.ngb.issues_logging_app.api.endpoint;

import dev.ngb.issues_logging_app.application.dto.project.ProjectDetailResponse;
import dev.ngb.issues_logging_app.application.dto.project.ProjectItemResponse;
import dev.ngb.issues_logging_app.common.model.ApiResult;
import dev.ngb.issues_logging_app.common.model.PageData;
import dev.ngb.issues_logging_app.common.model.PageInfoRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/projects")
public interface ProjectEndpoint {
    @GetMapping
    ApiResult<PageData<ProjectItemResponse>> getAll(@PageableDefault Pageable page);

    @GetMapping("/{projectId}")
    ApiResult<ProjectDetailResponse> getById(@PathVariable Integer projectId);

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    ApiResult<ProjectItemResponse> create();

    @PutMapping("/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResult<ProjectItemResponse> update(@PathVariable Long projectId);

    @DeleteMapping("/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ApiResult<Void> delete(@PathVariable Long projectId);
}
