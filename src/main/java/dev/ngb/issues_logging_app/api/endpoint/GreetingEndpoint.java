package dev.ngb.issues_logging_app.api.endpoint;

import dev.ngb.issues_logging_app.api.resource.GreetingResource;
import dev.ngb.issues_logging_app.common.model.ApiResult;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequestMapping("/greeting")
public interface GreetingEndpoint {
    @GetMapping("/public")
    ApiResult<Void> hello();

    @GetMapping("/private")
    ApiResult<Void> helloPrivate(@AuthenticationPrincipal Principal principal);
}
