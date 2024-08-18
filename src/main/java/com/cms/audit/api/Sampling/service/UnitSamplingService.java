package com.cms.audit.api.Sampling.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Sampling.dto.request.UnitDto;
import com.cms.audit.api.Sampling.model.Sampling;
import com.cms.audit.api.Sampling.model.UnitSampling;
import com.cms.audit.api.Sampling.repository.UnitRepo;

@Service
public class UnitSamplingService {

    @Autowired
    private UnitRepo repo;

    public List<UnitSampling> getUnitSamplingBySamplingId(Long sammplingId) {
        return repo.findAllBySamplingId(sammplingId);
    }

    public void createList(Sampling sample, List<UnitDto> dto) {
        for(UnitDto d : dto) {
            UnitSampling build = new UnitSampling();
            build.setSampling(sample);
            build.setName(d.getName());
            build.setUnit(d.getUnit());
            build.setValue(d.getValue());
            repo.save(build);
        }
    }
    
}
