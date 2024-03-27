package com.cms.audit.api.InspectionSchedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.InspectionSchedule.models.ScheduleTrx;

@Repository
public interface ScheduleTrxRepo extends JpaRepository<ScheduleTrx,Long>{
    
}
