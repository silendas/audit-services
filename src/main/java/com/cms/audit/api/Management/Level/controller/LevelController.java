package com.cms.audit.api.Management.Level.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Management.Level.dto.LevelDTO;
import com.cms.audit.api.Management.Level.response.Response;
import com.cms.audit.api.Management.Level.services.LevelService;
import com.cms.audit.api.ResponseEntity.ResponseEntittyHandler;

@RestController
@RequestMapping("/api/level")
public class LevelController {
    
    @Autowired
    private LevelService levelService;

    @GetMapping("/get")
    public ResponseEntity<Object> findAll(){
        Response levelResponse = levelService.findAll(); 
        return ResponseEntittyHandler.responseEntityGenerator(levelResponse.getData(),levelResponse.getMessage(), levelResponse.getStatus(), null);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id){
        Response levelResponse = levelService.findOne(id); 
        return ResponseEntittyHandler.responseEntityGenerator(levelResponse.getData(),levelResponse.getMessage(), levelResponse.getStatus(), null);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody LevelDTO levelDTO){
        Response levelResponse =  levelService.save(levelDTO);
        return ResponseEntittyHandler.responseEntityGenerator(levelResponse.getData(),levelResponse.getMessage(), levelResponse.getStatus(), null);
    }

    
    @PutMapping("/put")
    public ResponseEntity<Object> edit(@RequestBody LevelDTO levelDTO){
        Response levelResponse =  levelService.edit(levelDTO);
        return ResponseEntittyHandler.responseEntityGenerator(levelResponse.getData(),levelResponse.getMessage(), levelResponse.getStatus(), null);
    }


}
