package com.cms.audit.api.Management.User.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Management.User.services.LogUserService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(value = BasePath.BASE_PATH_USER_LOG)
@Tag(name = "Log Users", description = "Log Users Endpoints")
public class LogUserController {
    
    @Autowired
    private LogUserService service;

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestParam(required = false) Optional<Long> user_id
    ) {
        return service.getLogByUserId(user_id.orElse(null));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Object> getDetail(@PathVariable("id") Long id
    ) {
        return service.getLogById(id);
    }

}
