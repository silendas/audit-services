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
import com.cms.audit.api.Sampling.dto.request.CollectorSamplingDto;
import com.cms.audit.api.Sampling.dto.request.RealizeDto;
import com.cms.audit.api.Sampling.dto.request.SamplingDto;
import com.cms.audit.api.Sampling.dto.response.SamplingRes;
import com.cms.audit.api.Sampling.model.BranchSampling;
import com.cms.audit.api.Sampling.repository.SamplingRepository;

@Service
public class SamplingService {
    
    @Autowired
    private SamplingRepository repo;

    @Autowired 
    private BranchSamplingService branchSamplingService;

    @Autowired
    private CollectorSamplingService collectorService;

    @Autowired
    private RealizeService realizeService;

    @Autowired
    private BranchRepository branchRepository;

    public ResponseEntity<Object> getSampling(){
        List<BranchSampling> sample = repo.findAll();
        return ResponseEntittyHandler.allHandler(createRes(sample), "Berhasil", HttpStatus.OK, null);
    }
    
    public ResponseEntity<Object> createSampling(SamplingDto dto){
        BranchSampling buildBranchSampling = branchSamplingService.create(dto.getBranch());
        collectorService.create(buildBranchSampling, dto.getCollectors());
        realizeService.createList(buildBranchSampling, dto.getSampling());
        //createSpareLifeSampling(res, dto.getUnit_sampling(), dto.getRealize_sampling());
        return ResponseEntittyHandler.allHandler(null, "Berhasil", HttpStatus.OK, null);
    }

    public ResponseEntity<Object> deleteSampling(Long id){
        BranchSampling res = repo.findById(id).get();
        res.setIs_deleted(1);
        repo.save(res);
        return ResponseEntittyHandler.allHandler(null, "Berhasil", HttpStatus.OK, null);
    }

    public List<SamplingRes> createRes(List<BranchSampling> sample){
        List<SamplingRes> res = new ArrayList<>();
        for (BranchSampling s : sample) {
            SamplingRes build = new SamplingRes();
            build.setBranch(branchSamplingService.getBranchSamplingDtos(s));
            build.setCollectors(collectorService.getCollectorSamplingDtos(collectorService.getCollectorSamplingBySamplingId(s.getId())));
            build.setSampling(realizeService.getRealizeBySamplingId(s.getId()));
            res.add(build);
        }
        return res;
    }

    public void createSpareLifeSampling(BranchSampling sample, CollectorSamplingDto listUnit, List<RealizeDto> listRealize){
        doCreateRealize(sample, listRealize);
        doCreateCollectors(sample, listUnit);
    }

    public void doCreateCollectors(BranchSampling sample, CollectorSamplingDto dto){
        collectorService.create(sample, dto);
    }

    public void doCreateRealize(BranchSampling sample, List<RealizeDto> listDto){
        realizeService.createList(sample, listDto);
    }

    public Branch getBranchById(Long id){
        return branchRepository.findById(id).get();
    }

}
