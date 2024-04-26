package com.cms.audit.api.Common.response;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntittyHandler {
    public static ResponseEntity<Object> authSuccess(String message, String token, HttpStatus status) {

        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("timestamp", new Date());
        meta.put("api_version", "0.0.1");

        Map<String, Object> data = new LinkedHashMap<>();
        if (token != null) {
            data.put("token", token);
            data.put("refresh", null);
        }

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("meta", meta);
        map.put("message", message);
        map.put("status", status.value());
        if (token != null) {
            map.put("data", data);
        }

        return new ResponseEntity<Object>(map, status);
    }

    public static ResponseEntity<Object> errorResponse(String errors, String message, HttpStatus status) {

        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("timestamp", new Date());
        meta.put("api_version", "0.0.1");

        Map<String, Object> errora = new LinkedHashMap<>();
        errora.put("error", errors);

        Map<String, Object> map = new LinkedHashMap<>();

        if (errora != null) {
            map.put("meta", meta);
            map.put("message", errora);
            map.put("status", status.value());
            map.put("details", errora);
        }
        return new ResponseEntity<Object>(map, status);
    }

    public static ResponseEntity<Object> allHandler(Object data, String message, HttpStatus status, Exception error) {

        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("timestamp", new Date());
        meta.put("api_version", "0.0.1");

        Map<String, Object> map = new LinkedHashMap<>();

        if (error != null) {
            Map<String, Object> err = new LinkedHashMap<>();
            err.put("name", error.getClass());
            if (message != null) {
                err.put("message", message);
            } else {
                err.put("message", error.getMessage());
            }
            err.put("cause", error.getStackTrace());

            map.put("meta", meta);
            map.put("status", status.value());
            map.put("error", err);
        }

        if (message != null) {
            map.put("meta", meta);
            map.put("message", message);
            map.put("status", status.value());
            if (data != null) {
                map.put("data", data);
            } else {
                if (message != "Success") {
                    map.put("data", null);
                }
            }
        }

        return new ResponseEntity<Object>(map, status);
    }

}
