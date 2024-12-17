package dev.ngb.issues_logging_app.application.mapper;

import dev.ngb.issues_logging_app.application.dto.issue.IssueCreateRequest;
import dev.ngb.issues_logging_app.application.dto.issue.IssueSearchResponse;
import dev.ngb.issues_logging_app.domain.entity.Issue;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IssueMapper {

    IssueMapper INSTANCE = Mappers.getMapper(IssueMapper.class);

    IssueSearchResponse mapToSearchResponse(Issue issue);

    Issue mapToEntity(IssueCreateRequest request);
}
