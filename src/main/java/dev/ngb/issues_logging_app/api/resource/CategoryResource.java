package dev.ngb.issues_logging_app.api.resource;

import dev.ngb.issues_logging_app.api.endpoint.CategoryEndpoint;
import dev.ngb.issues_logging_app.application.dto.category.CategoryCreateRequest;
import dev.ngb.issues_logging_app.application.dto.category.CategoryItemResponse;
import dev.ngb.issues_logging_app.application.service.CategoryService;
import dev.ngb.issues_logging_app.common.factory.ResponseFactory;
import dev.ngb.issues_logging_app.common.model.ApiResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryResource implements CategoryEndpoint {

    private final CategoryService categoryService;

    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public ApiResult<List<CategoryItemResponse>> getAll() {
        List<CategoryItemResponse> categoriesList = categoryService.getAllCategories();
        return ResponseFactory.createResultResponse(categoriesList);
    }

    @Override
    public ApiResult<CategoryItemResponse> create(CategoryCreateRequest request) {
        CategoryItemResponse createdCategory = categoryService.createCategory(request);
        return ResponseFactory.createResultResponse(createdCategory);
    }
}
