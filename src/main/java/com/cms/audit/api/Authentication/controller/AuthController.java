package com.cms.audit.api.Authentication.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Authentication.dto.SigninDTO;
import com.cms.audit.api.Authentication.dto.response.AuthResponse;
import com.cms.audit.api.Authentication.services.AuthService;
import com.cms.audit.api.common.constant.BasePath;
import com.cms.audit.api.common.response.ResponseEntittyHandler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = BasePath.BASE_PATH_AUTH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@ModelAttribute SigninDTO signinDTO) {
        AuthResponse response = authService.login(signinDTO);
        return ResponseEntittyHandler.authSuccess(response.getToken(), response.getStatus());
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        final String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader == null) {
            return ResponseEntittyHandler.allHandler(null, "No token", HttpStatus.BAD_REQUEST, null);

        }
        String jwtToken = tokenHeader.substring(7);
        AuthResponse response = authService.logout(jwtToken);
        return ResponseEntittyHandler.authSuccess(null, HttpStatus.OK);
    }
}
