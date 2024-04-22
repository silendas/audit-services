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
import com.cms.audit.api.Common.exception.ResourceNotFoundException;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Flag.model.Flag;
import com.cms.audit.api.Flag.repository.FlagRepo;
import com.cms.audit.api.Management.User.models.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RevisionService {

    @Autowired
    private RevisionRepository repository;

    @Autowired
    private FlagRepo flagRepo;

    @Autowired
    private AuditDailyReportDetailRepository auditDailyReportDetailRepository;

    public GlobalResponse getAll(Long detaild) {
        try {
            if (detaild == null) {
                List<Revision> response = repository.findAll();
                if (response.isEmpty()) {
                    return GlobalResponse.builder().message("Data not found").status(HttpStatus.OK).build();
                }
                return GlobalResponse.builder().data(response).message("Success").status(HttpStatus.OK).build();
            } else {
                List<Revision> response = repository.findByDetailIdAll(detaild);
                if (response.isEmpty()) {
                    return GlobalResponse.builder().message("Data not found").status(HttpStatus.OK).build();
                }
                for(int i =0; i<response.size();i++){
                    if (response.get(i).getIs_research() == 1) {
                        Flag isFLag = flagRepo
                                .findOneByAuditDailyReportDetailId(
                                        detaild)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                        "Flag not found"));
                        if (isFLag.getClarification().getFilename() != null) {
                            response.get(i).setIs_research(0);
                        } else {
                            response.get(i).setIs_research(1);
                        }
                    } else {
                        response.get(i).setIs_research(0);
                    }
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
                if (response.get().getIs_research() == 1) {
                    Flag isFLag = flagRepo
                            .findOneByAuditDailyReportDetailId(
                                    response.get().getAuditDailyReportDetail().getId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Flag not found"));
                    if (isFLag.getClarification().getFilename() != null) {
                        response.get().setIs_research(0);
                    } else {
                        response.get().setIs_research(1);
                    }
                } else {
                    response.get().setIs_research(0);
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
            return GlobalResponse.builder().message("Detail Not found").status(HttpStatus.BAD_REQUEST).build();
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
