package dev.ngb.issues_logging_app.application.service;

import dev.ngb.issues_logging_app.application.dto.tag.TagCreateRequest;
import dev.ngb.issues_logging_app.application.dto.tag.TagItemResponse;

import java.util.List;

public interface TagService {
    List<TagItemResponse> getAllTags();

    TagItemResponse createTag(TagCreateRequest request);
}
