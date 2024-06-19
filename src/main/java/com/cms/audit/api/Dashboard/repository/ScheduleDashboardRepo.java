package com.cms.audit.api.Dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.InspectionSchedule.models.Schedule;

@Repository
public interface ScheduleDashboardRepo extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {
    
}
