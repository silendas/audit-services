package com.cms.audit.api.NewsInspection.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.apache.coyote.BadRequestException;
import org.apache.tomcat.util.http.fileupload.impl.IOFileUploadException;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.cms.audit.api.Common.constant.FileStorageBAP;
import com.cms.audit.api.Common.constant.FolderPath;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.NewsInspection.models.NewsInspection;
import com.cms.audit.api.NewsInspection.repository.NewsInspectionRepository;
import com.cms.audit.api.NewsInspection.repository.PagNewsInspection;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NewsInspectionService {

    @Autowired
    private FileStorageBAP fileStorageService;

    @Autowired
    private NewsInspectionRepository repository;

    @Autowired
    private PagNewsInspection pag;

    private final String FOLDER_PATH = FolderPath.FOLDER_PATH_UPLOAD_BAP;

    public GlobalResponse getAll(String name, Long branch, int page, int size, Date start_date, Date end_date) {
        try {
            User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Page<NewsInspection> response = null;
            if (name != null && branch != null && start_date != null && end_date != null) {
                response = pag.findBAPInAllFilter(name, branch, start_date, end_date, PageRequest.of(page, size));
            } else if (name != null) {
                if (branch != null) {
                    response = pag.findBAPInNameAndBranch(name, branch, PageRequest.of(page, size));
                } else if (start_date != null && end_date != null) {
                    response = pag.findBAPInNameAndDate(name, start_date, end_date, PageRequest.of(page, size));
                } else {
                    response = pag.findBAPInName(name, PageRequest.of(page, size));
                }
            } else if (branch != null) {
                if (start_date != null && end_date != null) {
                    response = pag.findBAPInBranchAndDate(branch, start_date, end_date, PageRequest.of(page, size));
                } else {
                    response = pag.findBAPInBranch(branch, PageRequest.of(page, size));
                }
            } else if (start_date != null && end_date != null) {
                response = pag.findBAPInDateRange(start_date, end_date, PageRequest.of(page, size));
            } else {
                if (getUser.getLevel().getCode().equals("C")) {
                    response = pag.findBAPInUserid(getUser.getId(), PageRequest.of(page, size));
                } else if (getUser.getLevel().getCode().equals("B")) {
                    Pageable pageable = PageRequest.of(page, size);
                    List<NewsInspection> lhaList = new ArrayList<>();
                    for (int i = 0; i < getUser.getRegionId().size(); i++) {
                        List<NewsInspection> clAgain = new ArrayList<>();
                        clAgain = repository.findByRegionId(getUser.getRegionId().get(i));
                        if (!clAgain.isEmpty()) {
                            for (int u = 0; u < clAgain.size(); u++) {
                                lhaList.add(clAgain.get(u));
                            }
                        }
                    }
                    try {
                        int start = (int) pageable.getOffset();
                        int end = Math.min((start + pageable.getPageSize()),
                                lhaList.size());
                        List<NewsInspection> pageContent = lhaList.subList(start, end);
                        Page<NewsInspection> response2 = new PageImpl<>(pageContent, pageable,
                                lhaList.size());
                        response = response2;
                    } catch (Exception e) {
                        return GlobalResponse
                                .builder()
                                .error(e)
                                .status(HttpStatus.BAD_REQUEST)
                                .build();
                    }
                } else if (getUser.getLevel().getCode().equals("A") || getUser.getLevel().getCode().equals("A")) {
                    response = pag.findAllBAP(PageRequest.of(page, size));
                }
            }
            List<Object> listBAP = new ArrayList<>();
            for (int i = 0; i < response.getContent().size(); i++) {
                NewsInspection bap = response.getContent().get(i);
                Map<String, Object> kkaMap = new LinkedHashMap<>();
                kkaMap.put("id", bap.getId());

                Map<String, Object> user = new LinkedHashMap<>();
                user.put("id", bap.getUser().getId());
                user.put("email", bap.getUser().getEmail());
                user.put("fullname", bap.getUser().getFullname());
                user.put("initial_name", bap.getUser().getInitial_name());
                user.put("level", bap.getUser().getLevel());
                kkaMap.put("user", user);

                kkaMap.put("branch", bap.getBranch());

                Map<String, Object> clarification = new LinkedHashMap<>();
                clarification.put("id", bap.getClarification().getId());
                clarification.put("code", bap.getClarification().getCode());
                clarification.put("nominal_loss", bap.getClarification().getNominal_loss());
                clarification.put("evaluation_limitation",bap.getClarification().getEvaluation_limitation());
                kkaMap.put("clarification", clarification);

                kkaMap.put("code", bap.getCode());
                kkaMap.put("filename", bap.getFileName());
                kkaMap.put("file_path", bap.getFile_path());

                listBAP.add(kkaMap);

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
            parent.put("content", listBAP);
            if (response.isEmpty()) {
                return GlobalResponse
                        .builder()
                        .message("Data tidak ditemukan")
                        .data(parent)
                        .status(HttpStatus.OK)
                        .build();
            }
            return GlobalResponse
                    .builder()
                    .message("Berhasil menampilkan data")
                    .data(parent)
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
                return GlobalResponse.builder().message("BAP with id:" + id + " is not foudn")
                        .status(HttpStatus.BAD_REQUEST).build();
            }
            NewsInspection bap = response.get();
            Map<String, Object> kkaMap = new LinkedHashMap<>();
            kkaMap.put("id", bap.getId());

            Map<String, Object> user = new LinkedHashMap<>();
            user.put("id", bap.getUser().getId());
            user.put("email", bap.getUser().getEmail());
            user.put("fullname", bap.getUser().getFullname());
            user.put("initial_name", bap.getUser().getInitial_name());
            user.put("level", bap.getUser().getLevel());
            kkaMap.put("user", user);

            kkaMap.put("branch", bap.getBranch());

            Map<String, Object> clarification = new LinkedHashMap<>();
            clarification.put("id", bap.getClarification().getId());
            clarification.put("code", bap.getClarification().getCode());
            clarification.put("nominal_loss", bap.getClarification().getNominal_loss());
            clarification.put("evaluation_limitation",
                    convertDateToRoman.convertDateToString(bap.getClarification().getEvaluation_limitation()));
            kkaMap.put("clarification", clarification);

            kkaMap.put("code", bap.getCode());
            kkaMap.put("filename", bap.getFileName());
            kkaMap.put("filename", bap.getFileName());
            kkaMap.put("file_path", bap.getFile_path());
            if (!response.isPresent()) {
                return GlobalResponse
                        .builder()
                        .message("No_Content")
                        .status(HttpStatus.OK)
                        .build();
            }
            return GlobalResponse
                    .builder()
                    .message("Berhasil menampilkan data")
                    .data(kkaMap)
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
            Optional<NewsInspection> getBAP = repository.findById(id);
            if (!getBAP.isPresent()) {
                return GlobalResponse.builder().message("BAP tidak ditemukan")
                        .errorMessage("BAP dengna id : " + id + " tidak ditemukan").status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            // String fileName = randomValueNumber.randomNumberGenerator() + "-" +
            // file.getOriginalFilename();

            String fileName = fileStorageService.storeFile(file);
            String path = FOLDER_PATH + fileName;
            String filePath = path;

            NewsInspection bap = getBAP.get();
            bap.setFileName(fileName);
            bap.setFile_path(filePath);

            repository.save(bap);

            // file.transferTo(new File(filePath));

            return GlobalResponse
                    .builder()
                    .message("Berhasil upload file")
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
        NewsInspection response = repository.findByFileName(fileName)
                .orElseThrow(() -> new BadRequestException("File not found with name: " + fileName));
        return response;
    }

}
