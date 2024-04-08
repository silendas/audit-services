package com.cms.audit.api.Report.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import com.cms.audit.api.AuditDailyReport.repository.AuditDailyReportDetailRepository;
import com.cms.audit.api.AuditDailyReport.repository.AuditDailyReportRepository;
import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.pdf.LHAReport;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.PDFResponse;
import com.cms.audit.api.Common.util.ExcelUtil;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;
import com.cms.audit.api.Management.Office.RegionOffice.repository.RegionRepository;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Management.User.repository.UserRepository;
import com.cms.audit.api.Report.dto.LhaReportDTO;
import com.cms.audit.api.Report.dto.ListLhaDTO;
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
    private AuditDailyReportRepository lhaRepository;

    @Autowired
    private UserRepository userRepository;

    public GlobalResponse getAll(Long areaId, String name, int page, int size, Date start_date, Date end_date) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Page<Clarification> response;
            if (user.getLevel().getId() == 3) {
                response = pagRepository.findByUserId(user.getId(), PageRequest.of(page, size));
            } else if (user.getLevel().getId() == 2 || user.getLevel().getId() == 1) {
                if (areaId != null && name != null && start_date != null && end_date != null) {
                    return GlobalResponse.builder().data(pagRepository.findByAllFilter(name, areaId, start_date,
                            end_date, PageRequest.of(page, size))).status(HttpStatus.OK).message("Success").build();
                } else if (name != null) {
                    List<User> getUser = userRepository.findByFullnameLike(name);
                    Pageable pageable = PageRequest.of(page, size);
                    List<Clarification> clarificationList = new ArrayList<>();
                    for (int i = 0; i < getUser.size(); i++) {
                        clarificationList.add(repository.findByUserId(getUser.get(i).getId()).orElse(null));
                    }
                    try {
                        int start = (int) pageable.getOffset();
                        int end = Math.min((start + pageable.getPageSize()), clarificationList.size());
                        List<Clarification> pageContent = clarificationList.subList(start, end);
                        Page<Clarification> response2 = new PageImpl<>(pageContent, pageable, clarificationList.size());
                        return GlobalResponse
                                .builder()
                                .message("Success")
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
                } else if (areaId != null) {
                    return getByBranchAllByDate(areaId, start_date, end_date, page, size);
                } else {
                    Pageable pageable = PageRequest.of(page, size);
                    List<Clarification> clarificationList = new ArrayList<>();
                    if (start_date == null || end_date == null) {
                        if (user.getLevel().getId() == 1) {
                            return GlobalResponse.builder().data(pagRepository.findAll(PageRequest.of(page, size)))
                                    .message("Success").status(HttpStatus.OK).build();
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
                        if (user.getLevel().getId() == 1) {
                            return GlobalResponse.builder()
                                    .data(pagRepository.findClarificationInDateRange(start_date, end_date,
                                            PageRequest.of(page, size)))
                                    .message("Success").status(HttpStatus.OK).build();
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
                                .message("Success")
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
            return GlobalResponse.builder().data(response).message("Success").status(HttpStatus.OK).build();
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
            return GlobalResponse.builder().data(response).message("Success").status(HttpStatus.OK).build();
        } catch (Exception e) {
            return GlobalResponse.builder().error(e).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public GlobalResponse getByBranchAllByDate(Long areaId, Date start_date, Date end_date, int page, int size) {
        try {
            Page<Clarification> response;
            if (start_date == null || end_date == null) {
                response = pagRepository.findByBranchId(areaId, PageRequest.of(page, size));
            } else {
                response = pagRepository.findByBranchInDateRange(areaId, start_date, end_date,
                        PageRequest.of(page, size));
            }
            if (response.isEmpty()) {
                return GlobalResponse.builder().message("Data Empty").status(HttpStatus.OK).build();
            }
            return GlobalResponse.builder().data(response).message("Success").status(HttpStatus.OK).build();
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
            return GlobalResponse.builder().data(response).message("Success").status(HttpStatus.OK).build();
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
            return GlobalResponse.builder().data(response).message("Success").status(HttpStatus.OK).build();
        } catch (Exception e) {
            return GlobalResponse.builder().error(e).message("Success").status(HttpStatus.OK).build();
        }
    }

    public ByteArrayInputStream getDataDownloadClarification(String name, Long areaId, Date start_date, Date end_date)
            throws IOException {
        List<Clarification> response;
        if (name != null && areaId != null && start_date != null && end_date != null) {
            response = repository.findByAllFilter(name, areaId, start_date, end_date);
        } else if (areaId != null && start_date != null && end_date != null) {
            response = repository.findByBranchInDateRange(areaId, start_date, end_date);
        } else if (name != null && start_date != null && end_date != null) {
            response = repository.findByNameInDateRange(name, start_date, end_date);
        } else if (name != null) {
            response = repository.findByFullname(name);
        } else if (areaId != null) {
            response = repository.findByBranchId(areaId);
        } else if (start_date != null && end_date != null) {
            response = repository.findClarificationInDateRange(start_date, end_date);
        } else {
            response = repository.findAll();
        }
        ByteArrayInputStream data = ExcelUtil.dataToExcel(response);
        return data;
    }

    public ResponseEntity<InputStreamResource> getDataDownloadLHA(Long user_id, Long areaId, Date start_date,
            Date end_date) throws FileNotFoundException, MalformedURLException {
        List<AuditDailyReport> response;
        LhaReportDTO dto = new LhaReportDTO();
        List<ListLhaDTO> list = new ArrayList<>();
        List<LhaReportDTO> listAllReport = new ArrayList<>();

        if (user_id != null && areaId != null && start_date != null && end_date != null) {
            response = lhaRepository.findLHAByAll(areaId, user_id, start_date, end_date);
        } else if (areaId != null && start_date != null && end_date != null) {
            response = lhaRepository.findLHAByRegionInDateRange(areaId, start_date, end_date);
        } else if (user_id != null && start_date != null && end_date != null) {
            response = lhaRepository.findAllLHAByUserIdInDateRange(user_id, start_date, end_date);
        } else if (user_id != null) {
            response = lhaRepository.findAllLHAByUserId(user_id);
        } else if (areaId != null) {
            response = lhaRepository.findLHAByRegion(areaId);
        } else if (start_date != null && end_date != null) {
            response = lhaRepository.findLHAInDateRange(start_date, end_date);
        } else {
            response = lhaRepository.findAll();
        }
        if (response.isEmpty()) {
            return null;
        }
        PDFResponse pdf;
        if (areaId != null) {
            Optional<Region> region = regionRepository.findOneRegionById(areaId);
            if (!region.isPresent()) {
                return null;
            }
            for (int i = 0; i < response.size(); i++) {
                String fullName = response.get(i).getUser().getFullname();
                List<AuditDailyReportDetail> detail = lhaDetailRepository.findByLHAId(response.get(i).getId());

                boolean found = false;
                for (int j = 0; j < list.size(); j++) {
                    if (fullName.equals(list.get(j).getName())) {
                        list.get(j).getLhaDetails().addAll(detail);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    ListLhaDTO listLha = new ListLhaDTO();
                    listLha.setName(fullName);
                    listLha.setBranch(response.get(i).getBranch().getName());
                    listLha.setLhaDetails(detail);
                    list.add(listLha);
                }
            }
            dto.setLhaDetail(list);
            dto.setAreaName(region.get().getName());
            dto.setDate(convertDateToRoman.convertDateToString(new Date()));
            pdf = LHAReport.generateLHAPDF(dto);
            list.clear();
        } else {
            for (int i = 0; i < response.size(); i++) {
                List<AuditDailyReportDetail> detail = lhaDetailRepository.findByLHAId(response.get(i).getId());
                String regionName = response.get(i).getBranch().getArea().getRegion().getName();
                String fullName = response.get(i).getUser().getFullname();

                boolean foundRegion = false;
                for (int x = 0; x < listAllReport.size(); x++) {
                    if (regionName.equals(listAllReport.get(x).getAreaName())) {
                        foundRegion = true;
                        List<ListLhaDTO> lhaDetails = listAllReport.get(x).getLhaDetail();
                        boolean foundUser = false;
                        for (int y = 0; y < lhaDetails.size(); y++) {
                            if (fullName.equals(lhaDetails.get(y).getName())) {
                                lhaDetails.get(y).getLhaDetails().addAll(detail);
                                foundUser = true;
                                break;
                            }
                        }
                        if (!foundUser) {
                            ListLhaDTO lhaDto = new ListLhaDTO();
                            lhaDto.setName(fullName);
                            lhaDto.setBranch(response.get(i).getBranch().getName());
                            lhaDto.setLhaDetails(detail);
                            lhaDetails.add(lhaDto);
                        }
                        break;
                    }
                }

                if (!foundRegion) {
                    LhaReportDTO reportDto = new LhaReportDTO();
                    reportDto.setAreaName(regionName);
                    reportDto.setDate(convertDateToRoman.convertDateToString(new Date()));
                    List<ListLhaDTO> lhaDetailList = new ArrayList<>();
                    ListLhaDTO lhaDto = new ListLhaDTO();
                    lhaDto.setName(fullName);
                    lhaDto.setBranch(response.get(i).getBranch().getName());
                    lhaDto.setLhaDetails(detail);
                    lhaDetailList.add(lhaDto);
                    reportDto.setLhaDetail(lhaDetailList);
                    listAllReport.add(reportDto);
                }
            }
            pdf = LHAReport.generateAllLHAPDF(listAllReport);
        }
        String path = pdf.getFilePath();
        File file = new File(path);
        InputStream inputStream = new FileInputStream(file);
        InputStreamResource isr = new InputStreamResource(inputStream);

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.valueOf("application/pdf"));
        httpHeaders.set("Content-Disposition", "inline; filename=" + pdf.getFileName());

        System.out.println("kesinin OK");
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(isr);
    }

}
