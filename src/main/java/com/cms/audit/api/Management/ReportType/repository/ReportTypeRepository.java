package com.cms.audit.api.Management.ReportType.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Management.ReportType.dto.response.ReportTypeInterface;
import com.cms.audit.api.Management.ReportType.models.ReportType;

@Repository
public interface ReportTypeRepository extends JpaRepository<ReportType,Long>{

    @Query(value = "SELECT * FROM report_type u WHERE u.is_delete <> 1", nativeQuery = true)
    public List<ReportType> findAllReportType();

    @Query(value = "SELECT u.id,u.name,u.code FROM report_type u WHERE u.is_delete <> 1", nativeQuery = true)
    public List<ReportTypeInterface> findSpecificReportType();

    @Query(value = "SELECT * FROM report_type u WHERE u.id = :rtId AND u.is_delete <> 1", nativeQuery = true)
    public Optional<ReportType> findOneReportTypeById(@Param("rtId") Long id);

}
