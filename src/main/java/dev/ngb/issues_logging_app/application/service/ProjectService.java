package dev.ngb.issues_logging_app.application.service;

import dev.ngb.issues_logging_app.application.dto.project.ProjectDetailResponse;
import dev.ngb.issues_logging_app.application.dto.project.ProjectItemResponse;
import dev.ngb.issues_logging_app.common.model.PageData;
import org.springframework.data.domain.Pageable;

public interface ProjectService {
    PageData<ProjectItemResponse> getAllProject(Pageable pageRequest);

    ProjectDetailResponse getProjectDetail(Integer projectId);
}
