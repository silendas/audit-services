package com.cms.audit.api.Clarifications.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.tomcat.util.http.fileupload.impl.IOFileUploadException;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.cms.audit.api.Clarifications.dto.GenerateCKDTO;
import com.cms.audit.api.Clarifications.dto.IdentificationDTO;
import com.cms.audit.api.Clarifications.dto.InputClarificationDTO;
import com.cms.audit.api.Clarifications.dto.response.NumberClarificationInterface;
import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Clarifications.models.EStatusClarification;
import com.cms.audit.api.Clarifications.repository.ClarificationRepository;
import com.cms.audit.api.Clarifications.repository.PagClarification;
import com.cms.audit.api.Common.constant.FileStorageService;
import com.cms.audit.api.Common.constant.FolderPath;
import com.cms.audit.api.Common.constant.SpecificationFIlter;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.dto.GapDTO;
import com.cms.audit.api.Common.exception.ResourceNotFoundException;
import com.cms.audit.api.Common.pdf.GeneratePdf;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.PDFResponse;
import com.cms.audit.api.FollowUp.models.EStatusFollowup;
import com.cms.audit.api.FollowUp.models.FollowUp;
import com.cms.audit.api.FollowUp.repository.FollowUpRepository;
import com.cms.audit.api.Management.Case.models.Case;
import com.cms.audit.api.Management.Case.repository.CaseRepository;
import com.cms.audit.api.Management.CaseCategory.models.CaseCategory;
import com.cms.audit.api.Management.CaseCategory.repository.CaseCategoryRepository;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.BranchOffice.repository.BranchRepository;
import com.cms.audit.api.Management.Penalty.models.Penalty;
import com.cms.audit.api.Management.Penalty.repository.PenaltyRepository;
import com.cms.audit.api.Management.ReportType.models.ReportType;
import com.cms.audit.api.Management.ReportType.repository.ReportTypeRepository;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.NewsInspection.models.NewsInspection;
import com.cms.audit.api.NewsInspection.repository.NewsInspectionRepository;

@Service
public class ClarificationService {

        @Autowired
        private ClarificationRepository repository;

        @Autowired
        private FileStorageService fileStorageService;

        @Autowired
        private ReportTypeRepository reportTypeRepository;

        @Autowired
        private CaseRepository caseRepository;

        @Autowired
        private CaseCategoryRepository caseCategoryRepository;

        @Autowired
        private BranchRepository branchRepository;

        @Autowired
        private PenaltyRepository penaltyRepository;

        @Autowired
        private NewsInspectionRepository newsInspectionRepository;

        @Autowired
        private FollowUpRepository followUpRepository;

        @Autowired
        private PagClarification pag;

        private final String UPLOAD_FOLDER_PATH = FolderPath.FOLDER_PATH_UPLOAD_CLARIFICATION;

        public GlobalResponse getAll(String name, Long branchId, int page, int size, Date start_date, Date end_date) {
                try {
                        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                        Page<Clarification> response = null;
                        Specification<Clarification> spec = Specification
                                        .where(new SpecificationFIlter<Clarification>().nameLike(name))
                                        .and(new SpecificationFIlter<Clarification>().branchIdEqual(branchId))
                                        .and(new SpecificationFIlter<Clarification>().dateRange(start_date, end_date))
                                        .and(new SpecificationFIlter<Clarification>().orderByIdDesc());

                        if (getUser.getLevel().getCode().equals("C")) {
                                spec = spec.and(new SpecificationFIlter<Clarification>().userId(getUser.getId()));
                        } else if (getUser.getLevel().getCode().equals("B")) {
                                spec = spec.and(new SpecificationFIlter<Clarification>()
                                                .getByRegionIds(getUser.getRegionId()))
                                                .or(new SpecificationFIlter<Clarification>().userId(getUser.getId()));
                        }
                        response = pag.findAll(spec, PageRequest.of(page, size));
                        List<Object> listCl = new ArrayList<>();
                        for (int i = 0; i < response.getContent().size(); i++) {
                                Map<String, Object> clarification = new LinkedHashMap<>();
                                clarification.put("id", response.getContent().get(i).getId());

                                Map<String, Object> user = new LinkedHashMap<>();
                                user.put("id", response.getContent().get(i).getUser().getId());
                                user.put("fullname", response.getContent().get(i).getUser().getFullname());
                                user.put("initial_name", response.getContent().get(i).getUser().getInitial_name());
                                user.put("email", response.getContent().get(i).getUser().getEmail());
                                user.put("level", response.getContent().get(i).getUser().getLevel());
                                clarification.put("user", user);

                                clarification.put("branch", response.getContent().get(i).getBranch());

                                Map<String, Object> cases = new LinkedHashMap<>();
                                cases.put("id", response.getContent().get(i).getCases().getId());
                                cases.put("name", response.getContent().get(i).getCases().getName());
                                cases.put("code", response.getContent().get(i).getCases().getCode());
                                clarification.put("cases", cases);

                                Map<String, Object> cc = new LinkedHashMap<>();
                                cc.put("id", response.getContent().get(i).getCaseCategory().getId());
                                cc.put("name", response.getContent().get(i).getCaseCategory().getName());
                                clarification.put("case_category", cc);

                                clarification.put("code", response.getContent().get(i).getCode());
                                clarification.put("priority", response.getContent().get(i).getPriority());
                                clarification.put("file_name", response.getContent().get(i).getFilename());
                                clarification.put("file_path", response.getContent().get(i).getFile_path());
                                clarification.put("description", response.getContent().get(i).getDescription());
                                clarification.put("status", response.getContent().get(i).getStatus());
                                clarification.put("is_follow_up", response.getContent().get(i).getIs_follow_up());
                                if (response.getContent().get(i).getFilename() == null) {
                                        clarification.put("is_flag", 1);
                                } else {
                                        clarification.put("is_flag", 0);
                                }
                                clarification.put("created_at", response.getContent().get(i).getCreated_at());
                                listCl.add(clarification);
                        }
                        if (response.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Data tidak ditemukan")
                                                .status(HttpStatus.OK).data(response)
                                                .build();
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
                        parent.put("content", listCl);
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
                        Clarification response = repository.findById(id).orElse(null);
                        if (response == null) {
                                return GlobalResponse
                                                .builder()
                                                .message("Data tidak ditemukan")
                                                .status(HttpStatus.BAD_REQUEST).data(response)
                                                .build();
                        }
                        Map<String, Object> clarification = new LinkedHashMap<>();
                        clarification.put("id", response.getId());

                        Map<String, Object> user = new LinkedHashMap<>();
                        user.put("id", response.getUser().getId());
                        user.put("fullname", response.getUser().getFullname());
                        user.put("initial_name", response.getUser().getInitial_name());
                        user.put("email", response.getUser().getEmail());
                        user.put("level", response.getUser().getLevel());
                        clarification.put("user", user);

                        clarification.put("branch", response.getBranch());

                        Map<String, Object> cases = new LinkedHashMap<>();
                        cases.put("id", response.getCases().getId());
                        cases.put("name", response.getCases().getName());
                        cases.put("code", response.getCases().getCode());
                        clarification.put("cases", cases);

                        Map<String, Object> cc = new LinkedHashMap<>();
                        cc.put("id", response.getCaseCategory().getId());
                        cc.put("name", response.getCaseCategory().getName());
                        clarification.put("case_category", cc);

                        clarification.put("code", response.getCode());
                        clarification.put("priority", response.getPriority());
                        clarification.put("file_name", response.getFilename());
                        clarification.put("file_path", response.getFile_path());
                        clarification.put("description", response.getDescription());
                        clarification.put("location", response.getLocation());
                        clarification.put("auditee", response.getAuditee());
                        clarification.put("auditee_leader", response.getAuditee_leader());
                        clarification.put("start_date_realization", response.getStart_date_realization());
                        clarification.put("end_date_realization", response.getEnd_date_realization());
                        if (response.getEnd_date_realization() != null
                                        && response.getStart_date_realization() != null) {
                                GapDTO gap = convertDateToRoman.calculateDateDifference(
                                                response.getStart_date_realization(),
                                                response.getEnd_date_realization());
                                String gapDay = "";
                                if (gap.getDay() > 0) {
                                        gapDay = gap.getDay() + " hari ";
                                } else {
                                        if (gap.getHour() != 0) {
                                                gapDay = gapDay + gap.getHour() + " jam ";
                                        }
                                        if (gap.getMinute() != 0) {
                                                gapDay = gapDay + gap.getMinute() + " menit ";
                                        }
                                        if (gap.getSecond() != 0) {
                                                gapDay = gapDay + gap.getSecond() + " detik";
                                        }
                                }
                                clarification.put("finish", gapDay);
                        } else {
                                clarification.put("finish", null);
                        }
                        if (response.getEnd_date_realization() != null && response.getEvaluation_limitation() != null) {
                                GapDTO gap = convertDateToRoman.calculateDateDifference(
                                                response.getEnd_date_realization(),
                                                response.getEvaluation_limitation());
                                String gapDay = "";
                                if (gap.getDay() > 0) {
                                        gapDay = gap.getDay() + " hari ";
                                } else {
                                        if (gap.getHour() != 0) {
                                                gapDay = gapDay + gap.getHour() + " jam ";
                                        }
                                        if (gap.getMinute() != 0) {
                                                gapDay = gapDay + gap.getMinute() + " menit ";
                                        }
                                        if (gap.getSecond() != 0) {
                                                gapDay = gapDay + gap.getSecond() + " detik";
                                        }
                                }
                                clarification.put("gap", gapDay);
                        } else {
                                clarification.put("gap", null);
                        }

                        List<Object> recomendation = new ArrayList<>();
                        if (response.getRecomendation() != null) {
                                for (Long penalty : response.getRecomendation()) {
                                        Optional<Penalty> penaltyGet = penaltyRepository.findById(penalty);
                                        Map<String, Object> recomendationSet = new LinkedHashMap<>();
                                        recomendationSet.put("id", penaltyGet.get().getId());
                                        recomendationSet.put("name", penaltyGet.get().getName());
                                        recomendation.add(recomendationSet);
                                }
                                clarification.put("recomendation", recomendation);
                        } else {
                                clarification.put("recomendation", recomendation);
                        }

                        clarification.put("case_category", cc);
                        clarification.put("evaluation", response.getEvaluation());
                        clarification.put("status", response.getStatus());
                        clarification.put("nominal_loss", response.getNominal_loss());
                        clarification.put("evaluation_limitation", response.getEvaluation_limitation());
                        if (response.getFilename() == null) {
                                clarification.put("is_flag", 1);
                        } else {
                                clarification.put("is_flag", 0);
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil menampilkan data")
                                        .data(clarification)
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

        public GlobalResponse generateCK(GenerateCKDTO dto) {
                try {
                        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                        if (dto.getBranch_id() == null) {
                                return GlobalResponse.builder().errorMessage("Branch harus diisi")
                                                .message("Branch harus diisi")
                                                .status(HttpStatus.BAD_REQUEST).build();
                        } else if (dto.getCase_category_id() == null) {
                                return GlobalResponse.builder().errorMessage("Kategori harus diisi")
                                                .message("Kategori harus diisi")
                                                .status(HttpStatus.BAD_REQUEST).build();
                        } else if (dto.getCase_id() == null) {
                                return GlobalResponse.builder().errorMessage("Divisi harus diisi")
                                                .message("Divisi harus diisi")
                                                .status(HttpStatus.BAD_REQUEST).build();
                        }

                        Optional<CaseCategory> setCaseCaegoryId = caseCategoryRepository
                                        .findById(dto.getCase_category_id());
                        if (!setCaseCaegoryId.isPresent()) {
                                return GlobalResponse.builder().errorMessage("Kategori tidak ditemukan")
                                                .message("Kategori dengan id : " + dto.getCase_id()
                                                                + " tidak ditemukan")
                                                .status(HttpStatus.BAD_REQUEST).build();
                        }
                        Optional<Case> getCase = caseRepository.findById(dto.getCase_id());
                        if (!getCase.isPresent()) {
                                return GlobalResponse.builder().errorMessage("Divisi tidak ditemukan")
                                                .message("Divisi dengan id : " + dto.getCase_id() + " tidak ditemukan")
                                                .status(HttpStatus.BAD_REQUEST).build();
                        }

                        Long reportNumber = null;
                        String rptNum = null;

                        Optional<NumberClarificationInterface> checkClBefore = repository
                                        .checkNumberClarification(user.getId());
                        if (checkClBefore.isPresent()) {
                                if (checkClBefore.get().getCreated_Year().longValue() == Long
                                                .valueOf(convertDateToRoman.getIntYear())) {
                                        reportNumber = checkClBefore.get().getReport_Number() + 1;
                                        if (reportNumber < 10) {
                                                rptNum = "00" + reportNumber;
                                        } else if (reportNumber < 100) {
                                                rptNum = "0" + reportNumber;
                                        } else {
                                                rptNum = reportNumber.toString();
                                        }
                                } else {
                                        rptNum = "001";
                                        reportNumber = Long.valueOf(1);
                                }
                        } else {
                                rptNum = "001";
                                reportNumber = Long.valueOf(1);
                        }

                        Optional<Branch> branch = branchRepository.findById(dto.getBranch_id());
                        if (!branch.isPresent()) {
                                return GlobalResponse.builder().errorMessage("Branch tidak ditemukan")
                                                .message("Branch dengan id : " + dto.getBranch_id()
                                                                + " tidak ditemukan")
                                                .status(HttpStatus.BAD_REQUEST).build();
                        }

                        String branchName = branch.get().getName();
                        String initialName = user.getInitial_name();
                        String caseName = getCase.get().getCode();
                        String lvlCode = user.getLevel().getCode();
                        String romanMonth = convertDateToRoman.getRomanMonth();
                        Integer thisYear = convertDateToRoman.getIntYear();

                        Optional<ReportType> reportType = reportTypeRepository.findById(1L);

                        String reportCode = rptNum + lvlCode + "/" + initialName + "-" + caseName
                                        + "/" + reportType.get().getCode() + "/" + branchName + "/" + romanMonth + "/"
                                        + thisYear;

                        Clarification clarification = new Clarification(
                                        null,
                                        user,
                                        branch.get(),
                                        getCase.get(),
                                        setCaseCaegoryId.get(),
                                        reportType.get(),
                                        reportNumber,
                                        reportCode,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        EStatusClarification.INPUT,
                                        user.getId(),
                                        null,
                                        null,
                                        new Date(),
                                        new Date());

                        repository.save(clarification);

                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil generate klarifikasi")
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

        public GlobalResponse inputClarification(InputClarificationDTO dto) {
                try {
                        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                        if (dto.getAuditee() == null) {
                                return GlobalResponse
                                                .builder()
                                                .message("Auditee harus diisi")
                                                .errorMessage("Auditee harus diisi")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        } else if (dto.getClarification_id() == null) {
                                return GlobalResponse
                                                .builder()
                                                .message("Id klarifikasi harus diisi")
                                                .errorMessage("Id klarifikasi harus diisi")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        } else if (dto.getAuditee() == null) {
                                return GlobalResponse
                                                .builder()
                                                .message("Auditee harus diisi")
                                                .errorMessage("Auditee harus diisi")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        } else if (dto.getAuditee_leader() == null) {
                                return GlobalResponse
                                                .builder()
                                                .message("Leader harus diisi")
                                                .errorMessage("Leader harus diisi")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        } else if (dto.getDescription() == null) {
                                return GlobalResponse
                                                .builder()
                                                .message("Deskripsi harus diisi")
                                                .errorMessage("Deskripsi harus diisi")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        } else if (dto.getEvaluation_limitation() == null) {
                                return GlobalResponse
                                                .builder()
                                                .message("Limitasi evaluasi harus diisi")
                                                .errorMessage("Limitasi evaluasi harus diisi")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        } else if (dto.getLocation() == null) {
                                return GlobalResponse
                                                .builder()
                                                .message("Lokasi harus diisi")
                                                .errorMessage("Lokasi harus diisi")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        } else if (dto.getPriority() == null) {
                                return GlobalResponse
                                                .builder()
                                                .message("Prioritas harus diisi")
                                                .errorMessage("Prioritas harus diisi")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }
                        Optional<Clarification> getClarification = repository.findById(dto.getClarification_id());
                        if (!getClarification.isPresent()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Clarificaiton tidak ditemukan")
                                                .errorMessage("Clarification dengan id : " + dto.getClarification_id()
                                                                + " tidak ditemukan")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }

                        if (getClarification.get().getUser().getId() != user.getId()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Tidak bisa melakukan klarifikasi milik orang lain")
                                                .errorMessage("Tidak bisa melakukan klarifikasi milik orang lain")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }

                        User setUserId = User.builder().id(getClarification.get().getUser().getId()).build();
                        Case setCaseId = Case.builder().id(getClarification.get().getCases().getId()).build();
                        CaseCategory setCaseCaegoryId = CaseCategory.builder()
                                        .id(getClarification.get().getCaseCategory().getId()).build();
                        ReportType setReportTypeId = ReportType.builder()
                                        .id(getClarification.get().getReportType().getId())
                                        .build();

                        Clarification clarification = new Clarification(
                                        dto.getClarification_id(),
                                        setUserId,
                                        getClarification.get().getBranch(),
                                        setCaseId,
                                        setCaseCaegoryId,
                                        setReportTypeId,
                                        getClarification.get().getReport_number(),
                                        getClarification.get().getCode(),
                                        0L,
                                        dto.getEvaluation_limitation(),
                                        dto.getLocation(),
                                        dto.getAuditee(),
                                        dto.getAuditee_leader(),
                                        null,
                                        null,
                                        dto.getDescription(),
                                        null,
                                        dto.getPriority(),
                                        null,
                                        null,
                                        EStatusClarification.DOWNLOAD,
                                        getClarification.get().getUser().getId(),
                                        new Date(),
                                        null,
                                        getClarification.get().getCreated_at(),
                                        new Date());

                        Clarification response = repository.save(clarification);

                        String formulir = "FM/SPI-05/00";

                        PDFResponse generatePDF = GeneratePdf.generateClarificationPDF(response, formulir);

                        Clarification clarification2 = response;
                        clarification2.setFilename(generatePDF.fileName);
                        clarification2.setFile_path(generatePDF.filePath);

                        Clarification getResponse = repository.save(clarification2);

                        Map<String, Object> returnResponse = new LinkedHashMap<>();
                        Map<String, Object> mappingRes = new LinkedHashMap<>();
                        mappingRes.put("id", getResponse.getId());
                        mappingRes.put("file_name", getResponse.getFilename());
                        mappingRes.put("file_path", getResponse.getFile_path());

                        returnResponse.put("clarification", mappingRes);

                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil input klarifikasi")
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

        public GlobalResponse identificationClarification(IdentificationDTO dto) {
                try {
                        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                        if (dto.getClarification_id() == null) {
                                return GlobalResponse
                                                .builder()
                                                .message("Clarification harus diisi")
                                                .errorMessage("Clarification harus diisi")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        } else if (dto.getNominal_loss() == null) {
                                return GlobalResponse
                                                .builder()
                                                .message("Nominal loss harus diisi")
                                                .errorMessage("Nominal loss harus diisi")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        } else if (dto.getIs_followup() == null) {
                                return GlobalResponse
                                                .builder()
                                                .message("Is followup harus diisi")
                                                .errorMessage("Is followup harus diisi")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        } else if (dto.getEvaluation() == null) {
                                return GlobalResponse
                                                .builder()
                                                .message("Evaluation harus diisi")
                                                .errorMessage("Evaluation harus diisi")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        } else if (dto.getRecommendation().isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Recommendation harus diisi")
                                                .errorMessage("Recommendation harus diisi")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }
                        Optional<Clarification> getBefore = repository.findById(dto.getClarification_id());
                        if (!getBefore.isPresent()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Clarification tidak dapat ditemukan")
                                                .errorMessage("Clarificaion with id :" + dto.getClarification_id()
                                                                + " Not found")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }

                        if (getBefore.get().getUser().getId() != user.getId()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Tidak bisa melakukan klarifikasi milik orang lain")
                                                .errorMessage("Tidak bisa melakukan klarifikasi milik orang lain")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }
                        if (dto.getIs_followup() == null) {
                                dto.setIs_followup(0L);
                        }
                        if (dto.getNominal_loss() == null) {
                                dto.setNominal_loss(0L);
                        }
                        List<Long> recommendation = new ArrayList<>();
                        if (dto.getRecommendation().isEmpty()) {
                                dto.setRecommendation(recommendation);
                        }
                        Clarification clarification = new Clarification(
                                        dto.getClarification_id(),
                                        getBefore.get().getUser(),
                                        getBefore.get().getBranch(),
                                        getBefore.get().getCases(),
                                        getBefore.get().getCaseCategory(),
                                        getBefore.get().getReportType(),
                                        getBefore.get().getReport_number(),
                                        getBefore.get().getCode(),
                                        dto.getNominal_loss(),
                                        getBefore.get().getEvaluation_limitation(),
                                        getBefore.get().getLocation(),
                                        getBefore.get().getAuditee(),
                                        getBefore.get().getAuditee_leader(),
                                        getBefore.get().getFilename(),
                                        getBefore.get().getFile_path(),
                                        getBefore.get().getDescription(),
                                        dto.getRecommendation(),
                                        getBefore.get().getPriority(),
                                        dto.getEvaluation(),
                                        dto.getIs_followup(),
                                        EStatusClarification.DONE,
                                        getBefore.get().getUser().getId(),
                                        getBefore.get().getStart_date_realization(),
                                        new Date(),
                                        getBefore.get().getCreated_at(),
                                        new Date());

                        Clarification response = repository.save(clarification);

                        Long reportNumber = null;
                        String rptNum = null;

                        Map<String, Object> returnResponse = new LinkedHashMap<>();

                        if (dto.getNominal_loss() != 0 && dto.getNominal_loss() != null) {
                                Optional<NumberClarificationInterface> checkClBefore = newsInspectionRepository
                                                .checkNumberBAP(response.getUser().getId());
                                if (checkClBefore.isPresent()) {
                                        if (checkClBefore.get().getCreated_Year().longValue() == Long
                                                        .valueOf(convertDateToRoman.getIntYear())) {
                                                reportNumber = checkClBefore.get().getReport_Number() + 1;
                                                if (reportNumber < 10) {
                                                        rptNum = "00" + reportNumber;
                                                } else if (reportNumber < 100) {
                                                        rptNum = "0" + reportNumber;
                                                } else {
                                                        rptNum = reportNumber.toString();
                                                }
                                        } else {
                                                rptNum = "001";
                                                reportNumber = Long.valueOf(1);
                                        }
                                } else {
                                        rptNum = "001";
                                        reportNumber = Long.valueOf(1);
                                }

                                String branchName = response.getBranch().getName();
                                String initialName = response.getUser().getInitial_name();
                                String caseName = response.getCases().getCode();
                                String lvlCode = response.getUser().getLevel().getCode();
                                String romanMonth = convertDateToRoman.getRomanMonth();
                                Integer thisYear = convertDateToRoman.getIntYear();

                                Optional<ReportType> reportType = reportTypeRepository.findById(2L);

                                String reportCode = rptNum + lvlCode + "/" + initialName + "-" + caseName + "/"
                                                + reportType.get().getCode() + "/" + branchName + "/" + romanMonth + "/"
                                                + thisYear;
                                Clarification setClarificationId = Clarification.builder().id(response.getId()).build();

                                NewsInspection newsInspection = new NewsInspection(
                                                null,
                                                response.getUser(),
                                                response.getBranch(),
                                                setClarificationId,
                                                reportType.get(),
                                                null,
                                                null,
                                                reportNumber,
                                                reportCode,
                                                response.getNominal_loss(),
                                                response.getCreated_by(),
                                                new Date(),
                                                new Date());

                                NewsInspection getResponse = newsInspectionRepository.save(newsInspection);

                                Map<String, Object> mappingFU = new LinkedHashMap<>();
                                mappingFU.put("id", getResponse.getId());
                                mappingFU.put("code", getResponse.getCode());

                                returnResponse.put("bap", mappingFU);
                        }

                        if (dto.getIs_followup() != null && dto.getIs_followup() != 0) {
                                Optional<NumberClarificationInterface> checkTLBefore = followUpRepository
                                                .checkNumberFollowUp(response.getUser().getId());
                                if (checkTLBefore.isPresent()) {
                                        if (checkTLBefore.get().getCreated_Year().longValue() == Long
                                                        .valueOf(convertDateToRoman.getIntYear())) {
                                                reportNumber = checkTLBefore.get().getReport_Number() + 1;
                                                if (reportNumber < 10) {
                                                        rptNum = "00" + reportNumber;
                                                } else if (reportNumber < 100) {
                                                        rptNum = "0" + reportNumber;
                                                } else {
                                                        rptNum = reportNumber.toString();
                                                }
                                        } else {
                                                rptNum = "001";
                                                reportNumber = Long.valueOf(1);
                                        }
                                } else {
                                        rptNum = "001";
                                        reportNumber = Long.valueOf(1);
                                }

                                String branchName2 = response.getBranch().getName();
                                String initialName2 = response.getUser().getInitial_name();
                                String caseName2 = response.getCases().getCode();
                                String lvlCode2 = response.getUser().getLevel().getCode();
                                String romanMonth2 = convertDateToRoman.getRomanMonth();
                                Integer thisYear2 = convertDateToRoman.getIntYear();

                                Optional<ReportType> reportType2 = reportTypeRepository.findById(3L);

                                String reportCode2 = rptNum + lvlCode2 + "/" + initialName2 + "-" + caseName2 + "/"
                                                + reportType2.get().getCode() + "/" + branchName2 + "/" + romanMonth2
                                                + "/"
                                                + thisYear2;

                                User setUserId = User.builder().id(response.getUser().getId()).build();
                                Clarification setClarificationId = Clarification.builder().id(response.getId()).build();

                                FollowUp followUp = new FollowUp();
                                followUp.setClarification(setClarificationId);
                                followUp.setBranch(response.getBranch());
                                followUp.setUser(setUserId);
                                followUp.setReportType(reportType2.get());
                                followUp.setReport_number(reportNumber);
                                followUp.setPenalty(new ArrayList<>());
                                followUp.setCode(reportCode2);
                                followUp.setPenaltyRealization(new ArrayList<>());
                                followUp.setNote(null);
                                followUp.setStatus(EStatusFollowup.CREATE);
                                followUp.setCreated_by(response.getUser().getId());
                                followUp.setCreated_at(new Date());

                                followUpRepository.save(followUp);
                        }

                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil melakukan identifikasi klarifikasi")
                                        .data(returnResponse)
                                        .status(HttpStatus.OK)
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
                                return GlobalResponse
                                                .builder()
                                                .message("File tidak boleh kosong")
                                                .errorMessage("File tidak boleh kosong")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }
                        Optional<Clarification> getClarification = repository.findById(id);
                        if (!getClarification.isPresent()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Clarification tidak bisa ditemukan")
                                                .errorMessage("Clarifcation with id: " + id + " not found")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }

                        if (getClarification.get().getFilename() != null) {
                                if (getClarification.get().getStatus() == EStatusClarification.DONE || getClarification
                                                .get().getStatus() == EStatusClarification.IDENTIFICATION) {
                                        return GlobalResponse
                                                        .builder()
                                                        .message("File sudah di upload")
                                                        .errorMessage("File sudah di upload")
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }
                        }

                        // Hapus file lama jika ada
                        if (getClarification.get().getFile_path() != null) {
                                File oldFile = new File(getClarification.get().getFile_path());
                                if (oldFile.exists()) {
                                        oldFile.delete();
                                }
                        }

                        String fileName = fileStorageService.storeFile(file);

                        String path = UPLOAD_FOLDER_PATH + fileName;

                        Clarification clarification = getClarification.get();
                        clarification.setFilename(fileName);
                        clarification.setFile_path(path);
                        clarification.setStatus(EStatusClarification.IDENTIFICATION);
                        clarification.setUpdated_at(new Date());
                        Clarification getResponse = repository.save(clarification);

                        Map<String, Object> returnResponse = new LinkedHashMap<>();
                        Map<String, Object> mappingRes = new LinkedHashMap<>();
                        mappingRes.put("id", getResponse.getId());
                        mappingRes.put("file_name", getResponse.getFilename());
                        mappingRes.put("file_path", getResponse.getFile_path());

                        returnResponse.put("clarification", mappingRes);

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

        public Clarification downloadFile(String fileName) throws java.io.IOException, IOFileUploadException {
                Clarification response = repository.findByFilename(fileName)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "File not found with name: " + fileName));

                Clarification clarification = response;
                if (!response.getStatus().equals(EStatusClarification.DONE)
                                && !response.getStatus().equals(EStatusClarification.IDENTIFICATION)
                                && !response.getStatus().equals(EStatusClarification.UPLOAD)
                                && !response.getStatus().equals(EStatusClarification.INPUT)) {
                        clarification.setStatus(EStatusClarification.UPLOAD);
                }
                clarification.setUpdated_at(new Date());
                repository.save(clarification);

                return response;
        }

        public Clarification getFile(String fileName) throws java.io.IOException, IOFileUploadException {
                Clarification response = repository.findByFilename(fileName)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "File not found with name: " + fileName));

                return response;
        }

}
