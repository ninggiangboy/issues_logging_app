package dev.ngb.issues_logging_app.api.endpoint;

import dev.ngb.issues_logging_app.application.dto.category.CategoryCreateRequest;
import dev.ngb.issues_logging_app.application.dto.category.CategoryItemResponse;
import dev.ngb.issues_logging_app.common.model.ApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/categories")
public interface CategoryEndpoint {
    @GetMapping
    ApiResult<List<CategoryItemResponse>> getAll();

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    ApiResult<CategoryItemResponse> create(@RequestBody CategoryCreateRequest request);
}
