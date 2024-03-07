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
import com.cms.audit.api.AuditDailyReport.models.AuditDailyReport;
import com.cms.audit.api.AuditDailyReport.models.AuditDailyReportDetail;
import com.cms.audit.api.AuditDailyReport.repository.AuditDailyReportDetailRepository;
import com.cms.audit.api.AuditDailyReport.repository.AuditDailyReportRepository;
import com.cms.audit.api.InspectionSchedule.models.Schedule;
import com.cms.audit.api.common.response.GlobalResponse;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuditDailyReportService {

    @Autowired
    private AuditDailyReportRepository auditDailyReportRepository;

    @Autowired
    private AuditDailyReportDetailRepository auditDailyReportDetailRepository;

    public GlobalResponse get() {
        try {
            List<AuditDailyReport> response = auditDailyReportRepository.findAll();
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
            AuditDailyReport response = auditDailyReportRepository.findById(id).orElseThrow(()-> new IllegalStateException(
                "lha with id " + id + " does now exist"
            ));
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

            Schedule scheduleId = Schedule.builder().id(dto.getSchedule_id()).build();
            
            AuditDailyReport auditDailyReport = new AuditDailyReport(
                null,
                scheduleId,
                dto.getIs_research(),
                0,
                dto.getCreated_by(),
                new Date(),
                new Date()
            );

            Optional<AuditDailyReport> checkLHA = auditDailyReportRepository.findByCurrentDay(new Date());

            if(checkLHA.isPresent()){

            }

            AuditDailyReport response1 = auditDailyReportRepository.save(auditDailyReport);

            AuditDailyReport setId =  AuditDailyReport.builder().id(response1.getId()).build();

            AuditDailyReportDetail auditDailyReportDetail = new AuditDailyReportDetail(
                null,
                setId,
                dto.getDescription(),
                dto.getSuggestion(),
                dto.getTemporary_recommendations(),
                dto.getPermanent_recommendations(),
                0,
                dto.getCreated_by(),
                new Date(),
                new Date()
            );

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

    public GlobalResponse edit(AuditDailyReportDTO dto, Long id) {
        try {
            Optional<AuditDailyReport> getBefore = auditDailyReportRepository.findById(id);
            if(!getBefore.isPresent()){
                return GlobalResponse
                .builder()
                .message("Not Found")
                .status(HttpStatus.NOT_FOUND)
                .build();
            }

            Schedule scheduleId = Schedule.builder().id(dto.getSchedule_id()).build();
            
            AuditDailyReport auditDailyReport = new AuditDailyReport(
                id,
                scheduleId,
                dto.getIs_research(),
                0,
                dto.getCreated_by(),
                getBefore.get().getCreated_at(),
                new Date()
            );

            AuditDailyReport response = auditDailyReportRepository.save(auditDailyReport);
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

    public GlobalResponse delete(Long id) {
        try {
            AuditDailyReport response = auditDailyReportRepository.softDelete(id);
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

}
