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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Sampling.dto.request.RandomTableDto;
import com.cms.audit.api.Sampling.service.RandomTableService;

@RestController
@RequestMapping(value = BasePath.BASE_PATH_RANDOM_TABLE)
public class RandomTableController {

    @Autowired
    private RandomTableService randomTableService;

    @GetMapping
    public ResponseEntity<Object> getRandomTables(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> start_date,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> end_date,
            @RequestParam("pageable") Optional<Boolean> pageable,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        return randomTableService.getRandomTables(pageable.orElse(false), page.orElse(0), size.orElse(10), start_date.orElse(null), end_date.orElse(null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRandomTable(@PathVariable("id") Long id) {
        return randomTableService.getRandomTable(id);
    }

    @PostMapping
    public ResponseEntity<Object> createRandomTable(@RequestBody RandomTableDto dto) {
        return randomTableService.createRandomTable(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateRandomTable(@PathVariable("id") Long id, @RequestBody RandomTableDto dto) {
        return randomTableService.updateRandomTable(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRandomTable(@PathVariable("id") Long id) {
        return randomTableService.deleteRandomTable(id);
    }

}
