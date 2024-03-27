package com.cms.audit.api.Management.CaseCategory.controller;


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
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.Management.CaseCategory.dto.CaseCategoryDTO;
import com.cms.audit.api.Management.CaseCategory.services.CaseCategoryService;

@RestController
@RequestMapping(value = BasePath.BASE_PATH_CASE_CATEGORY)
public class CaseCategoryController {
    
    @Autowired
    private CaseCategoryService caseCategoryService;

    @GetMapping
    public ResponseEntity<Object> findAll(
        @RequestParam("case_id") Optional<Long> caseId,
        @RequestParam("name") Optional<String> name,
        @RequestParam("page") Optional<Integer> page,
        @RequestParam("size") Optional<Integer> size
    ){
        GlobalResponse response = caseCategoryService.findAll(caseId.orElse(null),name.orElse(null), page.orElse(0), size.orElse(10)); 
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findOne(@PathVariable("id") Long id){
        GlobalResponse response = caseCategoryService.findOne(id); 
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    }

    // @GetMapping("/{id}/case")
    // public ResponseEntity<Object> findOneByCasesId(
    //     @PathVariable("id") Long id,
    //     @RequestParam("page") Optional<Integer> page,
    //     @RequestParam("size") Optional<Integer> size){
    //     GlobalResponse response = caseCategoryService.findOneByCasesId(id, page.orElse(0), size.orElse(10)); 
    //     if(response.getError() != null){
    //         return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
    //     }
    //     return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    // }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody CaseCategoryDTO caseCategoryDTO){
        GlobalResponse response =  caseCategoryService.save(caseCategoryDTO);
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestBody CaseCategoryDTO caseCategoryDTO, @PathVariable("id") Long id){
        GlobalResponse response =  caseCategoryService.edit(caseCategoryDTO, id);
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> edit(@PathVariable("id") Long id){
        GlobalResponse response =  caseCategoryService.delete(id);
        if(response.getError() != null){
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(),response.getMessage(), response.getStatus(), null);
    }


}
