package com.cms.audit.api.AuditWorkingPaper.service;

import java.io.File;
import java.util.Date;
import java.util.Optional;

import org.apache.tomcat.util.http.fileupload.impl.IOFileUploadException;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.cms.audit.api.AuditWorkingPaper.models.AuditWorkingPaper;
import com.cms.audit.api.AuditWorkingPaper.repository.AuditWorkingPaperRepository;
import com.cms.audit.api.AuditWorkingPaper.repository.PagAuditWorkingPaper;
import com.cms.audit.api.Common.constant.FolderPath;
import com.cms.audit.api.Common.constant.randomValueNumber;
import com.cms.audit.api.Common.exception.ResourceNotFoundException;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Flag.model.Flag;
import com.cms.audit.api.Flag.repository.FlagRepo;
import com.cms.audit.api.InspectionSchedule.models.EStatus;
import com.cms.audit.api.InspectionSchedule.models.Schedule;
import com.cms.audit.api.InspectionSchedule.repository.ScheduleRepository;
import com.cms.audit.api.NewsInspection.models.NewsInspection;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuditWorkingPaperService {

    @Autowired
    private AuditWorkingPaperRepository repository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private PagAuditWorkingPaper pag;

    private final String FOLDER_PATH = FolderPath.FOLDER_PATH_UPLOAD_WORKING_PAPER;

    public GlobalResponse getAll(Long schedule_id, int page, int size, Date start_date, Date end_date) {
        try {
            Page<AuditWorkingPaper> response;
            if (schedule_id != null) {
                return GlobalResponse.builder().data(repository.findByScheduleId(schedule_id)).message("Success").status(HttpStatus.OK).build();
            }
            if (start_date == null || end_date == null) {
                response = pag.findAll(PageRequest.of(page, size));
            } else {
                response = pag.findWorkingPaperInDateRange(start_date, end_date, PageRequest.of(page, size));
            }
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
        } catch (Exception e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public GlobalResponse getOneById(Long id) {
        try {
            AuditWorkingPaper response = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("LHA with id: " + id + " is undefined"));
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
        } catch (Exception e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public GlobalResponse uploadFile(MultipartFile file, Long id) {
        try {
            Schedule getSchedule = scheduleRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Schedule with id: " + id + " is undefined"));

            String fileName = randomValueNumber.randomNumberGenerator() + file.getOriginalFilename();
            String path = FOLDER_PATH + fileName;
            String filePath = path;

            AuditWorkingPaper kka = new AuditWorkingPaper();
            kka.setUser(getSchedule.getUser());
            kka.setBranch(getSchedule.getBranch());
            kka.setSchedule(getSchedule);
            kka.setStart_date(getSchedule.getStart_date());
            kka.setEnd_date(new Date());
            kka.setFilename(fileName);
            kka.setFile_path(path);
            kka.setIs_delete(0);
            kka.setCreated_by(getSchedule.getCreatedBy());
            kka.setCreated_at(new Date());

            repository.save(kka);

            file.transferTo(new File(filePath));

            Schedule schedule = getSchedule;
            schedule.setStatus(EStatus.DONE);
            schedule.setEnd_date_realization(new Date());

            scheduleRepository.save(schedule);

            return GlobalResponse
                    .builder()
                    .message("Success")
                    .status(HttpStatus.OK)
                    .build();
        } catch (DataException e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();
        } catch (ResponseStatusException e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        } catch (Exception e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public AuditWorkingPaper downloadFile(String fileName) throws java.io.IOException, IOFileUploadException {
        AuditWorkingPaper response = repository.findByFilename(fileName)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with name: " + fileName));
        return response;
    }

}
