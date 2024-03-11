package com.cms.audit.api.NewsInspection.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.cms.audit.api.NewsInspection.models.NewsInspection;

public interface PagNewsInspection extends PagingAndSortingRepository<NewsInspection, Long> {

    Page<NewsInspection> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM news_inspection u WHERE u.created_at BETWEEN :start_date AND :end_date ", nativeQuery = true)
    Page<NewsInspection> findBAPInDateRange(@Param("start_date") Date start_date,
            @Param("end_date") Date end_date, Pageable pageable);
}
