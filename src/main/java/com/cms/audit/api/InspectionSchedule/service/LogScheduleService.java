package com.cms.audit.api.InspectionSchedule.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cms.audit.api.InspectionSchedule.models.ECategory;
import com.cms.audit.api.InspectionSchedule.models.EStatus;
import com.cms.audit.api.InspectionSchedule.models.LogSchedule;
import com.cms.audit.api.InspectionSchedule.models.Schedule;
import com.cms.audit.api.InspectionSchedule.repository.LogScheduleRepository;
import com.cms.audit.api.common.response.GlobalResponse;

@Service
public class LogScheduleService {

        @Autowired
        private LogScheduleRepository repository;

        public GlobalResponse get() {
                try {
                        List<LogSchedule> response = repository.findAll();
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

        public GlobalResponse findById(Long id) {
                try {
                        Optional<LogSchedule> response = repository.findById(id);
                        if (response.isPresent()) {
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

        public void save(String created_by, String desc, Long id, ECategory category, EStatus status){
                Schedule setScheduleId = Schedule.builder().id(id).build();
                LogSchedule log = new LogSchedule(
                        null, 
                        setScheduleId, 
                        desc, 
                        status, 
                        category, 
                        "POST",
                        created_by, 
                        new Date());
                repository.save(log);
        }

        public void edit(String created_by, String desc, Long id, ECategory category, EStatus status){
                Schedule setScheduleId = Schedule.builder().id(id).build();
                LogSchedule log = new LogSchedule(
                null, 
                setScheduleId, 
                desc, 
                status,
                category,
                "PUT", 
                created_by, 
                new Date()
                );
                repository.save(log);
        }

        public void delete(String created_by, String desc, Long id, ECategory category, EStatus status){
                Schedule setScheduleId = Schedule.builder().id(id).build();
                LogSchedule log = new LogSchedule(
                null, 
                setScheduleId, 
                desc, 
                status,
                category,
                "DELETE", 
                created_by, 
                new Date()
                );
                repository.save(log);
        }

}
