package com.cms.audit.api.InspectionSchedule.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import com.cms.audit.api.Config.Jwt.JwtService;
import com.cms.audit.api.InspectionSchedule.dto.RequestReschedule;
import com.cms.audit.api.InspectionSchedule.dto.RescheduleDTO;
import com.cms.audit.api.InspectionSchedule.models.EStatus;
import com.cms.audit.api.InspectionSchedule.service.ScheduleService;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@Validated
@RequestMapping(value = BasePath.BASE_PATH_RESCHEDULE)
public class RescheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private JwtService jwtService;

    @GetMapping
    public ResponseEntity<Object> get(
            @NonNull HttpServletRequest request,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        final String tokenHeader = request.getHeader("Authorization");
        String jwtToken = tokenHeader.substring(7);
        String username = jwtService.extractUsername(jwtToken);
        GlobalResponse response = scheduleService.getByStatus(username, page.orElse(0), size.orElse(10));
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @PostMapping("/request")
    public ResponseEntity<Object> requestReschedule(
            @RequestBody RequestReschedule dto,
            @NonNull HttpServletRequest request) {
        final String tokenHeader = request.getHeader("Authorization");
        String jwtToken = tokenHeader.substring(7);
        String username = jwtService.extractUsername(jwtToken);
        GlobalResponse response = scheduleService.requestSchedule(dto, username);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
    }

    @PatchMapping("/approve/{id}")
    public ResponseEntity<Object> rescheduleApprove(@PathVariable("id") Long id) throws Exception {
        GlobalResponse response = scheduleService.approve(id);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
    }

    @PatchMapping("/reject/{id}")
    public ResponseEntity<Object> rescheduleRejected(@PathVariable("id") Long id,@NonNull HttpServletRequest request) {
        final String tokenHeader = request.getHeader("Authorization");
        String jwtToken = tokenHeader.substring(7);
        String username = jwtService.extractUsername(jwtToken);
        GlobalResponse response = scheduleService.editStatus(id, EStatus.REJECTED, username);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
    }

}
