package com.cms.audit.api.AuditWorkingPaper.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.apache.tomcat.util.http.fileupload.impl.IOFileUploadException;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.cms.audit.api.AuditWorkingPaper.models.AuditWorkingPaper;
import com.cms.audit.api.AuditWorkingPaper.repository.AuditWorkingPaperRepository;
import com.cms.audit.api.AuditWorkingPaper.repository.PagAuditWorkingPaper;
import com.cms.audit.api.Common.constant.FileStorageKKA;
import com.cms.audit.api.Common.constant.FolderPath;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.InspectionSchedule.models.EStatus;
import com.cms.audit.api.InspectionSchedule.models.Schedule;
import com.cms.audit.api.InspectionSchedule.repository.ScheduleRepository;
import com.cms.audit.api.Management.User.models.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuditWorkingPaperService {

    @Autowired
    private FileStorageKKA fileStorageService;

    @Autowired
    private AuditWorkingPaperRepository repository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private PagAuditWorkingPaper pag;

    private final String FOLDER_PATH = FolderPath.FOLDER_PATH_UPLOAD_WORKING_PAPER;

    public GlobalResponse getAll(String name, Long branchId, Long schedule_id, int page, int size, Date start_date,
            Date end_date) {
        try {
            User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Page<AuditWorkingPaper> response = null;
            if (schedule_id != null) {
                return GlobalResponse.builder().data(repository.findByScheduleId(schedule_id)).message("Success")
                        .status(HttpStatus.OK).build();
            } else if (name != null && branchId != null && start_date != null && end_date != null) {
                response = pag.findWorkingPaperByAllFilter(name, schedule_id, start_date, end_date,
                        PageRequest.of(page, size));
            } else if (name != null) {
                if (branchId != null) {
                    response = pag.findWorkingPaperByNameAndBranch(name, schedule_id, PageRequest.of(page, size));
                } else if (start_date != null && end_date != null) {
                    response = pag.findWorkingPaperByNameAndDate(name, start_date, end_date,
                            PageRequest.of(page, size));
                } else {
                    response = pag.findWorkingPaperByName(name, PageRequest.of(page, size));
                }
            } else if (branchId != null) {
                if (start_date != null && end_date != null) {
                    response = pag.findWorkingPaperByBranchAndDate(branchId, start_date, end_date,
                            PageRequest.of(page, size));
                } else {
                    response = pag.findWorkingPaperByBranch(branchId, PageRequest.of(page, size));
                }
            } else {
                if (getUser.getLevel().getId() == 1) {
                    if (start_date != null && end_date != null) {
                        response = pag.findWorkingPaperInDateRange(start_date, end_date, PageRequest.of(page, size));
                    } else {
                        response = pag.findAllWorkingPaper(PageRequest.of(page, size));
                    }
                } else if (getUser.getLevel().getId() == 2) {

                } else if (getUser.getLevel().getId() == 3) {
                    if (start_date != null && end_date != null) {
                        response = pag.findAllWorkingPaperByUserIdAndDate(getUser.getId(), start_date, end_date,
                                PageRequest.of(page, size));
                    } else {
                        response = pag.findAllWorkingPaperByUserId(getUser.getId(), PageRequest.of(page, size));
                    }
                } else {
                    return GlobalResponse.builder().message("Cannot Access").status(HttpStatus.UNAUTHORIZED).build();
                }
            }
            List<Object> listKka = new ArrayList<>();
            for (int i = 0; i < response.getContent().size(); i++) {
                AuditWorkingPaper kka = response.getContent().get(i);
                Map<String, Object> kkaMap = new LinkedHashMap<>();
                kkaMap.put("id", kka.getId());

                Map<String, Object> user = new LinkedHashMap<>();
                user.put("id", kka.getUser().getId());
                user.put("email", kka.getUser().getEmail());
                user.put("fullname", kka.getUser().getFullname());
                user.put("initial_name", kka.getUser().getInitial_name());
                kkaMap.put("user", user);

                Map<String, Object> branch = new LinkedHashMap<>();
                branch.put("id", kka.getBranch().getId());
                branch.put("name", kka.getBranch().getName());
                kkaMap.put("branch", branch);

                Map<String, Object> schedule = new LinkedHashMap<>();
                schedule.put("id", kka.getSchedule().getId());
                schedule.put("start_date", convertDateToRoman.convertDateToString(kka.getSchedule().getStart_date()));
                schedule.put("end_date", convertDateToRoman.convertDateToString(kka.getSchedule().getEnd_date()));
                kkaMap.put("schedule", schedule);

                kkaMap.put("start_date", convertDateToRoman.convertDateToString(kka.getStart_date()));
                kkaMap.put("end_date", convertDateToRoman.convertDateToString(kka.getEnd_date()));
                kkaMap.put("filename", kka.getFilename());
                kkaMap.put("file_path", kka.getFile_path());

                listKka.add(kkaMap);

            }
            Map<String, Object> parent = new LinkedHashMap<>();
            parent.put("content", listKka);
            parent.put("pageable", response.getPageable());
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
                    .data(parent)
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
                    .orElseThrow(() -> new BadRequestException("LHA with id: " + id + " is undefined"));

            Map<String, Object> kkaMap = new LinkedHashMap<>();
            kkaMap.put("id", response.getId());

            Map<String, Object> user = new LinkedHashMap<>();
            user.put("id", response.getUser().getId());
            user.put("email", response.getUser().getEmail());
            user.put("fullname", response.getUser().getFullname());
            user.put("initial_name", response.getUser().getInitial_name());
            kkaMap.put("user", user);

            Map<String, Object> branch = new LinkedHashMap<>();
            branch.put("id", response.getBranch().getId());
            branch.put("name", response.getBranch().getName());
            kkaMap.put("branch", branch);

            Map<String, Object> schedule = new LinkedHashMap<>();
            schedule.put("id", response.getSchedule().getId());
            schedule.put("start_date", convertDateToRoman.convertDateToString(response.getSchedule().getStart_date()));
            schedule.put("end_date", convertDateToRoman.convertDateToString(response.getSchedule().getEnd_date()));
            kkaMap.put("schedule", schedule);

            kkaMap.put("start_date", convertDateToRoman.convertDateToString(response.getStart_date()));
            kkaMap.put("end_date", convertDateToRoman.convertDateToString(response.getEnd_date()));
            kkaMap.put("filename", response.getFilename());
            kkaMap.put("file_path", response.getFile_path());
            return GlobalResponse
                    .builder()
                    .message("Success")
                    .data(kkaMap)
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
                    .orElseThrow(() -> new BadRequestException("Schedule with id: " + id + " is undefined"));

            // String name = randomValueNumber.randomNumberGenerator() +
            // file.getOriginalFilename();

            String fileName = fileStorageService.storeFile(file);
            String path = FOLDER_PATH + fileName;

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

            // file.transferTo(new File(filePath));

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
        AuditWorkingPaper response = repository.findByFilenameis(fileName)
                .orElseThrow(() -> new BadRequestException("File not found with name: " + fileName));
        return response;
    }

}
