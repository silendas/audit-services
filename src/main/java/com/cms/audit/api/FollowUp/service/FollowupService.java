package com.cms.audit.api.FollowUp.service;

import java.io.File;
import java.util.Date;

import org.apache.tomcat.util.http.fileupload.impl.IOFileUploadException;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.FollowUp.dto.FollowUpDTO;
import com.cms.audit.api.FollowUp.models.EStatusFollowup;
import com.cms.audit.api.FollowUp.models.FollowUp;
import com.cms.audit.api.FollowUp.repository.FollowUpRepository;
import com.cms.audit.api.FollowUp.repository.PagFollowup;
import com.cms.audit.api.FollowUp.models.FollowUp;
import com.cms.audit.api.common.constant.FolderPath;
import com.cms.audit.api.common.constant.randomValueNumber;
import com.cms.audit.api.common.exception.ResourceNotFoundException;
import com.cms.audit.api.common.pdf.GeneratePdf;
import com.cms.audit.api.common.response.GlobalResponse;
import com.cms.audit.api.common.response.PDFResponse;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FollowupService {
    @Autowired
    private FollowUpRepository repository;

    @Autowired
    private PagFollowup pagination;

    private final String FOLDER_PATH = FolderPath.FOLDER_PATH_UPLOAD_FOLLOW_UP;

    public GlobalResponse getAll(int page, int size, Date start_date, Date end_date) {
        try {
            Page<FollowUp> response;
            if (start_date == null || end_date == null) {
                response = pagination.findAll(PageRequest.of(page, size));
            } else {
                response = pagination.findFollowUpInDateRange(start_date, end_date, PageRequest.of(page, size));
            }
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .data(response)
                        .message("No Content")
                        .status(HttpStatus.OK)
                        .build();
            }
            return GlobalResponse
                    .builder()
                    .data(response)
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
            return GlobalResponse
                    .builder()
                    .data(response)
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

            FollowUp followUp = get;
            followUp.setDescription(dto.getDescription());
            followUp.setFileName(null);
            followUp.setFilePath(null);
            followUp.setIsPenalty(dto.getIs_penalty());
            followUp.setStatus(EStatusFollowup.PROGRESS);

            FollowUp response1 = repository.save(followUp);

            PDFResponse generate = GeneratePdf.generateFollowUpPDF(response1);

            FollowUp edit = response1;
            edit.setFileName(generate.getFileName());
            edit.setFilePath(generate.getFilePath());

            FollowUp resFollowUp = repository.save(edit);

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
            followUp.setFileName(fileName);
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
        FollowUp response = repository.findByFileName(fileName).orElseThrow(() -> new ResourceNotFoundException("File not found with name: " + fileName));
        return response;
    }
}
