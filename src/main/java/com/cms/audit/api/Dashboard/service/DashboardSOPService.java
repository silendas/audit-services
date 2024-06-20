package com.cms.audit.api.Dashboard.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Common.constant.SpecificationFIlter;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.util.ExcelUtil;
import com.cms.audit.api.Common.util.reportDashboard;
import com.cms.audit.api.Dashboard.dto.DateCompareDTO;
import com.cms.audit.api.Dashboard.repository.ClarificationDashboardRepo;
import com.cms.audit.api.FollowUp.models.FollowUp;
import com.cms.audit.api.Management.User.models.User;

@Service
public class DashboardSOPService {

    @Autowired
    private ClarificationDashboardRepo repo;

    public ResponseEntity<byte[]> dashboardSOP(Long year, Long month) {
        try {
            User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Specification<Clarification> spec = Specification
                    .where(new SpecificationFIlter<Clarification>().createdAtYear(year))
                    .and(new SpecificationFIlter<Clarification>().isNotDeleted());
            if (month != null && month != 0) {
                DateCompareDTO monthSeparate = convertDateToRoman.getDateRange(month);
                spec = spec.and(
                        new SpecificationFIlter<Clarification>().dateRange(monthSeparate.getDate1(),
                                monthSeparate.getDate2()));
            }
            if (getUser.getLevel().getCode().equals("C")) {
                spec = spec.and(new SpecificationFIlter<Clarification>().userId(getUser.getId()));
            } else if (getUser.getLevel().getCode().equals("B")) {
                Specification<Clarification> regionOrUserSpec = Specification
                        .where(new SpecificationFIlter<Clarification>().getByRegionIds(getUser.getRegionId()))
                        .or(new SpecificationFIlter<Clarification>().userId(getUser.getId()));
                spec = spec.and(regionOrUserSpec);
            }
            // Fetch data from the repository
            List<Clarification> clarificationsList = repo.findAll(spec);

            // Generate Excel file
            ByteArrayInputStream in = reportDashboard.dataToExcel(clarificationsList, month);

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=clarifications_report.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(in.readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
