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
import com.cms.audit.api.Management.Penalty.response.Response;
import com.cms.audit.api.Management.Penalty.services.PenaltyService;
import com.cms.audit.api.ResponseEntity.ResponseEntittyHandler;

@RestController
@RequestMapping("/api/penalty")
public class PenaltyController {
    
    @Autowired
    private PenaltyService PenaltyService;

    @GetMapping("/get")
    public ResponseEntity<Object> findAll(){
        Response PenaltyResponse = PenaltyService.findAll(); 
        return ResponseEntittyHandler.responseEntityGenerator(PenaltyResponse.getData(),PenaltyResponse.getMessage(), PenaltyResponse.getStatus(), null);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id){
        Response PenaltyResponse = PenaltyService.findOne(id); 
        return ResponseEntittyHandler.responseEntityGenerator(PenaltyResponse.getData(),PenaltyResponse.getMessage(), PenaltyResponse.getStatus(), null);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody PenaltyDTO PenaltyDTO){
        Response PenaltyResponse =  PenaltyService.save(PenaltyDTO);
        return ResponseEntittyHandler.responseEntityGenerator(PenaltyResponse.getData(),PenaltyResponse.getMessage(), PenaltyResponse.getStatus(), null);
    }

    
    @PutMapping("/put")
    public ResponseEntity<Object> edit(@RequestBody PenaltyDTO PenaltyDTO){
        Response PenaltyResponse =  PenaltyService.edit(PenaltyDTO);
        return ResponseEntittyHandler.responseEntityGenerator(PenaltyResponse.getData(),PenaltyResponse.getMessage(), PenaltyResponse.getStatus(), null);
    }


}
