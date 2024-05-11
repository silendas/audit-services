package com.cms.audit.api.AuditWorkingPaper.repository;

import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.AuditWorkingPaper.models.AuditWorkingPaper;

@Repository
public interface PagAuditWorkingPaper extends PagingAndSortingRepository<AuditWorkingPaper, Long>, JpaSpecificationExecutor<AuditWorkingPaper> {
}
