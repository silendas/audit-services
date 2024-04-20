package com.cms.audit.api.AuditWorkingPaper.repository;

import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.AuditWorkingPaper.models.AuditWorkingPaper;

@Repository
public interface PagAuditWorkingPaper extends PagingAndSortingRepository<AuditWorkingPaper, Long> {

    @Query(value = "SELECT * FROM audit_working_paper awp ORDER BY awp.id DESC;", nativeQuery = true)
    Page<AuditWorkingPaper> findAllWorkingPaper(Pageable pageable);

    @Query(value = "SELECT * FROM audit_working_paper awp WHERE awp.created_at BETWEEN :start_date AND :end_date ORDER BY awp.id DESC;", nativeQuery = true)
    Page<AuditWorkingPaper> findAllWorkingPaperByDate(@Param("start_date") Date start_date,
    @Param("end_date") Date end_date,Pageable pageable);

    @Query(value = "SELECT * FROM audit_working_paper awp WHERE awp.user_id = :userid AND awp.is_delete <> 1 ORDER BY awp.id DESC ;", nativeQuery = true)
    Page<AuditWorkingPaper> findAllWorkingPaperByUserId(@Param("userid")Long id,Pageable pageable);

    @Query(value = "SELECT * FROM audit_working_paper awp WHERE awp.user_id = :userid AND awp.created_at BETWEEN :start_date AND :end_date AND awp.is_delete <> 1 ORDER BY awp.id DESC;", nativeQuery = true)
    Page<AuditWorkingPaper> findAllWorkingPaperByUserIdAndDate(@Param("userid")Long id,@Param("start_date") Date start_date,
    @Param("end_date") Date end_date,Pageable pageable);

    @Query(value = "SELECT * FROM audit_working_paper awp WHERE awp.created_at BETWEEN :start_date AND :end_date AND awp.is_delete <> 1 ORDER BY awp.id DESC;", nativeQuery = true)
    Page<AuditWorkingPaper> findWorkingPaperInDateRange(@Param("start_date") Date start_date,
            @Param("end_date") Date end_date, Pageable pageable);

    @Query(value = "SELECT awp.* FROM audit_working_paper awp INNER JOIN users us ON awp.user_id=us.id WHERE us.fullname LIKE :name AND awp.is_delete <> 1 ORDER BY awp.id DESC;", nativeQuery = true)
    Page<AuditWorkingPaper> findWorkingPaperByName(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT * FROM audit_working_paper awp WHERE awp.branch_id= :id AND awp.is_delete <> 1 ORDER BY awp.id DESC;", nativeQuery = true)
    Page<AuditWorkingPaper> findWorkingPaperByBranch(@Param("id") Long id, Pageable pageable);

    @Query(value = "SELECT awp.* FROM audit_working_paper awp INNER JOIN users us ON awp.user_id=us.id WHERE us.fullname LIKE :name AND awp.branch_id = :id AND awp.is_delete <> 1 ORDER BY awp.id DESC;", nativeQuery = true)
    Page<AuditWorkingPaper> findWorkingPaperByNameAndBranch(@Param("name") String name,@Param("id") Long id, Pageable pageable);

    @Query(value = "SELECT awp.* FROM audit_working_paper awp INNER JOIN users us ON awp.user_id=us.id WHERE us.fullname LIKE :name AND awp.branch_id = :id AND awp.created_at BETWEEN :start_date AND :end_date AND awp.is_delete <> 1 ORDER BY awp.id DESC;", nativeQuery = true)
    Page<AuditWorkingPaper> findWorkingPaperByAllFilter(@Param("name") String name,@Param("id") Long id,@Param("start_date") Date start_date,
    @Param("end_date") Date end_date, Pageable pageable);

    @Query(value = "SELECT awp.* FROM audit_working_paper awp INNER JOIN users us ON awp.user_id=us.id WHERE us.fullname LIKE :name AND awp.created_at BETWEEN :start_date AND :end_date AND awp.is_delete <> 1 ORDER BY awp.id DESC;", nativeQuery = true)
    Page<AuditWorkingPaper> findWorkingPaperByNameAndDate(@Param("name") String name,@Param("start_date") Date start_date,
    @Param("end_date") Date end_date, Pageable pageable);

    @Query(value = "SELECT awp.* FROM audit_working_paper awp WHERE awp.branch_id = :id AND awp.created_at BETWEEN :start_date AND :end_date AND awp.is_delete <> 1 ORDER BY awp.id DESC;", nativeQuery = true)
    Page<AuditWorkingPaper> findWorkingPaperByBranchAndDate(@Param("id") Long id,@Param("start_date") Date start_date,
    @Param("end_date") Date end_date, Pageable pageable);
}
