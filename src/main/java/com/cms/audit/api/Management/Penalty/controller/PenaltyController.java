package com.cms.audit.api.Management.Penalty.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Management.Penalty.dto.PenaltyDTO;
import com.cms.audit.api.Management.Penalty.services.PenaltyService;
import com.cms.audit.api.common.response.GlobalResponse;
import com.cms.audit.api.common.response.ResponseEntittyHandler;

@RestController
@RequestMapping("/api/penalty")
public class PenaltyController {
    
    @Autowired
    private PenaltyService PenaltyService;

    @GetMapping("/get")
    public ResponseEntity<Object> findAll(){
        GlobalResponse response = PenaltyService.findAll(); 
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id){
        GlobalResponse response = PenaltyService.findOne(id); 
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody PenaltyDTO PenaltyDTO){
        GlobalResponse response =  PenaltyService.save(PenaltyDTO);
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    }

    
    @PutMapping("/put")
    public ResponseEntity<Object> edit(@RequestBody PenaltyDTO PenaltyDTO){
        GlobalResponse response =  PenaltyService.edit(PenaltyDTO);
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    }


}
