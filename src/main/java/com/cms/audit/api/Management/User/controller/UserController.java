package com.cms.audit.api.Management.User.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.Config.Jwt.JwtService;
import com.cms.audit.api.Management.User.dto.UserDTO;
import com.cms.audit.api.Management.User.services.UserService;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@Validated
@RequestMapping(value = BasePath.BASE_PATH_USER)
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @GetMapping
    public ResponseEntity<Object> findAll(
            @NonNull HttpServletRequest request,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        final String tokenHeader = request.getHeader("Authorization");
        String jwtToken = tokenHeader.substring(7);
        String username = jwtService.extractUsername(jwtToken);
        GlobalResponse response = userService.findAll(page.orElse(0), size.orElse(10), username);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id) {
        GlobalResponse response = userService.findOne(id);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @PostMapping
    public ResponseEntity<Object> save(
            @RequestBody UserDTO userDTO) {
        GlobalResponse response = userService.save(
                userDTO
        );
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editProfile(
            @RequestBody UserDTO userDTO,
            @PathVariable("id") Long id) {
        GlobalResponse response = userService.edit(userDTO, id);
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        GlobalResponse response = userService.delete(id);
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }
}
