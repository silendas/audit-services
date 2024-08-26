package com.cms.audit.api.Sampling.service;

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
        dto.setNip(collectorSampling.getNip());
        dto.setRmk_unit(collectorSampling.getRmk_unit());
        dto.setRmk_value(collectorSampling.getRmk_value());
        dto.setPending_unit(collectorSampling.getPending_unit());
        dto.setPending_value(collectorSampling.getPending_value());
        dto.setUnit_sampling_unit(collectorSampling.getUnit_sampling_unit());
        dto.setUnit_sampling_value(collectorSampling.getUnit_sampling_value());
        dto.setTarget_unit(collectorSampling.getTarget_unit());
        dto.setTarget_value(collectorSampling.getTarget_value());
        dto.setMargin_error(collectorSampling.getMargin_error());
        return dto;
    }

    public CollectorSampling create(BranchSampling branchSampling, CollectorSamplingDto dto) {
        validation(dto);
        CollectorSampling collectorSampling = new CollectorSampling();
        collectorSampling.setBranchSampling(branchSampling);
        collectorSampling.setCollectors(dto.getCollectors());
        collectorSampling.setNip(dto.getNip());
        collectorSampling.setRmk_unit(dto.getRmk_unit());
        collectorSampling.setRmk_value(dto.getRmk_value());
        collectorSampling.setPending_unit(dto.getPending_unit());
        collectorSampling.setPending_value(dto.getPending_value());
        collectorSampling.setUnit_sampling_unit(dto.getUnit_sampling_unit());
        collectorSampling.setUnit_sampling_value(dto.getUnit_sampling_value());
        collectorSampling.setTarget_unit(dto.getTarget_unit());
        collectorSampling.setTarget_value(dto.getTarget_value());
        collectorSampling.setMargin_error(dto.getMargin_error());
        return repo.save(collectorSampling);
    }

    public CollectorSampling update(BranchSampling branchSampling, CollectorSamplingDto dto, Long samplingId) {
        validation(dto);
        CollectorSampling collectorSampling = repo.findAllBySamplingId(samplingId);
        collectorSampling.setBranchSampling(branchSampling);
        collectorSampling.setCollectors(dto.getCollectors());
        collectorSampling.setNip(dto.getNip());
        collectorSampling.setRmk_unit(dto.getRmk_unit());
        collectorSampling.setRmk_value(dto.getRmk_value());
        collectorSampling.setPending_unit(dto.getPending_unit());
        collectorSampling.setPending_value(dto.getPending_value());
        collectorSampling.setUnit_sampling_unit(dto.getUnit_sampling_unit());
        collectorSampling.setUnit_sampling_value(dto.getUnit_sampling_value());
        collectorSampling.setTarget_unit(dto.getTarget_unit());
        collectorSampling.setTarget_value(dto.getTarget_value());
        collectorSampling.setMargin_error(dto.getMargin_error());
        return repo.save(collectorSampling);
    }



    public void validation(CollectorSamplingDto dto) {
        if (dto.getCollectors() == null) {
            throw new RuntimeException("Collectors is required");
        }

        if (dto.getNip() == null) {
            throw new RuntimeException("Nip is required");
        }

        if (dto.getRmk_unit() == null) {
            throw new RuntimeException("Rmk_unit is required");
        }

        if (dto.getRmk_value() == null) {
            throw new RuntimeException("Rmk_value is required");
        }

        if (dto.getPending_unit() == null) {
            throw new RuntimeException("Pending_unit is required");
        }

        if (dto.getPending_value() == null) {
            throw new RuntimeException("Pending_value is required");
        }

        if (dto.getUnit_sampling_unit() == null) {
            throw new RuntimeException("Unit_sampling_unit is required");
        }

        if (dto.getUnit_sampling_value() == null) {
            throw new RuntimeException("Unit_sampling_value is required");
        }

        if (dto.getTarget_unit() == null) {
            throw new RuntimeException("Target_unit is required");
        }

        if (dto.getTarget_value() == null) {
            throw new RuntimeException("Target_value is required");
        }

    }
}
