package com.cms.audit.api.Clarifications.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Clarifications.models.Clarification;

@Repository
public interface PagClarification extends PagingAndSortingRepository<Clarification, Long> {

    @Query(value = "SELECT * FROM clarification u ORDER BY u.id DESC ;", nativeQuery = true)
    Page<Clarification> findAllLHADetail(Pageable pageable);

    @Query(value = "SELECT * FROM clarification u WHERE u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
    Page<Clarification> findClarificationInDateRange(@Param("start_date") Date start_date,
            @Param("end_date") Date end_date, Pageable pageable);
}
