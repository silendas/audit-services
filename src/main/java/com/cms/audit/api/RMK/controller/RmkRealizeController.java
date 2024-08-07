package com.cms.audit.api.RMK.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.RMK.dto.RmkRealizeDto;
import com.cms.audit.api.RMK.service.RmkRealizeService;

@RestController
@RequestMapping(value = BasePath.BASE_PATH_RMK_REALIZE)
public class RmkRealizeController {

    @Autowired
    private RmkRealizeService service;

    @GetMapping
    public ResponseEntity<Object> getRmkRealize(
            @RequestParam("rmk_id") Optional<Long> rmk_id
    ){
        return service.getRmkRealize(rmk_id.orElse(null));
    }

    @PostMapping
    public ResponseEntity<Object> createRmkRealize(@RequestBody RmkRealizeDto rmkRealize){
        return service.createRmkRealize(rmkRealize);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateRmkRealize(@RequestBody RmkRealizeDto rmkRealize, @PathVariable("id") Long id){
        return service.updateRmkRealize(rmkRealize, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRmkRealize(@PathVariable("id") Long id){
        return service.deleteRmkRealize(id);
    }
    
}
