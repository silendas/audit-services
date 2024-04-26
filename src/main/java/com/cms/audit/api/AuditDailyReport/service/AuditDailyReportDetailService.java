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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public GlobalResponse get(int page, int size, Long lhaId, Date startDate, Date endDate) {
        try {
            User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Page<AuditDailyReportDetail> response = null;
            if (lhaId != null && startDate != null && endDate != null) {
                response = pag.findAllLHADetailByDateAndLhaId(lhaId, startDate, endDate, PageRequest.of(page, size));
            } else if (lhaId != null) {
                response = pag.findByLHAId(lhaId, PageRequest.of(page, size));
            } else {
                if (getUser.getLevel().getCode().equals("C")) {
                    if (startDate != null && endDate != null) {
                        response = pag.findByUserIdAndDate(getUser.getId(), startDate, endDate,
                                PageRequest.of(page, size));
                    } else {
                        response = pag.findByUserId(getUser.getId(), PageRequest.of(page, size));
                    }
                } else if (getUser.getLevel().getCode().equals("B")) {
                    Pageable pageable = PageRequest.of(page, size);
                    List<AuditDailyReportDetail> lhaList = new ArrayList<>();
                    for (int i = 0; i < getUser.getRegionId().size(); i++) {
                        List<AuditDailyReportDetail> lhaAgain = new ArrayList<>();
                        if (startDate != null && endDate != null) {
                            lhaAgain = repository
                                    .findAllLHAByRegionAndDate(getUser.getRegionId().get(i),
                                            startDate, endDate);
                        } else {
                            lhaAgain = repository.findAllLHAByRegion(
                                    getUser.getRegionId().get(i));
                        }
                        if (!lhaAgain.isEmpty()) {
                            for (int u = 0; u < lhaAgain.size(); u++) {
                                lhaList.add(lhaAgain.get(u));
                            }
                        }
                    }
                    try {
                        int start = (int) pageable.getOffset();
                        int end = Math.min((start + pageable.getPageSize()),
                                lhaList.size());
                        List<AuditDailyReportDetail> pageContent = lhaList.subList(start,
                                end);
                        Page<AuditDailyReportDetail> response2 = new PageImpl<>(pageContent,
                                pageable,
                                lhaList.size());
                        response = response2;
                    } catch (Exception e) {
                        return GlobalResponse
                                .builder()
                                .error(e)
                                .status(HttpStatus.BAD_REQUEST)
                                .build();
                    }
                } else if (getUser.getLevel().getCode().equals("A")) {
                    if (startDate != null && endDate != null) {
                        response = pag.findAllLHADetailByDateForLeader(startDate, endDate, PageRequest.of(page, size));
                    } else {
                        response = pag.findAllLHADetailForLeader(PageRequest.of(page, size));
                    }
                }
            }
            List<Object> details = new ArrayList<>();
            for (int i = 0; i < response.getContent().size(); i++) {
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
                if (response.getContent().get(i).getStatus_flow() != null) {
                    builder.setStatus_flow(response.getContent().get(i).getStatus_flow());
                } else {
                    builder.setStatus_flow(0);
                }
                if (response.getContent().get(i).getStatus_parsing() != null) {
                    builder.setStatus_parsing(response.getContent().get(i).getStatus_parsing());
                } else {
                    builder.setStatus_parsing(0);
                }
                details.add(builder);
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
            Map<String, Object> builder = new LinkedHashMap<>();
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
            if (response.get().getStatus_flow() != null) {
                builder.put("status_flow",response.get().getStatus_flow());
            } else {
                builder.put("status_flow",0);
            }
            if (response.get().getStatus_parsing() != null) {
                builder.put("status_parsing",response.get().getStatus_parsing());
            } else {
                builder.put("status_parsing",0);
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
                return GlobalResponse.builder().message("failed")
                        .message("Lha with id:" + dto.getAudit_daily_report_id() + " not found")
                        .status(HttpStatus.BAD_REQUEST).build();
            }
            Optional<Case> setCaseId = caseRepository.findById(dto.getCase_id());
            if (!setCaseId.isPresent()) {
                return GlobalResponse.builder().message("failed")
                        .message("Case with id:" + dto.getCase_id() + " not found")
                        .status(HttpStatus.BAD_REQUEST).build();
            }
            Optional<CaseCategory> setCCId = ccRepository.findById(dto.getCase_category_id());
            if (!setCCId.isPresent()) {
                return GlobalResponse.builder()
                        .message("failed")
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

    public GlobalResponse ingnoreLhaDetail(Long lhaDetailId){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.getLevel().getCode().equals("B") && !user.getLevel().getCode().equals("A")){
            return GlobalResponse.builder().message("failed").message("TIdak dapat mengakses").status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<AuditDailyReportDetail> response = repository.findOneByLHADetailId(lhaDetailId);
        if(!response.isPresent()){
            return GlobalResponse.builder().message("failed").message("LHA detail with id: "+ lhaDetailId + " tidak ditemukan").status(HttpStatus.BAD_REQUEST).build();
        }

        AuditDailyReportDetail dto = response.get();
        dto.setStatus_parsing(1);
        dto.setUpdated_by(user.getId());
        dto.setUpdate_at(new Date());
        repository.save(dto);

        return GlobalResponse.builder().message("Success").status(HttpStatus.OK).build();

    }

    public GlobalResponse sendToLeader(Long lhaDetailId){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.getLevel().getCode().equals("B") && !user.getLevel().getCode().equals("A")){
            return GlobalResponse.builder().message("failed").message("TIdak dapat mengakses").status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<AuditDailyReportDetail> response = repository.findOneByLHADetailId(lhaDetailId);
        if(!response.isPresent()){
            return GlobalResponse.builder().message("failed").message("LHA detail with id: "+ lhaDetailId + " tidak ditemukan").status(HttpStatus.BAD_REQUEST).build();
        }

        AuditDailyReportDetail dto = response.get();
        dto.setStatus_flow(1);
        dto.setUpdated_by(user.getId());
        dto.setUpdate_at(new Date());
        repository.save(dto);

        return GlobalResponse.builder().message("Success").status(HttpStatus.OK).build();

    }

    public GlobalResponse edit(EditAuditDailyReportDetailDTO dto, Long id) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Optional<AuditDailyReportDetail> getBefore = repository.findById(id);
            if (!getBefore.isPresent()) {
                return GlobalResponse
                        .builder()
                        .message("failed").message("LHA not found")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            if(user.getLevel().getCode().equals("C")){
                if(getBefore.get().getIs_revision() == 1){
                    return GlobalResponse.builder().message("failed").message("Tidak bisa mengedit karena sudah direvisi").status(HttpStatus.BAD_REQUEST).build();
                }
            }

            Optional<Case> setCaseId = caseRepository.findById(dto.getCase_id());
            if (!setCaseId.isPresent()) {
                return GlobalResponse.builder().message("failed").message("Case not found")
                        .status(HttpStatus.BAD_REQUEST).build();
            }
            Optional<CaseCategory> setCCId = ccRepository.findById(dto.getCase_category_id());
            if (!setCCId.isPresent()) {
                return GlobalResponse.builder().message("failed").message("Case Category not found")
                        .status(HttpStatus.BAD_REQUEST)
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
                    dto.getStatus_flow(),
                    dto.getStatus_parsing(),
                    getBefore.get().getIs_revision(),
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
                        .message("failed").message("Not Found")
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
