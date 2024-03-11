package com.cms.audit.api.Management.Office.MainOffice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cms.audit.api.Management.Office.MainOffice.models.Main;

public interface PagMain extends PagingAndSortingRepository<Main, Long> {
    Page<Main> findByNameContaining(String name, Pageable pageable);

}
