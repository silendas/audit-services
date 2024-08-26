package com.cms.audit.api.Sampling.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Common.constant.SpecificationFIlter;
import com.cms.audit.api.Management.Office.BranchOffice.services.BranchService;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Sampling.dto.request.BranchSampleDto;
import com.cms.audit.api.Sampling.dto.response.BranchSamplingRes;
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
        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Specification<BranchSampling> spec = Specification
                .where(new SpecificationFIlter<BranchSampling>().isNotDeleted())
                .and(new SpecificationFIlter<BranchSampling>().dateRange(start_date, end_date))
                .and(new SpecificationFIlter<BranchSampling>().orderByIdDesc());
        if (getUser.getLevel().getCode().equals("B")) {
            spec = spec.and(new SpecificationFIlter<BranchSampling>().getByRegionIds(getUser.getRegionId()));
        } else if (getUser.getLevel().getCode().equals("C")) {
            spec = spec.and(new SpecificationFIlter<BranchSampling>().createdByEqual(getUser.getId()));
        }
        return pagSampling.findAll(spec, PageRequest.of(page, size));
    }

    public List<BranchSampling> getAllList(Date start_date, Date end_date) {
        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Specification<BranchSampling> spec = Specification
                .where(new SpecificationFIlter<BranchSampling>().isNotDeleted())
                .and(new SpecificationFIlter<BranchSampling>().dateRange(start_date, end_date))
                .and(new SpecificationFIlter<BranchSampling>().orderByIdDesc());
        if (getUser.getLevel().getCode().equals("B")) {
            spec = spec.and(new SpecificationFIlter<BranchSampling>().getByRegionIds(getUser.getRegionId()));
        } else if (getUser.getLevel().getCode().equals("C")) {
            spec = spec.and(new SpecificationFIlter<BranchSampling>().createdByEqual(getUser.getId()));
        }
        return repo.findAll(spec);
    }

    public BranchSamplingRes getBranchSamplingDtos(BranchSampling branchSampling) {
        BranchSamplingRes dto = new BranchSamplingRes();
        dto.setSampling_id(branchSampling.getId());
        dto.setBranch_name(branchSampling.getBranch().getName());
        dto.setRegion_name(branchSampling.getBranch().getArea().getRegion().getName());
        dto.setCreated_sampling(branchSampling.getCreated_at());
        dto.setCurrent_branch(branchSampling.getCurrent_branch());
        dto.setCurrent_rmk(branchSampling.getCurrent_rmk());
        dto.setPending_unit(branchSampling.getPending_unit());
        dto.setPending_value(branchSampling.getPending_value());
        return dto;
    }

    public BranchSampling create(BranchSampleDto dto, Long userId) {
        validation(dto);
        BranchSampling build = new BranchSampling();
        build.setBranch(branchService.getBranchById(dto.getBranch()));
        build.setCurrent_branch(dto.getCurrent_branch());
        build.setCurrent_rmk(dto.getCurrent_rmk());
        build.setPending_unit(dto.getPending_unit());
        build.setPending_value(dto.getPending_value());
        build.setCreated_at(new Date());
        build.setCreated_by(userId);
        build.setIs_delete(0);
        return repo.save(build);
    }

    public BranchSampling update(BranchSampleDto dto, Long userId, Long branchId) {
        validation(dto);
        BranchSampling build = repo.findById(branchId).get();
        build.setBranch(branchService.getBranchById(dto.getBranch()));
        build.setCurrent_branch(dto.getCurrent_branch());
        build.setCurrent_rmk(dto.getCurrent_rmk());
        build.setPending_unit(dto.getPending_unit());
        build.setPending_value(dto.getPending_value());
        build.setCreated_at(new Date());
        build.setCreated_by(userId);
        return repo.save(build);
    }

    public void validation(BranchSampleDto dto) {
        if (dto.getBranch() == null) {
            throw new RuntimeException("Branch is required");
        }

        if (dto.getCurrent_branch() == null) {
            throw new RuntimeException("Current_branch is required");
        }

        if (dto.getCurrent_rmk() == null) {
            throw new RuntimeException("Current_rmk is required");
        }

        if (dto.getPending_unit() == null) {
            throw new RuntimeException("Pending_unit is required");
        }

        if (dto.getPending_value() == null) {
            throw new RuntimeException("Pending_value is required");
        }
    }

}
