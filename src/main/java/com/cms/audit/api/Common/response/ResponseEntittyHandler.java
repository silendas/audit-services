package com.cms.audit.api.Common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseEntittyHandler {

    public static ResponseEntity<Object> authSuccess(String message, String token, HttpStatus status) {
        Map<String, Object> response = createResponse(message, status);
        if (token != null) {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("token", token);
            response.put("data", data);
        }
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<Object> errorResponse(String errorMessage, String message, HttpStatus status) {
        Map<String, Object> response = createResponse(message, status);
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("error", errorMessage);
        response.put("details", details);
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<Object> allHandler(Object data, String message, HttpStatus status, Exception error) {
        Map<String, Object> response = createResponse(message, status);
        if (error != null) {
            Map<String, Object> errorDetails = new LinkedHashMap<>();
            errorDetails.put("name", error.getClass().getName());
            errorDetails.put("message", error.getMessage());
            errorDetails.put("cause", error.getStackTrace());
            response.put("error", errorDetails);
        }
        if (data != null) {
            response.put("data", data);
        }
        return new ResponseEntity<>(response, status);
    }

    private static Map<String, Object> createResponse(String message, HttpStatus status) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("meta", createMeta());
        response.put("message", message);
        response.put("status", status.value());
        return response;
    }

    private static Map<String, Object> createMeta() {
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("timestamp", new Date());
        meta.put("api_version", "0.0.1");
        return meta;
    }
}