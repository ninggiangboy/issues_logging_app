package dev.ngb.issues_logging_app.application.service.impl;

import dev.ngb.issues_logging_app.application.dto.project.ProjectDetailResponse;
import dev.ngb.issues_logging_app.application.dto.project.ProjectItemResponse;
import dev.ngb.issues_logging_app.application.exception.NotFoundException;
import dev.ngb.issues_logging_app.application.mapper.ProjectMapper;
import dev.ngb.issues_logging_app.application.service.ProjectService;
import dev.ngb.issues_logging_app.common.factory.PageFactory;
import dev.ngb.issues_logging_app.common.model.PageData;
import dev.ngb.issues_logging_app.common.util.SecurityUtils;
import dev.ngb.issues_logging_app.domain.entity.Project;
import dev.ngb.issues_logging_app.domain.repository.ProjectRepository;
import dev.ngb.issues_logging_app.domain.specification.ProjectSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper = ProjectMapper.INSTANCE;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public PageData<ProjectItemResponse> getAllProject(Pageable pageRequest) {
        Specification<Project> projectSpecification = getAllProjectsSpecification();
        if (projectSpecification == null) {
            return PageFactory.createEmptyPage();
        }
        Page<ProjectItemResponse> projectsPage = projectRepository
                .findAll(projectSpecification, pageRequest)
                .map(projectMapper::mapToProjectItemResponse);
        return PageFactory.createPage(projectsPage);
    }

    private Specification<Project> getAllProjectsSpecification() {
        if (SecurityUtils.isCurrentUserAdmin()) {
            return Specification.where(null);
        }
        List<Integer> projectIdsUserCanAccess = findAccessibleProjectIds();
        if (projectIdsUserCanAccess.isEmpty()) {
            return null;
        }
        return ProjectSpecification.hasIdIn(projectIdsUserCanAccess);
    }

    private List<Integer> findAccessibleProjectIds() {
        return projectRepository.findAllProjectIdsByMemberId(SecurityUtils.getCurrentUserId());
    }

    private Boolean canNotAccessAnyProject(List<Integer> projectIdsUserCanAccess) {
        return projectIdsUserCanAccess != null && projectIdsUserCanAccess.isEmpty();
    }

    @Override
    public ProjectDetailResponse getProjectDetailById(Integer projectId) {
        Specification<Project> projectSpecification = getProjectDetailSpecification(projectId);
        return projectRepository.findOneWithMembers(projectSpecification)
                .map(projectMapper::mapToProjectDetailResponse)
                .orElseThrow(() -> new NotFoundException("Project not found"));
    }

    private Specification<Project> getProjectDetailSpecification(Integer projectId) {
        Specification<Project> projectSpecification = ProjectSpecification.hasId(projectId);
        if (SecurityUtils.isCurrentUserAdmin()) {
            return projectSpecification;
        }
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        return projectSpecification.and(ProjectSpecification.hasMemberId(currentUserId));
    }
}
