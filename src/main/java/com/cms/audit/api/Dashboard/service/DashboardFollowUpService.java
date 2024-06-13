package com.cms.audit.api.Dashboard.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Common.constant.SpecificationFIlter;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.Dashboard.dto.DateCompareDTO;
import com.cms.audit.api.Dashboard.repository.FollowUpDashboardRepo;
import com.cms.audit.api.FollowUp.models.FollowUp;
import com.cms.audit.api.Management.User.models.User;

@Service
public class DashboardFollowUpService {

    @Autowired
    private FollowUpDashboardRepo dashboardFollowUpRepo;

    public ResponseEntity<Object> dasboardStatus(Long month, Long year) {

        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Specification<FollowUp> spec = Specification.where(new SpecificationFIlter<FollowUp>().createdAtYear(year))
                .and(new SpecificationFIlter<FollowUp>().isNotDeleted());
        if (month != null && month != 0) {
            DateCompareDTO monthSeparate = convertDateToRoman.getDateRange(month);
            spec = spec.and(
                    new SpecificationFIlter<FollowUp>().dateRange(monthSeparate.getDate1(), monthSeparate.getDate2()));
        }
        if (getUser.getLevel().getCode().equals("C")) {
            spec = spec.and(new SpecificationFIlter<FollowUp>().userId(getUser.getId()));
        } else if (getUser.getLevel().getCode().equals("B")) {
            Specification<FollowUp> regionOrUserSpec = Specification
                    .where(new SpecificationFIlter<FollowUp>().getByRegionIds(getUser.getRegionId()))
                    .or(new SpecificationFIlter<FollowUp>().userId(getUser.getId()));
            spec = spec.and(regionOrUserSpec);
        }
        List<FollowUp> fuList = dashboardFollowUpRepo.findAll(spec);

        List<Object> responseData = getStatusSummary(fuList);

        Map<String, Object> mapping = new LinkedHashMap<>();
        if (year != null && year != 0) {
            mapping.put("year", year);
        } else {
            mapping.put("year", convertDateToRoman.getLongYearNumber(new Date()));
        }
        if (month != null && month != 0) {
            mapping.put("month", convertDateToRoman.getMonthName(month));
        } else {
            mapping.put("month", convertDateToRoman.getMonthName(convertDateToRoman.getLongMonthNumber(new Date())));
        }
        addMissingStatus(responseData);

        mapping.put("chart", responseData);

        return returnResponse(mapping);
    }

    private List<Object> getStatusSummary(List<FollowUp> followUps) {
        // Create a Map to hold the count of each status
        Map<String, Long> statusCount = followUps.stream()
                .collect(Collectors.groupingBy(
                        fu -> "CLOSE".equals(fu.getStatus().toString()) ? "CLOSE" : "OPEN",
                        Collectors.counting()));

        // Convert the Map to the desired format
        List<Object> result = new ArrayList<>();
        statusCount.forEach((status, count) -> {
            Map<String, Object> statusMap = new LinkedHashMap<>();
            statusMap.put("status", status);
            statusMap.put("total", count);
            result.add(statusMap);
        });

        return result;
    }

    private void addMissingStatus(List<Object> responseData) {
        // Check if both OPEN and CLOSE statuses exist
        boolean openExists = false;
        boolean closeExists = false;

        for (Object item : responseData) {
            Map<String, Object> statusMap = (Map<String, Object>) item;
            String status = (String) statusMap.get("status");
            if ("OPEN".equals(status)) {
                openExists = true;
            } else if ("CLOSE".equals(status)) {
                closeExists = true;
            }
        }

        // If OPEN status doesn't exist, add it with total 0
        if (!openExists) {
            Map<String, Object> openStatus = new LinkedHashMap<>();
            openStatus.put("status", "OPEN");
            openStatus.put("total", 0L); // Total is 0
            responseData.add(openStatus);
        }

        // If CLOSE status doesn't exist, add it with total 0
        if (!closeExists) {
            Map<String, Object> closeStatus = new LinkedHashMap<>();
            closeStatus.put("status", "CLOSE");
            closeStatus.put("total", 0L); // Total is 0
            responseData.add(closeStatus);
        }
    }

    public ResponseEntity<Object> returnResponse(Map<String, Object> obj) {
        if (obj == null && obj.isEmpty()) {
            return ResponseEntittyHandler.allHandler(obj, "Data tidak ditemukan", HttpStatus.OK, null);
        }
        return ResponseEntittyHandler.allHandler(obj, "Data berhasil ditampilkan", HttpStatus.OK, null);
    }
}
