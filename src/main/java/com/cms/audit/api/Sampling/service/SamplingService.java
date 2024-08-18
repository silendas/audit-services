package com.cms.audit.api.Sampling.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    public ResponseEntity<Object> getSampling(Date start_date, Date end_date, int page, int size, boolean pageable){
        if (pageable) {
            Page<BranchSampling> sample = branchSamplingService.getAll(start_date, end_date, page, size);
            return ResponseEntittyHandler.allHandler(createRes(sample), "Berhasil", HttpStatus.OK, null);
        }else{
            List<BranchSampling> sample = branchSamplingService.getAllList(start_date, end_date);
            return ResponseEntittyHandler.allHandler(createListRes(sample), "Berhasil", HttpStatus.OK, null);
        }
    }
    
    public ResponseEntity<Object> createSampling(SamplingDto dto){
        branchSamplingService.validation(dto.getBranch());
        collectorService.validation(dto.getCollectors());
        BranchSampling buildBranchSampling = branchSamplingService.create(dto.getBranch());
        collectorService.create(buildBranchSampling, dto.getCollectors());
        realizeService.createList(buildBranchSampling, dto.getSampling());
        return ResponseEntittyHandler.allHandler(null, "Berhasil", HttpStatus.OK, null);
    }

    public ResponseEntity<Object> deleteSampling(Long id){
        BranchSampling res = repo.findById(id).get();
        res.setIs_delete(1);
        repo.save(res);
        return ResponseEntittyHandler.allHandler(null, "Berhasil", HttpStatus.OK, null);
    }

    public Object createListRes(List<BranchSampling> sample){
        List<SamplingRes> res = new ArrayList<>();
        for (BranchSampling s : sample) {
            SamplingRes build = new SamplingRes();
            build.setBranch_sampling_id(s.getId());
            build.setBranch(branchSamplingService.getBranchSamplingDtos(s));
            build.setCollectors(collectorService.getCollectorSamplingDtos(collectorService.getCollectorSamplingBySamplingId(s.getId())));
            build.setSampling(realizeService.getRealizeBySamplingId(s.getId()));
            res.add(build);
        }
        return res;
    }

    public Object createRes(Page<BranchSampling> sample){
        Map<String, Object> map = new LinkedHashMap<>();
        List<SamplingRes> res = new ArrayList<>();
        for (BranchSampling s : sample.getContent()) {
            SamplingRes build = new SamplingRes();
            build.setBranch_sampling_id(s.getId());
            build.setBranch(branchSamplingService.getBranchSamplingDtos(s));
            build.setCollectors(collectorService.getCollectorSamplingDtos(collectorService.getCollectorSamplingBySamplingId(s.getId())));
            build.setSampling(realizeService.getRealizeBySamplingId(s.getId()));
            res.add(build);
        }
        map.put("pageable", sample.getPageable());
        map.put("total_pages", sample.getTotalPages());
        map.put("total_elements", sample.getTotalElements());
        map.put("current_page", sample.getNumber());
        map.put("current_size", sample.getSize());
        map.put("last", sample.isLast());
        map.put("first", sample.isFirst());
        map.put("empty", sample.isEmpty());
        map.put("last", sample.isLast());
        map.put("content", res);
        return map;
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
