package com.cms.audit.api.Management.Office.RegionOffice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Management.Office.RegionOffice.dto.RegionDTO;
import com.cms.audit.api.Management.Office.RegionOffice.services.RegionService;
import com.cms.audit.api.common.response.GlobalResponse;
import com.cms.audit.api.common.response.ResponseEntittyHandler;

@RestController
@RequestMapping("/api/region_office")
public class RegionController {
    
    @Autowired
    private RegionService regionService;

    @GetMapping("/get")
    public ResponseEntity<Object> findAll(){
        GlobalResponse response = regionService.findAll(); 
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id){
        GlobalResponse response = regionService.findOne(id); 
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/get/{id}/main_office")
    public ResponseEntity<Object> findOneByMainId(@PathVariable("id") Long id){
        GlobalResponse response = regionService.findOneByMainId(id); 
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody RegionDTO regionDTO){
        GlobalResponse response =  regionService.save(regionDTO);
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    }
    
    @PutMapping("/put")
    public ResponseEntity<Object> edit(@RequestBody RegionDTO regionDTO){
        GlobalResponse response =  regionService.edit(regionDTO);
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    }


}
