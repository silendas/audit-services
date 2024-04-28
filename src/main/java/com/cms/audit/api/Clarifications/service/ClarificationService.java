package com.cms.audit.api.Clarifications.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
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
import com.cms.audit.api.Common.constant.convertDateToRoman;
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
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.BranchOffice.repository.BranchRepository;
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
        private BranchRepository branchRepository;

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
                        if (branchId != null && name != null && start_date != null && end_date != null) {
                                String likeName = name;
                                response = pag.findByAllFilter(likeName, start_date, end_date,
                                                PageRequest.of(page, size));
                        } else if (branchId != null) {
                                if (start_date != null && end_date != null) {
                                        response = pag.findByBranchIdByDate(branchId, start_date, end_date,
                                                        PageRequest.of(page, size));
                                } else if (name != null) {
                                        response = pag.findByFullnameLikeAndBranch(name, branchId,
                                                        PageRequest.of(page, size));
                                } else {
                                        response = pag.findByBranchId(branchId,
                                                        PageRequest.of(page, size));
                                }
                        } else if (name != null) {
                                String likeName = name;
                                if (start_date != null && end_date != null) {
                                        response = pag.findByFullnameLikeByDate(likeName, start_date, end_date,
                                                        PageRequest.of(page, size));
                                } else {
                                        response = pag.findByFullnameLike(likeName,
                                                        PageRequest.of(page, size));
                                }
                        } else {
                                if (getUser.getLevel().getCode().equals("C") ) {
                                        if (start_date != null && end_date != null) {
                                                response = pag.findClarificationInDateRangeAndUser(getUser.getId(),
                                                                start_date, end_date,
                                                                PageRequest.of(page, size));
                                        } else {
                                                response = pag.findByUserId(getUser.getId(),
                                                                PageRequest.of(page, size));
                                        }
                                } else if (getUser.getLevel().getCode().equals("B") ) {
                                        Pageable pageable = PageRequest.of(page, size);
                                        List<Clarification> lhaList = new ArrayList<>();
                                        for (int i = 0; i < getUser.getRegionId().size(); i++) {
                                                List<Clarification> clAgain = new ArrayList<>();
                                                if (start_date != null && end_date != null) {
                                                        response = pag.findByRegionIdAndDate(
                                                                        getUser.getRegionId().get(i), start_date, end_date,
                                                                        PageRequest.of(page, size));
                                                } else {
                                                        clAgain = repository.findByRegionId(getUser.getRegionId().get(i));

                                                }
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
                                                List<Clarification> pageContent = lhaList.subList(start, end);
                                                Page<Clarification> response2 = new PageImpl<>(pageContent, pageable,
                                                                lhaList.size());
                                                response = response2;
                                        } catch (Exception e) {
                                                return GlobalResponse
                                                                .builder()
                                                                .error(e)
                                                                .status(HttpStatus.BAD_REQUEST)
                                                                .build();
                                        }
                                } else if (getUser.getLevel().getCode().equals("A")  || getUser.getLevel().getCode().equals("A") ) {
                                        if (start_date != null && end_date != null) {
                                                response = pag.findClarificationInDateRange(start_date, end_date, PageRequest.of(page, size));
                                        }else{
                                                response = pag.findAllCLDetail(PageRequest.of(page, size));
                                        }
                                }
                        }
                        List<Object> listCl = new ArrayList<>();
                        for (int i = 0; i < response.getContent().size(); i++) {
                                Map<String, Object> clarification = new LinkedHashMap<>();
                                clarification.put("id", response.getContent().get(i).getId());

                                Map<String, Object> user = new LinkedHashMap<>();
                                user.put("id", response.getContent().get(i).getUser().getId());
                                user.put("fullname", response.getContent().get(i).getUser().getFullname());
                                user.put("initial_name", response.getContent().get(i).getUser().getInitial_name());
                                user.put("email", response.getContent().get(i).getUser().getEmail());
                                clarification.put("user", user);

                                Map<String, Object> branch = new LinkedHashMap<>();
                                branch.put("id", response.getContent().get(i).getBranch().getId());
                                branch.put("name", response.getContent().get(i).getBranch().getName());
                                clarification.put("branch", branch);

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
                                                .message("Data not found")
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
                                        .message("Success")
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
                                                .message("Data not found")
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
                        clarification.put("user", user);

                        Map<String, Object> branch = new LinkedHashMap<>();
                        branch.put("id", response.getBranch().getId());
                        branch.put("name", response.getBranch().getName());
                        clarification.put("branch", branch);

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
                        clarification.put("recomendation", response.getRecomendation());
                        clarification.put("evaluation", response.getEvaluation());
                        clarification.put("status", response.getStatus());
                        clarification.put("nominal_loss", response.getNominal_loss());
                        if (response.getEvaluation_limitation() == null) {
                                clarification.put("evaluation_limitation", null);
                        } else {
                                clarification.put("evaluation_limitation",
                                                convertDateToRoman.convertDateToString(
                                                                response.getEvaluation_limitation()));
                        }
                        clarification.put("is_follow_up", response.getIs_follow_up());
                        if (response.getFilename() == null) {
                                clarification.put("is_flag", 1);
                        } else {
                                clarification.put("is_flag", 0);
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Success")
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

        public GlobalResponse getByDateRange(Date start_date, Date end_date, int page, int size) {
                try {
                        Page<Clarification> response = pag.findClarificationInDateRange(start_date, end_date,
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

        public GlobalResponse generateCK(GenerateCKDTO dto) {
                try {
                        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                        Case setCaseId = Case.builder().id(dto.getCase_id()).build();
                        CaseCategory setCaseCaegoryId = CaseCategory.builder().id(dto.getCase_category_id()).build();

                        Case getCase = caseRepository.findById(dto.getCase_id())
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK,
                                                        "Case with id " + dto.getCase_id() + " does now exist"));

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

                        Branch branch = branchRepository.findById(dto.getBranch_id())
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Branch with id:" + dto.getBranch_id() + " is not found"));

                        String branchName = branch.getName();
                        String initialName = user.getInitial_name();
                        String caseName = getCase.getCode();
                        String lvlCode = user.getLevel().getCode();
                        String romanMonth = convertDateToRoman.getRomanMonth();
                        Integer thisYear = convertDateToRoman.getIntYear();

                        Optional<ReportType> reportType = reportTypeRepository.findByCode("CK");

                        String reportCode = rptNum + lvlCode + "/" + initialName + "-" + caseName
                                        + "/" + reportType.get().getCode() + "/" + branchName + "/" + romanMonth + "/"
                                        + thisYear;

                        Clarification clarification = new Clarification(
                                        null,
                                        user,
                                        branch,
                                        setCaseId,
                                        setCaseCaegoryId,
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
                                        new Date(),
                                        new Date());

                        repository.save(clarification);

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

        public GlobalResponse inputClarification(InputClarificationDTO dto) {
                try {
                        Optional<Clarification> getClarification = repository.findById(dto.getClarification_id());
                        if (!getClarification.isPresent()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Clarificaiton tidak ditemukan")
                                                .errorMessage("Clarification with id : "+dto.getClarification_id()+" not found")
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
                                        null,
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
                                        getClarification.get().getCreated_at(),
                                        new Date());

                        Clarification response = repository.save(clarification);

                        String reportNumber = "";
                        if (response.getReport_number() < 10) {
                                reportNumber = "00" + reportNumber;
                        } else if (response.getReport_number() < 100) {
                                reportNumber = "0" + reportNumber;
                        } else {
                                reportNumber = response.getReport_number().toString();
                        }

                        String formulir = "FM/" + response.getCases().getCode() + "-" + reportNumber;

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
                                        .message("Success")
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
                        Optional<Clarification> getBefore = repository.findById(dto.getClarification_id());
                        if (!getBefore.isPresent()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Clarification tidak dapat ditemukan")
                                                .errorMessage("Clarificaion with id :" + dto.getClarification_id() + " Not found")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }

                        if (dto.getIs_followup() == null) {
                                dto.setIs_followup(0L);
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
                                        getBefore.get().getCreated_at(),
                                        new Date());

                        Clarification response = repository.save(clarification);

                        Long reportNumber = null;
                        String rptNum = null;

                        Map<String, Object> returnResponse = new LinkedHashMap<>();

                        if (!dto.getNominal_loss().isEmpty() || dto.getNominal_loss() != "") {

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

                                Optional<ReportType> reportType = reportTypeRepository.findByCode("BA");

                                String reportCode = rptNum + lvlCode + "/" + initialName + "-" + caseName + "/"
                                                + reportType.get().getCode() + "/" + branchName + "/" + romanMonth + "/"
                                                + thisYear;

                                User setUserId = User.builder().id(response.getUser().getId()).build();
                                Clarification setClarificationId = Clarification.builder().id(response.getId()).build();

                                NewsInspection newsInspection = new NewsInspection(
                                                null,
                                                setUserId,
                                                response.getBranch(),
                                                setClarificationId,
                                                reportType.get(),
                                                null,
                                                null,
                                                reportNumber,
                                                reportCode,
                                                new Date(),
                                                new Date());

                                NewsInspection getResponse = newsInspectionRepository.save(newsInspection);

                                Map<String, Object> mappingFU = new LinkedHashMap<>();
                                mappingFU.put("id", getResponse.getId());
                                mappingFU.put("code", getResponse.getCode());

                                returnResponse.put("bap", mappingFU);
                        }

                        if (dto.getIs_followup() != 0 || dto.getIs_followup() != null
                                        || dto.getIs_followup().toString() != "") {
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

                                Optional<ReportType> reportType2 = reportTypeRepository.findByCode("TL");

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
                                followUp.setCode(reportCode2);
                                followUp.setStatus(EStatusFollowup.CREATE);
                                followUp.setCreatedAt(new Date());

                                followUpRepository.save(followUp);
                        }

                        return GlobalResponse
                                        .builder()
                                        .message("Success")
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
                        Optional<Clarification> getClarification = repository.findById(id);
                        if (!getClarification.isPresent()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Clarification tidak bisa ditemukan")
                                                .errorMessage("Clarifcation with id: "+id+ " not found")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }
                        String fileName = fileStorageService.storeFile(file);

                        String path = UPLOAD_FOLDER_PATH + fileName;

                        Clarification clarification = getClarification.get();
                        clarification.setFilename(fileName);
                        clarification.setFile_path(path);
                        clarification.setStatus(EStatusClarification.IDENTIFICATION);
                        clarification.setUpdated_at(new Date());
                        Clarification getResponse = repository.save(clarification);

                        // file.transferTo(new File(filePath));

                        Map<String, Object> returnResponse = new LinkedHashMap<>();
                        Map<String, Object> mappingRes = new LinkedHashMap<>();
                        mappingRes.put("id", getResponse.getId());
                        mappingRes.put("file_name", getResponse.getFilename());
                        mappingRes.put("file_path", getResponse.getFile_path());

                        returnResponse.put("clarification", mappingRes);

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

        public Clarification downloadFile(String fileName) throws java.io.IOException, IOFileUploadException {
                Clarification response = repository.findByFilename(fileName)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "File not found with name: " + fileName));

                Clarification clarification = response;
                clarification.setStatus(EStatusClarification.UPLOAD);
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
