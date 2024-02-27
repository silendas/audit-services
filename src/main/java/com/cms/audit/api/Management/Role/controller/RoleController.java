package com.cms.audit.api.Management.Role.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Management.Role.dto.RoleDTO;
import com.cms.audit.api.Management.Role.response.Response;
import com.cms.audit.api.Management.Role.services.RoleService;
import com.cms.audit.api.ResponseEntity.ResponseEntittyHandler;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    
    @Autowired
    private RoleService roleService;

    @GetMapping("/get")
    public ResponseEntity<Object> findAll(){
        Response roleResponse = roleService.findAll(); 
        return ResponseEntittyHandler.responseEntityGenerator(roleResponse.getData(),roleResponse.getMessage(), roleResponse.getStatus(), null);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id){
        Response roleResponse = roleService.findOne(id); 
        return ResponseEntittyHandler.responseEntityGenerator(roleResponse.getData(),roleResponse.getMessage(), roleResponse.getStatus(), null);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody RoleDTO roleDTO){
        Response roleResponse =  roleService.save(roleDTO);
        return ResponseEntittyHandler.responseEntityGenerator(roleResponse.getData(),roleResponse.getMessage(), roleResponse.getStatus(), null);
    }

    
    @PutMapping("/put")
    public ResponseEntity<Object> edit(@RequestBody RoleDTO roleDTO){
        Response roleResponse =  roleService.edit(roleDTO);
        return ResponseEntittyHandler.responseEntityGenerator(roleResponse.getData(),roleResponse.getMessage(), roleResponse.getStatus(), null);
    }


}
