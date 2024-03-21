package com.cms.audit.api.NewsInspection.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
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

import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Clarifications.repository.ClarificationRepository;
import com.cms.audit.api.Common.constant.FolderPath;
import com.cms.audit.api.Common.constant.randomValueNumber;
import com.cms.audit.api.Common.exception.ResourceNotFoundException;
import com.cms.audit.api.Common.pdf.GeneratePdf;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.FollowUp.models.FollowUp;
import com.cms.audit.api.Management.ReportType.models.ReportType;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.NewsInspection.models.NewsInspection;
import com.cms.audit.api.NewsInspection.repository.NewsInspectionRepository;
import com.cms.audit.api.NewsInspection.repository.PagNewsInspection;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NewsInspectionService {

    @Autowired
    private NewsInspectionRepository repository;

    @Autowired
    private ClarificationRepository clarificationRepository;

    @Autowired
    private PagNewsInspection pag;

    private final String FOLDER_PATH = FolderPath.FOLDER_PATH_UPLOAD_BAP;

    public GlobalResponse getAll(int page, int size, Date start_date, Date end_date) {
        try {
            Page<NewsInspection> response;
            if (start_date == null || end_date == null) {
                response = pag.findAll(PageRequest.of(page, size));
            } else {
                response = pag.findBAPInDateRange(start_date, end_date, PageRequest.of(page, size));
            }
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("No_Content")
                        .status(HttpStatus.OK)
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
        } catch (DataException e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
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
            Optional<NewsInspection> response = repository.findById(id);
            if (!response.isPresent()) {
                return GlobalResponse
                        .builder()
                        .message("No_Content")
                        .status(HttpStatus.OK)
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
        } catch (DataException e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
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
            NewsInspection getBAP = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("BAP with id: " + id + " is undefined"));

            String fileName = randomValueNumber.randomNumberGenerator() + "-" + file.getOriginalFilename();

            String path = FOLDER_PATH + fileName;
            String filePath = path;

            NewsInspection bap = getBAP;
            bap.setFileName(fileName);
            bap.setFile_path(filePath);

            repository.save(bap);

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

      public NewsInspection downloadFile(String fileName) throws java.io.IOException, IOFileUploadException {
                NewsInspection response = repository.findByFileName(fileName).orElseThrow(() -> new ResourceNotFoundException("File not found with name: " + fileName ));
                return response;
        }

}
