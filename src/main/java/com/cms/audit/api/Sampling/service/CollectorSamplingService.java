package com.cms.audit.api.Sampling.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Sampling.dto.request.CollectorSamplingDto;
import com.cms.audit.api.Sampling.model.BranchSampling;
import com.cms.audit.api.Sampling.model.CollectorSampling;
import com.cms.audit.api.Sampling.repository.CollectorRepo;

@Service
public class CollectorSamplingService {

    @Autowired
    private CollectorRepo repo;

    public CollectorSampling getCollectorSamplingBySamplingId(Long sammplingId) {
        return repo.findAllBySamplingId(sammplingId);
    }

    public CollectorSamplingDto getCollectorSamplingDtos(CollectorSampling collectorSampling) {
        CollectorSamplingDto dto = new CollectorSamplingDto();
        dto.setCollectors(collectorSampling.getCollectors());
        dto.setRmk_unit(collectorSampling.getRmk_unit());
        dto.setRmk_value(collectorSampling.getRmk_value());
        dto.setPending_unit(collectorSampling.getPending_unit());
        dto.setPending_value(collectorSampling.getPending_value());
        dto.setUnit_sampling_unit(collectorSampling.getUnit_sampling_unit());
        dto.setUnit_sampling_value(collectorSampling.getUnit_sampling_value());
        dto.setTarget_unit(collectorSampling.getTarget_unit());
        dto.setTarget_value(collectorSampling.getTarget_value());
        return dto;
    }

    public CollectorSampling create(BranchSampling branchSampling, CollectorSamplingDto dto) {
        CollectorSampling collectorSampling = new CollectorSampling();
        collectorSampling.setBranchSampling(branchSampling);
        collectorSampling.setCollectors(dto.getCollectors());
        collectorSampling.setRmk_unit(dto.getRmk_unit());
        collectorSampling.setRmk_value(dto.getRmk_value());
        collectorSampling.setPending_unit(dto.getPending_unit());
        collectorSampling.setPending_value(dto.getPending_value());
        collectorSampling.setUnit_sampling_unit(dto.getUnit_sampling_unit());
        collectorSampling.setUnit_sampling_value(dto.getUnit_sampling_value());
        collectorSampling.setTarget_unit(dto.getTarget_unit());
        collectorSampling.setTarget_value(dto.getTarget_value());
        return repo.save(collectorSampling);
    }
}
