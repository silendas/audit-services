package com.cms.audit.api.InspectionSchedule.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cms.audit.api.InspectionSchedule.dto.EditScheduleDTO;
import com.cms.audit.api.InspectionSchedule.dto.RescheduleDTO;
import com.cms.audit.api.InspectionSchedule.dto.ScheduleDTO;
import com.cms.audit.api.InspectionSchedule.models.ECategory;
import com.cms.audit.api.InspectionSchedule.models.EStatus;
import com.cms.audit.api.InspectionSchedule.models.Schedule;
import com.cms.audit.api.InspectionSchedule.repository.PagSchedule;
import com.cms.audit.api.InspectionSchedule.repository.ScheduleRepository;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;
import com.cms.audit.api.Management.Office.RegionOffice.repository.RegionRepository;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Management.User.repository.UserRepository;
import com.cms.audit.api.common.response.GlobalResponse;

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
        private UserRepository userRepository;

        @Autowired
        private RegionRepository regionRepository;

        public GlobalResponse get(String name, int page, int size) {
                try {
                        Page<Schedule> response = pagSchedule.findAll(PageRequest.of(page, size));
                        if (response.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Not Content")
                                                .status(HttpStatus.NO_CONTENT)
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

        public GlobalResponse getMainSchedule(int page, int size) {
                try {
                        Page<Schedule> response = pagSchedule.findByCategoryInByOrderByIdDesc("REGULAR", PageRequest.of(page, size));
                        if (response.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Not Content")
                                                .status(HttpStatus.NO_CONTENT)
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

        public GlobalResponse getSpecialSchedule(int page, int size) {
                try {
                        Page<Schedule> response = pagSchedule.findByCategoryInByOrderByIdDesc("SPECIAL", PageRequest.of(page, size));
                        if (response.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Not Content")
                                                .status(HttpStatus.NO_CONTENT)
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

        public GlobalResponse getByRegionId(Long id, String category, int page, int size) {
                try {
                        Page<Schedule> response = pagSchedule.findByRegionId(id, category,PageRequest.of(page, size));
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

        public GlobalResponse getById(Long id) {
                try {
                        Optional<Schedule> response = repository.findOneScheduleById(id);
                        if (!response.isPresent()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Not Content")
                                                .status(HttpStatus.NO_CONTENT)
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

        public GlobalResponse getByStatus(String status,int page, int size) {
                try {
                        Page<Schedule> response = pagSchedule.findOneScheduleByStatus(status,PageRequest.of(page, size));
                        if (response.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Not Content")
                                                .status(HttpStatus.NO_CONTENT)
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

        public GlobalResponse getByUserId(Long id,String category,int page, int size) {
                try {
                        Page<Schedule> response = pagSchedule.findAllScheduleByUserId(id,category, PageRequest.of(page, size));
                        if (response.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Not Content")
                                                .status(HttpStatus.NO_CONTENT)
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

        public GlobalResponse getByRangeDateAndUserId(Long id, String category, Date start_date, Date end_date,int page, int size) {
                try {
                        Page<Schedule> response = pagSchedule.findScheduleInDateRangeByUserId(id,
                                        category,
                                        start_date, end_date, PageRequest.of(page, size));
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
        public GlobalResponse insertRegularSchedule(ScheduleDTO scheduleDTO) {
                try {

                        Branch branchId = Branch.builder()
                                        .id(scheduleDTO.getBranch_id())
                                        .build();

                        User userId = User.builder()
                                        .id(scheduleDTO.getUser_id())
                                        .build();
                        Schedule schedule = new Schedule(
                                        null,
                                        userId,
                                        branchId,
                                        scheduleDTO.getDescription(),
                                        scheduleDTO.getStart_date(),
                                        scheduleDTO.getEnd_date(),
                                        null,
                                        null,
                                        EStatus.TODO,
                                        ECategory.REGULAR,
                                        0,
                                        null,
                                        scheduleDTO.getCreated_by(),
                                        new Date(),
                                        new Date());

                        // check if schedule already exist?
                        List<Schedule> checkIfExist = repository.findScheduleInDateRangeByUserId(
                                        scheduleDTO.getUser_id(), "REGULAR", scheduleDTO.getStart_date(),
                                        scheduleDTO.getEnd_date());
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

                        logService.save(response.getCreatedBy(), response.getDescription(), response.getId(), ECategory.REGULAR, response.getStatus());

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
        public GlobalResponse insertSpecialSchedule(ScheduleDTO scheduleDTO) {
                try {
                        Branch branchId = Branch.builder()
                                        .id(scheduleDTO.getBranch_id())
                                        .build();

                        User userId = User.builder()
                                        .id(scheduleDTO.getUser_id())
                                        .build();
                        Schedule schedule = new Schedule(
                                        null,
                                        userId,
                                        branchId,
                                        scheduleDTO.getDescription(),
                                        scheduleDTO.getStart_date(),
                                        scheduleDTO.getEnd_date(),
                                        null,
                                        null,
                                        EStatus.TODO,
                                        ECategory.SPECIAL,
                                        0,
                                        null,
                                        scheduleDTO.getCreated_by(),
                                        new Date(),
                                        new Date());

                        // change all todo or progress status to pending status
                        repository.editStatusPendingScheduleByDate(scheduleDTO.getUser_id(), scheduleDTO.getCreated_by(),scheduleDTO.getStart_date(), scheduleDTO.getEnd_date());

                        // Schedule response = repository.save(schedule);
                        Schedule response = repository.save(schedule);
                        if (response == null) {
                                return GlobalResponse
                                                .builder()
                                                .message("Failed")
                                                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                                .build();
                        }

                        logService.save(response.getCreatedBy(), response.getDescription(), response.getId(), ECategory.SPECIAL, response.getStatus());

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

        public GlobalResponse reSchedule(RescheduleDTO dto) {
                try {
                        Branch branchId = Branch.builder()
                                        .id(dto.getBranch_id())
                                        .build();

                        User userId = User.builder()
                                        .id(dto.getUser_id())
                                        .build();

                        Schedule schedule1 = new Schedule(
                                        null,
                                        userId,
                                        branchId,
                                        dto.getDescription(),
                                        dto.getStart_date(),
                                        dto.getEnd_date(),
                                        null,
                                        null,
                                        EStatus.NA,
                                        ECategory.REGULAR,
                                        0,
                                        dto.getCreated_by(),
                                        dto.getCreated_by(),
                                        new Date(),
                                        new Date());

                        List<Schedule> checkIfExist = repository.findScheduleInDateRangeByUserId(
                                        dto.getUser_id(), "REGULAR", dto.getStart_date(),
                                        dto.getEnd_date());
                        if (!checkIfExist.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Start and end date is already exist")
                                                .status(HttpStatus.FOUND)
                                                .build();
                        }

                        Schedule response1 = repository.save(schedule1);

                        logService.save(response1.getCreatedBy(), response1.getDescription(), response1.getId(), ECategory.REGULAR, response1.getStatus());

                        Optional<Schedule> getBefore = repository.findById(dto.getSchedule_id());

                        Schedule schedule2 = new Schedule(
                                        getBefore.get().getId(),
                                        userId,
                                        branchId,
                                        getBefore.get().getDescription(),
                                        getBefore.get().getStart_date(),
                                        getBefore.get().getEnd_date(),
                                        getBefore.get().getStart_date_realization(),
                                        getBefore.get().getEnd_date_realization(),
                                        EStatus.DONE,
                                        ECategory.REGULAR,
                                        0,
                                        getBefore.get().getCreatedBy(),
                                        dto.getCreated_by(),
                                        getBefore.get().getCreated_at(),
                                        new Date());

                        Schedule response2 = repository.save(schedule2);

                        logService.save(response2.getCreatedBy(), response2.getDescription(), response2.getId(), ECategory.REGULAR, response2.getStatus());

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

        public GlobalResponse editSchedule(EditScheduleDTO editScheduleDTO, Long id, ECategory category) {
                try {
                        Optional<Schedule> getSChedule = repository.findById(id);

                        Branch branchId = Branch.builder()
                                        .id(editScheduleDTO.getBranch_id())
                                        .build();

                        User userId = User.builder()
                                        .id(editScheduleDTO.getUser_id())
                                        .build();

                        Schedule schedule = new Schedule(
                                        id,
                                        userId,
                                        branchId,
                                        editScheduleDTO.getDescription(),
                                        editScheduleDTO.getStart_date(),
                                        editScheduleDTO.getEnd_date(),
                                        editScheduleDTO.getStart_date_realization(),
                                        editScheduleDTO.getEnd_date_realization(),
                                        editScheduleDTO.getStatus(),
                                        category,
                                        0,
                                        editScheduleDTO.getUpdate_by(),
                                        getSChedule.get().getCreatedBy(),
                                        getSChedule.get().getCreated_at(),
                                        new Date());

                        Schedule response = repository.save(schedule);

                        logService.edit(response.getCreatedBy(), response.getDescription(), response.getId(), ECategory.REGULAR, response.getStatus());

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

        public GlobalResponse editStatus(Long id, EStatus status, String updateBy) {
                try {
                        Optional<Schedule> getBefore = repository.findById(id);

                        Branch branchId = Branch.builder()
                                        .id(getBefore.get().getBranch().getId())
                                        .build();

                        User userId = User.builder()
                                        .id(getBefore.get().getUser().getId())
                                        .build();

                        Schedule schedule = new Schedule(
                                        getBefore.get().getId(),
                                        userId,
                                        branchId,
                                        getBefore.get().getDescription(),
                                        getBefore.get().getStart_date(),
                                        getBefore.get().getEnd_date(),
                                        getBefore.get().getStart_date_realization(),
                                        getBefore.get().getEnd_date_realization(),
                                        status,
                                        ECategory.REGULAR,
                                        0,
                                        getBefore.get().getCreatedBy(),
                                        updateBy,
                                        getBefore.get().getCreated_at(),
                                        new Date());

                        Schedule response = repository.save(schedule);
                        if (response == null) {
                                return GlobalResponse
                                                .builder()
                                                .message("Failed")
                                                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                                .build();
                        }

                        logService.edit(response.getCreatedBy(), response.getDescription(), response.getId(), ECategory.REGULAR, response.getStatus());

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
                        Optional<Schedule> getBefore = repository.findById(id);

                        Branch branchId = Branch.builder()
                                        .id(getBefore.get().getBranch().getId())
                                        .build();

                        User userId = User.builder()
                                        .id(getBefore.get().getUser().getId())
                                        .build();

                        Schedule schedule = new Schedule(
                                        getBefore.get().getId(),
                                        userId,
                                        branchId,
                                        getBefore.get().getDescription(),
                                        getBefore.get().getStart_date(),
                                        getBefore.get().getEnd_date(),
                                        getBefore.get().getStart_date_realization(),
                                        getBefore.get().getEnd_date_realization(),
                                        getBefore.get().getStatus(),
                                        getBefore.get().getCategory(),
                                        1,
                                        getBefore.get().getCreatedBy(),
                                        getBefore.get().getUpdatedBy(),
                                        getBefore.get().getCreated_at(),
                                        new Date());

                        Schedule response = repository.save(schedule);
                        if (response == null) {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
                        }
                        logService.delete(response.getCreatedBy(), response.getDescription(), response.getId(), ECategory.REGULAR, response.getStatus());
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
