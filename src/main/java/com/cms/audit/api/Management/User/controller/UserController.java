package com.cms.audit.api.Management.User.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Management.User.dto.ChangePasswordDTO;
import com.cms.audit.api.Management.User.dto.UserDTO;
import com.cms.audit.api.Management.User.services.UserService;
import com.cms.audit.api.common.constant.BasePath;
import com.cms.audit.api.common.response.GlobalResponse;
import com.cms.audit.api.common.response.ResponseEntittyHandler;

@RestController
@RequestMapping(value = BasePath.BASE_PATH_USER)
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Object> findAll(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        GlobalResponse response = userService.findAll(page.orElse(0), size.orElse(10));
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id) {
        GlobalResponse response = userService.findOne(id);
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/{id}/main")
    public ResponseEntity<Object> findOneByMainId(
            @PathVariable("id") Long id,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        GlobalResponse response = userService.findOneByMainId(id, page.orElse(0), size.orElse(10));
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/{id}/region")
    public ResponseEntity<Object> findOneByRegionId(
            @PathVariable("id") Long id,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        GlobalResponse response = userService.findOneByRegionId(id, page.orElse(0), size.orElse(10));
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/{id}/area")
    public ResponseEntity<Object> findOneByAreaId(@PathVariable("id") Long id,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        GlobalResponse response = userService.findOneByAreaId(id, page.orElse(0), size.orElse(10));
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/{id}/branch")
    public ResponseEntity<Object> findOneByBranchId(@PathVariable("id") Long id,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        GlobalResponse response = userService.findOneByBranchId(id, page.orElse(0), size.orElse(10));
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody UserDTO userDTO) {
        GlobalResponse response = userService.save(userDTO);
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @PostMapping("/change-password/{id}")
    public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO,
            @PathVariable("id") Long id) {
        GlobalResponse response = userService.changePassword(changePasswordDTO, id);
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody UserDTO userDTO, @PathVariable("id") Long id) {
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
