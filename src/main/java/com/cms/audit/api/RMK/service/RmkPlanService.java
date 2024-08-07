package com.cms.audit.api.RMK.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.RMK.dto.RmkPlanDto;
import com.cms.audit.api.RMK.model.RmkPlan;
import com.cms.audit.api.RMK.repository.RmkPlanRepo;

@Service
public class RmkPlanService {

    @Autowired
    private RmkPlanRepo repo;

    @Autowired
    private RmkPlanRepo pag;

    public ResponseEntity<Object> getRmkPlan(boolean pageable, int page, int size) {
        return ResponseEntittyHandler.allHandler(pag.findAll(PageRequest.of(page, size)), "Berhasil", HttpStatus.OK, null);
    }

    public ResponseEntity<Object> getRmkPlanDetail(Long id) {
        return ResponseEntittyHandler.allHandler(repo.findById(id), "Berhasil", HttpStatus.OK, null);
    }

    public ResponseEntity<Object> createRmkPlan(RmkPlanDto rmkPlan) {
        return ResponseEntittyHandler.allHandler(repo.save(builderRmkPlan(rmkPlan)), "Berhasil", HttpStatus.CREATED, null);
    }

    public ResponseEntity<Object> updateRmkPlan(RmkPlanDto rmkPlan, Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntittyHandler.errorResponse("Data tidak ditemukan", "Data tidak ditemukan", HttpStatus.NOT_FOUND);
        }
        return ResponseEntittyHandler.allHandler(repo.save(builderRmkPlan(rmkPlan)), "Berhasil", HttpStatus.OK, null);
    }

    public ResponseEntity<Object> calculate(Long rmk, Long pending) {
        double marginOfError = 0.05;
    
        if (rmk == null || marginOfError <= 0) {
            return ResponseEntittyHandler.errorResponse("Nilai RMK atau margin kesalahan tidak valid", "Nilai tidak valid", HttpStatus.BAD_REQUEST);
        }
    
        double slovin = rmk / (1 + rmk * Math.pow(marginOfError, 2));

        Map<String, Object> slovinMap = new HashMap<>();
        slovinMap.put("slovin", slovin);
        return ResponseEntittyHandler.allHandler(slovinMap, "Berhasil", HttpStatus.OK, null);
    }

    public ResponseEntity<Object> deleteRmkPlan(Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntittyHandler.errorResponse("Data tidak ditemukan", "Data tidak ditemukan", HttpStatus.NOT_FOUND);
        }
        RmkPlan model = repo.findById(id).get();
        model.setIs_delete(1);
        model.setUpdated_at(new Date());
        return ResponseEntittyHandler.allHandler(repo.save(model), "Berhasil", HttpStatus.OK, null);
    }

    public RmkPlan builderRmkPlan(RmkPlanDto rmkPlan) {
        RmkPlan model = new RmkPlan();
        model.setRmk(rmkPlan.getRmk());
        model.setPending(rmkPlan.getPending());
        model.setSlovin(rmkPlan.getSlovin());
        model.setIs_delete(0);
        model.setCreated_at(new Date());
        model.setUpdated_at(new Date());
        return model;
    }
    
}
