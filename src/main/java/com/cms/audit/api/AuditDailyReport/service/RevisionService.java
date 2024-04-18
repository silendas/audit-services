package com.cms.audit.api.AuditDailyReport.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cms.audit.api.AuditDailyReport.dto.RevisionDTO;
import com.cms.audit.api.AuditDailyReport.models.AuditDailyReportDetail;
import com.cms.audit.api.AuditDailyReport.models.Revision;
import com.cms.audit.api.AuditDailyReport.repository.AuditDailyReportDetailRepository;
import com.cms.audit.api.AuditDailyReport.repository.RevisionRepository;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Management.Case.repository.CaseRepository;
import com.cms.audit.api.Management.CaseCategory.repository.CaseCategoryRepository;
import com.cms.audit.api.Management.User.models.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RevisionService {

    @Autowired
    private RevisionRepository repository;

    @Autowired
    private AuditDailyReportDetailRepository auditDailyReportDetailRepository;

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private CaseCategoryRepository caseCategoryRepository;

    public GlobalResponse getAll(Long detaild) {
        try {

            if (detaild == null) {
                List<Revision> response = repository.findAll();
                if (response.isEmpty()) {
                    return GlobalResponse.builder().message("Data not found").status(HttpStatus.OK).build();
                }
                return GlobalResponse.builder().data(response).message("Success").status(HttpStatus.OK).build();
            } else {
                Optional<Revision> response = repository.findByDetailId(detaild);
                if (response.isEmpty()) {
                    return GlobalResponse.builder().message("Data not found").status(HttpStatus.OK).build();
                }
                return GlobalResponse.builder().data(response).message("Success").status(HttpStatus.OK).build();
            }
        } catch (Exception e) {
            return GlobalResponse.builder().error(e).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public GlobalResponse getOne(Long id) {
        try {
            Optional<Revision> response = repository.findById(id);
            if(!response.isPresent()){
                return GlobalResponse.builder().message("Data tidak ditemukan").status(HttpStatus.BAD_REQUEST).build();
            }
            return GlobalResponse.builder().message("Success").data(response).status(HttpStatus.OK).build();
        } catch (Exception e) {
            return GlobalResponse.builder().error(e).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public GlobalResponse insertNewRevision(RevisionDTO dto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<AuditDailyReportDetail> detail = auditDailyReportDetailRepository
                .findById(dto.getAudit_daily_report_detail_id());
        if (!detail.isPresent()) {
            return GlobalResponse.builder().message("DEtail Not found").status(HttpStatus.BAD_REQUEST).build();
        }

        Revision revision = new Revision();
        revision.setAuditDailyReportDetail(detail.get());
        revision.setCases(detail.get().getCases());
        revision.setCaseCategory(detail.get().getCaseCategory());
        revision.setDescription(dto.getDescription());
        revision.setIs_delete(0);
        revision.setPermanent_recommendations(dto.getPermanent_recommendations());
        revision.setTemporary_recommendations(dto.getTemporary_recommendations());
        revision.setSuggestion(dto.getSuggestion());
        revision.setCreated_by(user.getId());
        revision.setIs_research(detail.get().getIs_research());
        revision.setCreated_at(new Date());

        Optional<Revision> getRevision = repository.findByDetailId(detail.get().getId());
        if (getRevision.isPresent()) {
            revision.setRevisionNumber(getRevision.get().getRevisionNumber() + 1);
        } else {
            revision.setRevisionNumber(1L);
        }

        try {
            repository.save(revision);
            return GlobalResponse.builder().message("Success").status(HttpStatus.OK).build();
        } catch (Exception e) {
            return GlobalResponse.builder().error(e).status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
