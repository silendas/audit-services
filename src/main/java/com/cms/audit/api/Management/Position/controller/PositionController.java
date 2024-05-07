package com.cms.audit.api.Management.Position.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Management.Position.dto.PositionDTO;
import com.cms.audit.api.Management.Position.service.PositionService;

@RestController
@RequestMapping(value = BasePath.BASE_PATH_POSITION)
public class PositionController {
    
    @Autowired
    private PositionService service;

    @GetMapping()
    public ResponseEntity<Object> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getDetail(@PathVariable("id") Long id
    ) {
        return service.getById(id);
    }

    @PostMapping()
    public ResponseEntity<Object> create(PositionDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(PositionDTO dto, @PathVariable("id") Long id
    ) {
        return service.update(dto, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id
    ) {
        return service.delete(id);
    }
    
}
