package com.cms.audit.api.Report.service;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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
import com.cms.audit.api.AuditDailyReport.repository.RevisionRepository;
import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Common.constant.SpecificationFIlter;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.pdf.LHAReport;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.util.ExcelUtil;
import com.cms.audit.api.FollowUp.models.FollowUp;
import com.cms.audit.api.FollowUp.repository.FollowUpRepository;
import com.cms.audit.api.Management.Office.RegionOffice.repository.RegionRepository;
import com.cms.audit.api.Management.Penalty.models.Penalty;
import com.cms.audit.api.Management.Penalty.repository.PenaltyRepository;
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
    private FollowUpRepository fUpRepository;

    @Autowired
    private PenaltyRepository penaltyRepository;

    @Autowired
    private AuditDailyReportDetailRepository lhaDetailRepository;

    @Autowired
    private RevisionRepository revisionRepository;

    @Autowired
    private LhaReportRepository lhaRepository;

    public ByteArrayInputStream getDataDownloadClarification(Long region_id, Long user_id, Long branchId,
            Date start_date,
            Date end_date)
            throws IOException {
        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Specification<Clarification> spec = Specification
                .where(new SpecificationFIlter<Clarification>().userId(user_id))
                .and(new SpecificationFIlter<Clarification>().branchIdEqual(branchId))
                .and(new SpecificationFIlter<Clarification>().dateRange(start_date, end_date))
                .and(new SpecificationFIlter<Clarification>().isNotDeleted());

        if (getUser.getLevel().getCode().equals("C")) {
            spec = spec.and(new SpecificationFIlter<Clarification>().userId(getUser.getId()));
        } else if (getUser.getLevel().getCode().equals("B")) {
            spec = spec.and(new SpecificationFIlter<Clarification>()
                    .getByRegionIds(getUser.getRegionId()));
        }
        List<Clarification> response = repository.findAll(spec);

        String realizePenalty = "";
        // for (Clarification clarification : response) {
        //     Optional<FollowUp> getFU = fUpRepository.findByClId(clarification.getId());
        //     if (getFU.isPresent()) {
        //         FollowUp followUp = getFU.get();
        //         if (followUp.getPenaltyRealization() != null) {
        //             for (Long penaltyId : followUp.getPenaltyRealization()) {
        //                 Optional<Penalty> penaltyOpt = penaltyRepository.findById(penaltyId);
        //                 if (penaltyOpt.isPresent()) {
        //                     Penalty penalty = penaltyOpt.get();
        //                     if (!realizePenalty.isEmpty()) {
        //                         realizePenalty += ", ";
        //                     }
        //                     realizePenalty += penalty.getName();
        //                 }
        //             }
        //         }
        //     }
        // }
        ByteArrayInputStream data = ExcelUtil.dataToExcel(response, realizePenalty);
        return data;
    }

    public ResponseEntity<InputStreamResource> getDataDownloadLHA(Long user_id, Long regionId, Date start_date,
            Date end_date) throws FileNotFoundException, MalformedURLException {
        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<LhaReportDTO> listAllReport = new ArrayList<>();
        Specification<AuditDailyReport> spec = Specification
                .where(new SpecificationFIlter<AuditDailyReport>().userId(user_id))
                .and(new SpecificationFIlter<AuditDailyReport>().dateRange(start_date,
                        end_date))
                .and(new SpecificationFIlter<AuditDailyReport>().isNotDeleted())
                .and(new SpecificationFIlter<AuditDailyReport>().orderByIdAsc());
        if (regionId != null) {
            List<Long> regionList = new ArrayList<>();
            regionList.add(regionId);
            spec = spec.and(new SpecificationFIlter<AuditDailyReport>().getByRegionIds(regionList));
        } else if (getUser.getLevel().getCode().equals("C")) {
            spec = spec.and(new SpecificationFIlter<AuditDailyReport>().userId(getUser.getId()));
        } else if (getUser.getLevel().getCode().equals("B")) {
            spec = spec.and(new SpecificationFIlter<AuditDailyReport>()
                    .getByRegionIds(getUser.getRegionId()));
        }
        List<AuditDailyReport> response = lhaRepository.findAll(spec);
        if (response.isEmpty()) {
            ByteArrayInputStream pdf = LHAReport.generateIfNoData();
            InputStreamResource isr = new InputStreamResource(pdf);

            String filename = "No-Data-Report.pdf";

            ResponseEntity<InputStreamResource> responses = ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                    .contentType(MediaType.parseMediaType("application/pdf")).body(isr);
            return responses;
        }

        ByteArrayInputStream pdf = null;
        if (regionId != null) {
            // Proses data jika regionId tidak null
            processData(response, getUser, listAllReport);
        } else {
            // Proses data jika regionId null
            processData(response, getUser, listAllReport);
        }

        // Urutkan listAllReport berdasarkan area_name
        listAllReport.sort(Comparator.comparing(LhaReportDTO::getArea_name, new AlphanumericComparator()));

        pdf = LHAReport.generateAllLHAPDF(listAllReport);

        InputStreamResource isr = new InputStreamResource(pdf);

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

    private void processData(List<AuditDailyReport> response, User getUser, List<LhaReportDTO> listAllReport) {
        for (int i = 0; i < response.size(); i++) {
            List<AuditDailyReportDetail> detail = new ArrayList<>();
            if (getUser.getLevel().getCode().equals("A")) {
                detail = lhaDetailRepository.findByLHAIdForLeader(response.get(i).getId());
            } else {
                detail = lhaDetailRepository.findByLHAId(response.get(i).getId());
            }
            if (detail.isEmpty()) {
                continue;
            }
            String regionuser_id = response.get(i).getBranch().getArea().getRegion().getName();
            String datereport;
            if (response.get(i).getCreated_at() != null) {
                datereport = convertDateToRoman.convertDateHehe(response.get(i).getCreated_at());
            } else {
                datereport = "-";
            }
            String fulluser_id;
            if (response.get(i).getUser().getFullname() != null) {
                fulluser_id = response.get(i).getUser().getFullname();
            } else {
                fulluser_id = "-";
            }

            boolean foundRegion = false;
            for (int x = 0; x < listAllReport.size(); x++) {
                if (regionuser_id.equals(listAllReport.get(x).getArea_name())
                        && datereport.equals(listAllReport.get(x).getDate())) {
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
    }

    class AlphanumericComparator implements Comparator<String> {
        public int compare(String s1, String s2) {
            return extractInt(s1) - extractInt(s2);
        }

        int extractInt(String s) {
            String num = s.replaceAll("\\D", "");
            return num.isEmpty() ? 0 : Integer.parseInt(num);
        }
    }

}
