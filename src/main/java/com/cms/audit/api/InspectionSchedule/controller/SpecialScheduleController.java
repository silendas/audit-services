package com.cms.audit.api.InspectionSchedule.controller;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.Config.Jwt.JwtService;
import com.cms.audit.api.InspectionSchedule.dto.EditScheduleDTO;
import com.cms.audit.api.InspectionSchedule.dto.ScheduleDTO;
import com.cms.audit.api.InspectionSchedule.models.ECategory;
import com.cms.audit.api.InspectionSchedule.service.ScheduleService;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@Validated
@RequestMapping(value = BasePath.BASE_PATH_SPECIAL_SCHEDULE)
public class SpecialScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private JwtService jwtService;

    @GetMapping
    public ResponseEntity<Object> getAll(
            @RequestParam(required = false) Optional<Long> user_id,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> start_date,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> end_date,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        GlobalResponse response = scheduleService.getSpecialSchedule(user_id.orElse(null), page.orElse(0),
                size.orElse(10), start_date.orElse(null), end_date.orElse(null));
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
        GlobalResponse response = scheduleService.getById(id);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/{id}/user")
    public ResponseEntity<Object> getByUserId(
            @PathVariable("id") Long id,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> start_date,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> end_date,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        GlobalResponse response = scheduleService.getByUserId(id, "SPECIAL", page.orElse(0), size.orElse(10),
                start_date.orElse(null), end_date.orElse(null));
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/{id}/region")
    public ResponseEntity<Object> getByRegionId(
            @PathVariable("id") Long id,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> start_date,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> end_date,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        GlobalResponse response = scheduleService.getByRegionId(id, "SPECIAL", page.orElse(0), size.orElse(10),
                start_date.orElse(null), end_date.orElse(null));
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody ScheduleDTO scheduleDTO,@Nonnull HttpServletRequest request) {
        final String tokenHeader = request.getHeader("Authorization");
        String jwtToken = tokenHeader.substring(7);
        String username = jwtService.extractUsername(jwtToken);
        GlobalResponse response = scheduleService.insertSpecialSchedule(scheduleDTO, username);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> put(@RequestBody EditScheduleDTO scheduleDTO, @PathVariable("id") Long id,@Nonnull HttpServletRequest request) {
        final String tokenHeader = request.getHeader("Authorization");
        String jwtToken = tokenHeader.substring(7);
        String username = jwtService.extractUsername(jwtToken);
        GlobalResponse response = scheduleService.editSchedule(scheduleDTO, id, ECategory.SPECIAL,username);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id,@Nonnull HttpServletRequest request) {
        final String tokenHeader = request.getHeader("Authorization");
        String jwtToken = tokenHeader.substring(7);
        String username = jwtService.extractUsername(jwtToken);
        GlobalResponse response = scheduleService.delete(id,username);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
    }

}
