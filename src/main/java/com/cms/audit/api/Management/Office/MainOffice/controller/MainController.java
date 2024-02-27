package com.cms.audit.api.Management.Office.MainOffice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Management.Office.MainOffice.dto.MainDTO;
import com.cms.audit.api.Management.Office.MainOffice.response.Response;
import com.cms.audit.api.Management.Office.MainOffice.services.MainService;
import com.cms.audit.api.ResponseEntity.ResponseEntittyHandler;

@RestController
@RequestMapping("/api/main_office")
public class MainController {
    
    @Autowired
    private MainService mainService;

    @GetMapping("/get")
    public ResponseEntity<Object> findAll(){
        Response mainResponse = mainService.findAll(); 
        return ResponseEntittyHandler.responseEntityGenerator(mainResponse.getData(),mainResponse.getMessage(), mainResponse.getStatus(), null);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id){
        Response mainResponse = mainService.findOne(id); 
        return ResponseEntittyHandler.responseEntityGenerator(mainResponse.getData(),mainResponse.getMessage(), mainResponse.getStatus(), null);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody MainDTO levelDTO){
        Response mainResponse =  mainService.save(levelDTO);
        return ResponseEntittyHandler.responseEntityGenerator(null,mainResponse.getMessage(), mainResponse.getStatus(), null);
    }

    
    @PutMapping("/put")
    public ResponseEntity<Object> edit(@RequestBody MainDTO levelDTO){
        Response mainResponse =  mainService.edit(levelDTO);
        return ResponseEntittyHandler.responseEntityGenerator(null,mainResponse.getMessage(), mainResponse.getStatus(), null);
    }


}
