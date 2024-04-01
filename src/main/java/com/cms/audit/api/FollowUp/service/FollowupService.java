package com.cms.audit.api.FollowUp.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Common.constant.FolderPath;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.constant.randomValueNumber;
import com.cms.audit.api.Common.exception.ResourceNotFoundException;
import com.cms.audit.api.Common.pdf.GeneratePdf;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.PDFResponse;
import com.cms.audit.api.FollowUp.dto.FollowUpDTO;
import com.cms.audit.api.FollowUp.models.EStatusFollowup;
import com.cms.audit.api.FollowUp.models.FollowUp;
import com.cms.audit.api.FollowUp.repository.FollowUpRepository;
import com.cms.audit.api.FollowUp.repository.PagFollowup;
import com.cms.audit.api.Management.Penalty.models.Penalty;
import com.cms.audit.api.Management.Penalty.repository.PenaltyRepository;
import com.cms.audit.api.NewsInspection.models.NewsInspection;
import com.cms.audit.api.FollowUp.models.FollowUp;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FollowupService {
    @Autowired
    private FollowUpRepository repository;

    @Autowired
    private PagFollowup pagination;

    @Autowired
    private PenaltyRepository penaltyRepository;

    private final String FOLDER_PATH = FolderPath.FOLDER_PATH_UPLOAD_FOLLOW_UP;

    public GlobalResponse getAll(int page, int size, Date start_date, Date end_date) {
        try {
            Page<FollowUp> response;
            if (start_date == null || end_date == null) {
                response = pagination.findAll(PageRequest.of(page, size));
            } else {
                response = pagination.findFollowUpInDateRange(start_date, end_date, PageRequest.of(page, size));
            }
            List<Object> listFU = new ArrayList<>();
            for (int i = 0; i < response.getContent().size(); i++) {
                FollowUp fu = response.getContent().get(i);
                Map<String, Object> fuMap = new LinkedHashMap<>();
                fuMap.put("id", fu.getId());

                Map<String, Object> user = new LinkedHashMap<>();
                user.put("id", fu.getUser().getId());
                user.put("email", fu.getUser().getEmail());
                user.put("fullname", fu.getUser().getFullname());
                user.put("initial_name", fu.getUser().getInitial_name());
                fuMap.put("user", user);

                Map<String, Object> penalty = new LinkedHashMap<>();
                if(fu.getPenalty() != null){
                    penalty.put("id", fu.getPenalty().getId());
                    penalty.put("code", fu.getPenalty().getName());
                    fuMap.put("penalty", penalty);
                } else {
                    fuMap.put("penalty", null);
                }

                Map<String, Object> clarification = new LinkedHashMap<>();
                clarification.put("id", fu.getClarification().getId());
                clarification.put("code", fu.getClarification().getCode());
                clarification.put("evaluation_limitation",
                        convertDateToRoman.convertDateToString(fu.getClarification().getEvaluation_limitation()));
                fuMap.put("clarification", clarification);

                fuMap.put("code", fu.getCode());
                fuMap.put("description", fu.getDescription());
                fuMap.put("status", fu.getStatus());
                fuMap.put("filename", fu.getFilename());
                fuMap.put("file_path", fu.getFilePath());
                fuMap.put("is_penalty", fu.getIsPenalty());

                listFU.add(fuMap);

            }
            Map<String, Object> parent = new LinkedHashMap<>();
            parent.put("content", listFU);
            parent.put("pageable", response.getPageable());
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("No Content")
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }
            return GlobalResponse
                    .builder()
                    .data(parent)
                    .message("Success")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.OK)
                    .build();
        }
    }

    public GlobalResponse getOne(Long id) {
        try {
            FollowUp response = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Followup with id: " + id + " not found "));
            FollowUp fu = response;
            Map<String, Object> fuMap = new LinkedHashMap<>();
            fuMap.put("id", fu.getId());

            Map<String, Object> user = new LinkedHashMap<>();
            user.put("id", fu.getUser().getId());
            user.put("email", fu.getUser().getEmail());
            user.put("fullname", fu.getUser().getFullname());
            user.put("initial_name", fu.getUser().getInitial_name());
            fuMap.put("user", user);

            Map<String, Object> penalty = new LinkedHashMap<>();
            if(fu.getPenalty() != null){
                penalty.put("id", fu.getPenalty().getId());
                penalty.put("code", fu.getPenalty().getName());
                fuMap.put("penalty", penalty);
            } else {
                fuMap.put("penalty", null);
            }

            Map<String, Object> clarification = new LinkedHashMap<>();
            clarification.put("id", fu.getClarification().getId());
            clarification.put("code", fu.getClarification().getCode());
            clarification.put("evaluation_limitation",
                    convertDateToRoman.convertDateToString(fu.getClarification().getEvaluation_limitation()));
            fuMap.put("clarification", clarification);

            fuMap.put("code", fu.getCode());
            fuMap.put("description", fu.getDescription());
            fuMap.put("status", fu.getStatus());
            fuMap.put("filename", fu.getFilename());
            fuMap.put("file_path", fu.getFilePath());
            fuMap.put("is_penalty", fu.getIsPenalty());
            return GlobalResponse
                    .builder()
                    .data(fuMap)
                    .message("Success")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.OK)
                    .build();
        }
    }

    public GlobalResponse save(FollowUpDTO dto) {
        try {
            FollowUp get = repository.findById(dto.getFollowup_id()).orElseThrow(() -> new ResourceNotFoundException(
                    "Follow up with id: " + dto.getFollowup_id() + " is not found"));

            Penalty penalty = penaltyRepository.findById(dto.getPenalty_id())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Follow up with id: " + dto.getFollowup_id() + " is not found"));

            FollowUp followUp = get;
            followUp.setPenalty(penalty);
            followUp.setDescription(dto.getDescription());
            followUp.setFilename(null);
            followUp.setFilePath(null);
            followUp.setIsPenalty(dto.getIs_penalty());
            followUp.setStatus(EStatusFollowup.PROGRESS);

            FollowUp response1 = repository.save(followUp);

            PDFResponse generate = GeneratePdf.generateFollowUpPDF(response1);

            FollowUp edit = response1;
            edit.setFilename(generate.getFileName());
            edit.setFilePath(generate.getFilePath());

            try {
                repository.save(edit);
            } catch (Exception e) {
                return GlobalResponse
                        .builder()
                        .error(e)
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build();
            }

            return GlobalResponse.builder().message("Success").status(HttpStatus.OK).build();

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
            FollowUp getFollowUp = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("followUp with id: " + id + " is undefined"));

            String fileName = randomValueNumber.randomNumberGenerator() + "-" + file.getOriginalFilename();

            String path = FOLDER_PATH + fileName;
            String filePath = path;

            FollowUp followUp = getFollowUp;
            followUp.setFilename(fileName);
            followUp.setFilePath(filePath);

            repository.save(followUp);

            file.transferTo(new File(filePath));

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

    public FollowUp downloadFile(String fileName) throws java.io.IOException, IOFileUploadException {
        FollowUp response = repository.findByFilename(fileName)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with name: " + fileName));
        return response;
    }
}
