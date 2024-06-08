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
import com.cms.audit.api.Dashboard.service.DashboardServices;

@RestController
@RequestMapping(value =  BasePath.BASE_PATH_DASHBOARD)
public class DashboardController {

    @Autowired
    private DashboardServices dashboardServices;

    @GetMapping()
    public ResponseEntity<Object> getDashboard(
        @RequestParam(required = false) Optional<Long> date1,
        @RequestParam(required = false) Optional<Long> date2,
        @RequestParam(required = false) Optional<Long> year   
    ) {
        return dashboardServices.getDashboard(date1.orElse(convertDateToRoman.getLongMonthNumber(new Date())), date2.orElse(null));
    }
    
}
