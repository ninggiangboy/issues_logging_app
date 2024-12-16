package dev.ngb.issues_logging_app.application.mapper;

import dev.ngb.issues_logging_app.application.dto.tag.TagCreateRequest;
import dev.ngb.issues_logging_app.application.dto.tag.TagItemResponse;
import dev.ngb.issues_logging_app.domain.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TagMapper {

    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    TagItemResponse mapToResponse(Tag tag);

    Tag mapToEntity(TagCreateRequest tagCreateRequest);
}
