package com.cms.audit.api.Report.service;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cms.audit.api.AuditDailyReport.models.AuditDailyReport;
import com.cms.audit.api.AuditDailyReport.models.AuditDailyReportDetail;
import com.cms.audit.api.AuditDailyReport.models.Revision;
import com.cms.audit.api.AuditDailyReport.repository.AuditDailyReportDetailRepository;
import com.cms.audit.api.AuditDailyReport.repository.AuditDailyReportRepository;
import com.cms.audit.api.AuditDailyReport.repository.RevisionRepository;
import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.pdf.LHAReport;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.util.ExcelUtil;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;
import com.cms.audit.api.Management.Office.RegionOffice.repository.RegionRepository;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Report.dto.LhaReportDTO;
import com.cms.audit.api.Report.dto.ListLhaDTO;
import com.cms.audit.api.Report.repository.LhaReportRepository;
import com.cms.audit.api.Report.repository.PagReport;
import com.cms.audit.api.Report.repository.ReportRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReportService {

    @Autowired
    private ReportRepository repository;

    @Autowired
    private PagReport pagRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private AuditDailyReportDetailRepository lhaDetailRepository;

    @Autowired
    private RevisionRepository revisionRepository;

    @Autowired
    private LhaReportRepository lhaRepository;

    public GlobalResponse getAll(Long region_id, Long branchId, Long user_id, int page, int size, Date start_date,
            Date end_date) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Page<Clarification> response;
            if (user.getLevel().getCode().equals("C")) {
                if (start_date != null && end_date != null) {
                    response = pagRepository.findByUserInDateRange(user.getId(), start_date, end_date,
                            PageRequest.of(page, size));
                } else {
                    response = pagRepository.findByUserId(user.getId(), PageRequest.of(page, size));
                }
            } else if (user.getLevel().getCode().equals("B") || user.getLevel().getCode().equals("A")) {
                if (region_id != null && branchId != null && user_id != null && start_date != null
                        && end_date != null) {
                    return GlobalResponse.builder()
                            .data(pagRepository.findByAllFilter(user_id, region_id, branchId, start_date,
                                    end_date, PageRequest.of(page, size)))
                            .status(HttpStatus.OK).message("Berhasil menampilkan data").build();
                } else if (user_id != null) {
                    if (start_date != null && end_date != null) {
                        response = pagRepository.findByUserInDateRange(user.getId(), start_date, end_date,
                                PageRequest.of(page, size));
                    } else {
                        response = pagRepository.findByUserId(user.getId(), PageRequest.of(page, size));
                    }
                } else if (branchId != null) {
                    return getByBranchAllByDate(branchId, start_date, end_date, page, size);
                } else {
                    Pageable pageable = PageRequest.of(page, size);
                    List<Clarification> clarificationList = new ArrayList<>();
                    if (start_date == null || end_date == null) {
                        if (user.getLevel().getCode().equals("A")) {
                            return GlobalResponse.builder().data(pagRepository.findAll(PageRequest.of(page, size)))
                                    .message("Berhasil menampilkan data").status(HttpStatus.OK).build();
                        }
                        for (int i = 0; i < user.getRegionId().size(); i++) {
                            List<Clarification> getByRegion = repository.findByRegionId(user.getRegionId().get(i));
                            for (int u = 0; u < getByRegion.size(); u++) {
                                if (!clarificationList.contains(getByRegion.get(u))) {
                                    clarificationList.add(getByRegion.get(u));
                                }
                            }
                        }
                    } else {
                        if (user.getLevel().getCode().equals("A")) {
                            return GlobalResponse.builder()
                                    .data(pagRepository.findClarificationInDateRange(start_date, end_date,
                                            PageRequest.of(page, size)))
                                    .message("Berhasil menampilkan data").status(HttpStatus.OK).build();
                        }
                        for (int i = 0; i < user.getRegionId().size(); i++) {
                            List<Clarification> getByRegion = repository
                                    .findByRegionIdAndDate(user.getRegionId().get(i), start_date, end_date);
                            for (int u = 0; u < getByRegion.size(); u++) {
                                if (!clarificationList.contains(getByRegion.get(u))) {
                                    clarificationList.add(getByRegion.get(u));
                                }
                            }
                        }
                    }
                    try {
                        int start = (int) pageable.getOffset();
                        int end = Math.min((start + pageable.getPageSize()), clarificationList.size());
                        List<Clarification> pageContent = clarificationList.subList(start, end);
                        Page<Clarification> response2 = new PageImpl<>(pageContent, pageable, clarificationList.size());
                        return GlobalResponse
                                .builder()
                                .message("Berhasil menampilkan data")
                                .data(response2)
                                .status(HttpStatus.OK)
                                .build();
                    } catch (Exception e) {
                        return GlobalResponse
                                .builder()
                                .error(e)
                                .status(HttpStatus.BAD_REQUEST)
                                .build();
                    }
                }
            } else {
                if (start_date == null || end_date == null) {
                    response = pagRepository.findAll(PageRequest.of(page, size));
                } else {
                    response = pagRepository.findClarificationInDateRange(start_date, end_date,
                            PageRequest.of(page, size));
                }
            }
            if (response.isEmpty()) {
                return GlobalResponse.builder().message("Data Empty").status(HttpStatus.OK).build();
            }
            return GlobalResponse.builder().data(response).message("Berhasil menampilkan data").status(HttpStatus.OK).build();
        } catch (Exception e) {
            return GlobalResponse.builder().error(e).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public GlobalResponse getByUserAllByDate(Long userId, Date start_date, Date end_date, int page, int size) {
        try {
            Page<Clarification> response;
            if (start_date == null || end_date == null) {
                response = pagRepository.findByUserId(userId, PageRequest.of(page, size));
            } else {
                response = pagRepository.findByUserInDateRange(userId, start_date, end_date,
                        PageRequest.of(page, size));
            }
            if (response.isEmpty()) {
                return GlobalResponse.builder().message("Data Empty").status(HttpStatus.OK).build();
            }
            return GlobalResponse.builder().data(response).message("Berhasil menampilkan data").status(HttpStatus.OK).build();
        } catch (Exception e) {
            return GlobalResponse.builder().error(e).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public GlobalResponse getByBranchAllByDate(Long branchId, Date start_date, Date end_date, int page, int size) {
        try {
            Page<Clarification> response;
            if (start_date == null || end_date == null) {
                response = pagRepository.findByBranchId(branchId, PageRequest.of(page, size));
            } else {
                response = pagRepository.findByBranchInDateRange(branchId, start_date, end_date,
                        PageRequest.of(page, size));
            }
            if (response.isEmpty()) {
                return GlobalResponse.builder().message("Data Empty").status(HttpStatus.OK).build();
            }
            return GlobalResponse.builder().data(response).message("Berhasil menampilkan data").status(HttpStatus.OK).build();
        } catch (Exception e) {
            return GlobalResponse.builder().error(e).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public GlobalResponse getAllByDate(Date start_date, Date end_date, int page, int size) {
        try {
            Page<Clarification> response = pagRepository.findClarificationInDateRange(start_date, end_date,
                    PageRequest.of(page, size));
            if (response.isEmpty()) {
                return GlobalResponse.builder().message("Data Empty").status(HttpStatus.OK).build();
            }
            return GlobalResponse.builder().data(response).message("Berhasil menampilkan data").status(HttpStatus.OK).build();
        } catch (Exception e) {
            return GlobalResponse.builder().error(e).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public GlobalResponse getOne(Long id) {
        try {
            Optional<Clarification> response = repository.findById(id);
            if (!response.isPresent()) {
                return GlobalResponse.builder().message("Data Empty").status(HttpStatus.OK).build();
            }
            return GlobalResponse.builder().data(response).message("Berhasil menampilkan data").status(HttpStatus.OK).build();
        } catch (Exception e) {
            return GlobalResponse.builder().error(e).message("Error").status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ByteArrayInputStream getDataDownloadClarification(Long region_id, Long user_id, Long branchId,
            Date start_date,
            Date end_date)
            throws IOException {
        List<Clarification> response;
        if (user_id != null && region_id != null && branchId != null && start_date != null && end_date != null) {
            response = repository.findByAllFilters(user_id, region_id, branchId, start_date, end_date);
        } else if (region_id != null && start_date != null && end_date != null) {
            response = repository.findByRegionIdAndDate(branchId, start_date, end_date);
        } else if (branchId != null && start_date != null && end_date != null) {
            response = repository.findByBranchInDateRange(branchId, start_date, end_date);
        } else if (user_id != null && start_date != null && end_date != null) {
            response = repository.findByUserInDateRange(user_id, start_date, end_date);
        } else if (user_id != null) {
            response = repository.findByUserId(user_id);
        } else if (branchId != null) {
            response = repository.findByBranchId(branchId);
        } else if (region_id != null) {
            response = repository.findByRegionId(region_id);
        } else if (start_date != null && end_date != null) {
            response = repository.findClarificationInDateRange(start_date, end_date);
        } else {
            response = repository.findClarificationInDateRange(new Date(), new Date());
        }
        ByteArrayInputStream data = ExcelUtil.dataToExcel(response);
        return data;
    }

    public ResponseEntity<InputStreamResource> getDataDownloadLHA(Long user_id, Long regionId, Date start_date,
            Date end_date) throws FileNotFoundException, MalformedURLException {
        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<AuditDailyReport> response = new ArrayList<>();
        // LhaReportDTO dto = new LhaReportDTO();
        // List<ListLhaDTO> list = new ArrayList<>();
        List<LhaReportDTO> listAllReport = new ArrayList<>();

        if (user_id != null && regionId != null && start_date != null && end_date != null) {
            response = lhaRepository.findLHAByAll(regionId, user_id, start_date, end_date);
        } else if (regionId != null && start_date != null && end_date != null) {
            response = lhaRepository.findLHAByRegionInDateRange(regionId, start_date, end_date);
        } else if (user_id != null && start_date != null && end_date != null) {
            response = lhaRepository.findAllLHAByUserIdInDateRange(user_id, start_date, end_date);
        } else if (user_id != null) {
            response = lhaRepository.findAllLHAByUserId(user_id);
        } else if (regionId != null) {
            response = lhaRepository.findLHAByRegion(regionId);
        } else {
            if (getUser.getLevel().getCode().equals("B")  ) {
                for (int i = 0; i < getUser.getRegionId().size(); i++) {
                    List<AuditDailyReport> listLHA;
                    if (start_date != null && end_date != null) {
                        listLHA = lhaRepository.findLHAInDateRangeAndRegion(getUser.getRegionId().get(i), start_date, end_date);
                    } else {
                        listLHA = lhaRepository
                                .findLHAByRegion(getUser.getRegionId().get(i));
                    }
                    for (int u = 0; u < listLHA.size(); u++) {
                        response.add(listLHA.get(u));
                    }
                }
            } else if (getUser.getLevel().getCode().equals("C") ) {
                for (int i = 0; i < getUser.getBranchId().size(); i++) {
                    List<AuditDailyReport> listLHA;
                    if (start_date != null && end_date != null) {
                        listLHA = lhaRepository.findAllLHAByUserIdInDateRange(getUser.getId(), start_date, end_date);
                    } else {
                        listLHA = lhaRepository.findAllLHAByUserId(getUser.getId());
                    }
                    for (int u = 0; u < listLHA.size(); u++) {
                        response.add(listLHA.get(u));
                    }
                }
            } else if (getUser.getLevel().getCode().equals("A") ){
                if (start_date != null && end_date != null) {
                    response = lhaRepository.findLHAInDateRange(start_date, end_date);
                }else{
                    response = lhaRepository.findAll();
                }
            } else {
                response = null;
            }
        }
        if (response.isEmpty()) {
            ByteArrayInputStream pdf = LHAReport.generateIfNoData();
            InputStreamResource isr = new InputStreamResource(pdf);

            String filename = "No-Data-Report.pdf";

            ResponseEntity<InputStreamResource> responses = ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                    .contentType(MediaType.parseMediaType("application/pdf")).body(isr);
            return responses;
        }
        ByteArrayInputStream pdf;
        if (regionId != null) {
            for (int i = 0; i < response.size(); i++) {
                List<AuditDailyReportDetail> detail = lhaDetailRepository.findByLHAIdForLeader(response.get(i).getId());
                if(detail.isEmpty()){
                    continue;
                }
                String regionuser_id = response.get(i).getBranch().getArea().getRegion().getName();
                String datereport;
                if(response.get(i) != null){
                    datereport = convertDateToRoman.convertDateHehe(response.get(i).getCreated_at());
                }else{
                    datereport = "-";
                }
                String fulluser_id;
                if(getUser.getFullname() != null){
                    fulluser_id = response.get(i).getUser().getFullname();
                }else{
                    fulluser_id = "-";
                }

                boolean foundRegion = false;
                for (int x = 0; x < listAllReport.size(); x++) {
                    if (regionuser_id.equals(listAllReport.get(x).getArea_name()) && datereport.equals(listAllReport.get(x).getDate())) {
                        foundRegion = true;
                        List<ListLhaDTO> lhaDetails = listAllReport.get(x).getLha_detail();
                        boolean foundUser = false;
                        for (int y = 0; y < lhaDetails.size(); y++) {
                            if (fulluser_id.equals(lhaDetails.get(y).getFullname())) {
                                List<AuditDailyReportDetail> listOfDetail = new ArrayList<>();
                                for (int d = 0; d < detail.size(); d++) {
                                    Optional<Revision> check = revisionRepository.findByDetailId(detail.get(d).getId());
                                    if (check.isPresent()) {
                                        AuditDailyReportDetail makeDto = new AuditDailyReportDetail();
                                        makeDto.setAuditDailyReport(detail.get(d).getAuditDailyReport());
                                        makeDto.setCaseCategory(check.get().getCaseCategory());
                                        makeDto.setCases(check.get().getCases());
                                        makeDto.setCreated_at(check.get().getCreated_at());
                                        makeDto.setCreated_by(check.get().getCreated_by());
                                        makeDto.setDescription(check.get().getDescription());
                                        makeDto.setId(check.get().getId());
                                        makeDto.setIs_delete(check.get().getIs_delete());
                                        makeDto.setIs_research(check.get().getIs_research());
                                        makeDto.setPermanent_recommendations(
                                                check.get().getPermanent_recommendations());
                                        makeDto.setSuggestion(check.get().getSuggestion());
                                        makeDto.setTemporary_recommendations(
                                                check.get().getTemporary_recommendations());
                                        makeDto.setUpdate_at(detail.get(d).getUpdate_at());
                                        makeDto.setUpdated_by(detail.get(d).getUpdated_by());
                                        listOfDetail.add(makeDto);
                                    } else {
                                        listOfDetail.add(detail.get(d));
                                    }
                                }
                                lhaDetails.get(y).getDetails().addAll(listOfDetail);
                                foundUser = true;
                                break;
                            }
                        }
                        if (!foundUser) {
                            ListLhaDTO lhaDto = new ListLhaDTO();
                            lhaDto.setFullname(fulluser_id);
                            lhaDto.setBranch(response.get(i).getBranch().getName());
                            List<AuditDailyReportDetail> listOfDetail = new ArrayList<>();
                            for (int d = 0; d < detail.size(); d++) {
                                Optional<Revision> check = revisionRepository.findByDetailId(detail.get(d).getId());
                                if (check.isPresent()) {
                                    AuditDailyReportDetail makeDto = new AuditDailyReportDetail();
                                    makeDto.setAuditDailyReport(detail.get(d).getAuditDailyReport());
                                    makeDto.setCaseCategory(check.get().getCaseCategory());
                                    makeDto.setCases(check.get().getCases());
                                    makeDto.setCreated_at(check.get().getCreated_at());
                                    makeDto.setCreated_by(check.get().getCreated_by());
                                    makeDto.setDescription(check.get().getDescription());
                                    makeDto.setId(check.get().getId());
                                    makeDto.setIs_delete(check.get().getIs_delete());
                                    makeDto.setIs_research(check.get().getIs_research());
                                    makeDto.setPermanent_recommendations(check.get().getPermanent_recommendations());
                                    makeDto.setSuggestion(check.get().getSuggestion());
                                    makeDto.setTemporary_recommendations(check.get().getTemporary_recommendations());
                                    makeDto.setUpdate_at(detail.get(d).getUpdate_at());
                                    makeDto.setUpdated_by(detail.get(d).getUpdated_by());
                                    listOfDetail.add(makeDto);
                                } else {
                                    listOfDetail.add(detail.get(d));
                                }
                            }
                            lhaDto.setDetails(listOfDetail);
                            lhaDetails.add(lhaDto);
                        }
                        break;
                    }
                }

                if (!foundRegion) {
                    LhaReportDTO reportDto = new LhaReportDTO();
                    reportDto.setArea_name(regionuser_id);
                    reportDto.setDate(datereport);
                    List<ListLhaDTO> lhaDetailList = new ArrayList<>();
                    ListLhaDTO lhaDto = new ListLhaDTO();
                    lhaDto.setFullname(fulluser_id);
                    lhaDto.setBranch(response.get(i).getBranch().getName());

                    List<AuditDailyReportDetail> listOfDetail = new ArrayList<>();
                    for (int d = 0; d < detail.size(); d++) {
                        Optional<Revision> check = revisionRepository.findByDetailId(detail.get(d).getId());
                        if (check.isPresent()) {
                            AuditDailyReportDetail makeDto = new AuditDailyReportDetail();
                            makeDto.setAuditDailyReport(detail.get(d).getAuditDailyReport());
                            makeDto.setCaseCategory(check.get().getCaseCategory());
                            makeDto.setCases(check.get().getCases());
                            makeDto.setCreated_at(check.get().getCreated_at());
                            makeDto.setCreated_by(check.get().getCreated_by());
                            makeDto.setDescription(check.get().getDescription());
                            makeDto.setId(check.get().getId());
                            makeDto.setIs_delete(check.get().getIs_delete());
                            makeDto.setIs_research(check.get().getIs_research());
                            makeDto.setPermanent_recommendations(check.get().getPermanent_recommendations());
                            makeDto.setSuggestion(check.get().getSuggestion());
                            makeDto.setTemporary_recommendations(check.get().getTemporary_recommendations());
                            makeDto.setUpdate_at(detail.get(d).getUpdate_at());
                            makeDto.setUpdated_by(detail.get(d).getUpdated_by());
                            listOfDetail.add(makeDto);
                        } else {
                            listOfDetail.add(detail.get(d));
                        }
                    }
                    lhaDto.setDetails(listOfDetail);
                    lhaDetailList.add(lhaDto);
                    reportDto.setLha_detail(lhaDetailList);
                    listAllReport.add(reportDto);
                }
            }
            pdf = LHAReport.generateAllLHAPDF(listAllReport);
        } else {
            for (int i = 0; i < response.size(); i++) {
                List<AuditDailyReportDetail> detail = lhaDetailRepository.findByLHAIdForLeader(response.get(i).getId());
                if(detail.isEmpty()){
                    continue;
                }
                String regionuser_id = response.get(i).getBranch().getArea().getRegion().getName();
                String datereport;
                if(response.get(i) != null){
                    datereport = convertDateToRoman.convertDateHehe(response.get(i).getCreated_at());
                }else{
                    datereport = "-";
                }
                String fulluser_id;
                if(getUser.getFullname() != null){
                    fulluser_id = response.get(i).getUser().getFullname();
                }else{
                    fulluser_id = "-";
                }

                boolean foundRegion = false;
                for (int x = 0; x < listAllReport.size(); x++) {
                    if (regionuser_id.equals(listAllReport.get(x).getArea_name()) && datereport.equals(listAllReport.get(x).getDate())) {
                        foundRegion = true;
                        List<ListLhaDTO> lhaDetails = listAllReport.get(x).getLha_detail();
                        boolean foundUser = false;
                        for (int y = 0; y < lhaDetails.size(); y++) {
                            if (fulluser_id.equals(lhaDetails.get(y).getFullname())) {
                                List<AuditDailyReportDetail> listOfDetail = new ArrayList<>();
                                for (int d = 0; d < detail.size(); d++) {
                                    Optional<Revision> check = revisionRepository.findByDetailId(detail.get(d).getId());
                                    if (check.isPresent()) {
                                        AuditDailyReportDetail makeDto = new AuditDailyReportDetail();
                                        makeDto.setAuditDailyReport(detail.get(d).getAuditDailyReport());
                                        makeDto.setCaseCategory(check.get().getCaseCategory());
                                        makeDto.setCases(check.get().getCases());
                                        makeDto.setCreated_at(check.get().getCreated_at());
                                        makeDto.setCreated_by(check.get().getCreated_by());
                                        makeDto.setDescription(check.get().getDescription());
                                        makeDto.setId(check.get().getId());
                                        makeDto.setIs_delete(check.get().getIs_delete());
                                        makeDto.setIs_research(check.get().getIs_research());
                                        makeDto.setPermanent_recommendations(
                                                check.get().getPermanent_recommendations());
                                        makeDto.setSuggestion(check.get().getSuggestion());
                                        makeDto.setTemporary_recommendations(
                                                check.get().getTemporary_recommendations());
                                        makeDto.setUpdate_at(detail.get(d).getUpdate_at());
                                        makeDto.setUpdated_by(detail.get(d).getUpdated_by());
                                        listOfDetail.add(makeDto);
                                    } else {
                                        listOfDetail.add(detail.get(d));
                                    }
                                }
                                lhaDetails.get(y).getDetails().addAll(listOfDetail);
                                foundUser = true;
                                break;
                            }
                        }
                        if (!foundUser) {
                            ListLhaDTO lhaDto = new ListLhaDTO();
                            lhaDto.setFullname(fulluser_id);
                            lhaDto.setBranch(response.get(i).getBranch().getName());
                            List<AuditDailyReportDetail> listOfDetail = new ArrayList<>();
                            for (int d = 0; d < detail.size(); d++) {
                                Optional<Revision> check = revisionRepository.findByDetailId(detail.get(d).getId());
                                if (check.isPresent()) {
                                    AuditDailyReportDetail makeDto = new AuditDailyReportDetail();
                                    makeDto.setAuditDailyReport(detail.get(d).getAuditDailyReport());
                                    makeDto.setCaseCategory(check.get().getCaseCategory());
                                    makeDto.setCases(check.get().getCases());
                                    makeDto.setCreated_at(check.get().getCreated_at());
                                    makeDto.setCreated_by(check.get().getCreated_by());
                                    makeDto.setDescription(check.get().getDescription());
                                    makeDto.setId(check.get().getId());
                                    makeDto.setIs_delete(check.get().getIs_delete());
                                    makeDto.setIs_research(check.get().getIs_research());
                                    makeDto.setPermanent_recommendations(check.get().getPermanent_recommendations());
                                    makeDto.setSuggestion(check.get().getSuggestion());
                                    makeDto.setTemporary_recommendations(check.get().getTemporary_recommendations());
                                    makeDto.setUpdate_at(detail.get(d).getUpdate_at());
                                    makeDto.setUpdated_by(detail.get(d).getUpdated_by());
                                    listOfDetail.add(makeDto);
                                } else {
                                    listOfDetail.add(detail.get(d));
                                }
                            }
                            lhaDto.setDetails(listOfDetail);
                            lhaDetails.add(lhaDto);
                        }
                        break;
                    }
                }

                if (!foundRegion) {
                    LhaReportDTO reportDto = new LhaReportDTO();
                    reportDto.setArea_name(regionuser_id);
                    reportDto.setDate(datereport);
                    List<ListLhaDTO> lhaDetailList = new ArrayList<>();
                    ListLhaDTO lhaDto = new ListLhaDTO();
                    lhaDto.setFullname(fulluser_id);
                    lhaDto.setBranch(response.get(i).getBranch().getName());

                    List<AuditDailyReportDetail> listOfDetail = new ArrayList<>();
                    for (int d = 0; d < detail.size(); d++) {
                        Optional<Revision> check = revisionRepository.findByDetailId(detail.get(d).getId());
                        if (check.isPresent()) {
                            AuditDailyReportDetail makeDto = new AuditDailyReportDetail();
                            makeDto.setAuditDailyReport(detail.get(d).getAuditDailyReport());
                            makeDto.setCaseCategory(check.get().getCaseCategory());
                            makeDto.setCases(check.get().getCases());
                            makeDto.setCreated_at(check.get().getCreated_at());
                            makeDto.setCreated_by(check.get().getCreated_by());
                            makeDto.setDescription(check.get().getDescription());
                            makeDto.setId(check.get().getId());
                            makeDto.setIs_delete(check.get().getIs_delete());
                            makeDto.setIs_research(check.get().getIs_research());
                            makeDto.setPermanent_recommendations(check.get().getPermanent_recommendations());
                            makeDto.setSuggestion(check.get().getSuggestion());
                            makeDto.setTemporary_recommendations(check.get().getTemporary_recommendations());
                            makeDto.setUpdate_at(detail.get(d).getUpdate_at());
                            makeDto.setUpdated_by(detail.get(d).getUpdated_by());
                            listOfDetail.add(makeDto);
                        } else {
                            listOfDetail.add(detail.get(d));
                        }
                    }
                    lhaDto.setDetails(listOfDetail);
                    lhaDetailList.add(lhaDto);
                    reportDto.setLha_detail(lhaDetailList);
                    listAllReport.add(reportDto);
                }
            }
            pdf = LHAReport.generateAllLHAPDF(listAllReport);
        }
        // String path = pdf.getFilePath();
        // File file = new File(path);
        // InputStream inputStream = new FileInputStream(file);
        InputStreamResource isr = new InputStreamResource(pdf);

        // HttpHeaders httpHeaders = new HttpHeaders();

        String filename;
        if (start_date != null && end_date != null) {
            filename = convertDateToRoman.convertDateHehe(start_date) + "-"
                    + convertDateToRoman.convertDateHehe(end_date) + "-report.pdf";
        } else {
            filename = "report.pdf";
        }

        ResponseEntity<InputStreamResource> responses = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                .contentType(MediaType.parseMediaType("application/pdf")).body(isr);
        return responses;
    }
}
