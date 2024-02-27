package com.cms.audit.api.Management.Office.AreaOffice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Management.Office.AreaOffice.dto.AreaDTO;
import com.cms.audit.api.Management.Office.AreaOffice.response.Response;
import com.cms.audit.api.Management.Office.AreaOffice.services.AreaService;
import com.cms.audit.api.ResponseEntity.ResponseEntittyHandler;

@RestController
@RequestMapping("/api/area_office")
public class AreaController {
    
    @Autowired
    private AreaService areaService;

    @GetMapping("/get")
    public ResponseEntity<Object> findAll(){
        Response areaResponse = areaService.findAll(); 
        return ResponseEntittyHandler.responseEntityGenerator(areaResponse.getData(),areaResponse.getMessage(), areaResponse.getStatus(), null);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id){
        Response areaResponse = areaService.findOne(id); 
        return ResponseEntittyHandler.responseEntityGenerator(areaResponse.getData(),areaResponse.getMessage(), areaResponse.getStatus(), null);
    }

    @GetMapping("/get/{id}/region_office")
    public ResponseEntity<Object> findOneByRegionId(@PathVariable("id") Long id){
        Response areaResponse = areaService.findOneByRegionId(id); 
        return ResponseEntittyHandler.responseEntityGenerator(areaResponse.getData(),areaResponse.getMessage(), areaResponse.getStatus(), null);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody AreaDTO areaDTO){
        Response areaResponse =  areaService.save(areaDTO);
        return ResponseEntittyHandler.responseEntityGenerator(null,areaResponse.getMessage(), areaResponse.getStatus(), null);
    }

    @PutMapping("/put")
    public ResponseEntity<Object> edit(@RequestBody AreaDTO areaDTO){
        Response areaResponse =  areaService.edit(areaDTO);
        return ResponseEntittyHandler.responseEntityGenerator(null,areaResponse.getMessage(), areaResponse.getStatus(), null);
    }


}
