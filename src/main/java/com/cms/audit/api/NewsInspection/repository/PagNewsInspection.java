package com.cms.audit.api.NewsInspection.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.audit.api.NewsInspection.models.NewsInspection;

@Repository
public interface PagNewsInspection extends PagingAndSortingRepository<NewsInspection, Long>, JpaSpecificationExecutor<NewsInspection> {
}
