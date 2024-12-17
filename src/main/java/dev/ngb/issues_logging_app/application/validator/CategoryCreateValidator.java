package dev.ngb.issues_logging_app.application.validator;

import dev.ngb.issues_logging_app.application.dto.category.CategoryCreateRequest;
import dev.ngb.issues_logging_app.common.util.StringUtils;
import dev.ngb.issues_logging_app.common.validator.ValidatorChain;
import org.springframework.stereotype.Component;

@Component
public class CategoryCreateValidator extends ValidatorChain<CategoryCreateRequest> {

    private static final int NAME_MAX_LENGTH = 25;

    public CategoryCreateValidator() {
        ruleFor(CategoryCreateRequest.Fields.name, CategoryCreateRequest::name)
                .require(StringUtils::isNotBlank, "Name is required")
                .require(name -> StringUtils.hasLengthLessThan(name, NAME_MAX_LENGTH), "Name is too long");
    }
}
