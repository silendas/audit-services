package com.cms.audit.api.AuditWorkingPaper.repository;

import java.util.List;
import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.AuditWorkingPaper.models.AuditWorkingPaper;

@Repository
public interface AuditWorkingPaperRepository extends JpaRepository<AuditWorkingPaper, Long> {

    @Query(value = "SELECT u.* FROM audit_working_paper u INNER JOIN branch_office bo ON u.branch_id=bo.id INNER JOIN area_office ao ON bo.area_id=ao.id INNER JOIN region_office ro ON ao.region_id=ro.id WHERE ro.id = :regionId ORDER BY u.id DESC", nativeQuery = true)
    List<AuditWorkingPaper> findByRegionId(@Param("regionId") Long id);

    // @Query(value = "SELECT u.* FROM audit_working_paper u WHERE u.schedule_id =
    // :id ", nativeQuery = true)
    // List<AuditWorkingPaper> findByScheduleId(@Param("id")Long id);

    @Query(value = "SELECT * FROM audit_working_paper u WHERE u.file_name = :filename ORDER BY u.id DESC LIMIT 1;", nativeQuery = true)
    Optional<AuditWorkingPaper> findByFilenameString(@Param("filename") String filename);

    @Query(value = "SELECT * FROM audit_working_paper u WHERE u.schedule_id = :id ;", nativeQuery = true)
    List<AuditWorkingPaper> findListByScheduleId(@Param("id") Long id);

    @Query(value = "SELECT * FROM audit_working_paper u WHERE u.schedule_id = :id ORDER BY u.id DESC LIMIT 1;", nativeQuery = true)
    Optional<AuditWorkingPaper> findByScheduleId(@Param("id") Long id);

}
