package dev.ngb.issues_logging_app.application.validator;

import dev.ngb.issues_logging_app.application.dto.tag.TagCreateRequest;
import dev.ngb.issues_logging_app.common.util.StringUtils;
import dev.ngb.issues_logging_app.common.validator.ValidatorChain;
import org.springframework.stereotype.Component;

@Component
public class TagCreateValidator extends ValidatorChain<TagCreateRequest> {

    private static final int NAME_MAX_LENGTH = 25;

    public TagCreateValidator() {
        ruleFor(TagCreateRequest.Fields.name, TagCreateRequest::name)
                .require(StringUtils::isNotBlank, "Name is required")
                .require(name -> StringUtils.hasLengthNotExceed(name, NAME_MAX_LENGTH), "Name is too long");
    }
}
