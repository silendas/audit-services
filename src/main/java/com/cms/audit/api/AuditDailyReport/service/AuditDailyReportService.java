package com.cms.audit.api.AuditDailyReport.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cms.audit.api.AuditDailyReport.dto.AuditDailyReportDTO;
import com.cms.audit.api.AuditDailyReport.dto.EditAuditDailyReportDTO;
import com.cms.audit.api.AuditDailyReport.models.AuditDailyReport;
import com.cms.audit.api.AuditDailyReport.models.AuditDailyReportDetail;
import com.cms.audit.api.AuditDailyReport.repository.AuditDailyReportDetailRepository;
import com.cms.audit.api.AuditDailyReport.repository.AuditDailyReportRepository;
import com.cms.audit.api.InspectionSchedule.models.EStatus;
import com.cms.audit.api.InspectionSchedule.models.Schedule;
import com.cms.audit.api.InspectionSchedule.repository.ScheduleRepository;
import com.cms.audit.api.Management.Case.models.Case;
import com.cms.audit.api.Management.CaseCategory.models.CaseCategory;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.common.response.GlobalResponse;

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

    public GlobalResponse get() {
        try {
            List<AuditDailyReport> response = auditDailyReportRepository.findAllLHA();
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

    public GlobalResponse getById(Long id) {
        try {
            AuditDailyReport response = auditDailyReportRepository.findOneByLHAId(id)
                    .orElseThrow(() -> new IllegalStateException(
                            "LHA with id " + id + " does now exist"));
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

    public GlobalResponse getByScheduleId(Long id) {
        try {
            AuditDailyReport response = auditDailyReportRepository.findByScheduleId(id)
                    .orElseThrow(() -> new IllegalStateException(
                            "lha with schedule_id " + id + " does now exist"));
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

            List<AuditDailyReport> checkLHA = auditDailyReportRepository.findByCurrentDay(dto.getSchedule_id());
            if (!checkLHA.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Uploadnya sehari 1 aja blok")
                        .status(HttpStatus.FOUND)
                        .build();
            }

            Schedule scheduleId = Schedule.builder().id(dto.getSchedule_id()).build();
            User userId = User.builder().id(dto.getUser_id()).build();
            Branch branchId = Branch.builder().id(dto.getBranch_id()).build();

            AuditDailyReport auditDailyReport = new AuditDailyReport(
                    null,
                    scheduleId,
                    branchId,
                    userId,
                    dto.getIs_research(),
                    0,
                    dto.getCreate_by(),
                    dto.getCreate_by(),
                    new Date(),
                    new Date());

            Optional<Schedule> getSchedule = scheduleRepository.findOneScheduleById(dto.getSchedule_id());
            if (getSchedule.get().getStart_date_realization() == null) {
                Branch branchIdSchedule = Branch.builder()
                        .id(getSchedule.get().getBranch().getId())
                        .build();

                User userIdSchedule = User.builder()
                        .id(getSchedule.get().getUser().getId())
                        .build();

                Schedule schedule = new Schedule(
                        dto.getSchedule_id(),
                        userIdSchedule,
                        branchIdSchedule,
                        getSchedule.get().getDescription(),
                        getSchedule.get().getStart_date(),
                        getSchedule.get().getEnd_date(),
                        new Date(),
                        null,
                        EStatus.PROGRESS,
                        getSchedule.get().getCategory(),
                        0,
                        dto.getCreate_by(),
                        getSchedule.get().getCreatedBy(),
                        getSchedule.get().getCreated_at(),
                        new Date());

                Schedule responseSchedule = scheduleRepository.save(schedule);

            }

            AuditDailyReport response1 = auditDailyReportRepository.save(auditDailyReport);

            AuditDailyReport setId = AuditDailyReport.builder().id(response1.getId()).build();
            Case setCaseId = Case.builder().id(dto.getCase_id()).build();
            CaseCategory setCaseCategoryId = CaseCategory.builder().id(dto.getCase_category_id()).build();

            AuditDailyReportDetail auditDailyReportDetail = new AuditDailyReportDetail(
                    null,
                    setId,
                    setCaseId,
                    setCaseCategoryId,
                    dto.getDescription(),
                    dto.getSuggestion(),
                    dto.getTemporary_recommendations(),
                    dto.getPermanent_recommendations(),
                    0,
                    dto.getCreate_by(),
                    dto.getCreate_by(),
                    new Date(),
                    new Date());

            AuditDailyReportDetail response2 = auditDailyReportDetailRepository.save(auditDailyReportDetail);

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
                    dto.getIs_research(),
                    0,
                    getBefore.get().getCreated_by(),
                    dto.getUpdate_by(),
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
                    getBefore.get().getIsResearch(),
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
