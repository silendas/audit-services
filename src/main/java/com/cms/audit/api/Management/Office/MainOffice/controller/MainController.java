package com.cms.audit.api.Management.Office.MainOffice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.Management.Office.MainOffice.dto.MainDTO;
import com.cms.audit.api.Management.Office.MainOffice.services.MainService;

@RestController
@RequestMapping(value = BasePath.BASE_PATH_MAIN_OFFICE)
public class MainController {

    @Autowired
    private MainService mainService;

    @GetMapping
    public ResponseEntity<Object> findAll() {
        GlobalResponse response = mainService.findAll();
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id) {
        GlobalResponse response = mainService.findOne(id);
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody MainDTO levelDTO) {
        GlobalResponse response = mainService.save(levelDTO);
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody MainDTO levelDTO, @PathVariable("id") Long id) {
        GlobalResponse response = mainService.edit(levelDTO, id);
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        GlobalResponse response = mainService.delete(id);
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

}
