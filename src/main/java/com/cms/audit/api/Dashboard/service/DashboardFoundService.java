package com.cms.audit.api.Dashboard.service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cms.audit.api.AuditDailyReport.models.AuditDailyReportDetail;
import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Common.constant.SpecificationFIlter;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.Dashboard.dto.DateCompareDTO;
import com.cms.audit.api.Dashboard.repository.ClarificationDashboardRepo;
import com.cms.audit.api.FollowUp.models.FollowUp;
import com.cms.audit.api.Management.User.models.User;

@Service
public class DashboardFoundService {

    @Autowired
    private ClarificationDashboardRepo repo;

    public ResponseEntity<Object> dasboardFound(Long year) {
        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Specification<Clarification> spec = Specification
                .where(new SpecificationFIlter<Clarification>().createdAtYear(year))
                .and(new SpecificationFIlter<Clarification>().isNotDeleted());
        if (getUser.getLevel().getCode().equals("C")) {
            spec = spec.and(new SpecificationFIlter<Clarification>().userId(getUser.getId()));
        } else if (getUser.getLevel().getCode().equals("B")) {
            Specification<Clarification> regionOrUserSpec = Specification
                    .where(new SpecificationFIlter<Clarification>().getByRegionIds(getUser.getRegionId()))
                    .or(new SpecificationFIlter<Clarification>().userId(getUser.getId()));
            spec = spec.and(regionOrUserSpec);
        }

        List<Object> responseData = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            long total = calculateMonthlyTotal(spec, year, i);
            addToResponseData(responseData, i, total);
        }

        Map<String, Object> mapping = new LinkedHashMap<>();
        if (year != null && year != 0) {
            mapping.put("year", year);
        } else {
            mapping.put("year", convertDateToRoman.getLongYearNumber(new Date()));
        }
        mapping.put("chart", responseData);

        return returnResponse(mapping);
    }

    private long calculateMonthlyTotal(Specification<Clarification> spec, Long year, int month) {
        LocalDateTime startOfMonth = LocalDateTime.of(year.intValue(), month, 1, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.toLocalDate().lengthOfMonth()).withHour(23)
                .withMinute(59).withSecond(59);

        Specification<Clarification> monthlySpec = spec
                .and(new SpecificationFIlter<Clarification>().dateRange(
                        Date.from(startOfMonth.atZone(ZoneId.systemDefault()).toInstant()),
                        Date.from(endOfMonth.atZone(ZoneId.systemDefault()).toInstant())));

        List<Clarification> fuList = repo.findAll(monthlySpec);
        return fuList.size();
    }

    private void addToResponseData(List<Object> responseData, int month, long total) {
        Map<String, Object> monthData = new LinkedHashMap<>();
        monthData.put("month", convertDateToRoman.getMonthName(Long.valueOf(month)));
        monthData.put("total", total);
        responseData.add(monthData);
    }

    public ResponseEntity<Object> returnResponse(Map<String, Object> obj) {
        if (obj == null || obj.isEmpty()) {
            return ResponseEntittyHandler.allHandler(obj, "Data tidak ditemukan", HttpStatus.OK, null);
        }
        return ResponseEntittyHandler.allHandler(obj, "Data berhasil ditampilkan", HttpStatus.OK, null);
    }

}
