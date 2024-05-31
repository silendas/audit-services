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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.cms.audit.api.Common.constant.FileStorageFU;
import com.cms.audit.api.Common.constant.FolderPath;
import com.cms.audit.api.Common.constant.SpecificationFIlter;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.pdf.GeneratePdf;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.PDFResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.FollowUp.dto.FollowUpDTO;
import com.cms.audit.api.FollowUp.dto.PatchFollowUpDTO;
import com.cms.audit.api.FollowUp.models.EStatusFollowup;
import com.cms.audit.api.FollowUp.models.FollowUp;
import com.cms.audit.api.FollowUp.repository.FollowUpRepository;
import com.cms.audit.api.FollowUp.repository.PagFollowup;
import com.cms.audit.api.Management.Penalty.dto.response.PenaltyInterface;
import com.cms.audit.api.Management.Penalty.models.Penalty;
import com.cms.audit.api.Management.Penalty.repository.PenaltyRepository;
import com.cms.audit.api.Management.User.models.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FollowupService {
    @Autowired
    private FollowUpRepository repository;

    @Autowired
    private FileStorageFU fileStorageService;

    @Autowired
    private PagFollowup pag;

    @Autowired
    private PenaltyRepository penaltyRepository;

    private final String FOLDER_PATH = FolderPath.FOLDER_PATH_UPLOAD_FOLLOW_UP;

    public GlobalResponse getAll(String name, Long branch, int page, int size, Date start_date, Date end_date) {
        try {
            User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Page<FollowUp> response = null;
            Specification<FollowUp> spec = Specification
                    .where(new SpecificationFIlter<FollowUp>().nameLike(name))
                    .and(new SpecificationFIlter<FollowUp>().branchIdEqual(branch))
                    .and(new SpecificationFIlter<FollowUp>().dateRange(start_date, end_date))
                    .and(new SpecificationFIlter<FollowUp>().orderByIdDesc());

            if (getUser.getLevel().getCode().equals("C")) {
                spec = spec.and(new SpecificationFIlter<FollowUp>().userId(getUser.getId()));
            } else if (getUser.getLevel().getCode().equals("B")) {
                spec = spec.and(new SpecificationFIlter<FollowUp>().getByRegionIds(getUser.getRegionId()));
            }
            response = pag.findAll(spec, PageRequest.of(page, size));

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
                user.put("level", fu.getUser().getLevel());
                fuMap.put("user", user);

                fuMap.put("branch", fu.getBranch());

                List<Object> listPenalty = new ArrayList<>();
                if (fu.getPenalty().isEmpty()) {
                    for (int u = 0; u < fu.getPenalty().size(); u++) {
                        Optional<Penalty> getPenalty = penaltyRepository.findById(fu.getPenalty().get(u));
                        if (!getPenalty.isPresent()) {
                            return GlobalResponse.builder()
                                    .message("Penalty dengan id : " + fu.getPenalty().get(u) + " tidak ditemukan")
                                    .errorMessage("Tidak dapat menemukan penalty").status(HttpStatus.BAD_REQUEST)
                                    .build();
                        }
                        Map<String, Object> objPenalty = new LinkedHashMap<>();
                        objPenalty.put("id", getPenalty.get().getId());
                        objPenalty.put("code", getPenalty.get().getName());
                        listPenalty.add(objPenalty);
                    }
                    fuMap.put("penalty", listPenalty);
                } else {
                    fuMap.put("penalty", listPenalty);
                }

                Map<String, Object> clarification = new LinkedHashMap<>();
                clarification.put("id", fu.getClarification().getId());
                clarification.put("code", fu.getClarification().getCode());
                if (fu.getClarification().getEvaluation_limitation() != null) {
                    clarification.put("evaluation_limitation",
                            convertDateToRoman.convertDateToString(fu.getClarification().getEvaluation_limitation()));
                } else {
                    clarification.put("evaluation_limitation", "-");
                }
                fuMap.put("clarification", clarification);

                fuMap.put("code", fu.getCode());
                fuMap.put("charging_costs", fu.getCharging_costs());
                fuMap.put("description", fu.getDescription());
                fuMap.put("note", fu.getNote());
                fuMap.put("penalty_relization", fu.getPenaltyRealization());
                fuMap.put("status", fu.getStatus());
                fuMap.put("filename", fu.getFilename());
                fuMap.put("file_path", fu.getFilePath());
                fuMap.put("is_penalty", fu.getIsPenalty());
                fuMap.put("created_at", fu.getCreated_at());

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
                        .message("Data tidak ditemukan").data(response)
                        .status(HttpStatus.OK)
                        .build();
            }
            return GlobalResponse
                    .builder()
                    .data(parent)
                    .message("Berhasil menampilkan data")
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
                return GlobalResponse.builder().message("failed").message("Followup with id: " + id + " not found ")
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
            user.put("level", fu.getUser().getLevel());
            fuMap.put("user", user);

            fuMap.put("branch", fu.getBranch());

            List<Object> listPenalty = new ArrayList<>();
            if (!fu.getPenalty().isEmpty()) {
                for (int i = 0; i < fu.getPenalty().size(); i++) {
                    Optional<Penalty> getPenalty = penaltyRepository.findById(fu.getPenalty().get(i));
                    if (!getPenalty.isPresent()) {
                        return GlobalResponse.builder()
                                .message("Penalty dengan id : " + fu.getPenalty().get(i) + " tidak ditemukan")
                                .errorMessage("Tidak dapat menemukan penalty").status(HttpStatus.BAD_REQUEST).build();
                    }
                    Map<String, Object> objPenalty = new LinkedHashMap<>();
                    objPenalty.put("id", getPenalty.get().getId());
                    objPenalty.put("code", getPenalty.get().getName());
                    listPenalty.add(objPenalty);
                }
                fuMap.put("penalty", listPenalty);
            } else {
                fuMap.put("penalty", listPenalty);
            }

            Map<String, Object> clarification = new LinkedHashMap<>();
            clarification.put("id", fu.getClarification().getId());
            clarification.put("code", fu.getClarification().getCode());
            if (fu.getClarification().getEvaluation_limitation() != null) {
                clarification.put("evaluation_limitation",
                        convertDateToRoman.convertDateToString(fu.getClarification().getEvaluation_limitation()));
            } else {
                clarification.put("evaluation_limitation", null);
            }
            fuMap.put("clarification", clarification);

            fuMap.put("code", fu.getCode());
            fuMap.put("charging_costs", fu.getCharging_costs());
            fuMap.put("description", fu.getDescription());
            fuMap.put("note", fu.getNote());
            fuMap.put("penalty_relization", fu.getPenaltyRealization());
            fuMap.put("status", fu.getStatus());
            fuMap.put("filename", fu.getFilename());
            fuMap.put("file_path", fu.getFilePath());
            fuMap.put("is_penalty", fu.getIsPenalty());
            fuMap.put("created_at", fu.getCreated_at());
            return GlobalResponse
                    .builder()
                    .data(fuMap)
                    .message("Berhasil menampilkan data")
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
            if(dto.getFollowup_id() == null) {
                return GlobalResponse.builder().errorMessage("Tindak lanjut harus diisi")
                        .message("Follow Up with id:" + dto.getFollowup_id() + " is not found")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            } else if (dto.getPenalty_id() == null) {
                return GlobalResponse.builder().errorMessage("Penalty harus diisi")
                        .message("Follow Up with id:" + dto.getFollowup_id() + " is not found")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            } else if (dto.getDescription() == null) {
                return GlobalResponse.builder().errorMessage("Deskripsi harus diisi")
                        .message("Follow Up with id:" + dto.getFollowup_id() + " is not found")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            } else if (dto.getCharging_costs() == null) {
                return GlobalResponse.builder().errorMessage("Biaya harus diisi")
                        .message("Follow Up with id:" + dto.getFollowup_id() + " is not found")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            Optional<FollowUp> getFollowUp = repository.findById(dto.getFollowup_id());
            if (!getFollowUp.isPresent()) {
                return GlobalResponse.builder().errorMessage("TIndak lanjut tidak ditemukan")
                        .message("Follow Up with id:" + dto.getFollowup_id() + " is not found")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            List<Long> listPenalty = new ArrayList<>();
            if (!dto.getPenalty_id().isEmpty()) {
                for (int i = 0; i < dto.getPenalty_id().size(); i++) {
                    Optional<Penalty> penalty = penaltyRepository.findById(dto.getPenalty_id().get(i));
                    if (!penalty.isPresent()) {
                        return GlobalResponse.builder()
                                .message("Penalty with id:" + dto.getPenalty_id() + " is not found")
                                .status(HttpStatus.BAD_REQUEST).errorMessage("Penalty not found")
                                .build();
                    }
                    listPenalty.add(penalty.get().getId());
                }
            }

            FollowUp followUp = getFollowUp.get();
            followUp.setPenalty(listPenalty);
            if (dto.getCharging_costs() != null) {
                followUp.setCharging_costs(dto.getCharging_costs());
            } else {
                followUp.setCharging_costs(0L);
            }
            followUp.setDescription(dto.getDescription());
            if (dto.getPenalty_id() != null) {
                followUp.setIsPenalty(1L);
            } else {
                followUp.setIsPenalty(0L);
            }
            followUp.setStatus(EStatusFollowup.PROGRESS);
            FollowUp response1 = repository.save(followUp);

            List<Penalty> penalties = penaltyRepository.findAllPenalty();
            PDFResponse generate = GeneratePdf.generateFollowUpPDF(response1, penalties);

            FollowUp edit = response1;
            edit.setFilename(generate.getFileName());
            edit.setFilePath(generate.getFilePath());
            Map<String, Object> dataResponse = new LinkedHashMap<>();

            try {
                FollowUp getResponse = repository.save(edit);

                Map<String, Object> mappingRes = new LinkedHashMap<>();
                mappingRes.put("id", getResponse.getId());
                mappingRes.put("file_name", getResponse.getFilename());
                mappingRes.put("file_path", getResponse.getFilePath());

                dataResponse.put("followup", mappingRes);

            } catch (Exception e) {
                return GlobalResponse
                        .builder()
                        .error(e)
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build();
            }

            return GlobalResponse.builder().message("Berhasil menambahkan data").data(dataResponse)
                    .status(HttpStatus.OK).build();

        } catch (Exception e) {
            return GlobalResponse
                    .builder()
                    .error(e)
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public ResponseEntity<Object> edit(Long id, FollowUpDTO dto) {

        Optional<FollowUp> getFollowUp = repository.findById(id);
        if (!getFollowUp.isPresent()) {
            return ResponseEntittyHandler.errorResponse("Tindak lanjut tidak ditemukan",
                    "Follow up with id:" + id + " is not found", HttpStatus.BAD_REQUEST);
        }

        if (dto.getPenalty_id() == null && dto.getPenalty_id().isEmpty()) {
            dto.setPenalty_id(getFollowUp.get().getPenalty());
        }
        if (dto.getDescription() == null && dto.getDescription().equals("")) {
            dto.setDescription(getFollowUp.get().getDescription());
        }
        if (dto.getCharging_costs() == null) {
            dto.setCharging_costs(getFollowUp.get().getCharging_costs());
        }

        FollowUp followUp = getFollowUp.get();
        followUp.setPenalty(dto.getPenalty_id());
        followUp.setDescription(dto.getDescription());
        if (dto.getCharging_costs() != null) {
            followUp.setCharging_costs(dto.getCharging_costs());
        } else {
            followUp.setCharging_costs(getFollowUp.get().getCharging_costs());
        }
        if ( dto.getPenalty_id() != null && !dto.getPenalty_id().isEmpty()) {
            followUp.setIsPenalty(1L);
        } else {
            followUp.setIsPenalty(0L);
        }

        FollowUp response = repository.save(followUp);
        return ResponseEntittyHandler.allHandler(response, "Berhasil", HttpStatus.OK, null);
    }

    
    public ResponseEntity<Object> patch(Long id, PatchFollowUpDTO dto) {

        Optional<FollowUp> getFollowUp = repository.findById(id);
        if (!getFollowUp.isPresent()) {
            return ResponseEntittyHandler.errorResponse("Tindak lanjut tidak ditemukan",
                    "Follow up with id:" + id + " is not found", HttpStatus.BAD_REQUEST);
        }

        if ( dto.getNote() == null && dto.getNote().equals("")) {
            dto.setNote(getFollowUp.get().getNote());
        }
        if(dto.getPenalty_realization() == null && dto.getPenalty_realization().isEmpty()) {
            dto.setPenalty_realization(getFollowUp.get().getPenaltyRealization());
        }

        FollowUp followUp = getFollowUp.get();
        followUp.setPenaltyRealization(dto.getPenalty_realization());
        followUp.setNote(dto.getNote());
        followUp.setStatus(EStatusFollowup.CLOSE);
        FollowUp response = repository.save(followUp);

        return ResponseEntittyHandler.allHandler(response, "Berhasil", HttpStatus.OK, null);
    }

    public GlobalResponse uploadFile(MultipartFile file, Long id) {
        try {

            if (file == null) {
                return GlobalResponse.builder().message("File tidak boleh kosong").errorMessage("File tidak boleh kosong")
                        .status(HttpStatus.BAD_REQUEST).build();
            }

            Optional<FollowUp> getFollowUp = repository.findById(id);
            if (!getFollowUp.isPresent()) {
                return GlobalResponse.builder().errorMessage("Tindak lanjut tidak ditemukan")
                        .status(HttpStatus.BAD_REQUEST)
                        .message("Follow up with id:" + id + " is not found").build();
            }

            if(getFollowUp.get().getFilename() != null && getFollowUp.get().getStatus() == EStatusFollowup.CLOSE) {
                return GlobalResponse.builder().errorMessage("File sudah di upload")
                        .status(HttpStatus.BAD_REQUEST)
                        .message("File sudah di upload").build();
            }

            if (getFollowUp.get().getFilePath() != null) {
                File oldFile = new File(getFollowUp.get().getFilePath());
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }

            FollowUp followUp = getFollowUp.get();

            if (file != null) {
                String fileName = fileStorageService.storeFile(file);
                String path = FOLDER_PATH + fileName;
                String filePath = path;
                followUp.setFilename(fileName);
                followUp.setStatus(EStatusFollowup.REALIZE);
                followUp.setFilePath(filePath);
            }

            FollowUp getResponse = repository.save(followUp);

            Map<String, Object> returnResponse = new LinkedHashMap<>();
            Map<String, Object> mappingRes = new LinkedHashMap<>();
            mappingRes.put("id", getResponse.getId());
            mappingRes.put("file_name", getResponse.getFilename());
            mappingRes.put("file_path", getResponse.getFilePath());

            returnResponse.put("followup", mappingRes);

            return GlobalResponse
                    .builder()
                    .message("Berhasil upload file")
                    .data(returnResponse)
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

        if (!response.getStatus().equals(EStatusFollowup.CLOSE)) {
            FollowUp followUp = response;
            followUp.setStatus(EStatusFollowup.PROGRESS);
            repository.save(followUp);
        }

        return response;
    }
}
