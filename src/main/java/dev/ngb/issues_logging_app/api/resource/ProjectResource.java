package dev.ngb.issues_logging_app.api.resource;

import dev.ngb.issues_logging_app.api.endpoint.ProjectEndpoint;
import dev.ngb.issues_logging_app.application.dto.project.ProjectDetailResponse;
import dev.ngb.issues_logging_app.application.dto.project.ProjectItemResponse;
import dev.ngb.issues_logging_app.application.service.ProjectService;
import dev.ngb.issues_logging_app.common.factory.ResponseFactory;
import dev.ngb.issues_logging_app.common.model.ApiResult;
import dev.ngb.issues_logging_app.common.model.PageData;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjectResource implements ProjectEndpoint {

    private final ProjectService projectService;

    public ProjectResource(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public ApiResult<PageData<ProjectItemResponse>> getAll(Pageable page) {
        PageData<ProjectItemResponse> projectPage = projectService.getAllProject(page);
        return ResponseFactory.createResultResponse(projectPage);
    }

    @Override
    public ApiResult<ProjectDetailResponse> getById(Integer projectId) {
        ProjectDetailResponse projectDetail = projectService.getProjectDetailById(projectId);
        return ResponseFactory.createResultResponse(projectDetail);
    }

    @Override
    public ApiResult<ProjectItemResponse> create() {
        return ResponseFactory.createResultResponse();
    }

    @Override
    public ApiResult<ProjectItemResponse> update(Long projectId) {
        return ResponseFactory.createResultResponse();
    }

    @Override
    public ApiResult<Void> delete(Long projectId) {
        return ResponseFactory.createResultResponse();
    }
}
