package com.cms.audit.api.Clarifications.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.Clarifications.models.Clarification;

@Repository
public interface PagClarification extends PagingAndSortingRepository<Clarification, Long>, JpaSpecificationExecutor<Clarification> {

    // @Query("SELECT c FROM Clarification c " +
    //         "JOIN c.branchOffice bo " +
    //         "JOIN bo.areaOffice ao " +
    //         "JOIN ao.regionOffice ro " +
    //         "WHERE ro.id IN :regionIds")
    // Page<Clarification> findByRegionIds(@Param("regionIds") List<Long> regionIds, Pageable pageable);

}
