package com.cms.audit.api.Clarifications.service;

import java.io.File;
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

import com.cms.audit.api.Clarifications.dto.InputClarificationDTO;
import com.cms.audit.api.Clarifications.dto.GenerateCKDTO;
import com.cms.audit.api.Clarifications.dto.IdentificationDTO;
import com.cms.audit.api.Clarifications.dto.response.NumberClarificationInterface;
import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Clarifications.models.EStatus;
import com.cms.audit.api.Clarifications.repository.ClarificationRepository;
import com.cms.audit.api.Clarifications.repository.PagClarification;
import com.cms.audit.api.Management.Case.models.Case;
import com.cms.audit.api.Management.Case.repository.CaseRepository;
import com.cms.audit.api.Management.CaseCategory.models.CaseCategory;
import com.cms.audit.api.Management.Office.BranchOffice.repository.BranchRepository;
import com.cms.audit.api.Management.ReportType.models.ReportType;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Management.User.repository.UserRepository;
import com.cms.audit.api.NewsInspection.models.NewsInspection;
import com.cms.audit.api.NewsInspection.repository.NewsInspectionRepository;
import com.cms.audit.api.common.constant.FolderPath;
import com.cms.audit.api.common.constant.convertDateToRoman;
import com.cms.audit.api.common.pdf.GeneratePdf;
import com.cms.audit.api.common.response.GlobalResponse;
import com.cms.audit.api.common.response.PDFResponse;

@Service
public class ClarificationService {

        @Autowired
        private ClarificationRepository repository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private BranchRepository branchRepository;

        @Autowired
        private CaseRepository caseRepository;

        @Autowired
        private NewsInspectionRepository newsInspectionRepository;

        @Autowired
        private PagClarification pag;

        private final String FOLDER_PATH = FolderPath.FOLDER_PATH_CLARIFICATION;

        public GlobalResponse getAll(int page, int size) {
                try {
                        Page<Clarification> response = pag.findAll(PageRequest.of(page, size));
                        if (response.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("No Content")
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
                        Optional<Clarification> response = repository.findById(id);
                        if (!response.isPresent()) {
                                return GlobalResponse
                                                .builder()
                                                .message("No Content")
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
                        User setUserId = User.builder().id(dto.getUser_id()).build();
                        Case setCaseId = Case.builder().id(dto.getCase_id()).build();
                        CaseCategory setCaseCaegoryId = CaseCategory.builder().id(dto.getCase_category_id()).build();
                        ReportType setReportTypeId = ReportType.builder().id(dto.getReport_type_id()).build();
                        User getUser = userRepository.findById(dto.getUser_id())
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK,
                                                        "User with id " + dto.getUser_id() + " does now exist"));

                        Case getCase = caseRepository.findById(dto.getCase_id())
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK,
                                                        "Case with id " + dto.getCase_id() + " does now exist"));

                        Long reportNumber = null;
                        String rptNum = null;

                        Optional<NumberClarificationInterface> checkClBefore = repository
                                        .checkNumberClarification(dto.getUser_id());
                        if (checkClBefore.isPresent()) {
                                if (checkClBefore.get().getCreated_Year() == Long
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

                        String branchName = getUser.getBranch().getName();
                        String initialName = getUser.getInitial_name();
                        String caseName = getCase.getCode();
                        String lvlCode = getUser.getLevel().getCode();
                        String romanMonth = convertDateToRoman.getRomanMonth();
                        Integer thisYear = convertDateToRoman.getIntYear();

                        String reportCode = rptNum + lvlCode + "/" + initialName + "-" + caseName
                                        + "/CK/" + branchName + "/" + romanMonth + "/" + thisYear;

                        Clarification clarification = new Clarification(
                                        null,
                                        setUserId,
                                        setCaseId,
                                        setCaseCaegoryId,
                                        setReportTypeId,
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
                                        EStatus.INPUT,
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

        public GlobalResponse inputClarification(InputClarificationDTO dto, Long id) {
                try {
                        Optional<Clarification> getClarification = repository.findById(id);
                        if (!getClarification.isPresent()) {
                                return GlobalResponse
                                                .builder()
                                                .message("No Content")
                                                .status(HttpStatus.OK)
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
                                        id,
                                        setUserId,
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
                                        EStatus.DOWNLOAD,
                                        getClarification.get().getCreated_at(),
                                        new Date());

                        Clarification response = repository.save(clarification);

                        PDFResponse generatePDF = GeneratePdf.generateClarificationPDF(response);

                        Clarification clarification2 = new Clarification(
                                        id,
                                        setUserId,
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
                                        generatePDF.fileName,
                                        generatePDF.filePath,
                                        dto.getDescription(),
                                        null,
                                        dto.getPriority(),
                                        null,
                                        null,
                                        EStatus.DOWNLOAD,
                                        getClarification.get().getCreated_at(),
                                        new Date());

                        repository.save(clarification2);

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

        public GlobalResponse identificationClarification(IdentificationDTO dto, Long id) {
                try {
                        Optional<Clarification> getBefore = repository.findById(id);
                        if (!getBefore.isPresent()) {
                                return GlobalResponse
                                                .builder()
                                                .message("No Contennt")
                                                .status(HttpStatus.OK)
                                                .build();
                        }

                        Clarification clarification = new Clarification(
                                        id,
                                        getBefore.get().getUser(),
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
                                        getBefore.get().getFileName(),
                                        getBefore.get().getFile_path(),
                                        getBefore.get().getDescription(),
                                        dto.getRecommendation(),
                                        getBefore.get().getPriority(),
                                        dto.getEvaluation(),
                                        dto.getIs_followup(),
                                        EStatus.IK,
                                        getBefore.get().getCreated_at(),
                                        new Date());

                        Clarification response = repository.save(clarification);

                        if (dto.getNominal_loss().isEmpty() || dto.getNominal_loss() == null
                                        || dto.getNominal_loss() == "") {
                                return GlobalResponse
                                                .builder()
                                                .message("Success")
                                                .status(HttpStatus.OK)
                                                .build();
                        }

                        Long reportNumber = null;
                        String rptNum = null;

                        Optional<NumberClarificationInterface> checkClBefore = newsInspectionRepository
                                        .checkNumberBAP(response.getUser().getId());
                        if (checkClBefore.isPresent()) {
                                if (checkClBefore.get().getCreated_Year() == Long.valueOf(convertDateToRoman.getIntYear())) {
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

                        String branchName = response.getUser().getBranch().getName();
                        String initialName = response.getUser().getInitial_name();
                        String caseName = response.getCases().getCode();
                        String lvlCode = response.getUser().getLevel().getCode();
                        String romanMonth = convertDateToRoman.getRomanMonth();
                        Integer thisYear = convertDateToRoman.getIntYear();

                        String reportCode = rptNum + lvlCode + "/" + initialName + "-" + caseName
                                        + "/BA/" + branchName + "/" + romanMonth + "/" + thisYear;

                        User setUserId = User.builder().id(response.getUser().getId()).build();
                        ReportType setReportId = ReportType.builder().id(Long.valueOf(2)).build();
                        Clarification setClarificationId = Clarification.builder().id(response.getId()).build();

                        NewsInspection newsInspection = new NewsInspection(
                                        null,
                                        setUserId,
                                        setClarificationId,
                                        setReportId,
                                        null,
                                        null,
                                        reportNumber,
                                        reportCode,
                                        new Date(),
                                        new Date());

                        newsInspectionRepository.save(newsInspection);
                        return GlobalResponse
                                        .builder()
                                        .message("Success")
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
                                                .message("No Content")
                                                .status(HttpStatus.OK)
                                                .build();
                        }

                        String path = FOLDER_PATH + file.getOriginalFilename();

                        String fileName = file.getOriginalFilename();
                        String filePath = path;

                        User setUserId = User.builder().id(getClarification.get().getUser().getId()).build();
                        Case setCaseId = Case.builder().id(getClarification.get().getCases().getId()).build();
                        CaseCategory setCaseCaegoryId = CaseCategory.builder()
                                        .id(getClarification.get().getCaseCategory().getId())
                                        .build();
                        ReportType setReportTypeId = ReportType.builder()
                                        .id(getClarification.get().getReportType().getId())
                                        .build();
                        Clarification clarification = new Clarification(
                                        id,
                                        setUserId,
                                        setCaseId,
                                        setCaseCaegoryId,
                                        setReportTypeId,
                                        getClarification.get().getReport_number(),
                                        getClarification.get().getCode(),
                                        getClarification.get().getNominal_loss(),
                                        getClarification.get().getEvaluation_limitation(),
                                        getClarification.get().getLocation(),
                                        getClarification.get().getAuditee(),
                                        getClarification.get().getAuditee_leader(),
                                        fileName,
                                        filePath,
                                        getClarification.get().getDescription(),
                                        getClarification.get().getRecomendation(),
                                        getClarification.get().getPriority(),
                                        getClarification.get().getEvaluation(),
                                        getClarification.get().getIs_follow_up(),
                                        EStatus.UPLOAD,
                                        new Date(),
                                        new Date());
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

        public Clarification downloadFile(String fileName) throws java.io.IOException, IOFileUploadException {
                Clarification response = repository.findByFileName(fileName)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK,
                                                "not found"));
                // String filePath=response.getFile_path();
                // byte[] file = Files.readAllBytes(new File(filePath).toPath());
                return response;
        }

}
