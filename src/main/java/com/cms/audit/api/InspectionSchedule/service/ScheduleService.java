package com.cms.audit.api.InspectionSchedule.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
import com.cms.audit.api.AuditDailyReport.models.AuditDailyReportDetail;
import com.cms.audit.api.AuditDailyReport.repository.AuditDailyReportDetailRepository;
import com.cms.audit.api.AuditDailyReport.repository.AuditDailyReportRepository;
import com.cms.audit.api.AuditWorkingPaper.models.AuditWorkingPaper;
import com.cms.audit.api.AuditWorkingPaper.repository.AuditWorkingPaperRepository;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.exception.ResourceNotFoundException;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Flag.model.Flag;
import com.cms.audit.api.Flag.repository.FlagRepo;
import com.cms.audit.api.InspectionSchedule.dto.EditScheduleDTO;
import com.cms.audit.api.InspectionSchedule.dto.RequestReschedule;
import com.cms.audit.api.InspectionSchedule.dto.ScheduleRequest;
import com.cms.audit.api.InspectionSchedule.models.ECategory;
import com.cms.audit.api.InspectionSchedule.models.EStatus;
import com.cms.audit.api.InspectionSchedule.models.Schedule;
import com.cms.audit.api.InspectionSchedule.models.ScheduleTrx;
import com.cms.audit.api.InspectionSchedule.repository.PagSchedule;
import com.cms.audit.api.InspectionSchedule.repository.ScheduleRepository;
import com.cms.audit.api.InspectionSchedule.repository.ScheduleTrxRepo;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.BranchOffice.repository.BranchRepository;
import com.cms.audit.api.Management.User.dto.UserResponseOther;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Management.User.repository.UserRepository;

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
        private FlagRepo flagRepo;

        @Autowired
        private AuditDailyReportRepository auditDailyReportRepository;

        @Autowired
        private AuditDailyReportDetailRepository auditDailyReportDetailRepository;

        @Autowired
        private AuditWorkingPaperRepository auditWorkingPaperRepository;

        @Autowired
        private BranchRepository branchRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private ScheduleTrxRepo scheduleTrxRepo;

        public GlobalResponse getReschedule(String name, Long branch_id, int page, int size, Date start_date,
                        Date end_date, String status) {
                try {
                        Page<Schedule> response = null;
                        if (branch_id != null && name != null && start_date != null && end_date != null) {
                                response = pagSchedule.findOneScheduleByStatusAndFilterAll(status, name, start_date,
                                                end_date, branch_id, PageRequest.of(page, size));
                        } else if (name != null) {
                                if (start_date != null && end_date != null) {
                                        response = pagSchedule.findOneScheduleByStatusAndNameAndDate(status, name,
                                                        start_date, end_date, PageRequest.of(page, size));
                                } else if (branch_id != null) {
                                        response = pagSchedule.findOneScheduleByStatusAndBranchId(name, branch_id,
                                                        PageRequest.of(page, size));
                                } else {
                                        response = pagSchedule.findOneScheduleByStatusAndName(status, name,
                                                        PageRequest.of(page, size));
                                }
                        } else if (branch_id != null) {
                                if (start_date != null && end_date != null) {
                                        response = pagSchedule.findOneScheduleByStatusAndDateAndBranch(status,
                                                        branch_id, start_date, end_date, PageRequest.of(page, size));
                                } else {
                                        response = pagSchedule.findOneScheduleByStatusAndBranchId(status, branch_id,
                                                        PageRequest.of(page, size));
                                }
                        } else if (start_date != null && end_date != null) {
                                response = pagSchedule.findOneScheduleByStatusAndDate(status, start_date, end_date,
                                                PageRequest.of(page, size));
                        } else {
                                response = pagSchedule.findOneScheduleByStatus(status, PageRequest.of(page, size));
                        }
                        if (response.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Data not found")
                                                .data(null)
                                                .status(HttpStatus.OK)
                                                .build();
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .data(mappingPageSchedule(response))
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

        public GlobalResponse get(String name, Long branch_id, int page, int size, Date start_date, Date end_date,
                        String category) {
                try {
                        Page<Schedule> response = null;
                        if (branch_id != null && name != null && start_date != null && end_date != null) {
                                response = pagSchedule.findAllScheduleByAllFilter(name, branch_id, category, start_date,
                                                end_date, PageRequest.of(page, size));
                        } else if (name != null) {
                                if (start_date != null && end_date != null) {
                                        response = pagSchedule.findAllScheduleByFUllenameAndDate(name, category,
                                                        start_date, end_date, PageRequest.of(page, size));
                                } else if (branch_id != null) {
                                        response = pagSchedule.findAllScheduleByFUllenameAndBranch(name, branch_id,
                                                        category, PageRequest.of(page, size));
                                } else {
                                        response = pagSchedule.findAllScheduleByFUllename(name, category,
                                                        PageRequest.of(page, size));
                                }
                        } else if (branch_id != null) {
                                if (start_date != null && end_date != null) {
                                        response = pagSchedule.findAllScheduleByBranchAndDateRange(branch_id, category,
                                                        start_date, end_date, PageRequest.of(page, size));
                                } else {
                                        response = pagSchedule.findAllScheduleByBranchId(branch_id, category,
                                                        PageRequest.of(page, size));
                                }
                        } else if (start_date != null && end_date != null) {
                                response = pagSchedule.findAllScheduleByDateRange(category, start_date, end_date,
                                                PageRequest.of(page, size));
                        } else {
                                response = pagSchedule.findAllSchedule(category, PageRequest.of(page, size));
                        }
                        if (response.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Data not found")
                                                .data(null)
                                                .status(HttpStatus.OK)
                                                .build();
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .data(mappingPageSchedule(response))
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

        public GlobalResponse getScheduleArea(User user, String category, Long branch_id, String name, int page,
                        int size,
                        Date start_date,
                        Date end_date) {
                if (branch_id != null && name != null && start_date != null && end_date != null) {
                        Page<Schedule> response = pagSchedule.findAllScheduleByAllFilter(name,
                                        branch_id,
                                        category, start_date, end_date, PageRequest.of(page, size));
                        return GlobalResponse.builder()
                                        .data(mappingPageSchedule(response))
                                        .message("Success").status(HttpStatus.OK).build();
                } else if (name != null) {
                        List<User> getUser = userRepository.findByFullnameLike(name);
                        if (getUser.isEmpty()) {
                                return GlobalResponse.builder().message("Data not found").status(HttpStatus.OK)
                                                .build();
                        }
                        Pageable pageable = PageRequest.of(page, size);
                        List<Schedule> scheduleList = new ArrayList<>();
                        for (int i = 0; i < getUser.size(); i++) {
                                List<Schedule> getSchedule = repository.findAllScheduleByUserId(
                                                getUser.get(i).getId(), category);
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
                                                .data(mappingPageSchedule(response2))
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
                        return getByBranchIdInDate(branch_id, category, page, size, start_date,
                                        end_date);
                } else {
                        Pageable pageable = PageRequest.of(page, size);
                        List<Schedule> scheduleList = new ArrayList<>();
                        for (int i = 0; i < user.getRegionId().size(); i++) {
                                List<Schedule> scheduleAgain = repository.findByRegionId(user.getRegionId().get(i),
                                                category);
                                if (!scheduleAgain.isEmpty()) {
                                        for (int u = 0; u < scheduleAgain.size(); u++) {
                                                scheduleList.add(scheduleAgain.get(u));
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
                                                .data(mappingPageSchedule(response2))
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
        }

        public GlobalResponse getMainSchedule(Long branch_id, String name, int page, int size, Date start_date,
                        Date end_date) {
                try {
                        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        if (user.getLevel().getId() == 1) {
                                return get(name, branch_id, page, size, start_date, end_date, "REGULAR");
                        } else if (user.getLevel().getId() == 3) {
                                return getByUserId(user.getId(), "REGULAR", page, size, start_date, end_date);
                        } else if (user.getLevel().getId() == 2) {
                                if (branch_id != null && name != null && start_date != null && end_date != null) {
                                        String likeName = name;
                                        Page<Schedule> response = pagSchedule.findAllScheduleByAllFilter(likeName,
                                                        branch_id, "REGULAR", start_date, end_date,
                                                        PageRequest.of(page, size));
                                        return GlobalResponse.builder()
                                                        .data(mappingPageSchedule(response))
                                                        .message("Success").status(HttpStatus.OK).build();
                                } else if (name != null) {
                                        List<User> getUser = userRepository.findByFullnameLike(name);
                                        Pageable pageable = PageRequest.of(page, size);
                                        List<Schedule> scheduleList = new ArrayList<>();
                                        for (int i = 0; i < getUser.size(); i++) {
                                                List<Schedule> getSchedule = new ArrayList<>();
                                                if (start_date != null && end_date != null) {
                                                        getSchedule = repository.findScheduleInDateRangeByUserId(
                                                                        getUser.get(i).getId(), "REGULAR", start_date,
                                                                        end_date);
                                                } else if (branch_id != null) {
                                                        getSchedule = repository.findAllScheduleByFUllenameAndBranch(
                                                                        name, branch_id, "REGULAR");
                                                } else {
                                                        getSchedule = repository.findAllScheduleByUserId(
                                                                        getUser.get(i).getId(), "REGULAR");
                                                }
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
                                                                .data(mappingPageSchedule(response2))
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
                                        return getByBranchIdInDate(branch_id, "REGULAR", page, size, start_date,
                                                        end_date);
                                } else if (start_date != null && end_date != null) {
                                        Page<Schedule> response = pagSchedule.findAllScheduleByAllFilter(name,
                                                        branch_id,
                                                        "REGULAR", start_date, end_date, PageRequest.of(page, size));
                                        return GlobalResponse.builder()
                                                        .data(mappingPageSchedule(response))
                                                        .message("Success").status(HttpStatus.OK).build();
                                } else {
                                        return getScheduleArea(user, "REGULAR", branch_id, name, page, size, start_date,
                                                        end_date);
                                }
                        } else {
                                return GlobalResponse
                                                .builder()
                                                .message("Success")
                                                .data(null)
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

        public GlobalResponse getSpecialSchedule(Long branch_id, String name, int page, int size, Date start_date,
                        Date end_date) {
                try {
                        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                        if (user.getLevel().getId() == 1) {
                                return get(name, branch_id, page, size, start_date, end_date, "SPECIAL");
                        } else if (user.getLevel().getId() == 3) {
                                return getByUserId(user.getId(), "SPECIAL", page, size, start_date, end_date);
                        } else if (user.getLevel().getId() == 2) {
                                if (branch_id != null && name != null && start_date != null && end_date != null) {
                                        return GlobalResponse.builder()
                                                        .data(mappingPageSchedule(pagSchedule
                                                                        .findAllScheduleByAllFilter(name, branch_id,
                                                                                        "SPECIAL", start_date, end_date,
                                                                                        PageRequest.of(page, size))))
                                                        .message("Success").status(HttpStatus.OK).build();
                                } else if (name != null) {
                                        List<User> getUser = userRepository.findByFullnameLike(name);
                                        Pageable pageable = PageRequest.of(page, size);
                                        List<Schedule> scheduleList = new ArrayList<>();
                                        for (int i = 0; i < getUser.size(); i++) {
                                                List<Schedule> getSchedule = new ArrayList<>();
                                                if (start_date != null && end_date != null) {
                                                        getSchedule = repository.findScheduleInDateRangeByUserId(
                                                                        getUser.get(i).getId(), "SPECIAL", start_date,
                                                                        end_date);
                                                } else if (branch_id != null) {
                                                        getSchedule = repository.findAllScheduleByFUllenameAndBranch(
                                                                        name, branch_id, "SPECIAL");
                                                } else {
                                                        getSchedule = repository.findAllScheduleByUserId(
                                                                        getUser.get(i).getId(), "SPECIAL");
                                                }
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
                                                                .data(mappingPageSchedule(response2))
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
                                        return getByBranchIdInDate(branch_id, "SPECIAL", page, size, start_date,
                                                        end_date);
                                } else {
                                        return getScheduleArea(user, "SPECIAL", branch_id, name, page, size, start_date,
                                                        end_date);
                                }
                        } else {
                                return GlobalResponse
                                                .builder()
                                                .message("Success")
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
                                response = pagSchedule.findAllScheduleByBranchAndDateRange(id, category, start_date,
                                                end_date,
                                                PageRequest.of(page, size));
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .data(mappingPageSchedule(response))
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

        public List<Schedule> getByRegionId(String name, Long branchId, Long regionId, String category, int page,
                        int size, Date start_date,
                        Date end_date) {
                List<Schedule> response;
                if (start_date != null || end_date != null) {
                        response = repository.findByRegionId(regionId, category);
                } else {
                        response = repository.findScheduleInDateRangeByRegionId(regionId, category, start_date,
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
                                                .message("Data schedule with id :" + id + " is undefined")
                                                .status(HttpStatus.OK)
                                                .build();
                        }
                        Map<String, Object> response = new LinkedHashMap<>();
                        response.put("schedule", mappingSchedule(getSchedule.get()));

                        List<AuditDailyReport> getLha = auditDailyReportRepository.findByScheduleId(id);
                        List<Object> listLha = new ArrayList<>();
                        if (!getLha.isEmpty()) {
                                for (int i = 0; i < getLha.size(); i++) {
                                        Map<String, Object> lha = new LinkedHashMap<>();
                                        lha.put("id", getLha.get(i).getId());

                                        Map<String, Object> lhaBranch = new LinkedHashMap<>();
                                        lhaBranch.put("id", getLha.get(i).getBranch().getId());
                                        lhaBranch.put("name", getLha.get(i).getBranch().getName());
                                        lha.put("branch", lhaBranch);

                                        List<AuditDailyReportDetail> detail = auditDailyReportDetailRepository
                                                        .findByLHAId(getLha.get(i).getId());
                                        Integer flag = 0;
                                        for (int u = 0; u < detail.size(); u++) {
                                                if (getLha.get(i).getIs_research() != 1) {
                                                        if (detail.get(u).getIs_research() == 1) {
                                                                Flag isFlag = flagRepo
                                                                                .findOneByAuditDailyReportDetailId(
                                                                                                detail.get(u).getId())
                                                                                .orElse(null);
                                                                if (isFlag != null) {
                                                                        if (isFlag.getClarification()
                                                                                        .getFilename() == null) {
                                                                                flag = 1;
                                                                        } else {
                                                                                flag = 0;
                                                                        }
                                                                } else {
                                                                        flag = 0;
                                                                }
                                                        }
                                                }
                                        }
                                        lha.put("is_flag", flag);

                                        listLha.add(lha);
                                }
                                response.put("lha", listLha);
                        } else {
                                response.put("lha", null);
                        }

                        Optional<AuditWorkingPaper> getKka = auditWorkingPaperRepository.findByScheduleId(id);
                        Map<String, Object> kka = new LinkedHashMap<>();
                        if (getKka.isPresent()) {
                                kka.put("id", getKka.get().getId());
                                kka.put("filename", getKka.get().getFilename());
                                kka.put("file_path", getKka.get().getFile_path());
                                response.put("kka", kka);
                        } else {
                                response.put("kka", null);
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

        public GlobalResponse getByStatus(String name, Long branchId, Date startDate, Date endDate, int page,
                        int size) {
                User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                if (getUser.getLevel().getCode().equals("A")  || getUser.getLevel().getCode().equals("A") ) {
                        return getReschedule(name, branchId, page, size, startDate, endDate, "REQUEST");
                        // response = pagSchedule.findOneScheduleByStatus("PENDING",
                        // PageRequest.of(page, size));
                } else if (getUser.getLevel().getCode().equals("B") ) {
                        return getReschedule(name, branchId, page, size, startDate, endDate, "PENDING");
                } else {
                        return GlobalResponse.builder().message("Audit wilayah tidak dapat mengakses").data(null)
                                        .status(HttpStatus.OK).build();
                }

        }

        public List<Object> mappingListSchedule(List<Schedule> response) {
                List<Object> listSchedule = new ArrayList<>();
                for (int i = 0; i < response.size(); i++) {
                        Map<String, Object> mapParent = new LinkedHashMap<>();
                        mapParent.put("id", response.get(i).getId());

                        UserResponseOther setUser = new UserResponseOther();
                        setUser.setId(response.get(i).getUser().getId());
                        setUser.setEmail(response.get(i).getUser().getEmail());
                        setUser.setFullname(response.get(i).getUser().getFullname());
                        setUser.setInitial_name(response.get(i).getUser().getInitial_name());
                        setUser.setNip(response.get(i).getUser().getNip());
                        mapParent.put("user", setUser);

                        Map<String, Object> mapBranch = new LinkedHashMap<>();
                        mapBranch.put("id", response.get(i).getBranch().getId());
                        mapBranch.put("name", response.get(i).getBranch().getName());
                        mapParent.put("branch", mapBranch);

                        mapParent.put("description", response.get(i).getDescription());
                        mapParent.put("status", response.get(i).getStatus());
                        mapParent.put("category", response.get(i).getCategory());
                        if (response.get(i).getStart_date() == null) {
                                mapParent.put("start_date", null);
                        } else {
                                mapParent.put("start_date", convertDateToRoman
                                                .convertDateHehe(response.get(i).getStart_date()));
                        }
                        if (response.get(i).getEnd_date() == null) {
                                mapParent.put("end_date", null);
                        } else {
                                mapParent.put("end_date", convertDateToRoman
                                                .convertDateHehe(response.get(i).getEnd_date()));
                        }
                        if (response.get(i).getStart_date_realization() == null) {
                                mapParent.put("start_date_realization", null);
                        } else {
                                mapParent.put("start_date_realization",
                                                convertDateToRoman.convertDateHehe(response.get(i)
                                                                .getStart_date_realization()));
                        }
                        if (response.get(i).getEnd_date_realization() == null) {
                                mapParent.put("end_date_realization", null);
                        } else {
                                mapParent.put("end_date_realization", convertDateToRoman.convertDateHehe(
                                                response.get(i).getEnd_date_realization()));
                        }

                        Optional<AuditWorkingPaper> getKka = auditWorkingPaperRepository
                                        .findByScheduleId(response.get(i).getId());
                        Map<String, Object> kka = new LinkedHashMap<>();
                        if (getKka.isPresent()) {
                                kka.put("id", getKka.get().getId());
                                kka.put("filename", getKka.get().getFilename());
                                kka.put("file_path", getKka.get().getFile_path());
                                mapParent.put("kka", kka);
                        } else {
                                mapParent.put("kka", null);
                        }

                        listSchedule.add(mapParent);
                }
                return listSchedule;
        }

        public Map<String, Object> mappingSchedule(Schedule response) {
                Map<String, Object> map = new LinkedHashMap<>();
                UserResponseOther setUser = new UserResponseOther();

                map.put("id", response.getId());

                setUser.setId(response.getUser().getId());
                setUser.setEmail(response.getUser().getEmail());
                setUser.setFullname(response.getUser().getFullname());
                setUser.setInitial_name(response.getUser().getInitial_name());
                setUser.setNip(response.getUser().getNip());
                map.put("user", setUser);

                Map<String, Object> mapBranch = new LinkedHashMap<>();
                mapBranch.put("id", response.getBranch().getId());
                mapBranch.put("name", response.getBranch().getName());
                map.put("branch", mapBranch);

                map.put("description", response.getDescription());
                map.put("status", response.getStatus());
                map.put("category", response.getCategory());
                if (response.getStart_date() == null) {
                        map.put("start_date", "-");
                } else {
                        map.put("start_date", convertDateToRoman
                                        .convertDateHehe(response.getStart_date()));
                }
                if (response.getEnd_date() == null) {
                        map.put("end_date", "-");
                } else {
                        map.put("end_date", convertDateToRoman
                                        .convertDateHehe(response.getEnd_date()));
                }
                if (response.getStart_date_realization() == null) {
                        map.put("start_date_realization", "-");
                } else {
                        map.put("start_date_realization",
                                        convertDateToRoman.convertDateHehe(response.getStart_date_realization()));
                }
                if (response.getEnd_date_realization() == null) {
                        map.put("end_date_realization", "-");
                } else {
                        map.put("end_date_realization",
                                        convertDateToRoman.convertDateHehe(response.getEnd_date_realization()));
                }

                return map;
        }

        public Map<String, Object> mappingPageSchedule(Page<Schedule> response) {
                Map<String, Object> parentMap = new LinkedHashMap<>();
                List<Object> listSchedule = new ArrayList<>();
                for (int i = 0; i < response.getContent().size(); i++) {
                        Map<String, Object> map = new LinkedHashMap<>();
                        map.put("id", response.getContent().get(i).getId());

                        UserResponseOther setUser = new UserResponseOther();
                        setUser.setId(response.getContent().get(i).getUser().getId());
                        setUser.setEmail(response.getContent().get(i).getUser().getEmail());
                        setUser.setFullname(response.getContent().get(i).getUser().getFullname());
                        setUser.setInitial_name(response.getContent().get(i).getUser().getInitial_name());
                        setUser.setNip(response.getContent().get(i).getUser().getNip());
                        map.put("user", setUser);

                        Map<String, Object> mapBranch = new LinkedHashMap<>();
                        mapBranch.put("id", response.getContent().get(i).getBranch().getId());
                        mapBranch.put("name", response.getContent().get(i).getBranch().getName());
                        map.put("branch", mapBranch);

                        map.put("description", response.getContent().get(i).getDescription());
                        map.put("status", response.getContent().get(i).getStatus());
                        map.put("category", response.getContent().get(i).getCategory());
                        if (response.getContent().get(i).getStart_date() == null) {
                                map.put("start_date", null);
                        } else {
                                map.put("start_date", convertDateToRoman
                                                .convertDateHehe(response.getContent().get(i).getStart_date()));
                        }
                        if (response.getContent().get(i).getEnd_date() == null) {
                                map.put("end_date", null);
                        } else {
                                map.put("end_date", convertDateToRoman
                                                .convertDateHehe(response.getContent().get(i).getEnd_date()));
                        }
                        if (response.getContent().get(i).getStart_date_realization() == null) {
                                map.put("start_date_realization", null);
                        } else {
                                map.put("start_date_realization",
                                                convertDateToRoman.convertDateHehe(response.getContent().get(i)
                                                                .getStart_date_realization()));
                        }
                        if (response.getContent().get(i).getEnd_date_realization() == null) {
                                map.put("end_date_realization", null);
                        } else {
                                map.put("end_date_realization", convertDateToRoman.convertDateHehe(
                                                response.getContent().get(i).getEnd_date_realization()));
                        }

                        Optional<AuditWorkingPaper> getKka = auditWorkingPaperRepository
                                        .findByScheduleId(response.getContent().get(i).getId());
                        Map<String, Object> kka = new LinkedHashMap<>();
                        if (getKka.isPresent()) {
                                kka.put("id", getKka.get().getId());
                                kka.put("filename", getKka.get().getFilename());
                                kka.put("file_path", getKka.get().getFile_path());
                                map.put("kka", kka);
                        } else {
                                map.put("kka", null);
                        }

                        List<Schedule> scheduleList = repository.findForScheduleList(
                                        response.getContent().get(i).getUser().getId(),
                                        response.getContent().get(i).getStart_date(), "REGULAR");
                        if (scheduleList.isEmpty()) {
                                map.put("is_active", 1);
                        } else {
                                map.put("is_active", 0);
                        }

                        listSchedule.add(map);
                }
                parentMap.put("pageable", response.getPageable());
                parentMap.put("totalPage", response.getTotalPages());
                parentMap.put("totalElement", response.getTotalElements());
                parentMap.put("size", response.getSize());
                parentMap.put("number", response.getNumber());
                parentMap.put("last", response.isLast());
                parentMap.put("first", response.isFirst());
                parentMap.put("numberOfElement", response.getNumberOfElements());
                parentMap.put("empty", response.isEmpty());
                parentMap.put("sort", response.getSort());
                parentMap.put("content", listSchedule);
                return parentMap;
        }

        public GlobalResponse getByUserId(Long id, String category, int page, int size, Date start_date,
                        Date end_date) {
                try {
                        Page<Schedule> response = null;
                        if (start_date == null && end_date == null) {
                                response = pagSchedule.findAllScheduleByUserId(id, category,
                                                PageRequest.of(page, size));
                        } else {
                                response = pagSchedule.findScheduleInDateRangeByUserId(id, category, start_date,
                                                end_date, PageRequest.of(page, size));
                        }
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
                                        .data(mappingPageSchedule(response))
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
                                                .message("Data not found")
                                                .status(HttpStatus.OK)
                                                .build();
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Success")
                                        .data(mappingPageSchedule(response))
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

        public Date getDateNow() {
                Date today = new Date();
                Calendar todayCal = Calendar.getInstance();
                todayCal.setTime(today);
                todayCal.set(Calendar.HOUR_OF_DAY, 0);
                todayCal.set(Calendar.MINUTE, 0);
                todayCal.set(Calendar.SECOND, 0);
                todayCal.set(Calendar.MILLISECOND, 0);
                Date todayStart = todayCal.getTime();
                return todayStart;
        }

        @Transactional(value = TxType.REQUIRES_NEW)
        public GlobalResponse insertRegularSchedule(ScheduleRequest scheduleDTO, String username) {
                try {
                        User getUser = userRepository.findByUsername(username)
                                        .orElseThrow(() -> new ResourceNotFoundException("user not found"));
                        Map<String, Object> err = new HashMap<>();

                        for (int i = 0; i < scheduleDTO.getSchedules().size(); i++) {
                                if (scheduleDTO.getSchedules().get(i).getStart_date().before(getDateNow())
                                                || scheduleDTO.getSchedules().get(i).getEnd_date()
                                                                .before(getDateNow())) {
                                        return GlobalResponse
                                                        .builder()
                                                        .message("failed")
                                                        .errorMessage("Tidak bisa menambahkan jadwal kemarin")
                                                        .data(err.put("error", "Invalid start or end date"))
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }

                                List<Schedule> checkIfExist = repository.findScheduleInAllCheck(
                                                scheduleDTO.getSchedules().get(i).getUser_id(),
                                                scheduleDTO.getSchedules().get(i).getBranch_id(),
                                                "REGULAR",
                                                scheduleDTO.getSchedules().get(i).getStart_date(),
                                                scheduleDTO.getSchedules().get(i).getEnd_date());
                                if (!checkIfExist.isEmpty()) {
                                        return GlobalResponse
                                                        .builder()
                                                        .message("failed")
                                                        .errorMessage("Tanggal duplikat")
                                                        .data(err.put("error", "Conflict date exists"))
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }

                                if (scheduleDTO.getSchedules().get(i).getStart_date()
                                                .after(scheduleDTO.getSchedules().get(i).getEnd_date())) {
                                        return GlobalResponse
                                                        .builder()
                                                        .message("failed")
                                                        .errorMessage("Start date lebih besar dari end date")
                                                        .data(err.put("error", "Invalid time range"))
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }

                                List<Schedule> checkDatefExist = repository.findScheduleInDateRangeByUserId(
                                                scheduleDTO.getSchedules().get(i).getUser_id(), "REGULAR",
                                                scheduleDTO.getSchedules().get(i).getStart_date(),
                                                scheduleDTO.getSchedules().get(i).getEnd_date());
                                if (!checkDatefExist.isEmpty()) {
                                        return GlobalResponse
                                                        .builder()
                                                        .message("failed")
                                                        .errorMessage("Tanggal dengan start_date:"
                                                                        + scheduleDTO.getSchedules().get(i)
                                                                                        .getStart_date()
                                                                        + " and end_date:"
                                                                        + scheduleDTO.getSchedules().get(i)
                                                                                        .getEnd_date()
                                                                        + ", sudah terbuat sebelumnya")
                                                        .data(err.put("error", "Conflict date range already exist"))
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }
                        }

                        for (int i = 0; i < scheduleDTO.getSchedules().size(); i++) {
                                Optional<Branch> branchId = branchRepository
                                                .findById(scheduleDTO.getSchedules().get(i).getBranch_id());
                                if (!branchId.isPresent()) {
                                        return GlobalResponse.builder()
                                                        .message("failed")
                                                        .errorMessage("Branch with id: "
                                                                        + scheduleDTO.getSchedules().get(i)
                                                                                        .getBranch_id()
                                                                        + " is undefined")
                                                        .data(err.put("error", "Undefined Branch Id"))
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }

                                Optional<User> userId = userRepository
                                                .findById(scheduleDTO.getSchedules().get(i).getUser_id());
                                if (!userId.isPresent()) {
                                        return GlobalResponse.builder()
                                                        .message("failed")
                                                        .errorMessage("User with id: "
                                                                        + scheduleDTO.getSchedules().get(i).getUser_id()
                                                                        + " is undefined")
                                                        .data(err.put("error", "Undefined User Id"))
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }
                                Schedule schedule = new Schedule(
                                                null,
                                                null,
                                                userId.get(),
                                                branchId.get(),
                                                scheduleDTO.getSchedules().get(i).getDescription(),
                                                scheduleDTO.getSchedules().get(i).getStart_date(),
                                                scheduleDTO.getSchedules().get(i).getEnd_date(),
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

                        Map<String, Object> err = new HashMap<>();
                        for (int i = 0; i < scheduleDTO.getSchedules().size(); i++) {
                                if (scheduleDTO.getSchedules().get(i).getStart_date().before(getDateNow())
                                                || scheduleDTO.getSchedules().get(i).getEnd_date()
                                                                .before(getDateNow())) {
                                        return GlobalResponse
                                                        .builder()
                                                        .message("failed")
                                                        .errorMessage("Tidak bisa menambahkan jadwal kemarin")
                                                        .data(err.put("error", "Date is yesterday"))
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }

                                List<Schedule> checkIfExist = repository.findScheduleInAllCheck(
                                                scheduleDTO.getSchedules().get(i).getUser_id(),
                                                scheduleDTO.getSchedules().get(i).getBranch_id(),
                                                "SPECIAL",
                                                scheduleDTO.getSchedules().get(i).getStart_date(),
                                                scheduleDTO.getSchedules().get(i).getEnd_date());
                                if (!checkIfExist.isEmpty()) {
                                        return GlobalResponse
                                                        .builder()
                                                        .message("failed")
                                                        .errorMessage("Tanggal duplikat")
                                                        .data(err.put("error", "Repeated date"))
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }

                                if (scheduleDTO.getSchedules().get(i).getStart_date()
                                                .after(scheduleDTO.getSchedules().get(i).getEnd_date())) {
                                        return GlobalResponse
                                                        .builder()
                                                        .message("failed")
                                                        .errorMessage("Start date lebih besar dari end date")
                                                        .data(err.put("error", "Invalid start and end date"))
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }
                        }
                        for (int i = 0; i < scheduleDTO.getSchedules().size(); i++) {
                                Optional<Branch> branchId = branchRepository
                                                .findById(scheduleDTO.getSchedules().get(i).getBranch_id());
                                if (!branchId.isPresent()) {
                                        return GlobalResponse.builder()
                                                        .message("failed")
                                                        .errorMessage("Branch with id: "
                                                                        + scheduleDTO.getSchedules().get(i)
                                                                                        .getBranch_id()
                                                                        + " is undefined")
                                                        .data(err.put("error", "Undefined Branch Id"))
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }

                                Optional<User> userId = userRepository
                                                .findById(scheduleDTO.getSchedules().get(i).getUser_id());
                                if (!userId.isPresent()) {
                                        return GlobalResponse.builder()
                                                        .message("failed")
                                                        .errorMessage("User with id: "
                                                                        + scheduleDTO.getSchedules().get(i).getUser_id()
                                                                        + " is undefined")
                                                        .data(err.put("error", "Undefined User Id"))
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }
                                Schedule schedule = new Schedule(
                                                null,
                                                null,
                                                userId.get(),
                                                branchId.get(),
                                                scheduleDTO.getSchedules().get(i).getDescription(),
                                                scheduleDTO.getSchedules().get(i).getStart_date(),
                                                scheduleDTO.getSchedules().get(i).getEnd_date(),
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
                                                scheduleDTO.getSchedules().get(i).getUser_id(),
                                                getUser.getId(), scheduleDTO.getSchedules().get(i).getStart_date(),
                                                scheduleDTO.getSchedules().get(i).getEnd_date());
                                Schedule response = repository.save(schedule);

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
                        Map<String, Object> err = new HashMap<>();

                        if (dto.getStart_date().before(getDateNow())
                                        || dto.getEnd_date()
                                                        .before(getDateNow())) {
                                return GlobalResponse
                                                .builder()
                                                .message("failed")
                                                .errorMessage("Tidak bisa menambahkan jadwal kemarin")
                                                .data(err.put("error", "Date is yesterday"))
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }

                        List<Schedule> checkIfExist = repository.findScheduleInAllCheck(
                                        dto.getUser_id(),
                                        dto.getBranch_id(),
                                        "REGULAR",
                                        dto.getStart_date(),
                                        dto.getEnd_date());
                        if (!checkIfExist.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("failed")
                                                .errorMessage("Jadwal duplikat")
                                                .data(err.put("error", "Duplicate Schedule"))
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }
                        if (dto.getStart_date().after(dto.getEnd_date())) {
                                return GlobalResponse
                                                .builder()
                                                .message("failed")
                                                .errorMessage("Start date lebih besar dari end date")
                                                .data(err.put("error", "Invalid Date Range"))
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }

                        List<Schedule> checkDatefExist = repository.findScheduleInDateRangeByUserId(
                                        dto.getUser_id(), "REGULAR",
                                        dto.getStart_date(),
                                        dto.getEnd_date());
                        if (!checkDatefExist.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Failed")
                                                .errorMessage("Tanggal dengan start_date:"
                                                                + dto.getStart_date()
                                                                + " and end_date:"
                                                                + dto.getEnd_date()
                                                                + ", sudah terbuat sebelumnya")
                                                .data(err.put("error", "Conflict Date Range"))
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }
                        User getUser = userRepository.findByUsername(username)
                                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                        Schedule getSchedule = repository.findById(dto.getSchedule_id())
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Schedule with id: " + dto.getSchedule_id() + " is undefined"));

                        Optional<Branch> branchId = branchRepository
                                        .findById(dto.getBranch_id());
                        if (!branchId.isPresent()) {
                                return GlobalResponse.builder()
                                                .errorMessage("Branch with id: "
                                                                + dto
                                                                                .getBranch_id()
                                                                + " is undefined")
                                                .data(err.put("error", "Undefined Branch Id"))
                                                .message("Failed")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }

                        Optional<User> userId = userRepository
                                        .findById(dto.getUser_id());
                        if (!userId.isPresent()) {
                                return GlobalResponse.builder()
                                                .message("User with id: "
                                                                + dto.getUser_id()
                                                                + " is undefined")
                                                .data(err.put("error", "Undefined User Id"))
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }

                        ScheduleTrx scheduleTrx = new ScheduleTrx();
                        scheduleTrx.setUser(userId.get());
                        scheduleTrx.setBranch(branchId.get());
                        scheduleTrx.setDescription(dto.getDescription());
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
                                        response2.getCategory(), response2.getStatus());

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
                        schedule.setUpdatedBy(getUser.getId());
                        schedule.setUpdated_at(new Date());

                        Schedule response = repository.save(schedule);

                        logService.edit(response.getCreatedBy(), response.getDescription(), response.getId(),
                                        response.getCategory(), response.getStatus());

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
                if (user.getLevel().getName() == "LEAD" ) {
                        return GlobalResponse.builder().message("Selain audit leader tidak dapat akses")
                                        .status(HttpStatus.UNAUTHORIZED).build();
                }
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
                editSchedule.setStatus(EStatus.APPROVE);
                editSchedule.setUpdatedBy(user.getId());
                editSchedule.setUpdated_at(new Date());
                repository.save(editSchedule);

                return GlobalResponse.builder().message("Success").status(HttpStatus.OK).build();

        }

        public GlobalResponse editStatus(Long id, EStatus status, String username) {
                try {
                        User getUser = userRepository.findByUsername(username)
                                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                        if (getUser.getLevel().getCode().equals("A") ) {
                                return GlobalResponse.builder().message("Selain audit leader tidak dapat akses")
                                                .status(HttpStatus.UNAUTHORIZED).build();
                        }

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
                                        response.getCategory(), response.getStatus());

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

        public GlobalResponse delete(Long id) {
                try {
                        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                        Optional<Schedule> getSchedule = repository.findById(id);
                        if (!getSchedule.isPresent()) {
                                return GlobalResponse.builder().message(
                                                "Shcedule with id :" + getSchedule.get().getId() + " is not found")
                                                .build();
                        }
                        Schedule schedule = getSchedule.get();
                        schedule.setIs_delete(1);
                        schedule.setUpdatedBy(getUser.getId());
                        schedule.setUpdated_at(new Date());

                        Schedule response = repository.save(schedule);
                        if (response == null) {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
                        }
                        logService.delete(response.getCreatedBy(), response.getDescription(), response.getId(),
                                        response.getCategory(), response.getStatus());
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
