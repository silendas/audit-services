package com.cms.audit.api.Dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Dashboard.repository.ClarificationDashboardRepo;

@Service
public class DashboardSOPService {
    
    @Autowired
    private ClarificationDashboardRepo repo;

    public ResponseEntity<Object> dashboardSOP(Long year, Long month) {
        return null;
    }

}
