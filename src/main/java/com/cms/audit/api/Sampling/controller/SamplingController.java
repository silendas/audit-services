package com.cms.audit.api.Sampling.controller;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Sampling.dto.request.SamplingDto;
import com.cms.audit.api.Sampling.service.SamplingService;

@RestController
@RequestMapping(value = BasePath.BASE_PATH_SAMPLING)
public class SamplingController {

    @Autowired
    private SamplingService service;

    @GetMapping
    public ResponseEntity<Object> getSampling(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> start_date,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> end_date,
            @RequestParam("pageable") Optional<Boolean> pageable,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        return service.getSampling(start_date.orElse(null), end_date.orElse(null), page.orElse(0), size.orElse(10), pageable.orElse(false));
    }

    // @GetMapping("/unit")
    // public ResponseEntity<Object> getUnit() {
    // return ResponseEntittyHandler.allHandler(EName.values(), "Berhasil",
    // HttpStatus.OK, null);
    // }

    @PostMapping
    public ResponseEntity<Object> createSampling(@RequestBody SamplingDto dto) {
        return service.createSampling(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSampling(@PathVariable("id") Long id) {
        return service.deleteSampling(id);
    }

}
