package com.cms.audit.api.InspectionSchedule.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.InspectionSchedule.models.Schedule;

@Repository
public interface PagSchedule extends PagingAndSortingRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {

}
