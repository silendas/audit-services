package com.cms.audit.api.InspectionSchedule.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cms.audit.api.Common.constant.randomValueNumber;
import com.cms.audit.api.Common.exception.ResourceNotFoundException;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.InspectionSchedule.dto.EditScheduleDTO;
import com.cms.audit.api.InspectionSchedule.dto.RequestReschedule;
import com.cms.audit.api.InspectionSchedule.dto.RescheduleDTO;
import com.cms.audit.api.InspectionSchedule.dto.ScheduleDTO;
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

        @Autowired
        private ScheduleTrxRepo scheduleTrxRepo;

        public GlobalResponse get(String name, int page, int size) {
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

        public GlobalResponse getMainSchedule(Long userId, int page, int size, Date start_date, Date end_date) {
                try {
                        Page<Schedule> response;
                        if (start_date == null || end_date == null) {
                                response = pagSchedule.findByCategoryInByOrderByIdDesc("REGULAR",
                                                PageRequest.of(page, size));
                        } else {
                                response = pagSchedule.findScheduleInDateRangeByUserId(userId, "REGULAR", start_date,
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

        public GlobalResponse getSpecialSchedule(Long userId, int page, int size, Date start_date, Date end_date) {
                try {
                        Page<Schedule> response;
                        if (start_date == null || end_date == null) {
                                response = pagSchedule.findByCategoryInByOrderByIdDesc("SPECIAL",
                                                PageRequest.of(page, size));
                        } else {
                                response = pagSchedule.findScheduleInDateRangeByUserId(userId, "SPECIAL", start_date,
                                                end_date, PageRequest.of(page, size));
                        }
                        if (response.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("No Content")
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

        public GlobalResponse getByRegionId(Long id, String category, int page, int size, Date start_date,
                        Date end_date) {
                try {
                        Page<Schedule> response;
                        if (start_date == null || end_date == null) {
                                response = pagSchedule.findByRegionId(id, category, PageRequest.of(page, size));
                        } else {
                                response = pagSchedule.findScheduleInDateRangeByRegionId(id, category, start_date,
                                                end_date, PageRequest.of(page, size));
                        }
                        if (response.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("No Content")
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

        public GlobalResponse getById(Long id) {
                try {
                        Optional<Schedule> response = repository.findOneScheduleById(id);
                        if (!response.isPresent()) {
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
        public GlobalResponse insertRegularSchedule(ScheduleDTO scheduleDTO, String username) {
                try {
                        User getUser = userRepository.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("user not found"));
                        Branch branchId = Branch.builder()
                                        .id(scheduleDTO.getBranch_id())
                                        .build();

                        User userId = User.builder()
                                        .id(scheduleDTO.getUser_id())
                                        .build();
                        Schedule schedule = new Schedule(
                                        null,
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
                                        getUser.getId(),
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

                        logService.save(response.getCreatedBy(), response.getDescription(), response.getId(),
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

        @Transactional(value = TxType.REQUIRES_NEW)
        public GlobalResponse insertSpecialSchedule(ScheduleDTO scheduleDTO, String username) {
                try {
                        User getUser = userRepository.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("user not found"));
                        Branch branchId = Branch.builder()
                                        .id(scheduleDTO.getBranch_id())
                                        .build();

                        User userId = User.builder()
                                        .id(scheduleDTO.getUser_id())
                                        .build();
                        Schedule schedule = new Schedule(
                                        null,
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
                                        getUser.getId(),
                                        new Date(),
                                        new Date());

                        // change all todo or progress status to pending status
                        repository.editStatusPendingScheduleByDate(scheduleDTO.getUser_id(),
                                        getUser.getId(), scheduleDTO.getStart_date(),
                                        scheduleDTO.getEnd_date());

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
                        User getUser = userRepository.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("user not found"));
                        Schedule getSchedule = repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Schedule not found"));

                        Branch branchId = Branch.builder()
                                        .id(dto.getBranch_id())
                                        .build();

                        User userId = User.builder()
                                        .id(dto.getUser_id())
                                        .build();

                        Schedule schedule =  getSchedule;
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

        public GlobalResponse approve(Long id) throws Exception{
                User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Schedule getSchedule = repository.findById(id).orElseThrow(()->new Exception("Not found"));
                ScheduleTrx getTrx = scheduleTrxRepo.findById(getSchedule.getScheduleTrx().getId()).orElseThrow(()->new Exception("Not found"));

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
                        User getUser = userRepository.findByUsername(username).orElseThrow(()-> new ResourceNotFoundException("User not found"));
                        
                        Schedule getSchedule = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Schedule not found"));

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
                        User getUser = userRepository.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("user not found"));
                        Schedule getSchedule = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Schedule not found"));

                        Schedule schedule = getSchedule;
                        schedule.setIs_delete(1);
                        schedule.setUpdatedBy(getUser.getId());
                        schedule.setUpdated_at(new Date());

                        Schedule response = repository.save(schedule);
                        if (response ==null) {
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
