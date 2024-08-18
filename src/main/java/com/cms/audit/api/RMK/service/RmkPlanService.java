package com.cms.audit.api.RMK.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Common.constant.SpecificationFIlter;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.RMK.dto.ResponseDetailRmk;
import com.cms.audit.api.RMK.dto.RmkPlanDto;
import com.cms.audit.api.RMK.model.RmkPlan;
import com.cms.audit.api.RMK.repository.PagRmkPlan;
import com.cms.audit.api.RMK.repository.RmkPlanRepo;

import lombok.val;

@Service
public class RmkPlanService {

    @Autowired
    private RmkPlanRepo repo;

    @Autowired
    private PagRmkPlan pag;

    @Autowired
    private RmkRealizeService realizeService;

    public ResponseEntity<Object> getRmkPlan(
            boolean pageable, int page, int size) {
        Specification<RmkPlan> spec = Specification
                .where(new SpecificationFIlter<RmkPlan>().isNotDeleted())
                .and(new SpecificationFIlter<RmkPlan>().orderByIdDesc());
        return ResponseEntittyHandler.allHandler(pag.findAll(spec, PageRequest.of(page, size)), "Berhasil",
                HttpStatus.OK,
                null);
    }

    public ResponseEntity<Object> getRmkPlanDetail(Long id) {
        return ResponseEntittyHandler.allHandler(buildDetailResponse(id), "Berhasil", HttpStatus.OK, null);
    }

    public ResponseEntity<Object> createRmkPlan(RmkPlanDto dto) {
        if (dto.getPending() == null || dto.getPending() == 0) {
            return ResponseEntittyHandler.allHandler(repo.save(builderRmkPlan(dto)), "Nilai pending tidak boleh kosong",
                    HttpStatus.CREATED,
                    null);
        }
        if (dto.getRmk() == null || dto.getRmk() == 0) {
            return ResponseEntittyHandler.allHandler(repo.save(builderRmkPlan(dto)), "Nilai Rmk tidak boleh kosong",
                    HttpStatus.CREATED,
                    null);
        }
        if (dto.getSlovin() == null || dto.getSlovin() == 0) {
            return ResponseEntittyHandler.allHandler(repo.save(builderRmkPlan(dto)), "Nilai Slovin tidak boleh kosong",
                    HttpStatus.CREATED,
                    null);
        }
        return ResponseEntittyHandler.allHandler(repo.save(builderRmkPlan(dto)), "Berhasil", HttpStatus.CREATED,
                null);
    }

    public ResponseEntity<Object> updateRmkPlan(RmkPlanDto rmkPlan, Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntittyHandler.errorResponse("Data tidak ditemukan", "Data tidak ditemukan",
                    HttpStatus.NOT_FOUND);
        }
        return ResponseEntittyHandler.allHandler(repo.save(builderRmkPlan(rmkPlan)), "Berhasil", HttpStatus.OK, null);
    }

    public ResponseEntity<Object> calculate(Long value, Double margin_error) {
        double marginOfError = margin_error != null ? margin_error : 0.05;
        if (value == null || marginOfError <= 0) {
            return ResponseEntittyHandler.errorResponse("Nilai atau margin kesalahan tidak valid", "Nilai tidak valid",
                    HttpStatus.BAD_REQUEST);
        }
    
        double slovin = value / (1 + value * Math.pow(marginOfError, 2));
    
        double roundedSlovin = Math.round(slovin * 100.0) / 100.0;
    
        Map<String, Object> slovinMap = new HashMap<>();
        slovinMap.put("slovin", roundedSlovin);
        return ResponseEntittyHandler.allHandler(slovinMap, "Berhasil", HttpStatus.OK, null);
    }

    public ResponseEntity<Object> deleteRmkPlan(Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntittyHandler.errorResponse("Data tidak ditemukan", "Data tidak ditemukan",
                    HttpStatus.NOT_FOUND);
        }
        RmkPlan model = repo.findById(id).get();
        model.setIs_delete(1);
        model.setUpdated_at(new Date());
        return ResponseEntittyHandler.allHandler(repo.save(model), "Berhasil", HttpStatus.OK, null);
    }

    public ResponseDetailRmk buildDetailResponse(Long id) {
        RmkPlan plan = repo.findById(id).get();
        ResponseDetailRmk res = new ResponseDetailRmk();
        res.setCollector(plan.getCollector());
        res.setSlovin(plan.getSlovin());
        res.setRealize(realizeService.getRmkRealizeByPlanId(plan.getId()));
        return res;
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
