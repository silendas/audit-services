package com.cms.audit.api.InspectionSchedule.controller;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.Config.Jwt.JwtService;
import com.cms.audit.api.InspectionSchedule.dto.EditScheduleDTO;
import com.cms.audit.api.InspectionSchedule.dto.ScheduleRequest;
import com.cms.audit.api.InspectionSchedule.models.ECategory;
import com.cms.audit.api.InspectionSchedule.models.EStatus;
import com.cms.audit.api.InspectionSchedule.service.ScheduleService;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@Validated
@RequestMapping(value = BasePath.BASE_PATH_MAIN_SCHEDULE)
public class MainScheduleConroller {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private JwtService jwtService;

    @GetMapping
    public ResponseEntity<Object> getAll(
            @RequestParam("branch_id") Optional<Long> branch_id,
            @RequestParam("name") Optional<String> name,
            @RequestParam("status") Optional<EStatus> status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> start_date,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> end_date,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        Long branchId = branch_id.orElse(null);
        String fullname = name.orElse(null);
        Date startDate = start_date.orElse(null);
        Date endDate = end_date.orElse(null);
        if (startDate != null) {
            startDate = convertDateToRoman.setTimeToZero(startDate);
        }
        if (endDate != null) {
            endDate = convertDateToRoman.setTimeToLastSecond(endDate);
        }
        GlobalResponse response = scheduleService.getSchedule(branchId, fullname, page.orElse(0), size.orElse(10),
                startDate, endDate, ECategory.REGULAR, status.orElse(null));
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
        GlobalResponse response = scheduleService.getById(id);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @PostMapping
    public ResponseEntity<Object> add(@Nonnull @RequestBody ScheduleRequest scheduleDTO,
            @Nonnull HttpServletRequest request) {
        final String tokenHeader = request.getHeader("Authorization");
        String jwtToken = tokenHeader.substring(7);
        String username = jwtService.extractUsername(jwtToken);
        GlobalResponse response = scheduleService.insertRegularSchedule(scheduleDTO, username);
        if (response.getStatus().value() == 400) {
            return ResponseEntittyHandler.errorResponse(response.getErrorMessage(), response.getMessage(),
                    response.getStatus());
        } else {
            return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> put(@RequestBody EditScheduleDTO scheduleDTO, @PathVariable("id") Long id,
            @Nonnull HttpServletRequest request) {
        final String tokenHeader = request.getHeader("Authorization");
        String jwtToken = tokenHeader.substring(7);
        String username = jwtService.extractUsername(jwtToken);
        GlobalResponse response = scheduleService.editSchedule(scheduleDTO, id, ECategory.REGULAR, username);
        if (response.getStatus().value() == 400) {
            return ResponseEntittyHandler.errorResponse(response.getErrorMessage(), response.getMessage(),
                    response.getStatus());
        } else {
            return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        GlobalResponse response = scheduleService.delete(id);
        return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(), null);
    }

}
