package dev.ngb.issues_logging_app.application.dto.project;

import java.util.List;

public record ProjectDetailResponse(
        Long id,
        String name,
        String description,
        String createdAt,
        List<ProjectMemberResponse> member
) {
    public record ProjectMemberResponse(
            Long id,
            String username,
            String fullName
    ) {
    }
}
