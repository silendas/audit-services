package com.cms.audit.api.Management.Office.BranchOffice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Management.Office.BranchOffice.dto.BranchDTO;
import com.cms.audit.api.Management.Office.BranchOffice.response.Response;
import com.cms.audit.api.Management.Office.BranchOffice.services.BranchService;
import com.cms.audit.api.ResponseEntity.ResponseEntittyHandler;

@RestController
@RequestMapping("/api/branch_office")
public class BranchController {
    
    @Autowired
    private BranchService branchService;

    @GetMapping("/get")
    public ResponseEntity<Object> findAll(){
        Response branchResponse = branchService.findAll(); 
        return ResponseEntittyHandler.responseEntityGenerator(branchResponse.getData(),branchResponse.getMessage(), branchResponse.getStatus(), null);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id){
        Response branchResponse = branchService.findOne(id); 
        return ResponseEntittyHandler.responseEntityGenerator(branchResponse.getData(),branchResponse.getMessage(), branchResponse.getStatus(), null);
    }

    @GetMapping("/get/{id}/area_office")
    public ResponseEntity<Object> findOneByAreaId(@PathVariable("id") Long id){
        Response branchResponse = branchService.findOneByAreaId(id); 
        return ResponseEntittyHandler.responseEntityGenerator(branchResponse.getData(),branchResponse.getMessage(), branchResponse.getStatus(), null);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody BranchDTO branchDTO){
        Response branchResponse =  branchService.save(branchDTO);
        return ResponseEntittyHandler.responseEntityGenerator(null,branchResponse.getMessage(), branchResponse.getStatus(), null);
    }

    
    @PutMapping("/put")
    public ResponseEntity<Object> edit(@RequestBody BranchDTO branchDTO){
        Response branchResponse =  branchService.edit(branchDTO);
        return ResponseEntittyHandler.responseEntityGenerator(null,branchResponse.getMessage(), branchResponse.getStatus(), null);
    }


}
