package com.cms.audit.api.Authentication.services;

import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import org.hibernate.exception.DataException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Authentication.dto.SigninDTO;
import com.cms.audit.api.Authentication.dto.response.AuthResponse;
//import com.cms.audit.api.Authentication.dto.SignupDTO;
import com.cms.audit.api.Authentication.repository.AuthRepository;
import com.cms.audit.api.Config.Jwt.JwtService;
import com.cms.audit.api.Management.User.models.User;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
        private final AuthRepository authRepository;

        // private final PasswordEncoder passwordEncoder;

        private final JwtService jwtService;

        private final AuthenticationManager authenticationManager;

        public AuthResponse login(SigninDTO signinDTO) {
                try {
                        // validate username or email
                        Optional<User> response = authRepository.findOneUsersByEmailOrUsername(signinDTO.getUsername(),
                                        signinDTO.getUsername());
                        if (!response.isPresent()) {
                                return AuthResponse.builder()
                                                .message("Wrong username or email")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }
                        ;

                        // valdite auth manager user
                        authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                        response.get().getEmail(),
                                                        signinDTO.getPassword()));

                        User user = authRepository.findByEmail(response.get().getEmail()).orElseThrow();

                        // generate jwt token
                        var jwtToken = jwtService.generateToken(user);

                        return AuthResponse.builder()
                                        .message("Success")
                                        .token(jwtToken)
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        return AuthResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (AuthenticationException e) {
                        return AuthResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.FORBIDDEN)
                                        .build();
                } catch (JwtException e) {
                        return AuthResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.FORBIDDEN)
                                        .build();
                } catch (Exception e) {
                        return AuthResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }

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
                        return AuthResponse
                        .builder()
                        .error(e)
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build();
                }
        }
}
