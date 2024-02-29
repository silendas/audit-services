package com.cms.audit.api.Management.User.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Management.User.dto.UserDTO;
import com.cms.audit.api.Management.User.services.UserProfileService;
import com.cms.audit.api.common.constant.BasePath;
import com.cms.audit.api.common.response.GlobalResponse;
import com.cms.audit.api.common.response.ResponseEntittyHandler;

@RestController
@RequestMapping(value = BasePath.BASE_PATH_USERS)
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/get")
    public ResponseEntity<Object> findAll(){
        GlobalResponse response = userProfileService.findAll(); 
        if(response.getError() != null){
        return ResponseEntittyHandler.allHandler(null,null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id) {
        GlobalResponse response = userProfileService.findOne(id);
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null,null, response.getStatus(), response.getError());
            }
            return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/get/{id}/main_office")
    public ResponseEntity<Object> findOneByMainId(@PathVariable("id") Long id) {
        GlobalResponse response = userProfileService.findOneByMainId(id);
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null,null, response.getStatus(), response.getError());
            }
            return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody UserDTO userDTO) {
        GlobalResponse response = userProfileService.save(userDTO);
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null,null, response.getStatus(), response.getError());
            }
            return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @PutMapping("/put")
    public ResponseEntity<Object> edit(@RequestBody UserDTO userDTO) {
        GlobalResponse response = userProfileService.edit(userDTO);
        if(response.getError() != null){
        return ResponseEntittyHandler.allHandler(null,null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }
}
