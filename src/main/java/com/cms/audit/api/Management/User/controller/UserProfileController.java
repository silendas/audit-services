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
import com.cms.audit.api.Management.User.response.Response;
import com.cms.audit.api.Management.User.services.UserProfileService;
import com.cms.audit.api.ResponseEntity.ResponseEntittyHandler;

@RestController
@RequestMapping("/api/users/profile")
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/get")
    public ResponseEntity<Object> findAll(){
        Response userProfileResponse = userProfileService.findAll(); 
        return ResponseEntittyHandler.responseEntityGenerator(userProfileResponse.getData(),userProfileResponse.getMessage(), userProfileResponse.getStatus(), null);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id){
        Response userProfileResponse = userProfileService.findOne(id); 
        return ResponseEntittyHandler.responseEntityGenerator(userProfileResponse.getData(),userProfileResponse.getMessage(), userProfileResponse.getStatus(), null);
    }

    @GetMapping("/get/{id}/main_office")
    public ResponseEntity<Object> findOneByMainId(@PathVariable("id") Long id){
        Response userProfileResponse = userProfileService.findOneByMainId(id); 
        return ResponseEntittyHandler.responseEntityGenerator(userProfileResponse.getData(),userProfileResponse.getMessage(), userProfileResponse.getStatus(), null);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody UserDTO userDTO){
        Response userProfileResponse =  userProfileService.save(userDTO);
        return ResponseEntittyHandler.responseEntityGenerator(userProfileResponse.getData(),userProfileResponse.getMessage(), userProfileResponse.getStatus(), null);
    }

    
    @PutMapping("/put")
    public ResponseEntity<Object> edit(@RequestBody UserDTO userDTO){
        Response userProfileResponse =  userProfileService.edit(userDTO);
        return ResponseEntittyHandler.responseEntityGenerator(userProfileResponse.getData(),userProfileResponse.getMessage(), userProfileResponse.getStatus(), null);
    }
}
