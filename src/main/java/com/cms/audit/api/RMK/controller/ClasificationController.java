package com.cms.audit.api.RMK.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.RMK.dto.ClasificationDto;
import com.cms.audit.api.RMK.service.ClasificationService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping(value = BasePath.BASE_PATH_CLASIFICATION)
public class ClasificationController {

    @Autowired
    private ClasificationService service;

    @GetMapping
    public ResponseEntity<Object> getClasification(
            @RequestParam("pageable") Optional<Boolean> pageable,
            @RequestParam("pageable") Optional<Integer> page,
            @RequestParam("pageable") Optional<Integer> size) {
        return service.getClasification(pageable.orElse(false), page.orElse(0), size.orElse(10));
    }

    @GetMapping("/category")
    public ResponseEntity<Object> getCategory() {
        return service.getCategory();
    }

    @GetMapping("/priority")
    public ResponseEntity<Object> getPriority() {
        return service.getPriority();
    }

    @PostMapping
    public ResponseEntity<Object> createClasification(@RequestBody ClasificationDto clasification) {
        return service.createClasification(clasification);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateClasification(@RequestBody ClasificationDto clasification,
            @PathVariable("id") Long id) {
        return service.updateClasification(clasification, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteClasification(@PathVariable("id") Long id) {
        return service.deleteClasification(id);
    }

}
