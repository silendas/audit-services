package com.cms.audit.api.Common.response;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntittyHandler {
    public static ResponseEntity<Object> authSuccess(String message,String token, HttpStatus status) {

        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("timestamp", null);
        meta.put("api_version", null);

        Map<String, Object> data = new LinkedHashMap<>();
        if (token != null) {
            data.put("token", token);
            data.put("refresh", null);
        }

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("meta", meta);
        map.put("data", data);
        map.put("message", message);
        map.put("status", status.value());

        return new ResponseEntity<Object>(map, status);
    }

    public static ResponseEntity<Object> allHandler(Object data, String message, HttpStatus status, Exception error) {

        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("timestamp", null);
        meta.put("api_version", null);

        Map<String, Object> map = new LinkedHashMap<>();

        if (error != null) {
            Map<String, Object> err = new LinkedHashMap<>();
            err.put("name", error.getClass());
            err.put("message", error.getMessage());
            err.put("header", error.getStackTrace());

            map.put("meta", meta);
            map.put("status", status.value());
            map.put("error", err);
        }

        if (message != null) {
            map.put("meta", meta);
            map.put("message", message);
            map.put("status", status.value());
            map.put("data", data);
        }

        return new ResponseEntity<Object>(map, status);
    }
}
