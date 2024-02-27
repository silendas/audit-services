package com.cms.audit.api.Management.PermanentRecomendation.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Management.PermanentRecomendation.dto.PermanentRecomendationDTO;
import com.cms.audit.api.Management.PermanentRecomendation.response.Response;
import com.cms.audit.api.Management.PermanentRecomendation.services.PermanentRecomendationService;
import com.cms.audit.api.ResponseEntity.ResponseEntittyHandler;

@RestController
@RequestMapping("/api/permanentrecomendation")
public class PermanentRecomendationController {
    
    @Autowired
    private PermanentRecomendationService permanentRecomendationService;

    @GetMapping("/get")
    public ResponseEntity<Object> findAll(){
        Response permanentRecomendationResponse = permanentRecomendationService.findAll(); 
        return ResponseEntittyHandler.responseEntityGenerator(permanentRecomendationResponse.getData(),permanentRecomendationResponse.getMessage(), permanentRecomendationResponse.getStatus(), null);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id){
        Response permanentRecomendationResponse = permanentRecomendationService.findOne(id); 
        return ResponseEntittyHandler.responseEntityGenerator(permanentRecomendationResponse.getData(),permanentRecomendationResponse.getMessage(), permanentRecomendationResponse.getStatus(), null);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody PermanentRecomendationDTO levelDTO){
        Response permanentRecomendationResponse =  permanentRecomendationService.save(levelDTO);
        return ResponseEntittyHandler.responseEntityGenerator(permanentRecomendationResponse.getData(),permanentRecomendationResponse.getMessage(), permanentRecomendationResponse.getStatus(), null);
    }

    
    @PutMapping("/put")
    public ResponseEntity<Object> edit(@RequestBody PermanentRecomendationDTO levelDTO){
        Response permanentRecomendationResponse =  permanentRecomendationService.edit(levelDTO);
        return ResponseEntittyHandler.responseEntityGenerator(permanentRecomendationResponse.getData(),permanentRecomendationResponse.getMessage(), permanentRecomendationResponse.getStatus(), null);
    }


}
