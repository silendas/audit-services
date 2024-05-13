package com.cms.audit.api.Management.User.controller;

import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.cms.audit.api.Management.User.dto.EditUserDTO;
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
        if (userDTO.getRegion_id() != null) {
            if (CollectionUtils.isEmpty(userDTO.getRegion_id())) {
                userDTO.setRegion_id(null);
            }
        }
        if (userDTO.getBranch_id() != null) {
            if (CollectionUtils.isEmpty(userDTO.getBranch_id())) {
                userDTO.setBranch_id(null);
            }
        }
        if (userDTO.getArea_id() != null) {
            if (CollectionUtils.isEmpty(userDTO.getArea_id())) {
                userDTO.setArea_id(null);
            }
        }
        GlobalResponse response = userService.save(userDTO);
        if (response.getStatus().equals(HttpStatus.BAD_REQUEST)) {
            return ResponseEntittyHandler.errorResponse(response.getErrorMessage(), response.getMessage(),
                    response.getStatus());
        } else {
            return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                    response.getError());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editProfile(
            @RequestBody EditUserDTO userDTO,
            @PathVariable("id") Long id) {
        if (userDTO.getRegion_id() != null) {
            if (CollectionUtils.isEmpty(userDTO.getRegion_id())) {
                userDTO.setRegion_id(null);
            }
        }
        if (userDTO.getBranch_id() != null) {
            if (CollectionUtils.isEmpty(userDTO.getBranch_id())) {
                userDTO.setBranch_id(null);
            }
        }
        if (userDTO.getArea_id() != null) {
            if (CollectionUtils.isEmpty(userDTO.getArea_id())) {
                userDTO.setArea_id(null);
            }
        }
        GlobalResponse response = userService.edit(userDTO, id);
        if (response.getStatus().equals(HttpStatus.BAD_REQUEST)) {
            return ResponseEntittyHandler.errorResponse(response.getErrorMessage(), response.getMessage(),
                    response.getStatus());
        } else {
            return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                    response.getError());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        GlobalResponse response = userService.delete(id);
        if (response.getStatus().equals(HttpStatus.BAD_REQUEST)) {
            return ResponseEntittyHandler.errorResponse(response.getErrorMessage(), response.getMessage(),
                    response.getStatus());
        } else {
            return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                    response.getError());
        }
    }
}
