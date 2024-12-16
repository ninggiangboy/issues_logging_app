package dev.ngb.issues_logging_app.api.resource;

import dev.ngb.issues_logging_app.api.endpoint.AuthEndpoint;
import dev.ngb.issues_logging_app.application.dto.auth.LoginRequest;
import dev.ngb.issues_logging_app.application.dto.auth.LoginResponse;
import dev.ngb.issues_logging_app.application.service.AuthService;
import dev.ngb.issues_logging_app.common.factory.ResponseFactory;
import dev.ngb.issues_logging_app.common.model.ApiResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthResource implements AuthEndpoint {

    private final AuthService authService;

    public AuthResource(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public ApiResult<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse loginResult = authService.authenticate(request);
        return ResponseFactory.createResultResponse(loginResult);
    }

    @Override
    public ApiResult<LoginResponse> refresh(String refreshToken) {
        LoginResponse refreshResult = authService.refresh(refreshToken);
        return ResponseFactory.createResultResponse(refreshResult);
    }

    @Override
    public ApiResult<Void> logout(String refreshToken) {
        authService.invalidate(refreshToken);
        return ResponseFactory.createResultResponse();
    }
}
