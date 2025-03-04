package com.cms.audit.api.NewsInspection.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
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

import com.cms.audit.api.Common.constant.FileStorageBAP;
import com.cms.audit.api.Common.constant.FolderPath;
import com.cms.audit.api.Common.constant.SpecificationFIlter;
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
            Specification<NewsInspection> spec = Specification
                    .where(new SpecificationFIlter<NewsInspection>().nameLike(name))
                    .and(new SpecificationFIlter<NewsInspection>().branchIdEqual(branch))
                    .and(new SpecificationFIlter<NewsInspection>().dateRange(start_date, end_date))
                    .and(new SpecificationFIlter<NewsInspection>().isNotDeleted())
                    .and(new SpecificationFIlter<NewsInspection>().orderByIdDesc());
            if (getUser.getLevel().getCode().equals("C")) {
                spec = spec.and(new SpecificationFIlter<NewsInspection>().userId(getUser.getId()));
            } else if (getUser.getLevel().getCode().equals("B")) {
                Specification<NewsInspection> regionOrUserSpec = Specification
                        .where(new SpecificationFIlter<NewsInspection>().getByRegionIds(getUser.getRegionId()))
                        .or(new SpecificationFIlter<NewsInspection>().userId(getUser.getId()));
                spec = spec.and(regionOrUserSpec);
            }
            response = pag.findAll(spec, PageRequest.of(page, size));
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
                clarification.put("evaluation_limitation", bap.getClarification().getEvaluation_limitation());
                kkaMap.put("clarification", clarification);

                kkaMap.put("code", bap.getCode());
                kkaMap.put("filename", bap.getFileName());
                kkaMap.put("file_path", bap.getFile_path());
                kkaMap.put("created_at", bap.getCreated_at());

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
            clarification.put("evaluation_limitation", bap.getClarification().getEvaluation_limitation());
            kkaMap.put("clarification", clarification);

            kkaMap.put("code", bap.getCode());
            kkaMap.put("filename", bap.getFileName());
            kkaMap.put("filename", bap.getFileName());
            kkaMap.put("file_path", bap.getFile_path());
            kkaMap.put("created_at", bap.getCreated_at());
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
            if (file == null) {
                return GlobalResponse.builder()
                        .message("File tidak boleh kosong")
                        .errorMessage("File tidak boleh kosong")
                        .status(HttpStatus.BAD_REQUEST).build();
            }
            Optional<NewsInspection> getBAP = repository.findById(id);
            if (!getBAP.isPresent()) {
                return GlobalResponse.builder().message("BAP tidak ditemukan")
                        .errorMessage("BAP dengna id : " + id + " tidak ditemukan").status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            if (getBAP.get().getFileName() != null) {
                return GlobalResponse.builder().errorMessage("BAP sudah ada file, tidak dapat upload file")
                        .message("BAP sudah dibuat, tidak dapat upload file")
                        .status(HttpStatus.BAD_REQUEST).build();
            }

            if (getBAP.get().getFile_path() != null) {
                File oldFile = new File(getBAP.get().getFile_path());
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }

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

    public GlobalResponse deleteFile(Long id) {
        try {
            Optional<NewsInspection> getBAP = repository.findById(id);
            if (!getBAP.isPresent()) {
                return GlobalResponse.builder().message("BAP tidak ditemukan")
                        .errorMessage("BAP dengna id : " + id + " tidak ditemukan").status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            // if (getBAP.get().getFile_path() != null) {
            //     File oldFile = new File(getBAP.get().getFile_path());
            //     if (oldFile.exists()) {
            //         oldFile.delete();
            //     }
            // }

            NewsInspection bap = getBAP.get();
            bap.setIs_delete(1);
            bap.setUpdated_at(new Date());
            repository.save(bap);
            
            return GlobalResponse
                    .builder()
                    .message("Berhasil menghapus data")
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
