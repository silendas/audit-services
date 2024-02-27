package com.cms.audit.api.ResponseEntity;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntittyHandler {
    public static ResponseEntity<Object> responseEntityGenerator(Object data, String message, HttpStatus status,
            String token) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", message);
        map.put("status", status.value());
        map.put("timestamp", new Date());

        if (data == null) {
            if (token != null) {
                map.put("result", data);
                map.put("token", token);
            }
        } else {
            map.put("result", data);
        }

        return new ResponseEntity<Object>(map, status);
    }
}
