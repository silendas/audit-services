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
import com.cms.audit.api.Management.Office.RegionOffice.response.Response;
import com.cms.audit.api.Management.Office.RegionOffice.services.RegionService;
import com.cms.audit.api.ResponseEntity.ResponseEntittyHandler;

@RestController
@RequestMapping("/api/region_office")
public class RegionController {
    
    @Autowired
    private RegionService regionService;

    @GetMapping("/get")
    public ResponseEntity<Object> findAll(){
        Response regionResponse = regionService.findAll(); 
        return ResponseEntittyHandler.responseEntityGenerator(regionResponse.getData(),regionResponse.getMessage(), regionResponse.getStatus(), null);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id){
        Response regionResponse = regionService.findOne(id); 
        return ResponseEntittyHandler.responseEntityGenerator(regionResponse.getData(),regionResponse.getMessage(), regionResponse.getStatus(), null);
    }

    @GetMapping("/get/{id}/main_office")
    public ResponseEntity<Object> findOneByMainId(@PathVariable("id") Long id){
        Response regionResponse = regionService.findOneByMainId(id); 
        return ResponseEntittyHandler.responseEntityGenerator(regionResponse.getData(),regionResponse.getMessage(), regionResponse.getStatus(), null);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody RegionDTO regionDTO){
        Response regionResponse =  regionService.save(regionDTO);
        return ResponseEntittyHandler.responseEntityGenerator(null,regionResponse.getMessage(), regionResponse.getStatus(), null);
    }
    
    @PutMapping("/put")
    public ResponseEntity<Object> edit(@RequestBody RegionDTO regionDTO){
        Response regionResponse =  regionService.edit(regionDTO);
        return ResponseEntittyHandler.responseEntityGenerator(null,regionResponse.getMessage(), regionResponse.getStatus(), null);
    }


}
