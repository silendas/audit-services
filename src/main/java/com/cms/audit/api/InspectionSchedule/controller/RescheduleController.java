package com.cms.audit.api.InspectionSchedule.controller;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.Config.Jwt.JwtService;
import com.cms.audit.api.InspectionSchedule.dto.RequestReschedule;
import com.cms.audit.api.InspectionSchedule.service.ScheduleService;

import io.micrometer.common.lang.NonNull;
import jakarta.annotation.Nonnull;
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
            @RequestParam("branch_id") Optional<Long> branch_id,
            @RequestParam("name") Optional<String> name,
            @RequestParam("status") Optional<String> status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> start_date,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> end_date,
            @NonNull HttpServletRequest request,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        Long branchId;
        if (branch_id.isPresent()) {
            if (branch_id.get().toString() != "") {
                branchId = branch_id.get();
            } else {
                branchId = null;
            }
        } else {
            branchId = null;
        }
        String fullname;
        if (name.isPresent()) {
            if (name.get().toString() != "") {
                fullname = name.get();
            } else {
                fullname = null;
            }
        } else {
            fullname = null;
        }
        Date startDate;
        if (start_date.isPresent()) {
            if (start_date.get().toString() != "") {
                startDate = start_date.get();
            } else {
                startDate = null;
            }
        } else {
            startDate = null;
        }
        if (startDate != null) {
            startDate = convertDateToRoman.setTimeToZero(startDate);
        }
        Date endDate;
        if (end_date.isPresent()) {
            if (end_date.get().toString() != "") {
                endDate = end_date.get();
            } else {
                endDate = null;
            }
        } else {
            endDate = null;
        }
        if (endDate != null) {
            endDate = convertDateToRoman.setTimeToLastSecond(endDate);
        }
        GlobalResponse response = scheduleService.getByStatus(fullname, branchId, startDate, endDate, page.orElse(0),
                size.orElse(10));
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
        GlobalResponse response = scheduleService.getById(id);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @PostMapping("/request")
    public ResponseEntity<Object> requestReschedule(
            @Nonnull @RequestBody RequestReschedule dto,
            @NonNull HttpServletRequest request) {
        final String tokenHeader = request.getHeader("Authorization");
        String jwtToken = tokenHeader.substring(7);
        String username = jwtService.extractUsername(jwtToken);
        GlobalResponse response = scheduleService.requestSchedule(dto, username);
        if (response.getStatus().value() == 400) {
            return ResponseEntittyHandler.errorResponse(response.getErrorMessage(), response.getMessage(),
                    response.getStatus());
        } else {
            return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
        }
    }

    @PatchMapping("/approve/{id}")
    public ResponseEntity<Object> rescheduleApprove(@PathVariable("id") Long id) throws Exception {
        GlobalResponse response = scheduleService.approve(id);
        if(response.getStatus().equals(HttpStatus.BAD_REQUEST)){
            return ResponseEntittyHandler.errorResponse(response.getErrorMessage(), response.getMessage(), response.getStatus());
        }else{
            return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
        }
    }

    @PatchMapping("/reject/{id}")
    public ResponseEntity<Object> rescheduleRejected(@PathVariable("id") Long id) throws Exception {
        GlobalResponse response = scheduleService.rejected(id);
        if(response.getStatus().equals(HttpStatus.BAD_REQUEST)){
            return ResponseEntittyHandler.errorResponse(response.getErrorMessage(), response.getMessage(), response.getStatus());
        }else{
            return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
        }
    }

}
