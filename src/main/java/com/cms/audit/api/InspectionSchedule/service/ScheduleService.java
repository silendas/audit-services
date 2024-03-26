package com.cms.audit.api.InspectionSchedule.service;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Map;
import java.util.LinkedHashMap;

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

import com.cms.audit.api.AuditDailyReport.models.AuditDailyReport;
import com.cms.audit.api.AuditDailyReport.repository.AuditDailyReportRepository;
import com.cms.audit.api.AuditDailyReport.service.AuditDailyReportService;
import com.cms.audit.api.AuditWorkingPaper.models.AuditWorkingPaper;
import com.cms.audit.api.AuditWorkingPaper.repository.AuditWorkingPaperRepository;
import com.cms.audit.api.AuditWorkingPaper.service.AuditWorkingPaperService;
import com.cms.audit.api.Common.constant.randomValueNumber;
import com.cms.audit.api.Common.exception.ResourceNotFoundException;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.InspectionSchedule.dto.EditScheduleDTO;
import com.cms.audit.api.InspectionSchedule.dto.RequestReschedule;
import com.cms.audit.api.InspectionSchedule.dto.RescheduleDTO;
import com.cms.audit.api.InspectionSchedule.dto.ScheduleDTO;
import com.cms.audit.api.InspectionSchedule.dto.ScheduleRequest;
import com.cms.audit.api.InspectionSchedule.models.ECategory;
import com.cms.audit.api.InspectionSchedule.models.EStatus;
import com.cms.audit.api.InspectionSchedule.models.Schedule;
import com.cms.audit.api.InspectionSchedule.models.ScheduleTrx;
import com.cms.audit.api.InspectionSchedule.repository.PagSchedule;
import com.cms.audit.api.InspectionSchedule.repository.ScheduleRepository;
import com.cms.audit.api.InspectionSchedule.repository.ScheduleTrxRepo;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;
import com.cms.audit.api.Management.Office.RegionOffice.repository.RegionRepository;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Management.User.repository.UserRepository;
import com.itextpdf.io.IOException;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@Service
public class ScheduleService {

        @Autowired
        private ScheduleRepository repository;

        @Autowired
        private LogScheduleService logService;

        @Autowired
        private PagSchedule pagSchedule;

        @Autowired
        private AuditDailyReportRepository auditDailyReportRepository;

        @Autowired
        private AuditWorkingPaperRepository auditWorkingPaperRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private ScheduleTrxRepo scheduleTrxRepo;

        public GlobalResponse get(Long branch_id, String name, int page, int size) {
                try {
                        Page<Schedule> response = pagSchedule.findAll(PageRequest.of(page, size));
                        if (response.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Not Content")
                                                .data(response)
                                                .status(HttpStatus.OK)
                                                .build();
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .data(response)
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }

        }

        public GlobalResponse getMainSchedule(Long branch_id, String name, int page, int size, Date start_date,
                        Date end_date) {
                try {
                        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                        if (user.getLevel().getId() == 3) {
                                return getByUserId(user.getId(), "REGULAR", page, size, start_date, end_date);
                        } else if (user.getLevel().getId() == 2 || user.getLevel().getId() == 1) {
                                if(branch_id != null && name != null && start_date != null && end_date != null){
                                        return GlobalResponse.builder().data(pagSchedule.findAllScheduleByAllFilter(name, branch_id, "REGULAR", start_date, end_date, PageRequest.of(page, size))).message("Success").status(HttpStatus.OK).build();
                                }else if (name != null) {
                                        List<User> getUser = userRepository.findByFullnameLike(name);
                                        Pageable pageable = PageRequest.of(page, size);
                                        List<Schedule> scheduleList = new ArrayList<>();
                                        for (int i = 0; i < getUser.size(); i++) {
                                                List<Schedule> getSchedule = repository.findAllScheduleByUserId(
                                                                getUser.get(i).getId(), "REGULAR");
                                                for (int u = 0; u < getSchedule.size(); u++) {
                                                        if (!scheduleList.contains(getSchedule.get(u))) {
                                                                scheduleList.add(getSchedule.get(u));
                                                        }
                                                }
                                        }
                                        try {
                                                int start = (int) pageable.getOffset();
                                                int end = Math.min((start + pageable.getPageSize()),
                                                                scheduleList.size());
                                                List<Schedule> pageContent = scheduleList.subList(start, end);
                                                Page<Schedule> response2 = new PageImpl<>(pageContent, pageable,
                                                                scheduleList.size());
                                                return GlobalResponse
                                                                .builder()
                                                                .message("Success")
                                                                .data(response2)
                                                                .status(HttpStatus.OK)
                                                                .build();
                                        } catch (Exception e) {
                                                return GlobalResponse
                                                                .builder()
                                                                .error(e)
                                                                .status(HttpStatus.BAD_REQUEST)
                                                                .build();
                                        }
                                } else if (branch_id != null) {
                                        return getByBranchIdInDate(branch_id, "REGULAR", page, size, start_date, end_date);
                                } else {
                                        Pageable pageable = PageRequest.of(page, size);
                                        List<Schedule> scheduleList = new ArrayList<>();
                                        if(user.getLevel().getId() == 1){
                                                if(start_date!=null || end_date!=null){
                                                        return GlobalResponse.builder().data(pagSchedule.findAllScheduleByDateRange("REGULAR", start_date, end_date, PageRequest.of(page, size))).build();
                                                }
                                                return GlobalResponse.builder().data(pagSchedule.findAll(PageRequest.of(page, size))).message("Success").status(HttpStatus.OK).build();
                                        }
                                        if(start_date == null || end_date == null){
                                                for (int i = 0; i < user.getRegionId().size(); i++) {
                                                        List<Schedule> getByRegion = repository.findByRegionId(user.getRegionId().get(i), "REGULAR");
                                                        for (int u = 0; u < getByRegion.size(); u++) {
                                                                if (!scheduleList.contains(getByRegion.get(u))) {
                                                                        scheduleList.add(getByRegion.get(u));
                                                                }
                                                        }
                                                }
                                        } else {
                                                for (int i = 0; i < user.getRegionId().size(); i++) {
                                                        List<Schedule> getByRegion = repository.findScheduleInDateRangeByRegionId(branch_id, "REGULAR", start_date, end_date);
                                                        for (int u = 0; u < getByRegion.size(); u++) {
                                                                if (!scheduleList.contains(getByRegion.get(u))) {
                                                                        scheduleList.add(getByRegion.get(u));
                                                                }
                                                        }
                                                }
                                        }
                                        try {
                                                int start = (int) pageable.getOffset();
                                                int end = Math.min((start + pageable.getPageSize()),
                                                                scheduleList.size());
                                                List<Schedule> pageContent = scheduleList.subList(start, end);
                                                Page<Schedule> response2 = new PageImpl<>(pageContent, pageable,
                                                                scheduleList.size());
                                                return GlobalResponse
                                                                .builder()
                                                                .message("Success")
                                                                .data(response2)
                                                                .status(HttpStatus.OK)
                                                                .build();
                                        } catch (Exception e) {
                                                return GlobalResponse
                                                                .builder()
                                                                .error(e)
                                                                .status(HttpStatus.BAD_REQUEST)
                                                                .build();
                                        }
                                }
                        } else {
                                Page<Schedule> response = null;
                                if (start_date == null || end_date == null) {
                                        response = pagSchedule.findByCategoryInByOrderByIdDesc("REGULAR",
                                                        PageRequest.of(page, size));
                                } else {
                                        response = pagSchedule.findScheduleByCategoryInDateRange("REGULAR", start_date,
                                                        end_date, PageRequest.of(page, size));
                                }
                                if (response.isEmpty()) {
                                        return GlobalResponse
                                                        .builder()
                                                        .message("Not Content")
                                                        .data(response)
                                                        .status(HttpStatus.OK)
                                                        .build();
                                }
                                return GlobalResponse
                                                .builder()
                                                .message("Success")
                                                .data(response)
                                                .status(HttpStatus.OK)
                                                .build();
                        }
                        // if (start_date == null || end_date == null) {
                        // response = pagSchedule.findByCategoryInByOrderByIdDesc("REGULAR",
                        // PageRequest.of(page, size));
                        // } else {
                        // if(userId!=null){
                        // getByUserId(userId, "REGULAR", page, size, start_date, end_date);
                        // }
                        // response = pagSchedule.findScheduleInDateRangeByUserId(userId, "REGULAR",
                        // start_date, end_date, PageRequest.of(page, size));
                        // }
                } catch (DataException e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }

        }

        public GlobalResponse getSpecialSchedule(Long branch_id,String name,int page, int size, Date start_date, Date end_date) {
                try {
                        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                        if (user.getLevel().getId() == 3) {
                                return getByUserId(user.getId(), "SPECIAL", page, size, start_date, end_date);
                        } else if (user.getLevel().getId() == 2 || user.getLevel().getId() == 1) {
                                if(branch_id != null && name != null && start_date != null && end_date != null){
                                        return GlobalResponse.builder().data(pagSchedule.findAllScheduleByAllFilter(name, branch_id, "SPECIAL", start_date, end_date, PageRequest.of(page, size))).message("Success").status(HttpStatus.OK).build();
                                }else if (name != null) {
                                        List<User> getUser = userRepository.findByFullnameLike(name);
                                        Pageable pageable = PageRequest.of(page, size);
                                        List<Schedule> scheduleList = new ArrayList<>();
                                        for (int i = 0; i < getUser.size(); i++) {
                                                List<Schedule> getSchedule = repository.findAllScheduleByUserId(
                                                                getUser.get(i).getId(), "SPECIAL");
                                                for (int u = 0; u < getSchedule.size(); u++) {
                                                        if (!scheduleList.contains(getSchedule.get(u))) {
                                                                scheduleList.add(getSchedule.get(u));
                                                        }
                                                }
                                        }
                                        try {
                                                int start = (int) pageable.getOffset();
                                                int end = Math.min((start + pageable.getPageSize()),
                                                                scheduleList.size());
                                                List<Schedule> pageContent = scheduleList.subList(start, end);
                                                Page<Schedule> response2 = new PageImpl<>(pageContent, pageable,
                                                                scheduleList.size());
                                                return GlobalResponse
                                                                .builder()
                                                                .message("Success")
                                                                .data(response2)
                                                                .status(HttpStatus.OK)
                                                                .build();
                                        } catch (Exception e) {
                                                return GlobalResponse
                                                                .builder()
                                                                .error(e)
                                                                .status(HttpStatus.BAD_REQUEST)
                                                                .build();
                                        }
                                } else if (branch_id != null) {
                                        return getByBranchIdInDate(branch_id, "SPECIAL", page, size, start_date, end_date);
                                } else {
                                        Pageable pageable = PageRequest.of(page, size);
                                        List<Schedule> scheduleList = new ArrayList<>();
                                        if(user.getLevel().getId() == 1){
                                                if(start_date!=null || end_date!=null){
                                                        return GlobalResponse.builder().data(pagSchedule.findAllScheduleByDateRange("SPECIAL", start_date, end_date, PageRequest.of(page, size))).build();
                                                }
                                                return GlobalResponse.builder().data(pagSchedule.findAll(PageRequest.of(page, size))).message("Success").status(HttpStatus.OK).build();
                                        }
                                        if(start_date == null || end_date == null){
                                                for (int i = 0; i < user.getRegionId().size(); i++) {
                                                        List<Schedule> getByRegion = repository.findByRegionId(user.getRegionId().get(i), "SPECIAL");
                                                        for (int u = 0; u < getByRegion.size(); u++) {
                                                                if (!scheduleList.contains(getByRegion.get(u))) {
                                                                        scheduleList.add(getByRegion.get(u));
                                                                }
                                                        }
                                                }
                                        } else {
                                                for (int i = 0; i < user.getRegionId().size(); i++) {
                                                        List<Schedule> getByRegion = repository.findScheduleInDateRangeByRegionId(branch_id, "SPECIAL", start_date, end_date);
                                                        for (int u = 0; u < getByRegion.size(); u++) {
                                                                if (!scheduleList.contains(getByRegion.get(u))) {
                                                                        scheduleList.add(getByRegion.get(u));
                                                                }
                                                        }
                                                }
                                        }
                                        try {
                                                int start = (int) pageable.getOffset();
                                                int end = Math.min((start + pageable.getPageSize()),
                                                                scheduleList.size());
                                                List<Schedule> pageContent = scheduleList.subList(start, end);
                                                Page<Schedule> response2 = new PageImpl<>(pageContent, pageable,
                                                                scheduleList.size());
                                                return GlobalResponse
                                                                .builder()
                                                                .message("Success")
                                                                .data(response2)
                                                                .status(HttpStatus.OK)
                                                                .build();
                                        } catch (Exception e) {
                                                return GlobalResponse
                                                                .builder()
                                                                .error(e)
                                                                .status(HttpStatus.BAD_REQUEST)
                                                                .build();
                                        }
                                }
                        } else {
                                Page<Schedule> response = null;
                                if (start_date == null || end_date == null) {
                                        response = pagSchedule.findByCategoryInByOrderByIdDesc("SPECIAL",
                                                        PageRequest.of(page, size));
                                } else {
                                        response = pagSchedule.findScheduleByCategoryInDateRange("SPECIAL", start_date,
                                                        end_date, PageRequest.of(page, size));
                                }
                                if (response.isEmpty()) {
                                        return GlobalResponse
                                                        .builder()
                                                        .message("Not Content")
                                                        .data(response)
                                                        .status(HttpStatus.OK)
                                                        .build();
                                }
                                return GlobalResponse
                                                .builder()
                                                .message("Success")
                                                .data(response)
                                                .status(HttpStatus.OK)
                                                .build();
                        }
                } catch (DataException e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }
        }

        public GlobalResponse getByBranchId(Long id, String category, int page, int size, Date start_date,
                        Date end_date) {
                List<Schedule> response;
                if (start_date == null || end_date == null) {
                        response = repository.findAllScheduleByBranchId(id, category);
                } else {
                        response = repository.findScheduleInDateRangeByRegionId(id, category, start_date,
                                        end_date);
                }
                return GlobalResponse
                                .builder()
                                .message("Success")
                                .data(response)
                                .status(HttpStatus.OK)
                                .build();

        }

        public GlobalResponse getByBranchIdInDate(Long id, String category, int page, int size, Date start_date,
                        Date end_date) {
                try {
                        Page<Schedule> response;
                        if (start_date == null || end_date == null) {
                                response = pagSchedule.findAllScheduleByBranchId(id, category,
                                                PageRequest.of(page, size));
                        } else {
                                response = pagSchedule.findAllScheduleByDateRange(category, start_date, end_date, PageRequest.of(page,size));
                        }
                        return GlobalResponse
                                .builder()
                                .message("Success")
                                .data(response)
                                .status(HttpStatus.OK)
                                .build();
                } catch (Exception e) {
                        return GlobalResponse
                                .builder()
                                .error(e)
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .build();
                }

        }

        public List<Schedule> getByRegionId(Long id, String category, int page, int size, Date start_date,
                        Date end_date) {
                List<Schedule> response;
                if (start_date == null || end_date == null) {
                        response = repository.findByRegionId(id, category);
                } else {
                        response = repository.findScheduleInDateRangeByRegionId(id, category, start_date,
                                        end_date);
                }
                return response;

        }

        public GlobalResponse getById(Long id) {
                try {
                        Optional<Schedule> getSchedule = repository.findOneScheduleById(id);
                        if (!getSchedule.isPresent()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Not Contentof schedule")
                                                .status(HttpStatus.OK)
                                                .build();
                        }
                        List<AuditDailyReport> getLha = auditDailyReportRepository.findByScheduleId(id);
                        Optional<AuditWorkingPaper> getKka = auditWorkingPaperRepository.findByScheduleId(id);
                        Map<String, Object> response = new LinkedHashMap<>();
                        response.put("schedule", getSchedule);
                        response.put("lha", getLha);
                        response.put("kka", getKka.orElse(null));

                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .data(response)
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }

        }

        public GlobalResponse getByStatus(String username, int page, int size) {
                try {
                        User getUser = userRepository.findByUsername(username)
                                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                        Page<Schedule> response = new PageImpl<>(null);
                        if (getUser.getLevel().getId() == 1) {
                                response = pagSchedule.findOneScheduleByStatus("REQUEST",
                                                PageRequest.of(page, size));

                        } else if (getUser.getLevel().getId() == 2) {
                                response = pagSchedule.findOneScheduleByStatus("PENDING",
                                                PageRequest.of(page, size));
                        }
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
                } catch (DataException e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }

        }

        public GlobalResponse getByUserId(Long id, String category, int page, int size, Date start_date,
                        Date end_date) {
                try {
                        Page<Schedule> response;
                        if (start_date == null || end_date == null) {
                                response = pagSchedule.findAllScheduleByUserId(id, category,
                                                PageRequest.of(page, size));
                        } else {
                                response = pagSchedule.findScheduleInDateRangeByUserId(id, category, start_date,
                                                end_date, PageRequest.of(page, size));
                        }
                        if (response.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Not Content")
                                                .status(HttpStatus.OK)
                                                .build();
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .data(response)
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }

        }

        public GlobalResponse getByRangeDateAndUserId(Long id, String category, Date start_date, Date end_date,
                        int page, int size) {
                try {
                        Page<Schedule> response;
                        if (start_date == null || end_date == null) {
                                response = pagSchedule.findAllScheduleByUserId(id, category,
                                                PageRequest.of(page, size));
                        } else {
                                response = pagSchedule.findScheduleInDateRangeByUserId(id,
                                                category,
                                                start_date, end_date, PageRequest.of(page, size));
                        }

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
                } catch (DataException e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }

        }

        @Transactional(value = TxType.REQUIRES_NEW)
        public GlobalResponse insertRegularSchedule(ScheduleRequest scheduleDTO, String username) {
                try {
                        User getUser = userRepository.findByUsername(username)
                                        .orElseThrow(() -> new ResourceNotFoundException("user not found"));
                        for (int i = 0; i < scheduleDTO.getListSchedule().size(); i++) {
                                Branch branchId = Branch.builder()
                                                .id(scheduleDTO.getListSchedule().get(i).getBranch_id())
                                                .build();

                                User userId = User.builder()
                                                .id(scheduleDTO.getListSchedule().get(i).getUser_id())
                                                .build();
                                Schedule schedule = new Schedule(
                                                null,
                                                null,
                                                userId,
                                                branchId,
                                                scheduleDTO.getListSchedule().get(i).getDescription(),
                                                scheduleDTO.getListSchedule().get(i).getStart_date(),
                                                scheduleDTO.getListSchedule().get(i).getEnd_date(),
                                                null,
                                                null,
                                                EStatus.TODO,
                                                ECategory.REGULAR,
                                                0,
                                                null,
                                                getUser.getId(),
                                                new Date(),
                                                new Date());

                                // check if schedule already exist?
                                List<Schedule> checkIfExist = repository.findScheduleInDateRangeByUserId(
                                                scheduleDTO.getListSchedule().get(i).getUser_id(), "REGULAR",
                                                scheduleDTO.getListSchedule().get(i).getStart_date(),
                                                scheduleDTO.getListSchedule().get(i).getEnd_date());
                                if (!checkIfExist.isEmpty()) {
                                        return GlobalResponse
                                                        .builder()
                                                        .message("Start and end date is already exist")
                                                        .status(HttpStatus.FOUND)
                                                        .build();
                                }

                                Schedule response = repository.save(schedule);
                                if (response == null) {
                                        return GlobalResponse
                                                        .builder()
                                                        .message("Failed")
                                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                                        .build();
                                }

                                logService.save(response.getCreatedBy(), response.getDescription(), response.getId(),
                                                ECategory.REGULAR, response.getStatus());
                        }

                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .status(HttpStatus.OK)
                                        .build();

                } catch (DataException e) {
                        return GlobalResponse
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }
        }

        @Transactional(value = TxType.REQUIRES_NEW)
        public GlobalResponse insertSpecialSchedule(ScheduleRequest scheduleDTO, String username) {
                try {
                        User getUser = userRepository.findByUsername(username)
                                        .orElseThrow(() -> new ResourceNotFoundException("user not found"));
                        for (int i = 0; i < scheduleDTO.getListSchedule().size(); i++) {
                                Branch branchId = Branch.builder()
                                                .id(scheduleDTO.getListSchedule().get(i).getBranch_id())
                                                .build();

                                User userId = User.builder()
                                                .id(scheduleDTO.getListSchedule().get(i).getUser_id())
                                                .build();
                                Schedule schedule = new Schedule(
                                                null,
                                                null,
                                                userId,
                                                branchId,
                                                scheduleDTO.getListSchedule().get(i).getDescription(),
                                                scheduleDTO.getListSchedule().get(i).getStart_date(),
                                                scheduleDTO.getListSchedule().get(i).getEnd_date(),
                                                null,
                                                null,
                                                EStatus.TODO,
                                                ECategory.SPECIAL,
                                                0,
                                                null,
                                                getUser.getId(),
                                                new Date(),
                                                new Date());

                                // change all todo or progress status to pending status
                                repository.editStatusPendingScheduleByDate(
                                                scheduleDTO.getListSchedule().get(i).getUser_id(),
                                                getUser.getId(), scheduleDTO.getListSchedule().get(i).getStart_date(),
                                                scheduleDTO.getListSchedule().get(i).getEnd_date());

                                // Schedule response = repository.save(schedule);
                                Schedule response = repository.save(schedule);
                                if (response == null) {
                                        return GlobalResponse
                                                        .builder()
                                                        .message("Failed")
                                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                                        .build();
                                }

                                logService.save(response.getCreatedBy(), response.getDescription(), response.getId(),
                                                ECategory.SPECIAL, response.getStatus());
                        }

                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .status(HttpStatus.OK)
                                        .build();

                } catch (DataException e) {
                        return GlobalResponse
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }
        }

        public GlobalResponse requestSchedule(RequestReschedule dto, String username) {
                try {
                        User getUser = userRepository.findByUsername(username)
                                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                        Schedule getSchedule = repository.findById(dto.getSchedule_id())
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Schedule with id: " + dto.getSchedule_id() + " is undefined"));

                        User setUser = User.builder().id(dto.getUser_id()).build();
                        Branch setBranch = Branch.builder().id(dto.getBranch_id()).build();

                        ScheduleTrx scheduleTrx = new ScheduleTrx();
                        scheduleTrx.setUser(setUser);
                        scheduleTrx.setBranch(setBranch);
                        scheduleTrx.setStart_date(dto.getStart_date());
                        scheduleTrx.setEnd_date(dto.getEnd_date());
                        scheduleTrx.setStatus(EStatus.TODO);
                        scheduleTrx.setCategory(ECategory.REGULAR);
                        scheduleTrx.setCreatedBy(getUser.getId());
                        scheduleTrx.setCreated_at(new Date());
                        ScheduleTrx response1 = scheduleTrxRepo.save(scheduleTrx);

                        Schedule schedule1 = getSchedule;
                        schedule1.setScheduleTrx(response1);
                        schedule1.setStatus(EStatus.REQUEST);
                        schedule1.setUpdatedBy(getUser.getId());
                        schedule1.setUpdated_at(new Date());
                        Schedule response2 = repository.save(schedule1);

                        logService.save(response2.getUpdatedBy(), response2.getDescription(), response2.getId(),
                                        ECategory.REGULAR, response2.getStatus());

                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        return GlobalResponse
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }
        }

        // public GlobalResponse reschedule(RescheduleDTO dto) {
        // try {
        // Schedule getSchedule = repository.findById(dto.getSchedule_id())
        // .orElseThrow(() -> new ResourceNotFoundException(
        // "Schedule with id: " + dto.getSchedule_id() + " is undefined"));
        // Schedule editSchedule = getSchedule;
        // editSchedule.setStatus(EStatus.DONE);
        // repository.save(editSchedule);

        // User setUser = User.builder().id(dto.getUser_id()).build();
        // Branch setBranch = Branch.builder().id(dto.getBranch_id()).build();

        // Schedule reschedule = new Schedule(
        // null,
        // setUser,
        // setBranch,
        // dto.getDescription(),
        // dto.getStart_date(),
        // dto.getEnd_date(),
        // null,
        // null,
        // EStatus.TODO,
        // ECategory.REGULAR,
        // 0,
        // dto.getCreate_by(),
        // dto.getCreate_by(),
        // new Date(),
        // new Date());

        // repository.save(reschedule);

        // return
        // GlobalResponse.builder().message("Success").status(HttpStatus.OK).build();
        // } catch (Exception e) {
        // return GlobalResponse
        // .builder()
        // .message("Exception :" + e.getMessage())
        // .status(HttpStatus.INTERNAL_SERVER_ERROR)
        // .build();
        // }
        // }

        public GlobalResponse editSchedule(EditScheduleDTO dto, Long id, ECategory category, String username) {
                try {
                        User getUser = userRepository.findByUsername(username)
                                        .orElseThrow(() -> new ResourceNotFoundException("user not found"));
                        Schedule getSchedule = repository.findById(id)
                                        .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));

                        Branch branchId = Branch.builder()
                                        .id(dto.getBranch_id())
                                        .build();

                        User userId = User.builder()
                                        .id(dto.getUser_id())
                                        .build();

                        Schedule schedule = getSchedule;
                        schedule.setUser(userId);
                        schedule.setBranch(branchId);
                        schedule.setDescription(dto.getDescription());
                        schedule.setStart_date(dto.getStart_date());
                        schedule.setEnd_date(dto.getEnd_date());
                        schedule.setStart_date_realization(dto.getStart_date_realization());
                        schedule.setEnd_date_realization(dto.getEnd_date_realization());
                        schedule.setStatus(dto.getStatus());
                        schedule.setUpdatedBy(getUser.getId());
                        schedule.setUpdated_at(new Date());

                        Schedule response = repository.save(schedule);

                        logService.edit(response.getCreatedBy(), response.getDescription(), response.getId(),
                                        ECategory.REGULAR, response.getStatus());

                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        return GlobalResponse
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }
        }

        public GlobalResponse approve(Long id) throws Exception {
                User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Schedule getSchedule = repository.findById(id).orElseThrow(() -> new Exception("Not found"));
                ScheduleTrx getTrx = scheduleTrxRepo.findById(getSchedule.getScheduleTrx().getId())
                                .orElseThrow(() -> new Exception("Not found"));

                Schedule schedule = new Schedule();
                schedule.setUser(getTrx.getUser());
                schedule.setBranch(getTrx.getBranch());
                schedule.setDescription(getTrx.getDescription());
                schedule.setStart_date(getTrx.getStart_date());
                schedule.setStatus(getTrx.getStatus());
                schedule.setCategory(getTrx.getCategory());
                schedule.setEnd_date(getTrx.getEnd_date());
                schedule.setCreatedBy(user.getId());
                schedule.setUpdatedBy(user.getId());
                schedule.setCreated_at(new Date());
                schedule.setUpdated_at(new Date());
                repository.save(schedule);

                Schedule editSchedule = getSchedule;
                editSchedule.setStatus(EStatus.DONE);
                editSchedule.setUpdatedBy(user.getId());
                editSchedule.setUpdated_at(new Date());
                repository.save(editSchedule);

                return GlobalResponse.builder().message("Success").status(HttpStatus.OK).build();

        }

        public GlobalResponse editStatus(Long id, EStatus status, String username) {
                try {
                        User getUser = userRepository.findByUsername(username)
                                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                        Schedule getSchedule = repository.findById(id)
                                        .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));

                        Schedule schedule = getSchedule;
                        schedule.setStatus(status);
                        schedule.setUpdatedBy(getUser.getId());
                        schedule.setUpdated_at(new Date());

                        Schedule response = repository.save(schedule);
                        if (response == null) {
                                return GlobalResponse
                                                .builder()
                                                .message("Failed")
                                                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                                .build();
                        }

                        logService.edit(response.getCreatedBy(), response.getDescription(), response.getId(),
                                        ECategory.REGULAR, response.getStatus());

                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        return GlobalResponse
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }
        }

        public GlobalResponse delete(Long id, String username) {
                try {
                        User getUser = userRepository.findByUsername(username)
                                        .orElseThrow(() -> new ResourceNotFoundException("user not found"));
                        Schedule getSchedule = repository.findById(id)
                                        .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));

                        Schedule schedule = getSchedule;
                        schedule.setIs_delete(1);
                        schedule.setUpdatedBy(getUser.getId());
                        schedule.setUpdated_at(new Date());

                        Schedule response = repository.save(schedule);
                        if (response == null) {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
                        }
                        logService.delete(response.getCreatedBy(), response.getDescription(), response.getId(),
                                        ECategory.REGULAR, response.getStatus());
                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Data error");
                } catch (Exception e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");
                }
        }

}
