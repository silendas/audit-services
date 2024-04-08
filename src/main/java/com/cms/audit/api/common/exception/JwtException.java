package com.cms.audit.api.Common.exception;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtException implements AuthenticationEntryPoint {

    // @Override
    // public void commence(HttpServletRequest request, HttpServletResponse
    // response, AuthenticationException authException)
    // throws IOException, ServletException {
    // response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    // final Map<String, Object> body = new HashMap<>();
    // body.put("status_code", HttpServletResponse.SC_UNAUTHORIZED);
    // body.put("payload", "anda belum login");

    // final ObjectMapper mapper = new ObjectMapper();
    // mapper.writeValue(response.getOutputStream(), body);
    // }

    @Override
    public void commence(jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("timestamp", new Date());
        meta.put("version", "0.0.1");

        final Map<String, Object> body = new LinkedHashMap<>();
        body.put("meta", meta);
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("message", "Anda belum login");

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }

}
