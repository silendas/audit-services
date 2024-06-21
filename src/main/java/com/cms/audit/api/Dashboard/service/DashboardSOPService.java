package com.cms.audit.api.Dashboard.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Common.constant.SpecificationFIlter;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.Dashboard.dto.DateCompareDTO;
import com.cms.audit.api.Dashboard.repository.ClarificationDashboardRepo;
import com.cms.audit.api.Management.CaseCategory.models.CaseCategory;
import com.cms.audit.api.Management.CaseCategory.repository.CaseCategoryRepository;
import com.cms.audit.api.Management.User.models.User;

@Service
public class DashboardSOPService {
    
    @Autowired
    private ClarificationDashboardRepo repo;

    @Autowired
    private CaseCategoryRepository caseCategoryRepo;

    public ResponseEntity<Object> dashboardSOP(Long year, Long month) {
        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Specification<Clarification> spec = Specification
                .where(new SpecificationFIlter<Clarification>().createdAtYear(year))
                .and(new SpecificationFIlter<Clarification>().isNotDeleted());

        if (month != null && month != 0) {
            DateCompareDTO monthSeparate = convertDateToRoman.getDateRange(month);
            spec = spec.and(
                    new SpecificationFIlter<Clarification>().dateRange(monthSeparate.getDate1(), monthSeparate.getDate2()));
        }

        if (getUser.getLevel().getCode().equals("C")) {
            spec = spec.and(new SpecificationFIlter<Clarification>().userId(getUser.getId()));
        } else if (getUser.getLevel().getCode().equals("B")) {
            Specification<Clarification> regionOrUserSpec = Specification
                    .where(new SpecificationFIlter<Clarification>().getByRegionIds(getUser.getRegionId()))
                    .or(new SpecificationFIlter<Clarification>().userId(getUser.getId()));
            spec = spec.and(regionOrUserSpec);
        }

        List<Clarification> clarifications = repo.findAll(spec);
        
        // Filter out clarifications with nominal_loss null or 0
        List<Clarification> filteredClarifications = clarifications.stream()
                .filter(c -> c.getNominal_loss() != null && c.getNominal_loss() > 0)
                .collect(Collectors.toList());

        Map<String, Long> caseCategoryCountMap = filteredClarifications.stream()
                .collect(Collectors.groupingBy(c -> c.getCaseCategory().getName(), Collectors.counting()));

        Map<String, Long> caseCategoryNominalLossMap = filteredClarifications.stream()
                .collect(Collectors.groupingBy(c -> c.getCaseCategory().getName(), Collectors.summingLong(Clarification::getNominal_loss)));

        // Fetch only case categories that are referenced in clarifications
        List<CaseCategory> existingCaseCategories = filteredClarifications.stream()
                .map(Clarification::getCaseCategory)
                .distinct()
                .collect(Collectors.toList());

        List<Map<String, Object>> responseData = new ArrayList<>();
        for (CaseCategory caseCategory : existingCaseCategories) {
            Map<String, Object> caseData = new HashMap<>();
            caseData.put("case", caseCategory.getCases().getName());
            caseData.put("case_category", caseCategory.getName());
            caseData.put("total", caseCategoryCountMap.getOrDefault(caseCategory.getName(), 0L));
            caseData.put("total_nominal_loss", caseCategoryNominalLossMap.getOrDefault(caseCategory.getName(), 0L));
            responseData.add(caseData);
        }

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

        mapping.put("chart", responseData);

        return returnResponse(mapping);
    }

    public ResponseEntity<Object> returnResponse(Map<String, Object> obj) {
        if (obj == null || obj.isEmpty()) {
            return ResponseEntittyHandler.allHandler(obj, "Data tidak ditemukan", HttpStatus.OK, null);
        }
        return ResponseEntittyHandler.allHandler(obj, "Data berhasil ditampilkan", HttpStatus.OK, null);
    }
}
