package com.cms.audit.api.Dashboard.controller;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Dashboard.service.DashboardClarificationService;
import com.cms.audit.api.Dashboard.service.DashboardDivisiService;
import com.cms.audit.api.Dashboard.service.DashboardFollowUpService;
import com.cms.audit.api.Dashboard.service.DashboardFoundService;
import com.cms.audit.api.Dashboard.service.DashboardNominalService;
import com.cms.audit.api.Dashboard.service.DashboardTotalService;

@RestController
@RequestMapping(value =  BasePath.BASE_API)
public class DashboardController {

    @Autowired
    private DashboardFollowUpService dashboardServices;

    @Autowired
    private DashboardFoundService foundService;

    @Autowired
    private DashboardNominalService nominalService;

    @Autowired
    private DashboardDivisiService divisiService;

    @Autowired
    private DashboardTotalService totalService;

    @Autowired
    private DashboardClarificationService clarificationService;

    @GetMapping("/dashboard-followup")
    public ResponseEntity<Object> getDashboardFollowUp(
        @RequestParam(required = false) Optional<Long> month,
        @RequestParam(required = false) Optional<Long> year   
    ) {
        return dashboardServices.dasboardStatus(month.orElse(convertDateToRoman.getLongMonthNumber(new Date())), year.orElse(convertDateToRoman.getLongYearNumber(new Date())));
    }

    @GetMapping("/dashboard-found")
    public ResponseEntity<Object> getDashboardFound(
        @RequestParam(required = false) Optional<Long> year   
    ) {
        return foundService.dasboardFound(year.orElse(convertDateToRoman.getLongYearNumber(new Date())));
    }

    @GetMapping("/dashboard-nominal")
    public ResponseEntity<Object> getDashboardNominal(
        @RequestParam(required = false) Optional<Long> year   
    ) {
        return nominalService.dasboardNominal(year.orElse(convertDateToRoman.getLongYearNumber(new Date())));
    }

    @GetMapping("/dashboard-clarification")
    public ResponseEntity<byte[]> getDashboardSOP(
        @RequestParam(required = false) Optional<Long> month,
        @RequestParam(required = false) Optional<Long> year   
    ) {
        return clarificationService.dashboardClarification(year.orElse(convertDateToRoman.getLongYearNumber(new Date())), month.orElse(convertDateToRoman.getLongMonthNumber(new Date())));
    }

    @GetMapping("/dashboard-divisi")
    public ResponseEntity<Object> getDashboardDivisi(
        @RequestParam(required = false) Optional<Long> month,
        @RequestParam(required = false) Optional<Long> year   
    ) {
        return divisiService.dashboardDivision(year.orElse(convertDateToRoman.getLongYearNumber(new Date())), month.orElse(convertDateToRoman.getLongMonthNumber(new Date())));
    }

    @GetMapping("/dashboard-total")
    public ResponseEntity<Object> getDashboardTotal(
        @RequestParam(required = false) Optional<Long> month,
        @RequestParam(required = false) Optional<Long> year   
    ) {
        return totalService.dashboardTotal(year.orElse(convertDateToRoman.getLongYearNumber(new Date())), month.orElse(convertDateToRoman.getLongMonthNumber(new Date())));
    }
    
}
