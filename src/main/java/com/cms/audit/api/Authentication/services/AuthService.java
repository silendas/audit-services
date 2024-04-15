package com.cms.audit.api.Authentication.services;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Authentication.dto.SigninDTO;
import com.cms.audit.api.Authentication.dto.response.AuthResponse;
import com.cms.audit.api.Authentication.repository.AuthRepository;
import com.cms.audit.api.Config.Jwt.JwtService;
import com.cms.audit.api.Management.User.models.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
        private final AuthRepository authRepository;

        // private final PasswordEncoder passwordEncoder;

        private final JwtService jwtService;

        private final AuthenticationManager authenticationManager;

        public AuthResponse login(SigninDTO signinDTO) {
                // validate username or email
                Optional<User> response = authRepository.findOneUsersByEmailOrUsername(signinDTO.getUsername(),
                                signinDTO.getUsername());
                if (!response.isPresent()) {
                        return AuthResponse.builder().message("Wrong username or email")
                                        .status(HttpStatus.BAD_REQUEST).build();
                };

                // validate auth manager user
                try {
                        authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                        response.get().getUsername(),
                                                        signinDTO.getPassword())
                                        );
                } catch (BadCredentialsException e) {
                        return AuthResponse.builder().message("Invalid username or password")
                                        .status(HttpStatus.BAD_REQUEST).build();
                }

                //User user = authRepository.findByUsername(response.get().getUsername()).orElseThrow();
                UserDetails userDetails = authRepository.findByUsername(response.get().getUsername()).orElseThrow();

                // generate jwt token
                var jwtToken = jwtService.generateToken(userDetails, response.orElseThrow());

                return AuthResponse.builder()
                                .message("Success")
                                .token(jwtToken)
                                .status(HttpStatus.OK)
                                .build();

        }

        public AuthResponse logout(String token) {
                try {
                        jwtService.blacklistToken(token);
                        return AuthResponse
                                        .builder()
                                        .message("Success")
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (Exception e) {
                        return AuthResponse.builder().error(e).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
        }
}
