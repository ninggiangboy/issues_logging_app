package dev.ngb.issues_logging_app.application.mapper;

import dev.ngb.issues_logging_app.application.dto.category.CategoryCreateRequest;
import dev.ngb.issues_logging_app.application.dto.category.CategoryItemResponse;
import dev.ngb.issues_logging_app.domain.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryItemResponse mapToResponse(Category category);

    Category mapToEntity(CategoryCreateRequest request);
}
