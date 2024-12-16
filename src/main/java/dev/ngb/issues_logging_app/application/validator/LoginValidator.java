package dev.ngb.issues_logging_app.application.validator;

import dev.ngb.issues_logging_app.application.dto.auth.LoginRequest;
import dev.ngb.issues_logging_app.common.util.StringUtils;
import dev.ngb.issues_logging_app.common.validator.ValidatorChain;
import org.springframework.stereotype.Service;

@Service
public class LoginValidator extends ValidatorChain<LoginRequest> {
    public LoginValidator() {
        ruleFor(LoginRequest.Fields.email, LoginRequest::email)
                .require(StringUtils::isNotBlank, "Email is required")
                .require(StringUtils::isEmail, "Email is invalid");
        ruleFor(LoginRequest.Fields.password, LoginRequest::password)
                .require(StringUtils::isNotBlank, "Password is required");
        // Easter eggs
        ruleFor(LoginRequest.Fields.password,
                request ->
                        !"admin".equals(request.email()) || !"admin".equals(request.password()),
                "Are you trying to hack me?");
    }
}
