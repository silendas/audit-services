package com.cms.audit.api.Management.TemporaryRecomendation.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Management.TemporaryRecomendation.dto.TemporaryRecomendationDTO;
import com.cms.audit.api.Management.TemporaryRecomendation.response.Response;
import com.cms.audit.api.Management.TemporaryRecomendation.services.TemporaryRecomendationService;
import com.cms.audit.api.ResponseEntity.ResponseEntittyHandler;

@RestController
@RequestMapping("/api/tamporaryrecomendation")
public class TemporaryRecomendationController {
    
    @Autowired
    private TemporaryRecomendationService temporaryRecomendationService;

    @GetMapping("/get")
    public ResponseEntity<Object> findAll(){
        Response temporaryRecomendationResponse = temporaryRecomendationService.findAll(); 
        return ResponseEntittyHandler.responseEntityGenerator(temporaryRecomendationResponse.getData(),temporaryRecomendationResponse.getMessage(), temporaryRecomendationResponse.getStatus(), null);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id){
        Response temporaryRecomendationResponse = temporaryRecomendationService.findOne(id); 
        return ResponseEntittyHandler.responseEntityGenerator(temporaryRecomendationResponse.getData(),temporaryRecomendationResponse.getMessage(), temporaryRecomendationResponse.getStatus(), null);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody TemporaryRecomendationDTO levelDTO){
        Response temporaryRecomendationResponse =  temporaryRecomendationService.save(levelDTO);
        return ResponseEntittyHandler.responseEntityGenerator(temporaryRecomendationResponse.getData(),temporaryRecomendationResponse.getMessage(), temporaryRecomendationResponse.getStatus(), null);
    }

    
    @PutMapping("/put")
    public ResponseEntity<Object> edit(@RequestBody TemporaryRecomendationDTO levelDTO){
        Response temporaryRecomendationResponse =  temporaryRecomendationService.edit(levelDTO);
        return ResponseEntittyHandler.responseEntityGenerator(temporaryRecomendationResponse.getData(),temporaryRecomendationResponse.getMessage(), temporaryRecomendationResponse.getStatus(), null);
    }


}
