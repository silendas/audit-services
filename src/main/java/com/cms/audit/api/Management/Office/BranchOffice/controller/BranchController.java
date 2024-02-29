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
import com.cms.audit.api.Management.Office.BranchOffice.services.BranchService;
import com.cms.audit.api.common.response.GlobalResponse;
import com.cms.audit.api.common.response.ResponseEntittyHandler;

@RestController
@RequestMapping("/api/branch_office")
public class BranchController {
    
    @Autowired
    private BranchService branchService;

    @GetMapping("/get")
    public ResponseEntity<Object> findAll(){
        GlobalResponse response = branchService.findAll(); 
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id){
        GlobalResponse response = branchService.findOne(id); 
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/get/{id}/area_office")
    public ResponseEntity<Object> findOneByAreaId(@PathVariable("id") Long id){
        GlobalResponse response = branchService.findOneByAreaId(id); 
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody BranchDTO branchDTO){
        GlobalResponse response =  branchService.save(branchDTO);
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    }

    
    @PutMapping("/put")
    public ResponseEntity<Object> edit(@RequestBody BranchDTO branchDTO){
        GlobalResponse response =  branchService.edit(branchDTO);
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    }


}
