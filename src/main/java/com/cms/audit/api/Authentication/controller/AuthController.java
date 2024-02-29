package com.cms.audit.api.Authentication.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Authentication.dto.SigninDTO;
import com.cms.audit.api.Authentication.dto.response.AuthResponse;
import com.cms.audit.api.Authentication.services.AuthService;
import com.cms.audit.api.common.constant.BasePath;
import com.cms.audit.api.common.response.ErrorResponseEntityHandler;
import com.cms.audit.api.common.response.ResponseEntittyHandler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = BasePath.BASE_PATH_AUTH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> login(@RequestBody SigninDTO signinDTO) {
        AuthResponse response = authService.login(signinDTO);
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.authSuccess(response.getToken(), response.getStatus());
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        final String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader == null) {
            return ErrorResponseEntityHandler.error("Failed", "Authorized", "Unauthorized", HttpStatus.BAD_REQUEST);
        }

        // Jwt token
        String jwtToken = tokenHeader.substring(7);

        AuthResponse response = authService.logout(jwtToken);
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }

        return ResponseEntittyHandler.authSuccess(null, HttpStatus.OK);
    }
}
