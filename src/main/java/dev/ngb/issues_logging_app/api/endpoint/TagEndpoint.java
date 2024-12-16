package dev.ngb.issues_logging_app.api.endpoint;

import dev.ngb.issues_logging_app.application.dto.tag.TagCreateRequest;
import dev.ngb.issues_logging_app.application.dto.tag.TagItemResponse;
import dev.ngb.issues_logging_app.common.model.ApiResult;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/tags")
public interface TagEndpoint {
    @GetMapping
    ApiResult<List<TagItemResponse>> getAll();

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    ApiResult<TagItemResponse> create(@Valid @RequestBody TagCreateRequest request);
}
