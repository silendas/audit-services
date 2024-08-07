package com.cms.audit.api.RMK.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.RMK.dto.RmkRealizeDto;
import com.cms.audit.api.RMK.model.Clasification;
import com.cms.audit.api.RMK.model.RmkPlan;
import com.cms.audit.api.RMK.model.RmkRealize;
import com.cms.audit.api.RMK.repository.ClasificationRepo;
import com.cms.audit.api.RMK.repository.RmkPlanRepo;
import com.cms.audit.api.RMK.repository.RmkRealizeRepo;

@Service
public class RmkRealizeService {

    @Autowired
    private RmkRealizeRepo repo;

    @Autowired
    private ClasificationRepo clasificationRepo;

    @Autowired
    private RmkPlanRepo rmkPlanRepo;

    public ResponseEntity<Object> getRmkRealize(Long rmkId) {
        return ResponseEntittyHandler.allHandler(repo.findAll(), "Berhasil", HttpStatus.OK, null);
    }

    public ResponseEntity<Object> createRmkRealize(RmkRealizeDto rmkRealize) {
        return ResponseEntittyHandler.allHandler(repo.save(createRealize(rmkRealize)), "Berhasil", HttpStatus.CREATED, null);
    }

    public ResponseEntity<Object> updateRmkRealize(RmkRealizeDto rmkRealize, Long id) {
        RmkRealize model = getRealizeById(id);
        model.setClasification(getClasificationById(rmkRealize.getClasificationId()));
        model.setRmkPlan(getRmkPlanById(rmkRealize.getRmkPlanId()));
        model.setValue(rmkRealize.getValue());
        model.setUpdated_at(new Date());
        return ResponseEntittyHandler.allHandler(repo.save(model), "Berhasil", HttpStatus.OK, null);
    }

    public ResponseEntity<Object> deleteRmkRealize(Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntittyHandler.errorResponse("Data tidak ditemukan", "Data tidak ditemukan", HttpStatus.NOT_FOUND);
        }
        RmkRealize model = repo.findById(id).get();
        model.setIs_delete(1);
        return ResponseEntittyHandler.allHandler(repo.save(model), "Berhasil", HttpStatus.OK, null);
    }

    public List<RmkRealize> resRmkRealizeByRmkPlanId(Long rmkPlanId) {
        return repo.findByRmkRealize(rmkPlanId);
    }

    public RmkRealize createRealize(RmkRealizeDto rmkRealize) {
        RmkRealize model = new RmkRealize();
        model.setClasification(getClasificationById(rmkRealize.getClasificationId()));
        model.setRmkPlan(getRmkPlanById(rmkRealize.getRmkPlanId()));
        model.setValue(rmkRealize.getValue());
        model.setCreated_at(new Date());
        model.setUpdated_at(new Date());
        return model;
    }

    public RmkRealize getRealizeById(Long id) {
        return repo.findById(id).get();
    }

    public Clasification getClasificationById(Long id) {
        return clasificationRepo.findById(id).get();
    }

    public RmkPlan getRmkPlanById(Long id) {
        return rmkPlanRepo.findById(id).get();
    }
    
}
