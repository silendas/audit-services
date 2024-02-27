package com.cms.audit.api.Management.CaseCategory.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Management.CaseCategory.dto.CaseCategoryDTO;
import com.cms.audit.api.Management.CaseCategory.response.Response;
import com.cms.audit.api.Management.CaseCategory.services.CaseCategoryService;
import com.cms.audit.api.ResponseEntity.ResponseEntittyHandler;

@RestController
@RequestMapping("/api/case_category")
public class CaseCategoryController {
    
    @Autowired
    private CaseCategoryService caseCategoryService;

    @GetMapping("/get")
    public ResponseEntity<Object> findAll(){
        Response caseCategoryResponse = caseCategoryService.findAll(); 
        return ResponseEntittyHandler.responseEntityGenerator(caseCategoryResponse.getData(),caseCategoryResponse.getMessage(), caseCategoryResponse.getStatus(), null);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id){
        Response caseCategoryResponse = caseCategoryService.findOne(id); 
        return ResponseEntittyHandler.responseEntityGenerator(caseCategoryResponse.getData(),caseCategoryResponse.getMessage(), caseCategoryResponse.getStatus(), null);
    }

    @GetMapping("/get/{id}/cases")
    public ResponseEntity<Object> findOneByCasesId(@PathVariable("id") Long id){
        Response caseCategoryResponse = caseCategoryService.findOneByCasesId(id); 
        return ResponseEntittyHandler.responseEntityGenerator(caseCategoryResponse.getData(),caseCategoryResponse.getMessage(), caseCategoryResponse.getStatus(), null);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody CaseCategoryDTO caseCategoryDTO){
        Response caseCategoryResponse =  caseCategoryService.save(caseCategoryDTO);
        return ResponseEntittyHandler.responseEntityGenerator(caseCategoryResponse.getData(),caseCategoryResponse.getMessage(), caseCategoryResponse.getStatus(), null);
    }
    
    @PutMapping("/put")
    public ResponseEntity<Object> edit(@RequestBody CaseCategoryDTO caseCategoryDTO){
        Response caseCategoryResponse =  caseCategoryService.edit(caseCategoryDTO);
        return ResponseEntittyHandler.responseEntityGenerator(caseCategoryResponse.getData(),caseCategoryResponse.getMessage(), caseCategoryResponse.getStatus(), null);
    }


}
