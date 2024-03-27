package com.cms.audit.api.Management.ReportType.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Management.ReportType.models.ReportType;
import com.cms.audit.api.Management.ReportType.repository.ReportTypeRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class InsertDefaultReportType {

    @Autowired
    private ReportTypeRepository repository;

    @PostConstruct
    public void InsertDefaultReportType() {
        List<ReportType> response = repository.findAll();
        if (!response.isEmpty()) {
            return;
        }
        ReportType report1 = new ReportType(
                null,
                "Comment Clarification",
                "CK",
                0,
                new Date(),
                new Date());
        repository.save(report1);
        ReportType report2 = new ReportType(
                null,
                "Berita Acara",
                "BA",
                0,
                new Date(),
                new Date());
        repository.save(report2);
        ReportType report3 = new ReportType(
                null,
                "Tindak Lanjut",
                "TL",
                0,
                new Date(),
                new Date());
        repository.save(report3);
    }

}
