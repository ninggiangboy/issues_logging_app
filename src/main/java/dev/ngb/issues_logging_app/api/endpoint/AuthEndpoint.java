package dev.ngb.issues_logging_app.api.endpoint;

import dev.ngb.issues_logging_app.application.dto.auth.LoginRequest;
import dev.ngb.issues_logging_app.application.dto.auth.LoginResponse;
import dev.ngb.issues_logging_app.common.model.ApiResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
public interface AuthEndpoint {
    @PostMapping("/login")
    ApiResult<LoginResponse> login(@Valid @RequestBody LoginRequest request);

    @PostMapping("/refresh")
    ApiResult<LoginResponse> refresh(@RequestParam("token") String refreshToken);

    @DeleteMapping("/logout")
    ApiResult<Void> logout(@RequestParam("token") String refreshToken);
}
