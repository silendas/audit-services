package com.cms.audit.api.AuditWorkingPaper.service;

import java.util.Date;
import java.util.Optional;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.cms.audit.api.AuditDailyReport.repository.pagAuditDailyReport;
import com.cms.audit.api.AuditWorkingPaper.models.AuditWorkingPaper;
import com.cms.audit.api.AuditWorkingPaper.repository.AuditWorkingPaperRepository;
import com.cms.audit.api.AuditWorkingPaper.repository.PagAuditWorkingPaper;
import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Management.User.repository.UserRepository;
import com.cms.audit.api.common.constant.FolderPath;
import com.cms.audit.api.common.response.GlobalResponse;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuditWorkingPaperService {
    
    @Autowired 
    private AuditWorkingPaperRepository repository;



    @Autowired 
    private PagAuditWorkingPaper pag;


    //private final String FOLDER_PATH = FolderPath.FOLDER_PATH_WORKING_PAPER;


    public GlobalResponse getAll(int page, int size) {
        try {
            Page<AuditWorkingPaper> response = pag.findAll(PageRequest.of(page,size));
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
            Optional<AuditWorkingPaper> response = repository.findById(id);
            if (!response.isPresent()) {
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

     public GlobalResponse getByDateRange(Date start_date, Date end_date, int page, int size) {
        try {
            Page<AuditWorkingPaper> response = pag.findWorkingPaperInDateRange(start_date, end_date,
                    PageRequest.of(page, size));
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

}
