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
import com.cms.audit.api.Dashboard.service.DashboardFollowUpService;
import com.cms.audit.api.Dashboard.service.DashboardFoundService;
import com.cms.audit.api.Dashboard.service.DashboardNominalService;

@RestController
@RequestMapping(value =  BasePath.BASE_API)
public class DashboardController {

    @Autowired
    private DashboardFollowUpService dashboardServices;

    @Autowired
    private DashboardFoundService foundService;

    @Autowired
    private DashboardNominalService nominalService;

    @GetMapping("/dashboard-followup")
    public ResponseEntity<Object> getDashboardFollowUp(
        @RequestParam(required = false) Optional<Long> date,
        @RequestParam(required = false) Optional<Long> year   
    ) {
        return dashboardServices.dasboardStatus(date.orElse(convertDateToRoman.getLongMonthNumber(new Date())), year.orElse(convertDateToRoman.getLongYearNumber(new Date())));
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
    
}
