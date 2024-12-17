package dev.ngb.issues_logging_app.application.dto.issue;

import lombok.experimental.FieldNameConstants;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;

@FieldNameConstants
public record IssueCreateRequest(
        String title,
        String description,
        Integer projectId,
        Integer categoryId,
        Set<Integer> tagIds,
        UUID assigneeId,
        Set<IssueCreateRequest.AttachmentCreateRequest> attachments
) {
    public record AttachmentCreateRequest(
            String name,
            MultipartFile file
    ) {
    }
}
