package com.cms.audit.api.AuditDailyReport.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.cms.audit.api.AuditDailyReport.repository.AuditDailyReportRepository;
import com.cms.audit.api.AuditDailyReport.repository.PagAuditDailyReportDetail;
import com.cms.audit.api.AuditDailyReport.repository.RevisionRepository;
import com.cms.audit.api.Clarifications.dto.response.NumberClarificationInterface;
import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Clarifications.models.EStatusClarification;
import com.cms.audit.api.Clarifications.repository.ClarificationRepository;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.exception.ResourceNotFoundException;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Flag.model.Flag;
import com.cms.audit.api.Flag.repository.FlagRepo;
import com.cms.audit.api.Management.Case.models.Case;
import com.cms.audit.api.Management.Case.repository.CaseRepository;
import com.cms.audit.api.Management.CaseCategory.models.CaseCategory;
import com.cms.audit.api.Management.CaseCategory.repository.CaseCategoryRepository;
import com.cms.audit.api.Management.ReportType.models.ReportType;
import com.cms.audit.api.Management.ReportType.repository.ReportTypeRepository;
import com.cms.audit.api.Management.User.models.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuditDailyReportDetailService {
    @Autowired
    private AuditDailyReportDetailRepository repository;

    @Autowired
    private ReportTypeRepository reportTypeRepository;

    @Autowired
    private AuditDailyReportRepository lhaReportsitory;

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private ClarificationRepository clarificationRepository;

    @Autowired
    private FlagRepo flagRepo;

    @Autowired
    private CaseCategoryRepository ccRepository;

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
                    if (response.getContent().get(i).getIs_research() == 1) {
                        Flag isFLag = flagRepo
                                .findOneByAuditDailyReportDetailId(
                                        response.getContent().get(i).getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                        "Flag not found"));
                        if (isFLag.getClarification().getFilename() != null) {
                            builder.setIs_research(0);
                        } else {
                            builder.setIs_research(1);
                        }
                    } else {
                        builder.setIs_research(0);
                    }
                    // builder.setIs_research(response.getContent().get(i).getIs_research());
                    details.add(builder);
                } else {
                    DetailResponse builder = new DetailResponse();
                    builder.setId(response.getContent().get(i).getId());
                    builder.setCases(getRevision.get().getCases().getName());
                    builder.setCaseCategory(getRevision.get().getCaseCategory().getName());
                    builder.setDescription(getRevision.get().getDescription());
                    builder.setPermanent_recommendations(
                            getRevision.get().getPermanent_recommendations());
                    builder.setTemporary_recommendations(
                            getRevision.get().getTemporary_recommendations());
                    builder.setSuggestion(getRevision.get().getSuggestion());
                    if (response.getContent().get(i).getIs_research() == 1) {
                        Flag isFLag = flagRepo
                                .findOneByAuditDailyReportDetailId(
                                        response.getContent().get(i).getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                        "Flag not found"));
                        if (isFLag.getClarification().getFilename() != null) {
                            builder.setIs_research(0);
                        } else {
                            builder.setIs_research(1);
                        }
                    } else {
                        builder.setIs_research(0);
                    }
                    // builder.setIs_research(getRevision.get().getIs_research());
                    details.add(builder);
                }
            }
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data not found")
                        .status(HttpStatus.OK)
                        .build();
            }
            Map<String, Object> parent = new LinkedHashMap<>();
            parent.put("pageable", response.getPageable());
            parent.put("totalPage", response.getTotalPages());
            parent.put("totalElement", response.getTotalElements());
            parent.put("size", response.getSize());
            parent.put("number", response.getNumber());
            parent.put("last", response.isLast());
            parent.put("first", response.isFirst());
            parent.put("numberOfElement", response.getNumberOfElements());
            parent.put("empty", response.isEmpty());
            parent.put("sort", response.getSort());
            parent.put("content", details);
            // parent.put("lha_details", details);

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
            Optional<Revision> checkRevision = revisionRepo.findByDetailId(id);
            Map<String, Object> builder = new LinkedHashMap<>();
            if (checkRevision.isPresent()) {
                builder.put("id", id);
                builder.put("revision_id", checkRevision.get().getId());

                Map<String, Object> cases = new LinkedHashMap<>();
                cases.put("id", checkRevision.get().getCases().getId());
                cases.put("name", checkRevision.get().getCases().getName());
                cases.put("code", checkRevision.get().getCases().getCode());
                builder.put("case", cases);

                Map<String, Object> caseCategory = new LinkedHashMap<>();
                caseCategory.put("id", checkRevision.get().getCaseCategory().getId());
                caseCategory.put("name", checkRevision.get().getCaseCategory().getName());
                builder.put("case_category", caseCategory);

                builder.put("description", checkRevision.get().getDescription());
                builder.put(
                        "permanent_recommendation", checkRevision.get().getPermanent_recommendations());
                builder.put(
                        "temporary_recommendation", checkRevision.get().getTemporary_recommendations());
                builder.put("suggestion", checkRevision.get().getSuggestion());
                // builder.put("is_research", checkRevision.get().getIs_research());
                if (checkRevision.get().getIs_research() == 1) {
                    Flag isFLag = flagRepo
                            .findOneByAuditDailyReportDetailId(
                                    id)
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Flag not found"));
                    if (isFLag.getClarification().getFilename() != null) {
                        builder.put("is_research", 0);
                    } else {
                        builder.put("is_research", 1);
                    }
                } else {
                    builder.put("is_research", 0);
                }
            } else {
                Optional<AuditDailyReportDetail> response = repository.findOneByLHADetailId(id);
                if (!response.isPresent()) {
                    return GlobalResponse.builder().message("LHA with id :" + id + " is undefined").build();
                }
                builder.put("id", response.get().getId());

                Map<String, Object> cases = new LinkedHashMap<>();
                cases.put("id", response.get().getCases().getId());
                cases.put("name", response.get().getCases().getName());
                cases.put("code", response.get().getCases().getCode());
                builder.put("case", cases);

                Map<String, Object> caseCategory = new LinkedHashMap<>();
                caseCategory.put("id", response.get().getCaseCategory().getId());
                caseCategory.put("name", response.get().getCaseCategory().getName());
                builder.put("case_category", caseCategory);

                builder.put("description", response.get().getDescription());
                builder.put(
                        "permanent_recommendation", response.get().getPermanent_recommendations());
                builder.put(
                        "temporary_recommendation", response.get().getTemporary_recommendations());
                builder.put("suggestion", response.get().getSuggestion());
                if (response.get().getIs_research() == 1) {
                    Flag isFLag = flagRepo
                            .findOneByAuditDailyReportDetailId(
                                    response.get().getId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Flag not found"));
                    if (isFLag.getClarification().getFilename() != null) {
                        builder.put("is_research", 0);
                    } else {
                        builder.put("is_research", 1);
                    }
                } else {
                    builder.put("is_research", 0);
                }
            }
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
                        .message("Data not found")
                        .status(HttpStatus.OK)
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
                        .message("Data not found")
                        .status(HttpStatus.OK)
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

            Optional<AuditDailyReport> setId = lhaReportsitory.findById(dto.getAudit_daily_report_id());
            if (!setId.isPresent()) {
                return GlobalResponse.builder().message("Lha with id:" + dto.getAudit_daily_report_id() + " not found")
                        .status(HttpStatus.BAD_REQUEST).build();
            }
            Optional<Case> setCaseId = caseRepository.findById(dto.getCase_id());
            if (!setCaseId.isPresent()) {
                return GlobalResponse.builder().message("Case with id:" + dto.getCase_id() + " not found")
                        .status(HttpStatus.BAD_REQUEST).build();
            }
            Optional<CaseCategory> setCCId = ccRepository.findById(dto.getCase_category_id());
            if (!setCCId.isPresent()) {
                return GlobalResponse.builder()
                        .message("Case Category with id:" + dto.getCase_category_id() + " not found")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            AuditDailyReportDetail auditDailyReport = new AuditDailyReportDetail(
                    null,
                    setId.get(),
                    setCaseId.get(),
                    setCCId.get(),
                    dto.getDescription(),
                    dto.getSuggestion(),
                    dto.getTemporary_recommendations(),
                    dto.getPermanent_recommendations(),
                    dto.getIs_research(),
                    0,
                    0,
                    0,
                    user.getId(),
                    user.getId(),
                    new Date(),
                    new Date());

            AuditDailyReportDetail lhaDetail = repository.save(auditDailyReport);

            Revision revision = new Revision();
            revision.setAuditDailyReportDetail(lhaDetail);
            revision.setCases(lhaDetail.getCases());
            revision.setCaseCategory(lhaDetail.getCaseCategory());
            revision.setRevisionNumber(0L);
            revision.setDescription(lhaDetail.getDescription());
            revision.setSuggestion(lhaDetail.getSuggestion());
            revision.setTemporary_recommendations(lhaDetail.getTemporary_recommendations());
            revision.setPermanent_recommendations(lhaDetail.getPermanent_recommendations());
            revision.setIs_research(lhaDetail.getIs_research());
            revision.setIs_delete(lhaDetail.getIs_delete());
            revision.setCreated_by(lhaDetail.getCreated_by());
            revision.setCreated_at(lhaDetail.getCreated_at());
            revisionRepo.save(revision);

            if (lhaDetail.getIs_research() == 1) {

                Long reportNumber = null;
                String rptNum = null;

                Optional<NumberClarificationInterface> checkClBefore = clarificationRepository
                        .checkNumberClarification(user.getId());
                if (checkClBefore.isPresent()) {
                    if (checkClBefore.get().getCreated_Year().longValue() == Long
                            .valueOf(convertDateToRoman.getIntYear())) {
                        reportNumber = checkClBefore.get().getReport_Number() + 1;
                        if (reportNumber < 10) {
                            rptNum = "00" + reportNumber;
                        } else if (reportNumber < 100) {
                            rptNum = "0" + reportNumber;
                        } else {
                            rptNum = reportNumber.toString();
                        }
                    } else {
                        rptNum = "001";
                        reportNumber = Long.valueOf(1);
                    }
                } else {
                    rptNum = "001";
                    reportNumber = Long.valueOf(1);
                }

                String branchName = lhaDetail.getAuditDailyReport().getBranch().getName();
                String initialName = user.getInitial_name();
                String caseName = lhaDetail.getCases().getName();
                String lvlCode = user.getLevel().getCode();
                String romanMonth = convertDateToRoman.getRomanMonth();
                Integer thisYear = convertDateToRoman.getIntYear();

                ReportType reportType = reportTypeRepository.findByCode("CK")
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Report Type Nt found"));

                String reportCode = rptNum + lvlCode + "/" + initialName + "-" + caseName
                        + "/" + reportType.getCode() + "/" + branchName + "/"
                        + romanMonth + "/"
                        + thisYear;

                Clarification setCL = new Clarification();
                setCL.setUser(user);
                setCL.setBranch(lhaDetail.getAuditDailyReport().getBranch());
                setCL.setCases(lhaDetail.getCases());
                setCL.setCaseCategory(lhaDetail.getCaseCategory());
                setCL.setReportType(reportType);
                setCL.setReport_number(reportNumber);
                setCL.setCode(reportCode);
                setCL.setStatus(EStatusClarification.INPUT);
                setCL.setCreated_at(new Date());
                setCL.setUpdated_at(new Date());

                Clarification response3 = clarificationRepository.save(setCL);

                Flag flag = new Flag();
                flag.setAuditDailyReportDetail(lhaDetail);
                flag.setClarification(response3);
                flag.setCreatedAt(new Date());
                flagRepo.save(flag);
            }

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
                        .message("LHA not found")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            Optional<Case> setCaseId = caseRepository.findById(dto.getCase_id());
            if (!setCaseId.isPresent()) {
                return GlobalResponse.builder().message("Case not found").status(HttpStatus.BAD_REQUEST).build();
            }
            Optional<CaseCategory> setCCId = ccRepository.findById(dto.getCase_category_id());
            if (!setCCId.isPresent()) {
                return GlobalResponse.builder().message("Case Category not found").status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            AuditDailyReportDetail auditDailyReport = new AuditDailyReportDetail(
                    id,
                    getBefore.get().getAuditDailyReport(),
                    setCaseId.get(),
                    setCCId.get(),
                    dto.getDescription(),
                    dto.getSuggestion(),
                    dto.getTemporary_recommendations(),
                    dto.getPermanent_recommendations(),
                    dto.getIs_research(),
                    getBefore.get().getStatus_flow(),
                    getBefore.get().getStatus_parsing(),
                    0,
                    getBefore.get().getCreated_by(),
                    user.getId(),
                    getBefore.get().getCreated_at(),
                    new Date());

            repository.save(auditDailyReport);
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

            repository.save(auditDailyReport);

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
