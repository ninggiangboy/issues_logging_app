package dev.ngb.issues_logging_app.application.validator;

import dev.ngb.issues_logging_app.application.dto.issue.IssueCreateRequest;
import dev.ngb.issues_logging_app.common.util.StringUtils;
import dev.ngb.issues_logging_app.common.validator.ValidatorChain;
import org.springframework.stereotype.Component;

@Component
public class IssueCreateValidator extends ValidatorChain<IssueCreateRequest> {

    private static final int TITLE_MAX_LENGTH = 100;
    private static final int DESCRIPTION_MIN_LENGTH = 100;

    public IssueCreateValidator() {
        ruleFor(IssueCreateRequest.Fields.title, IssueCreateRequest::title)
                .require(StringUtils::isNotBlank, "Title is required")
                .require(title -> StringUtils.hasLengthLessThan(title, TITLE_MAX_LENGTH), "Title is too long");
        ruleFor(IssueCreateRequest.Fields.description, IssueCreateRequest::description)
                .require(StringUtils::isNotBlank, "Description is required")
                .require(description -> StringUtils.hasLengthGreaterThan(description, DESCRIPTION_MIN_LENGTH), "Description is too long");
    }
}
