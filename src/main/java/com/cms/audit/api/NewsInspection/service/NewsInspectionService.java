package com.cms.audit.api.NewsInspection.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import com.cms.audit.api.NewsInspection.models.NewsInspection;
import com.cms.audit.api.NewsInspection.repository.NewsInspectionRepository;
import com.cms.audit.api.NewsInspection.repository.PagNewsInspection;
import com.cms.audit.api.common.constant.FolderPath;
import com.cms.audit.api.common.response.GlobalResponse;

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

    private final String FOLDER_PATH = FolderPath.FOLDER_PATH_BAP;

    public GlobalResponse getAll(int page, int size) {
        try {
            Page<NewsInspection> response = pag.findAll(PageRequest.of(page, size));
            if(response.isEmpty()){
                return GlobalResponse
                .builder()
                .message("NoContent")
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
            if(!response.isPresent()){
                return GlobalResponse
                .builder()
                .message("NoContent")
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

    public GlobalResponse getByDateRange(Date start_date, Date end_date,int page,int size) {
        try {
            Page<NewsInspection> response = pag.findBAPInDateRange(start_date, end_date, PageRequest.of(page, size));
            if(response.isEmpty()){
                return GlobalResponse
                .builder()
                .message("NoContent")
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
            NewsInspection getBAP = repository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "No Content"));

            String path = FOLDER_PATH + file.getOriginalFilename();

            String fileName = file.getOriginalFilename();
            String filePath = path;

            Clarification setClarificationId = Clarification.builder().id(getBAP.getClarification().getId()).build();

            NewsInspection clarification = new NewsInspection(
                    id,
                    setClarificationId,
                    fileName,
                    filePath,
                    getBAP.getReport_number(),
                    getBAP.getCode());
            repository.save(clarification);

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

}
