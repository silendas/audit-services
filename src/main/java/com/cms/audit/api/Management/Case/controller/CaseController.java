package com.cms.audit.api.Management.Case.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Management.Case.dto.CaseDTO;
import com.cms.audit.api.Management.Case.response.Response;
import com.cms.audit.api.Management.Case.services.CaseService;
import com.cms.audit.api.ResponseEntity.ResponseEntittyHandler;

@RestController
@RequestMapping("/api/case")
public class CaseController {
    
    @Autowired
    private CaseService caseService;

    @GetMapping("/get")
    public ResponseEntity<Object> findAll(){
        Response caseResponse = caseService.findAll(); 
        return ResponseEntittyHandler.responseEntityGenerator(caseResponse.getData(),caseResponse.getMessage(), caseResponse.getStatus(), null);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id){
        Response caseResponse = caseService.findOne(id); 
        return ResponseEntittyHandler.responseEntityGenerator(caseResponse.getData(),caseResponse.getMessage(), caseResponse.getStatus(), null);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody CaseDTO caseDTO){
        Response caseResponse =  caseService.save(caseDTO);
        return ResponseEntittyHandler.responseEntityGenerator(caseResponse.getData(),caseResponse.getMessage(), caseResponse.getStatus(), null);
    }

    
    @PutMapping("/put")
    public ResponseEntity<Object> edit(@RequestBody CaseDTO caseDTO){
        Response caseResponse =  caseService.edit(caseDTO);
        return ResponseEntittyHandler.responseEntityGenerator(caseResponse.getData(),caseResponse.getMessage(), caseResponse.getStatus(), null);
    }


}
