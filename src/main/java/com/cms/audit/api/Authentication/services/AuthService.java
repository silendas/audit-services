package com.cms.audit.api.Authentication.services;

import java.util.Optional;

import org.hibernate.exception.DataException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cms.audit.api.Authentication.dto.SigninDTO;
//import com.cms.audit.api.Authentication.dto.SignupDTO;
import com.cms.audit.api.Authentication.repository.AuthRepository;
import com.cms.audit.api.Authentication.response.Response;
import com.cms.audit.api.Config.Jwt.JwtService;
import com.cms.audit.api.Management.User.models.User;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
        private final AuthRepository authRepository;

        // private final PasswordEncoder passwordEncoder;

        private final JwtService jwtService;

        private final AuthenticationManager authenticationManager;

        // this is for signup or register, but not used for now
        // public Response signup(SignupDTO signupDTO) {
        // try {
        // var user = User
        // .builder()
        // .email(signupDTO.getEmail())
        // .fullname(signupDTO.getFullName())
        // .initialName(signupDTO.getInitialName())
        // .nip(signupDTO.getNip())
        // .username(signupDTO.getUsername())
        // .password(passwordEncoder.encode(signupDTO.getPassword()))
        // .created_at(new Date())
        // .updatedAt(new Date())
        // .build();

        // User response = authRepository.save(user);

        // return Response.builder()
        // .status(HttpStatus.OK)
        // .build();
        // } catch (RequestRejectedException e) {
        // return Response.builder()
        // .message("Something went wrong : " + e.getMessage())
        // .status(HttpStatus.UNPROCESSABLE_ENTITY)
        // .build();
        // } catch (DataException e) {
        // return Response.builder()
        // .message("Something went wrong : " + e.getMessage())
        // .status(HttpStatus.UNPROCESSABLE_ENTITY)
        // .build();
        // } catch (JwtException e) {
        // return Response.builder()
        // .message("Something went wrong : " + e.getMessage())
        // .status(HttpStatus.FORBIDDEN)
        // .build();
        // } catch (Exception e) {
        // return Response.builder()
        // .message("Something went wrong : " + e.getMessage())
        // .status(HttpStatus.INTERNAL_SERVER_ERROR)
        // .build();
        // }
        // }

        public Response signin(SigninDTO signinDTO) {
                try {
                        // validate username or email
                        Optional<User> response = authRepository.findOneUsersByEmailOrUsername(signinDTO.getUsername(),
                                        signinDTO.getUsername());
                        if (!response.isPresent()) {
                                return Response.builder()
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

                        return Response.builder()
                                        .message("Success")
                                        .token(jwtToken)
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        return Response.builder()
                                        .message("Something went wrong : " + e.getMessage())
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (AuthenticationException e) {
                        return Response.builder()
                                        .message("Something went wrong : " + e.getMessage())
                                        .status(HttpStatus.FORBIDDEN)
                                        .build();
                } catch (JwtException e) {
                        return Response.builder()
                                        .message("Something went wrong : " + e.getMessage())
                                        .status(HttpStatus.FORBIDDEN)
                                        .build();
                } catch (Exception e) {
                        return Response.builder()
                                        .message("Something went wrong : " + e.getMessage())
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }

        }

        public Response extractTokenFromRequest(HttpServletRequest request) {
                try {
                        // Get the Authorization header from the request
                        String token ;
                        String authorizationHeader = request.getHeader("Authorization");

                        // Check if the Authorization header is not null and starts with "Bearer "
                        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
                                // Extract the JWT token (remove "Bearer " prefix)
                                token =  authorizationHeader.substring(7);
                                return Response
                                        .builder()
                                        .token(token)
                                        .message("Success")
                                        .status(HttpStatus.OK)
                                        .build();
                        }

                        // If the Authorization header is not valid, return null
                        return null;
                } catch (Exception e) {
                        return Response
                                .builder()
                                .message("Something went wrong : " + e.getMessage())
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .build();
                }
        }
}
