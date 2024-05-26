package com.cms.audit.api.Report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Clarifications.models.Clarification;

@Repository
public interface ReportRepository extends JpaRepository<Clarification, Long>, JpaSpecificationExecutor<Clarification> {
}
