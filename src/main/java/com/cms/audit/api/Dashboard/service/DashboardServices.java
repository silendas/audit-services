package com.cms.audit.api.Dashboard.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Common.constant.SpecificationFIlter;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.Dashboard.dto.DateCompareDTO;
import com.cms.audit.api.Dashboard.repository.FollowUpDashboardRepo;
import com.cms.audit.api.FollowUp.models.EStatusFollowup;
import com.cms.audit.api.FollowUp.models.FollowUp;

@Service
public class DashboardServices {

    @Autowired
    private FollowUpDashboardRepo fuRepo;

    public ResponseEntity<Object> getDashboard(Long date1, Long date2) {
        List<FollowUp> fu1 = new ArrayList<>();
        List<FollowUp> fu2 = new ArrayList<>();
        if (date2 != null && date2 != 0) {
            DateCompareDTO dateCompare = convertDateToRoman.getDateRange(date2);
            Specification<FollowUp> spec = Specification
                    .where(new SpecificationFIlter<FollowUp>().dateRange(dateCompare.getDate1(),
                            dateCompare.getDate2()))
                    .and(new SpecificationFIlter<FollowUp>().orderByIdDesc());
            fu2 = fuRepo.findAll(spec);
        }
        DateCompareDTO dateCompare = convertDateToRoman.getDateRange(date1);
        Specification<FollowUp> spec = Specification
                .where(new SpecificationFIlter<FollowUp>().dateRange(dateCompare.getDate1(), dateCompare.getDate2()))
                .and(new SpecificationFIlter<FollowUp>().orderByIdDesc());
        fu1 = fuRepo.findAll(spec);

        return returnResponse(fu1, fu2);
    }

    public ResponseEntity<Object> returnResponse(List<FollowUp> listFu1, List<FollowUp> listFu2) {
        Map<String, Object> mapping = new LinkedHashMap<>();
        Map<String, Object> fuMap = new LinkedHashMap<>();
        String message;
        if (listFu1 != null) {
            for (FollowUp fu : listFu1) {
                fuMap.put("id", fu.getId());
                fuMap.put("created_at", fu.getCreated_at());
                fuMap.put("year", convertDateToRoman.convertIntYear(fu.getCreated_at()));
                if (!fu.getStatus().equals(EStatusFollowup.CLOSE)) {
                    fuMap.put("status", "OPEN");
                } else {
                    fuMap.put("status", "CLOSE");
                }
            }
            mapping.put("follow_up1", fuMap);
            message = "Data berhasil ditampilkan";
        } else {
            message = "Data tidak ditemukan";
        }
        if (listFu2 != null) {
            for (FollowUp fu : listFu2) {
                fuMap.put("id", fu.getId());
                fuMap.put("created_at", fu.getCreated_at());
                fuMap.put("year", convertDateToRoman.convertIntYear(fu.getCreated_at()));
                if (!fu.getStatus().equals(EStatusFollowup.CLOSE)) {
                    fuMap.put("status", "OPEN");
                } else {
                    fuMap.put("status", "CLOSE");
                }
            }
            mapping.put("follow_up2", listFu2);
        }
        return ResponseEntittyHandler.allHandler(mapping, message, HttpStatus.OK, null);
    }

}
