package com.cms.audit.api.FollowUp.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.apache.tomcat.util.http.fileupload.impl.IOFileUploadException;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.cms.audit.api.Common.constant.FileStorageFU;
import com.cms.audit.api.Common.constant.FolderPath;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.pdf.GeneratePdf;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.PDFResponse;
import com.cms.audit.api.FollowUp.dto.FollowUpDTO;
import com.cms.audit.api.FollowUp.models.EStatusFollowup;
import com.cms.audit.api.FollowUp.models.FollowUp;
import com.cms.audit.api.FollowUp.repository.FollowUpRepository;
import com.cms.audit.api.FollowUp.repository.PagFollowup;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.BranchOffice.repository.BranchRepository;
import com.cms.audit.api.Management.Penalty.models.Penalty;
import com.cms.audit.api.Management.Penalty.repository.PenaltyRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FollowupService {
    @Autowired
    private FollowUpRepository repository;

    @Autowired
    private FileStorageFU fileStorageService;

    @Autowired
    private PagFollowup pagination;

    @Autowired
    private PenaltyRepository penaltyRepository;

    private final String FOLDER_PATH = FolderPath.FOLDER_PATH_UPLOAD_FOLLOW_UP;

    public GlobalResponse getAll(String name, Long branch, int page, int size, Date start_date, Date end_date) {
        try {
            Page<FollowUp> response = null;
            if (name != null && branch != null && start_date != null && end_date != null) {
                response = pagination.findFollowUpInAllFilter(name, branch, start_date, end_date,
                        PageRequest.of(page, size));
            } else if (name != null) {
                if (branch != null) {
                    response = pagination.findFollowUpInNameAndBranch(name, branch, PageRequest.of(page, size));
                } else if (start_date != null && end_date != null) {
                    response = pagination.findFollowUpInNameAndDate(name, start_date, end_date,
                            PageRequest.of(page, size));
                } else {
                    response = pagination.findFollowUpInName(name, PageRequest.of(page, size));
                }
            } else if (branch != null) {
                if (start_date != null && end_date != null) {
                    response = pagination.findFollowUpInBranchAndDateRange(branch, start_date, end_date,
                            PageRequest.of(page, size));
                } else {
                    response = pagination.findFollowUpInBranch(branch, PageRequest.of(page, size));
                }
            } else if (start_date != null || end_date != null) {
                response = pagination.findFollowUpInDateRange(start_date, end_date, PageRequest.of(page, size));
            } else {
                response = pagination.findAll(PageRequest.of(page, size));
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
                if (fu.getPenalty() != null) {
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
            parent.put("pageable", response.getPageable());
            parent.put("totalPage", response.getTotalPages());
            parent.put("totalElement", response.getTotalElements());
            parent.put("size", response.getSize());
            parent.put("number", response.getNumber());
            parent.put("last", response.isLast());
            parent.put("first", response.isFirst());
            parent.put("numberOfElement", response.getNumberOfElements());
            parent.put("empty", response.isEmpty());
            parent.put("sort", response.getSort());
            parent.put("content", listFU);
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data not found")
                        .status(HttpStatus.OK)
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
            Optional<FollowUp> response = repository.findById(id);
            if (!response.isPresent()) {
                return GlobalResponse.builder().message("Followup with id: " + id + " not found ")
                        .status(HttpStatus.BAD_REQUEST).build();
            }
            FollowUp fu = response.get();
            Map<String, Object> fuMap = new LinkedHashMap<>();
            fuMap.put("id", fu.getId());

            Map<String, Object> user = new LinkedHashMap<>();
            user.put("id", fu.getUser().getId());
            user.put("email", fu.getUser().getEmail());
            user.put("fullname", fu.getUser().getFullname());
            user.put("initial_name", fu.getUser().getInitial_name());
            fuMap.put("user", user);

            Map<String, Object> penalty = new LinkedHashMap<>();
            if (fu.getPenalty() != null) {
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
            Optional<FollowUp> getFollowUp = repository.findById(dto.getFollowup_id());
            if (!getFollowUp.isPresent()) {
                return GlobalResponse.builder().message("Follow Up with id:" + dto.getFollowup_id() + " is not found")
                        .build();
            }
            Optional<Penalty> penalty;
            if (dto.getPenalty_id() != null) {
                penalty = penaltyRepository.findById(dto.getPenalty_id());
                if (!penalty.isPresent()) {
                    return GlobalResponse.builder().message("Penalty with id:" + dto.getPenalty_id() + " is not foudn")
                            .build();
                }
            } else {
                penalty = null;
            }

            FollowUp followUp = getFollowUp.get();
            followUp.setPenalty(penalty.get());
            followUp.setDescription(dto.getDescription());
            if (dto.getPenalty_id() != null) {
                followUp.setIsPenalty(1L);
            } else {
                followUp.setIsPenalty(0L);
            }
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
            Optional<FollowUp> getFollowUp = repository.findById(id);
            if (!getFollowUp.isPresent()) {
                return GlobalResponse.builder().message("Follow up with id:" + id + " is not found").build();
            }

            // String fileName = randomValueNumber.randomNumberGenerator() + "-" +
            // file.getOriginalFilename();

            String fileName = fileStorageService.storeFile(file);
            String path = FOLDER_PATH + fileName;
            String filePath = path;

            FollowUp followUp = getFollowUp.get();
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
                .orElseThrow(() -> new BadRequestException("File not found with name: " + fileName));
        return response;
    }
}
