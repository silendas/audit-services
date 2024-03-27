package com.cms.audit.api.AuditDailyReport.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.LinkedHashMap;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cms.audit.api.AuditDailyReport.dto.AuditDailyReportDTO;
import com.cms.audit.api.AuditDailyReport.dto.EditAuditDailyReportDTO;
import com.cms.audit.api.AuditDailyReport.dto.response.DetailResponse;
import com.cms.audit.api.AuditDailyReport.models.AuditDailyReport;
import com.cms.audit.api.AuditDailyReport.models.AuditDailyReportDetail;
import com.cms.audit.api.AuditDailyReport.models.Revision;
import com.cms.audit.api.AuditDailyReport.repository.AuditDailyReportDetailRepository;
import com.cms.audit.api.AuditDailyReport.repository.AuditDailyReportRepository;
import com.cms.audit.api.AuditDailyReport.repository.RevisionRepository;
import com.cms.audit.api.AuditDailyReport.repository.pagAuditDailyReport;
import com.cms.audit.api.Clarifications.dto.response.NumberClarificationInterface;
import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Clarifications.models.EStatusClarification;
import com.cms.audit.api.Clarifications.repository.ClarificationRepository;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.exception.ResourceNotFoundException;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Flag.model.Flag;
import com.cms.audit.api.Flag.repository.FlagRepo;
import com.cms.audit.api.InspectionSchedule.models.EStatus;
import com.cms.audit.api.InspectionSchedule.models.Schedule;
import com.cms.audit.api.InspectionSchedule.repository.ScheduleRepository;
import com.cms.audit.api.Management.Case.models.Case;
import com.cms.audit.api.Management.Case.repository.CaseRepository;
import com.cms.audit.api.Management.CaseCategory.models.CaseCategory;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.ReportType.models.ReportType;
import com.cms.audit.api.Management.ReportType.repository.ReportTypeRepository;
import com.cms.audit.api.Management.User.models.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuditDailyReportService {

        @Autowired
        private AuditDailyReportRepository auditDailyReportRepository;

        @Autowired
        private AuditDailyReportDetailRepository auditDailyReportDetailRepository;

        @Autowired
        private ScheduleRepository scheduleRepository;

        @Autowired
        private ClarificationRepository clarificationRepository;

        @Autowired
        private CaseRepository caseRepository;

        @Autowired
        private ReportTypeRepository reportTypeRepository;

        @Autowired
        private RevisionRepository revisionRepo;

        @Autowired
        private FlagRepo flagRepo;

        @Autowired
        private pagAuditDailyReport pagAuditDailyReport;

        public GlobalResponse get(int page, int size, Date startDate, Date endDate, Long shcedule_id) {
                try {
                        Page<AuditDailyReport> response;
                        if (startDate != null || endDate != null) {
                                response = pagAuditDailyReport.findLHAInDateRange(startDate, endDate,
                                                PageRequest.of(page, size));
                        } else if (shcedule_id != null) {
                                response = pagAuditDailyReport.findByScheduleId(shcedule_id,
                                                PageRequest.of(page, size));
                        } else {
                                response = pagAuditDailyReport.findAllLHA(PageRequest.of(page, size));
                        }
                        if (response.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("No Content")
                                                .status(HttpStatus.OK)
                                                .build();
                        }
                        for (int i = 0; i < response.getContent().size(); i++) {
                                List<AuditDailyReportDetail> getDetail = auditDailyReportDetailRepository
                                                .findByLHAId(response.getContent().get(i).getId());
                                for (int u = 0; u < getDetail.size(); u++) {
                                        if (response.getContent().get(i).getIs_research() != 1) {
                                                if (getDetail.get(u).getIs_research() == 1) {
                                                        Flag isFlag = flagRepo.findOneByAuditDailyReportDetailId(
                                                                        getDetail.get(u).getId()).orElse(null);
                                                        if (isFlag != null) {
                                                                if (isFlag.getClarification().getFilename() == null) {
                                                                        response.getContent().get(i).setIs_research(1);
                                                                } else {
                                                                        response.getContent().get(i).setIs_research(0);
                                                                }
                                                        } else {
                                                                response.getContent().get(i).setIs_research(0);
                                                        }
                                                }
                                        }
                                }
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

        public GlobalResponse getById(Long id) {
                try {
                        AuditDailyReport getLha = auditDailyReportRepository.findOneByLHAId(id)
                                        .orElseThrow(() -> new ResourceNotFoundException("Not found lha"));

                        List<AuditDailyReportDetail> getDetail = auditDailyReportDetailRepository.findByLHAId(id);
                        List<DetailResponse> details = new ArrayList<>();
                        for (int i = 0; i < getDetail.size(); i++) {
                                Optional<Revision> getRevision = revisionRepo.findByDetailId(getDetail.get(i).getId());
                                if (!getRevision.isPresent()) {
                                        DetailResponse builder = new DetailResponse();
                                        builder.setId(getDetail.get(i).getId());
                                        builder.setCases(getDetail.get(i).getCases().getName());
                                        builder.setCaseCategory(getDetail.get(i).getCaseCategory().getName());
                                        builder.setDescription(getDetail.get(i).getDescription());
                                        builder.setPermanent_recommendations(
                                                        getDetail.get(i).getPermanent_recommendations());
                                        builder.setTemporary_recommendations(
                                                        getDetail.get(i).getTemporary_recommendations());
                                        builder.setSuggestion(getDetail.get(i).getSuggestion());
                                        if (getDetail.get(i).getIs_research() == 1) {
                                                Flag isFLag = flagRepo
                                                                .findOneByAuditDailyReportDetailId(
                                                                                getDetail.get(i).getId())
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
                                        if (getDetail.get(i).getIs_research() == 1) {
                                                Flag isFLag = flagRepo
                                                                .findOneByAuditDailyReportDetailId(
                                                                                getDetail.get(i).getId())
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
                                        details.add(builder);
                                }

                        }

                        Map<String, Object> response = new LinkedHashMap<>();
                        response.put("id", getLha.getId());
                        response.put("user", getLha.getUser());
                        response.put("branch", getLha.getBranch());
                        response.put("schedule", getLha.getSchedule());
                        response.put("is_research", getLha.getIs_research());
                        response.put("details", details);
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

        public GlobalResponse getByScheduleId(Long id, int page, int size) {
                try {
                        Page<AuditDailyReport> response = pagAuditDailyReport.findByScheduleId(id,
                                        PageRequest.of(page, size));
                        if (response.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("No Content")
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

        public GlobalResponse save(AuditDailyReportDTO dto) {
                try {
                        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                        List<AuditDailyReport> checkLHA = auditDailyReportRepository
                                        .findByCurrentDay(dto.getSchedule_id());
                        if (!checkLHA.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("LHA already exist for today, insert again tommorow")
                                                .status(HttpStatus.FOUND)
                                                .build();
                        }

                        Schedule scheduleId = Schedule.builder().id(dto.getSchedule_id()).build();
                        Branch branchId = Branch.builder().id(dto.getBranch_id()).build();

                        AuditDailyReport auditDailyReport = new AuditDailyReport(
                                        null,
                                        scheduleId,
                                        branchId,
                                        user,
                                        0,
                                        0,
                                        user.getId(),
                                        user.getId(),
                                        new Date(),
                                        new Date());

                        Optional<Schedule> getSchedule = scheduleRepository.findOneScheduleById(dto.getSchedule_id());
                        if (!getSchedule.isPresent()) {
                                return GlobalResponse
                                                .builder()
                                                .message("No Content")
                                                .status(HttpStatus.OK)
                                                .build();
                        }
                        if (getSchedule.get().getStart_date_realization() == null) {
                                Schedule schedule = getSchedule
                                                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
                                schedule.setStart_date_realization(new Date());
                                schedule.setStatus(EStatus.PROGRESS);
                                scheduleRepository.save(schedule);
                        }

                        AuditDailyReport response1 = auditDailyReportRepository.save(auditDailyReport);
                        AuditDailyReport setId = AuditDailyReport.builder().id(response1.getId()).build();

                        for (int i = 0; i < dto.getLha_detail().size(); i++) {
                                Case setCaseId = Case.builder().id(dto.getLha_detail().get(i).getCase_id()).build();
                                CaseCategory setCaseCategoryId = CaseCategory.builder()
                                                .id(dto.getLha_detail().get(i).getCase_category_id()).build();
                                AuditDailyReportDetail auditDailyReportDetail = new AuditDailyReportDetail(
                                                null,
                                                setId,
                                                setCaseId,
                                                setCaseCategoryId,
                                                dto.getLha_detail().get(i).getDescription(),
                                                dto.getLha_detail().get(i).getSuggestion(),
                                                dto.getLha_detail().get(i).getTemporary_recommendations(),
                                                dto.getLha_detail().get(i).getPermanent_recommendations(),
                                                dto.getLha_detail().get(i).getIs_research(),
                                                0,
                                                user.getId(),
                                                user.getId(),
                                                new Date(),
                                                new Date());

                                AuditDailyReportDetail response2 = auditDailyReportDetailRepository
                                                .save(auditDailyReportDetail);

                                if (dto.getLha_detail().get(i).getIs_research() == 1) {

                                        Case getCase = caseRepository.findById(dto.getLha_detail().get(i).getCase_id())
                                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK,
                                                                        "no content"));

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

                                        String branchName = getSchedule.get().getBranch().getName();
                                        String initialName = user.getInitial_name();
                                        String caseName = getCase.getCode();
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

                                        Clarification clarification = new Clarification(
                                                        null,
                                                        user,
                                                        getSchedule.get().getBranch(),
                                                        setCaseId,
                                                        setCaseCategoryId,
                                                        reportType,
                                                        reportNumber,
                                                        reportCode,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        EStatusClarification.INPUT,
                                                        new Date(),
                                                        new Date());

                                        Clarification response3 = clarificationRepository.save(clarification);

                                        Flag flag = new Flag();
                                        flag.setAuditDailyReportDetail(response2);
                                        flag.setClarification(response3);
                                        flag.setCreatedAt(new Date());
                                        flagRepo.save(flag);
                                }
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

        public GlobalResponse edit(EditAuditDailyReportDTO dto, Long id) {
                try {
                        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                        Optional<AuditDailyReport> getBefore = auditDailyReportRepository.findById(id);
                        if (!getBefore.isPresent()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Not Found")
                                                .status(HttpStatus.NOT_FOUND)
                                                .build();
                        }

                        Schedule scheduleId = Schedule.builder().id(dto.getSchedule_id()).build();
                        User userId = User.builder().id(dto.getUser_id()).build();
                        Branch branchId = Branch.builder().id(dto.getBranch_id()).build();

                        AuditDailyReport auditDailyReport = new AuditDailyReport(
                                        id,
                                        scheduleId,
                                        branchId,
                                        userId,
                                        null,
                                        0,
                                        getBefore.get().getCreated_by(),
                                        user.getId(),
                                        getBefore.get().getCreated_at(),
                                        new Date());

                        AuditDailyReport response = auditDailyReportRepository.save(auditDailyReport);
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
                        Optional<AuditDailyReport> getBefore = auditDailyReportRepository.findById(id);
                        if (!getBefore.isPresent()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Not Found")
                                                .status(HttpStatus.NOT_FOUND)
                                                .build();
                        }

                        Schedule scheduleId = Schedule.builder().id(getBefore.get().getSchedule().getId()).build();
                        User userId = User.builder().id(getBefore.get().getUser().getId()).build();
                        Branch branchId = Branch.builder().id(getBefore.get().getBranch().getId()).build();

                        AuditDailyReport auditDailyReport = new AuditDailyReport(
                                        id,
                                        scheduleId,
                                        branchId,
                                        userId,
                                        null,
                                        1,
                                        getBefore.get().getCreated_by(),
                                        getBefore.get().getCreated_by(),
                                        getBefore.get().getCreated_at(),
                                        new Date());

                        AuditDailyReport response = auditDailyReportRepository.save(auditDailyReport);
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
