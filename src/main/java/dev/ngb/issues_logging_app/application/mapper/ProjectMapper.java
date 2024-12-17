package dev.ngb.issues_logging_app.application.mapper;

import dev.ngb.issues_logging_app.application.dto.project.ProjectDetailResponse;
import dev.ngb.issues_logging_app.application.dto.project.ProjectItemResponse;
import dev.ngb.issues_logging_app.domain.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProjectMapper {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    ProjectItemResponse mapToItemResponse(Project project);

    ProjectDetailResponse mapToDetailResponse(Project project);

}
