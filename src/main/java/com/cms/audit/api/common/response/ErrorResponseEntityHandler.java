package com.cms.audit.api.common.response;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorResponseEntityHandler {
    public static ResponseEntity<Object> error(String errorHeader,String errorName,String errorMsg, HttpStatus status) {


        Map<String, Object> meta = new HashMap<String, Object>();
        meta.put("time_stamp", null);
        meta.put("pagination", null);
        meta.put("api_version", null);

        Map<String, Object> err = new HashMap<String, Object>();
        meta.put("name", errorName);
        meta.put("message", errorMsg);
        meta.put("header", errorHeader);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("meta", meta);
        map.put("error", err);
        map.put("status", status.value());

        return new ResponseEntity<Object>(map, status);
    }
}
