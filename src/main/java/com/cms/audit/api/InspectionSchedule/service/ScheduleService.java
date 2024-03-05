package com.cms.audit.api.InspectionSchedule.service;

import java.util.Date;
import java.util.List;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.InspectionSchedule.dto.ScheduleDTO;
import com.cms.audit.api.InspectionSchedule.dto.ScheduleFilterDTO;
import com.cms.audit.api.InspectionSchedule.dto.response.ScheduleInterface;
import com.cms.audit.api.InspectionSchedule.models.Schedule;
import com.cms.audit.api.InspectionSchedule.repository.ScheduleRepository;
import com.cms.audit.api.Management.User.models.User;

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
            List<ScheduleInterface> response = scheduleRepository.findAllScheduleForRegular();
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
            List<ScheduleInterface> response = scheduleRepository.findAllScheduleForSpecial();
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
            List<ScheduleInterface> response = scheduleRepository.findOneScheduleById(id);
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
            List<ScheduleInterface> response = scheduleRepository.findAllScheduleByUserId(id);
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

    public GlobalResponse getByRangeDateAndUserId( Integer id, String category,Date start_date, Date end_date) {
        try {
            List<ScheduleInterface> response = scheduleRepository.findScheduleInDateRangeByUserId(id,category,
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
            Schedule schedule = new Schedule(
                    null,
                    scheduleDTO.getUser_id(),
                    scheduleDTO.getBranch_id(),
                    scheduleDTO.getDescription(),
                    scheduleDTO.getStart_date(),
                    scheduleDTO.getEnd_date(),
                    scheduleDTO.getStart_date_realization(),
                    scheduleDTO.getEnd_date_realization(),
                    "TODO",
                    "REGULAR",
                    0,
                    scheduleDTO.getUpdatedBy(),
                    scheduleDTO.getCreatedBy(),
                    new Date(),
                    new Date());

            // check if schedule already exist?
            List<ScheduleInterface> checkIfExist = scheduleRepository.findScheduleInDateRangeByUserId(
                    scheduleDTO.getUser_id(), "REGULAR",scheduleDTO.getStart_date(),
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
            Schedule schedule = new Schedule(
                    null,
                    scheduleDTO.getUser_id(),
                    scheduleDTO.getBranch_id(),
                    scheduleDTO.getDescription(),
                    scheduleDTO.getStart_date(),
                    scheduleDTO.getEnd_date(),
                    scheduleDTO.getStart_date_realization(),
                    scheduleDTO.getEnd_date_realization(),
                    "TODO",
                    "SPECIAL",
                    0,
                    scheduleDTO.getUpdatedBy(),
                    scheduleDTO.getCreatedBy(),
                    new Date(),
                    new Date());

            // change all todo or progress status to pending status
            // scheduleRepository.editStatusPendingScheduleByDate(scheduleDTO.getUser_id(),scheduleDTO.getStart_date(),
            // scheduleDTO.getEnd_date());

            // Schedule response = scheduleRepository.save(schedule);

            // return GlobalResponse
            // .builder()
            // .data(response)
            // .message("Success")
            // .status(HttpStatus.OK)
            // .build();
            // }

            // check if schedule already exist?
            List<ScheduleInterface> checkIfExist = scheduleRepository.findScheduleInDateRangeByUserId(
                    scheduleDTO.getUser_id(), "REGULAR",scheduleDTO.getStart_date(),
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

    public GlobalResponse reSchedule(ScheduleDTO scheduleDTO) {
        try {

            Schedule schedule = new Schedule(
                    null,
                    scheduleDTO.getUser_id(),
                    scheduleDTO.getBranch_id(),
                    scheduleDTO.getDescription(),
                    scheduleDTO.getStart_date(),
                    scheduleDTO.getEnd_date(),
                    scheduleDTO.getStart_date_realization(),
                    scheduleDTO.getEnd_date_realization(),
                    "TODO",
                    "REGULAR",
                    0,
                    scheduleDTO.getUpdatedBy(),
                    scheduleDTO.getCreatedBy(),
                    new Date(),
                    new Date());
            // Schedule schedule = Schedule
            // .builder()
            // .userId(scheduleDTO.getUser_id())
            // .branchId(scheduleDTO.getBranch_id())
            // .description(scheduleDTO.getDescription())
            // .start_date(scheduleDTO.getStart_date())
            // .end_date(scheduleDTO.getEnd_date())
            // .start_date_realization(null)
            // .end_date_realization(null)
            // .status("WAITING")
            // .category("REGULAR")
            // .created_by(null)
            // .updated_by(null)
            // .created_at(new Date())
            // .updated_at(new Date())
            // .build();

            List<ScheduleInterface> checkIfExist = scheduleRepository.findScheduleInDateRangeByUserId(
                    scheduleDTO.getUser_id(), "REGULAR",scheduleDTO.getStart_date(),
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

    public GlobalResponse editRegularSchedule(ScheduleDTO scheduleDTO, Long id) {
        try {
            Schedule schedule = Schedule
                    .builder()
                    .id(id)
                    .userId(scheduleDTO.getUser_id())
                    .branchId(scheduleDTO.getBranch_id())
                    .description(scheduleDTO.getDescription())
                    .start_date(scheduleDTO.getStart_date())
                    .end_date(scheduleDTO.getEnd_date())
                    .start_date_realization(null)
                    .end_date_realization(null)
                    .status("WAITING")
                    .category("REGULAR")
                    .createdBy(scheduleDTO.getCreatedBy())
                    .updatedBy(scheduleDTO.getUpdatedBy())
                    .created_at(new Date())
                    .updated_at(new Date())
                    .build();

            List<ScheduleInterface> checkIfExist = scheduleRepository.findScheduleInDateRangeByUserId(
                    scheduleDTO.getUser_id(), "REGULAR",scheduleDTO.getStart_date(),
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

    public GlobalResponse editStatusTodo(Long id) {
        try {
            Schedule response = scheduleRepository.editStatusTodoScheduleById(id);
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

    public GlobalResponse editStatusProgress(Long id) {
        try {
            Schedule response = scheduleRepository.editStatusProgresScheduleById(id);
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

    public GlobalResponse editStatusDone(Long id) {
        try {
            Schedule response = scheduleRepository.editStatusDoneScheduleById(id);
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

    public GlobalResponse editStatusClose(Long id) {
        try {
            Schedule response = scheduleRepository.editStatusCloseScheduleById(id);
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

    public GlobalResponse editStatusPending(Long id) {
        try {
            Schedule response = scheduleRepository.editStatusPendingScheduleById(id);
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
