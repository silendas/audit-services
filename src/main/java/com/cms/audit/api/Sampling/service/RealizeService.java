package com.cms.audit.api.Sampling.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cms.audit.api.RMK.model.Clasification;
import com.cms.audit.api.RMK.model.ClasificationCategory;
import com.cms.audit.api.RMK.service.ClasificationService;
import com.cms.audit.api.Sampling.dto.request.RealizeDto;
import com.cms.audit.api.Sampling.dto.response.RealizeRes;
import com.cms.audit.api.Sampling.model.RealizeSampling;
import com.cms.audit.api.Sampling.model.Sampling;
import com.cms.audit.api.Sampling.repository.RealizeRepo;

@Service
public class RealizeService {

    @Autowired
    private RealizeRepo repo;

    @Autowired
    private ClasificationService clasificationService;

    public RealizeRes getRealizeBySamplingId(Long samplingId) {
        List<RealizeSampling> realize = repo.findAllBySamplingId(samplingId);
        return createRes(buildAllRealize(realize));
    }

    public void createList(Sampling sample, List<RealizeDto> dto) {
        for (RealizeDto d : dto) {
            RealizeSampling build = new RealizeSampling();
            build.setSampling(sample);
            build.setClasification(clasificationService.getClasificationById(d.getClasification_id()));
            build.setValue(d.getValue());
            build.setUnit(d.getUnit());
            repo.save(build);
        }
    }

    public List<RealizeSampling> buildAllRealize(List<RealizeSampling> sample) {
        List<Clasification> allClasifications = clasificationService.getAllClasification();

        for (Clasification clasification : allClasifications) {
            boolean exists = sample.stream()
                .anyMatch(r -> r.getClasification().getId().equals(clasification.getId()));

            if (!exists) {
                RealizeSampling defaultRealize = new RealizeSampling();
                defaultRealize.setClasification(clasification);
                defaultRealize.setValue(0L);
                defaultRealize.setUnit(0L);
                sample.add(defaultRealize);
            }
        }

        return sample;
    }

    public RealizeRes createRes(List<RealizeSampling> sample) {

        List<RealizeSampling> external = sample.stream()
            .filter(r -> r.getClasification().getCategory().equals(ClasificationCategory.EXTERNAL))
            .collect(Collectors.toList());

        List<RealizeSampling> internal = sample.stream()
            .filter(r -> r.getClasification().getCategory().equals(ClasificationCategory.INTERNAL))
            .collect(Collectors.toList());

        RealizeRes res = new RealizeRes();
        res.setExternal(external);
        res.setInternal(internal);
        return res;
    }
}
