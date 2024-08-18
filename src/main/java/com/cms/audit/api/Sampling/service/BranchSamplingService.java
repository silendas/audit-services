package com.cms.audit.api.Sampling.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Common.constant.SpecificationFIlter;
import com.cms.audit.api.InspectionSchedule.models.Schedule;
import com.cms.audit.api.Management.Office.BranchOffice.services.BranchService;
import com.cms.audit.api.Sampling.dto.request.BranchSamplingDto;
import com.cms.audit.api.Sampling.model.BranchSampling;
import com.cms.audit.api.Sampling.repository.PagSampling;
import com.cms.audit.api.Sampling.repository.SamplingRepository;

@Service
public class BranchSamplingService {

    @Autowired
    private SamplingRepository repo;

    @Autowired
    private PagSampling pagSampling;

    @Autowired
    private BranchService branchService;

    public Page<BranchSampling> getAll(Date start_date, Date end_date, int page, int size) {
        Specification<BranchSampling> spec = Specification
                .where(new SpecificationFIlter<BranchSampling>().isNotDeleted())
                .and(new SpecificationFIlter<BranchSampling>().dateRange(start_date, end_date))
                .and(new SpecificationFIlter<BranchSampling>().orderByIdDesc());
        return pagSampling.findAll(spec, PageRequest.of(page, size));
    }

    // public List<BranchSamplingDto> getBranchSamplingDtos(List<BranchSampling>
    // branchSampling) {
    // List<BranchSamplingDto> build = new ArrayList<>();
    // for (BranchSampling item : branchSampling) {
    // BranchSamplingDto dto = new BranchSamplingDto();
    // dto.setBranch(item.getBranch().getId());
    // dto.setCurrent_branch(item.getCurrent_branch());
    // dto.setCurrent_rmk(item.getCurrent_rmk());
    // dto.setPending_unit(item.getPending_unit());
    // dto.setPending_value(item.getPending_value());
    // build.add(dto);
    // }
    // return build;
    // }

    public BranchSamplingDto getBranchSamplingDtos(BranchSampling branchSampling) {
        BranchSamplingDto dto = new BranchSamplingDto();
        dto.setBranch(branchSampling.getBranch().getId());
        dto.setCurrent_branch(branchSampling.getCurrent_branch());
        dto.setCurrent_rmk(branchSampling.getCurrent_rmk());
        dto.setPending_unit(branchSampling.getPending_unit());
        dto.setPending_value(branchSampling.getPending_value());
        return dto;
    }

    public BranchSampling create(BranchSamplingDto dto) {
        BranchSampling build = new BranchSampling();
        build.setBranch(branchService.getBranchById(dto.getBranch()));
        build.setCurrent_branch(dto.getCurrent_branch());
        build.setCurrent_rmk(dto.getCurrent_rmk());
        build.setPending_unit(dto.getPending_unit());
        build.setPending_value(dto.getPending_value());
        build.setCreated_at(new Date());
        build.setIs_deleted(0);
        return repo.save(build);
    }

}
