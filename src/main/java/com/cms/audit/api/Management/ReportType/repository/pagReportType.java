package com.cms.audit.api.Management.ReportType.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.ReportType.models.ReportType;
@Repository
public interface pagReportType extends PagingAndSortingRepository<ReportType, Long>, JpaSpecificationExecutor<ReportType>{
    
}
