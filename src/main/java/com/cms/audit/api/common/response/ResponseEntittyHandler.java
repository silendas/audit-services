package com.cms.audit.api.Common.response;

import java.util.Map;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntittyHandler {
    public static ResponseEntity<Object> authSuccess(String token, HttpStatus status) {

        Map<String, Object> meta = new HashMap<String, Object>();
        meta.put("time_stamp", null);
        meta.put("api_version", null);

        Map<String, Object> data = new HashMap<String, Object>();      
        if (token != null) {
            data.put("token", token);
            data.put("refresh", null);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", "Success");
        map.put("status", status.value());
        map.put("meta", meta);
        map.put("data", data);

        return new ResponseEntity<Object>(map, status);
    }

    public static ResponseEntity<Object> pagination(Object data, String message, HttpStatus status, Exception error) {

        Map<String, Object> meta = new HashMap<String, Object>();
        meta.put("time_stamp", null);
        meta.put("pagination", null);
        meta.put("api_version", null);

        Map<String, Object> map = new HashMap<String, Object>();


        if (error != null) {
            Map<String, Object> err = new HashMap<String, Object>();
            meta.put("name", error.getCause());
            meta.put("message", error.getMessage());
            meta.put("header", error.getClass());

            map.put("meta", meta);
            map.put("error", err);
            map.put("status", status.value());
        } 

        if(message != null){
            map.put("meta", meta);
            map.put("message", message);
            map.put("status", status.value());
            if (data != null) {
                map.put("data", data);
            }
        }

        return new ResponseEntity<Object>(map, status);
    }

    public static ResponseEntity<Object> allHandler(Object data, String message, HttpStatus status, Exception error) {

        Map<String, Object> meta = new HashMap<String, Object>();
        meta.put("time_stamp", null);
        meta.put("api_version", null);

        Map<String, Object> map = new HashMap<String, Object>();


        if (error != null) {
            Map<String, Object> err = new HashMap<String, Object>();
            meta.put("name", error.getCause());
            meta.put("message", error.getMessage());
            meta.put("header", error.getLocalizedMessage());

            map.put("meta", meta);
            map.put("error", err);
            map.put("status", status.value());
        } 

        if(message != null){
            map.put("meta", meta);
            map.put("message", message);
            map.put("status", status.value());
            if (data != null) {
                map.put("data", data);
            }
        }

        return new ResponseEntity<Object>(map, status);
    }
}
