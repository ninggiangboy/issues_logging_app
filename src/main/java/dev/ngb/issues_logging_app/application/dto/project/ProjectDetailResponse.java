package dev.ngb.issues_logging_app.application.dto.project;

import java.util.List;
import java.util.UUID;

public record ProjectDetailResponse(
        Long id,
        String name,
        String description,
        String createdAt,
        List<ProjectMemberResponse> members
) {
    public record ProjectMemberResponse(
            UUID id,
            String username,
            String fullName
    ) {
    }
}
