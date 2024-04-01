package com.cms.audit.api.AuditDailyReport.service;

import java.util.*;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cms.audit.api.AuditDailyReport.dto.AuditDailyReportDetailDTO;
import com.cms.audit.api.AuditDailyReport.dto.EditAuditDailyReportDetailDTO;
import com.cms.audit.api.AuditDailyReport.dto.response.DetailResponse;
import com.cms.audit.api.AuditDailyReport.models.AuditDailyReport;
import com.cms.audit.api.AuditDailyReport.models.AuditDailyReportDetail;
import com.cms.audit.api.AuditDailyReport.models.Revision;
import com.cms.audit.api.AuditDailyReport.repository.AuditDailyReportDetailRepository;
import com.cms.audit.api.AuditDailyReport.repository.PagAuditDailyReportDetail;
import com.cms.audit.api.AuditDailyReport.repository.RevisionRepository;
import com.cms.audit.api.Common.exception.ResourceNotFoundException;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Flag.model.Flag;
import com.cms.audit.api.Management.Case.models.Case;
import com.cms.audit.api.Management.CaseCategory.models.CaseCategory;
import com.cms.audit.api.Management.User.models.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuditDailyReportDetailService {
    @Autowired
    private AuditDailyReportDetailRepository repository;

    @Autowired
    private PagAuditDailyReportDetail pag;

    @Autowired
    private RevisionRepository revisionRepo;

    public GlobalResponse get(int page, int size, Long lhaId) {
        try {
            Page<AuditDailyReportDetail> response;
            if (lhaId == null) {
                response = pag.findAllLHADetail(PageRequest.of(page, size));
            } else {
                response = pag.findByLHAId(lhaId, PageRequest.of(page, size));
            }
            List<Object> details = new ArrayList<>();
            for (int i = 0; i < response.getContent().size(); i++) {
                Optional<Revision> getRevision = revisionRepo.findByDetailId(response.getContent().get(i).getId());
                if (!getRevision.isPresent()) {
                    DetailResponse builder = new DetailResponse();
                    builder.setId(response.getContent().get(i).getId());
                    builder.setCases(response.getContent().get(i).getCases().getName());
                    builder.setCaseCategory(response.getContent().get(i).getCaseCategory().getName());
                    builder.setDescription(response.getContent().get(i).getDescription());
                    builder.setPermanent_recommendations(
                            response.getContent().get(i).getPermanent_recommendations());
                    builder.setTemporary_recommendations(
                            response.getContent().get(i).getTemporary_recommendations());
                    builder.setSuggestion(response.getContent().get(i).getSuggestion());
                    builder.setIs_research(response.getContent().get(i).getIs_research());
                    details.add(builder);
                } else {
                    DetailResponse builder = new DetailResponse();
                    builder.setId(getRevision.get().getId());
                    builder.setCases(getRevision.get().getCases().getName());
                    builder.setCaseCategory(getRevision.get().getCaseCategory().getName());
                    builder.setDescription(getRevision.get().getDescription());
                    builder.setPermanent_recommendations(
                            getRevision.get().getPermanent_recommendations());
                    builder.setTemporary_recommendations(
                            getRevision.get().getTemporary_recommendations());
                    builder.setSuggestion(getRevision.get().getSuggestion());
                    builder.setIs_research(getRevision.get().getIs_research());
                    details.add(builder);
                }
            }
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("No Content")
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }
            Map<String, Object> parent = new LinkedHashMap<>();
            parent.put("lha_details", details);
            parent.put("pageable", response.getPageable());

            return GlobalResponse
                    .builder()
                    .message("Success")
                    .data(parent)
                    .status(HttpStatus.OK)
                    .build();
        } catch (ResponseStatusException e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        } catch (DataException e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();
        } catch (Exception e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public GlobalResponse getById(Long id) {
        try {
            AuditDailyReportDetail response = repository.findOneByLHADetailId(id)
                    .orElseThrow(() -> new IllegalStateException(
                            "lha with id " + id + " does now exist"));
            Map<String,Object> builder = new LinkedHashMap<>();
            builder.put("id",response.getId());

            Map<String,Object> cases = new LinkedHashMap<>();
            cases.put("id", response.getCases().getId());
            cases.put("name", response.getCases().getName());
            cases.put("code", response.getCases().getCode());
            builder.put("case",cases);

            Map<String,Object> caseCategory = new LinkedHashMap<>();
            caseCategory.put("id", response.getCaseCategory().getId());
            caseCategory.put("name", response.getCaseCategory().getName());
            builder.put("case_category",caseCategory);

            builder.put("description",response.getDescription());
            builder.put(
                    "permanent_recommendation",response.getPermanent_recommendations());
            builder.put(
                    "temporary_recommendation",response.getTemporary_recommendations());
            builder.put("suggestion",response.getSuggestion());
            builder.put("is_research",response.getIs_research());
            return GlobalResponse
                    .builder()
                    .message("Success")
                    .data(builder)
                    .status(HttpStatus.OK)
                    .build();
        } catch (ResponseStatusException e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        } catch (DataException e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();
        } catch (Exception e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public GlobalResponse getByLHAId(Long id, int page, int size) {
        try {
            Page<AuditDailyReportDetail> response = pag.findByLHAId(id, PageRequest.of(page, size));
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("No Content")
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }
            return GlobalResponse
                    .builder()
                    .message("Success")
                    .data(response)
                    .status(HttpStatus.OK)
                    .build();
        } catch (ResponseStatusException e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        } catch (DataException e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();
        } catch (Exception e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public GlobalResponse getOneByLHAId(Long id) {
        try {
            Optional<AuditDailyReportDetail> response = repository.findOneByLHAId(id);
            if (!response.isPresent()) {
                return GlobalResponse
                        .builder()
                        .message("No Content")
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }
            return GlobalResponse
                    .builder()
                    .message("Success")
                    .data(response)
                    .status(HttpStatus.OK)
                    .build();
        } catch (ResponseStatusException e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        } catch (DataException e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();
        } catch (Exception e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public GlobalResponse save(AuditDailyReportDetailDTO dto) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            AuditDailyReport setId = AuditDailyReport.builder().id(dto.getAudit_daily_report_id()).build();
            Case setCaseId = Case.builder().id(dto.getCase_id()).build();
            CaseCategory setCCId = CaseCategory.builder().id(dto.getCase_category_id()).build();

            AuditDailyReportDetail auditDailyReport = new AuditDailyReportDetail(
                    null,
                    setId,
                    setCaseId,
                    setCCId,
                    dto.getDescription(),
                    dto.getSuggestion(),
                    dto.getTemporary_recommendations(),
                    dto.getPermanent_recommendations(),
                    dto.getIs_research(),
                    0,
                    user.getId(),
                    user.getId(),
                    new Date(),
                    new Date());

            AuditDailyReportDetail response = repository.save(auditDailyReport);
            return GlobalResponse
                    .builder()
                    .message("Success")
                    .status(HttpStatus.OK)
                    .build();
        } catch (ResponseStatusException e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        } catch (DataException e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();
        } catch (Exception e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public GlobalResponse edit(EditAuditDailyReportDetailDTO dto, Long id) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Optional<AuditDailyReportDetail> getBefore = repository.findById(id);
            if (!getBefore.isPresent()) {
                return GlobalResponse
                        .builder()
                        .message("Not Found")
                        .status(HttpStatus.NOT_FOUND)
                        .build();
            }

            Case setCaseId = Case.builder().id(dto.getCase_id()).build();
            CaseCategory setCCId = CaseCategory.builder().id(dto.getCase_category_id()).build();

            AuditDailyReportDetail auditDailyReport = new AuditDailyReportDetail(
                    id,
                    getBefore.get().getAuditDailyReport(),
                    setCaseId,
                    setCCId,
                    dto.getDescription(),
                    dto.getSuggestion(),
                    dto.getTemporary_recommendations(),
                    dto.getPermanent_recommendations(),
                    dto.getIs_research(),
                    0,
                    getBefore.get().getCreated_by(),
                    user.getId(),
                    getBefore.get().getCreated_at(),
                    new Date());

            AuditDailyReportDetail response = repository.save(auditDailyReport);
            return GlobalResponse
                    .builder()
                    .message("Success")
                    .status(HttpStatus.OK)
                    .build();
        } catch (ResponseStatusException e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        } catch (DataException e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();
        } catch (Exception e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public GlobalResponse delete(Long id) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Optional<AuditDailyReportDetail> getBefore = repository.findById(id);
            if (!getBefore.isPresent()) {
                return GlobalResponse
                        .builder()
                        .message("Not Found")
                        .status(HttpStatus.NOT_FOUND)
                        .build();
            }

            AuditDailyReportDetail auditDailyReport = getBefore.orElse(null);
            auditDailyReport.setIs_delete(1);
            auditDailyReport.setUpdated_by(user.getId());
            auditDailyReport.setUpdate_at(new Date());

            AuditDailyReportDetail response = repository.save(auditDailyReport);

            return GlobalResponse
                    .builder()
                    .message("Success")
                    .status(HttpStatus.OK)
                    .build();
        } catch (ResponseStatusException e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        } catch (DataException e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();
        } catch (Exception e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

}
