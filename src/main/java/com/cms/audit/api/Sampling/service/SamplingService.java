package com.cms.audit.api.Sampling.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.BranchOffice.repository.BranchRepository;
import com.cms.audit.api.Sampling.dto.request.RealizeDto;
import com.cms.audit.api.Sampling.dto.request.SamplingDto;
import com.cms.audit.api.Sampling.dto.request.UnitDto;
import com.cms.audit.api.Sampling.dto.response.SamplingRes;
import com.cms.audit.api.Sampling.model.Sampling;
import com.cms.audit.api.Sampling.repository.SamplingRepository;

@Service
public class SamplingService {
    
    @Autowired
    private SamplingRepository repo;

    @Autowired
    private UnitSamplingService unitSamplingService;

    @Autowired
    private RealizeService realizeService;

    @Autowired
    private BranchRepository branchRepository;

    public ResponseEntity<Object> getSampling(){
        List<Sampling> sample = repo.findAll();
        return ResponseEntittyHandler.allHandler(createRes(sample), "Berhasil", HttpStatus.OK, null);
    }
    
    public ResponseEntity<Object> createSampling(SamplingDto dto){
        Sampling build = new Sampling();
        build.setBranch(getBranchById(dto.getBranch_id()));
        build.setCurrent(dto.getCurrent());
        build.setTarget(dto.getTarget());
        build.setCollectors(dto.getCollectors());
        Sampling res = repo.save(build);
        createSpareLifeSampling(res, dto.getUnit_sampling(), dto.getRealize_sampling());
        return ResponseEntittyHandler.allHandler(null, "Berhasil", HttpStatus.OK, null);
    }

    public ResponseEntity<Object> deleteSampling(Long id){
        Sampling res = repo.findById(id).get();
        res.setIs_deleted(1);
        repo.save(res);
        return ResponseEntittyHandler.allHandler(null, "Berhasil", HttpStatus.OK, null);
    }

    public List<SamplingRes> createRes(List<Sampling> sample){
        List<SamplingRes> res = new ArrayList<>();
        for (Sampling s : sample) {
            SamplingRes build = new SamplingRes();
            build.setId(s.getId());
            build.setBranch_name(s.getBranch().getName());
            build.setRegion_name(s.getBranch().getArea().getRegion().getName());
            build.setCurrent(s.getCurrent());
            build.setTarget(s.getTarget());
            build.setUnit_sampling(unitSamplingService.getUnitSamplingBySamplingId(s.getId()));
            build.setRealize_sampling(realizeService.getRealizeBySamplingId(s.getId()));
            res.add(build);
        }
        return res;
    }

    public void createSpareLifeSampling(Sampling sample, List<UnitDto> listUnit, List<RealizeDto> listRealize){
        doCreateRealize(sample, listRealize);
        doCreateUnit(sample, listUnit);
    }

    public void doCreateUnit(Sampling sample, List<UnitDto> listDto){
        unitSamplingService.createList(sample, listDto);
    }

    public void doCreateRealize(Sampling sample, List<RealizeDto> listDto){
        realizeService.createList(sample, listDto);
    }

    public Branch getBranchById(Long id){
        return branchRepository.findById(id).get();
    }

}
