package dev.ngb.issues_logging_app.application.service;

import dev.ngb.issues_logging_app.application.dto.category.CategoryCreateRequest;
import dev.ngb.issues_logging_app.application.dto.category.CategoryItemResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryItemResponse> getAllCategories();

    CategoryItemResponse createCategory(CategoryCreateRequest request);
}
