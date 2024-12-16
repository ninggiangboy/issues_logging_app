package dev.ngb.issues_logging_app.api.resource;

import dev.ngb.issues_logging_app.api.endpoint.GreetingEndpoint;
import dev.ngb.issues_logging_app.common.factory.ResponseFactory;
import dev.ngb.issues_logging_app.common.model.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Slf4j
public class GreetingResource implements GreetingEndpoint {
    @Override
    public ApiResult<Void> hello() {
        return ResponseFactory.createResultResponse("Hello, world!");
    }

    @Override
    public ApiResult<Void> helloPrivate(Principal principal) {
        return ResponseFactory.createResultResponse(String.format("Hello, %s!", principal.getName()));
    }
}
