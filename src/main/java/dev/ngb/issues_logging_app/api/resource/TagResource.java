package dev.ngb.issues_logging_app.api.resource;

import dev.ngb.issues_logging_app.api.endpoint.TagEndpoint;
import dev.ngb.issues_logging_app.application.dto.tag.TagCreateRequest;
import dev.ngb.issues_logging_app.application.dto.tag.TagItemResponse;
import dev.ngb.issues_logging_app.application.service.TagService;
import dev.ngb.issues_logging_app.common.factory.ResponseFactory;
import dev.ngb.issues_logging_app.common.model.ApiResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TagResource implements TagEndpoint {

    private final TagService tagService;

    public TagResource(TagService tagService) {
        this.tagService = tagService;
    }

    @Override
    public ApiResult<List<TagItemResponse>> getAll() {
        List<TagItemResponse> tags = tagService.getAllTags();
        return ResponseFactory.createResultResponse(tags);
    }

    @Override
    public ApiResult<TagItemResponse> create(TagCreateRequest request) {
        TagItemResponse tag = tagService.createTag(request);
        return ResponseFactory.createResultResponse(tag);
    }
}
