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
import com.cms.audit.api.Management.CaseCategory.services.CaseCategoryService;
import com.cms.audit.api.common.response.GlobalResponse;
import com.cms.audit.api.common.response.ResponseEntittyHandler;

@RestController
@RequestMapping("/api/case_category")
public class CaseCategoryController {
    
    @Autowired
    private CaseCategoryService caseCategoryService;

    @GetMapping("/get")
    public ResponseEntity<Object> findAll(){
        GlobalResponse response = caseCategoryService.findAll(); 
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id){
        GlobalResponse response = caseCategoryService.findOne(id); 
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/get/{id}/cases")
    public ResponseEntity<Object> findOneByCasesId(@PathVariable("id") Long id){
        GlobalResponse response = caseCategoryService.findOneByCasesId(id); 
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody CaseCategoryDTO caseCategoryDTO){
        GlobalResponse response =  caseCategoryService.save(caseCategoryDTO);
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    }
    
    @PutMapping("/put")
    public ResponseEntity<Object> edit(@RequestBody CaseCategoryDTO caseCategoryDTO){
        GlobalResponse response =  caseCategoryService.edit(caseCategoryDTO);
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    }


}
