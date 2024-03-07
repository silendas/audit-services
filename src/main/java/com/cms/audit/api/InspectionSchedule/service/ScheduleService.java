package com.cms.audit.api.InspectionSchedule.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cms.audit.api.InspectionSchedule.dto.RescheduleDTO;
import com.cms.audit.api.InspectionSchedule.dto.ScheduleDTO;
import com.cms.audit.api.InspectionSchedule.dto.response.ScheduleInterface;
import com.cms.audit.api.InspectionSchedule.models.ECategory;
import com.cms.audit.api.InspectionSchedule.models.EStatus;
import com.cms.audit.api.InspectionSchedule.models.Schedule;
import com.cms.audit.api.InspectionSchedule.repository.ScheduleRepository;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.common.response.GlobalResponse;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@Service
public class ScheduleService {

        @Autowired
        private ScheduleRepository scheduleRepository;

        public GlobalResponse get() {
                try {
                        List<Schedule> response = scheduleRepository.findAll();
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

        public GlobalResponse getMainSchedule() {
                try {
                        List<Schedule> response = scheduleRepository.findAllScheduleForRegular();
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

        public GlobalResponse getSpecialSchedule() {
                try {
                        List<Schedule> response = scheduleRepository.findAllScheduleForSpecial();
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

        public GlobalResponse getByRegionId(Long id) {
                try {
                        List<ScheduleInterface> response = scheduleRepository.findOneScheduleByRegionId(id);
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
                        Optional<Schedule> response = scheduleRepository.findOneScheduleById(id);
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

        public GlobalResponse getByStatus(String status) {
                try {
                        List<Schedule> response = scheduleRepository.findOneScheduleByStatus(status);
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

        public GlobalResponse getByUserId(Long id) {
                try {
                        List<Schedule> response = scheduleRepository.findAllScheduleByUserId(id);
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

        public GlobalResponse getByRangeDateAndUserId(Long id, String category, Date start_date, Date end_date) {
                try {
                        List<Schedule> response = scheduleRepository.findScheduleInDateRangeByUserId(id,
                                        category,
                                        start_date, end_date);
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
                                        scheduleDTO.getCreatedBy(),
                                        new Date(),
                                        new Date());

                        // check if schedule already exist?
                        List<Schedule> checkIfExist = scheduleRepository.findScheduleInDateRangeByUserId(
                                        scheduleDTO.getUser_id(), "REGULAR", scheduleDTO.getStart_date(),
                                        scheduleDTO.getEnd_date());
                        if (!checkIfExist.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Already exist")
                                                .status(HttpStatus.FOUND)
                                                .build();
                        }

                        Schedule response = scheduleRepository.save(schedule);
                        if (response == null) {
                                return GlobalResponse
                                                .builder()
                                                .message("Failed")
                                                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                                .build();
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
                                        scheduleDTO.getCreatedBy(),
                                        new Date(),
                                        new Date());

                        // change all todo or progress status to pending status
                        scheduleRepository.editStatusPendingScheduleByDate(scheduleDTO.getUser_id(), scheduleDTO.getCreatedBy(),scheduleDTO.getStart_date(), scheduleDTO.getEnd_date());

                        // Schedule response = scheduleRepository.save(schedule);
                        Schedule response = scheduleRepository.save(schedule);
                        if (response == null) {
                                return GlobalResponse
                                                .builder()
                                                .message("Failed")
                                                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                                .build();
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
                                        dto.getCreatedBy(),
                                        dto.getCreatedBy(),
                                        new Date(),
                                        new Date());

                        List<Schedule> checkIfExist = scheduleRepository.findScheduleInDateRangeByUserId(
                                        dto.getUser_id(), "REGULAR", dto.getStart_date(),
                                        dto.getEnd_date());
                        if (!checkIfExist.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Already exist")
                                                .status(HttpStatus.FOUND)
                                                .build();
                        }

                        Schedule response1 = scheduleRepository.save(schedule1);

                        Optional<Schedule> getBefore = scheduleRepository.findById(dto.getSchedule_id());

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
                                        dto.getCreatedBy(),
                                        getBefore.get().getCreated_at(),
                                        new Date());

                        Schedule response2 = scheduleRepository.save(schedule2);

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

        public GlobalResponse editRegularSchedule(ScheduleDTO scheduleDTO, Long id) {
                try {
                        Optional<Schedule> getSChedule = scheduleRepository.findById(id);

                        Branch branchId = Branch.builder()
                                        .id(scheduleDTO.getBranch_id())
                                        .build();

                        User userId = User.builder()
                                        .id(scheduleDTO.getUser_id())
                                        .build();

                        Schedule schedule = new Schedule(
                                        id,
                                        userId,
                                        branchId,
                                        scheduleDTO.getDescription(),
                                        scheduleDTO.getStart_date(),
                                        scheduleDTO.getEnd_date(),
                                        null,
                                        null,
                                        scheduleDTO.getStatus(),
                                        ECategory.REGULAR,
                                        0,
                                        scheduleDTO.getCreatedBy(),
                                        scheduleDTO.getCreatedBy(),
                                        getSChedule.get().getCreated_at(),
                                        new Date());

                        List<Schedule> checkIfExist = scheduleRepository.findScheduleInDateRangeByUserId(
                                        scheduleDTO.getUser_id(), "REGULAR", scheduleDTO.getStart_date(),
                                        scheduleDTO.getEnd_date());
                        if (!checkIfExist.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Already exist")
                                                .status(HttpStatus.FOUND)
                                                .build();
                        }

                        scheduleRepository.save(schedule);

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

        public GlobalResponse editSpecialSchedule(ScheduleDTO scheduleDTO, Long id) {
                try {
                        Optional<Schedule> getSChedule = scheduleRepository.findById(id);

                        Branch branchId = Branch.builder()
                                        .id(scheduleDTO.getBranch_id())
                                        .build();

                        User userId = User.builder()
                                        .id(scheduleDTO.getUser_id())
                                        .build();

                        Schedule schedule = new Schedule(
                                        id,
                                        userId,
                                        branchId,
                                        scheduleDTO.getDescription(),
                                        scheduleDTO.getStart_date(),
                                        scheduleDTO.getEnd_date(),
                                        null,
                                        null,
                                        scheduleDTO.getStatus(),
                                        ECategory.SPECIAL,
                                        0,
                                        scheduleDTO.getCreatedBy(),
                                        scheduleDTO.getCreatedBy(),
                                        getSChedule.get().getCreated_at(),
                                        new Date());

                        List<Schedule> checkIfExist = scheduleRepository.findScheduleInDateRangeByUserId(
                                        scheduleDTO.getUser_id(), "REGULAR", scheduleDTO.getStart_date(),
                                        scheduleDTO.getEnd_date());
                        if (!checkIfExist.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Already exist")
                                                .status(HttpStatus.FOUND)
                                                .build();
                        }

                        scheduleRepository.save(schedule);

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
                        Optional<Schedule> getBefore = scheduleRepository.findById(id);

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

                        Schedule response = scheduleRepository.save(schedule);

                        if (response == null) {
                                return GlobalResponse
                                                .builder()
                                                .message("Failed")
                                                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                                .build();
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

        public GlobalResponse delete(Long id) {
                try {

                        User response = scheduleRepository.softDelete(id);
                        if (response == null) {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
                        }
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
