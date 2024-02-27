package com.cms.audit.api.Authentication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Authentication.dto.SigninDTO;
//import com.cms.audit.api.Authentication.dto.SignupDTO;
import com.cms.audit.api.Authentication.response.Response;
import com.cms.audit.api.Authentication.services.AuthService;
import com.cms.audit.api.Config.Jwt.TokenBlacklist;
import com.cms.audit.api.ResponseEntity.ResponseEntittyHandler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final TokenBlacklist tokenBlacklist;

    // @PostMapping("/signup")
    // public ResponseEntity<Object> signup(@RequestBody SignupDTO signupDTO) {
    // Response response = authService.signup(signupDTO);
    // if (response.getMessage() != null) {
    // return ResponseEntittyHandler.responseErrorGenerator(response.getMessage(),
    // response.getStatus());
    // }
    // return ResponseEntittyHandler.responseEntityGenerator("Register success..",
    // response.getStatus(), null,
    // response.getToken());
    // }

    @PostMapping("/signin")
    public ResponseEntity<Object> signin(@RequestBody SigninDTO signinDTO) {
        Response response = authService.signin(signinDTO);
        return ResponseEntittyHandler.responseEntityGenerator(null, response.getMessage(), response.getStatus(),
                response.getToken());
    }

    @PostMapping("/signout")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        Response response = authService.extractTokenFromRequest(request);
        tokenBlacklist.addToBlacklist(response.getToken());

        // Clear any session-related data if necessary

        return ResponseEntittyHandler.responseEntityGenerator(null, response.getMessage(), response.getStatus(), response.getToken());
    }
}
