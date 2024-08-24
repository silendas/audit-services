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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.BranchOffice.repository.BranchRepository;
import com.cms.audit.api.Management.User.dto.DropDownUserDTO;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Management.User.services.UserService;
import com.cms.audit.api.Sampling.dto.request.CollectorSamplingDto;
import com.cms.audit.api.Sampling.dto.request.RealizeDto;
import com.cms.audit.api.Sampling.dto.request.SamplingDto;
import com.cms.audit.api.Sampling.dto.request.SamplingUpdateDto;
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
    private UserService userService;

    @Autowired
    private RealizeService realizeService;

    @Autowired
    private BranchRepository branchRepository;

    public ResponseEntity<Object> getSampling(Date start_date, Date end_date, int page, int size, boolean pageable) {
        if (pageable) {
            Page<BranchSampling> sample = branchSamplingService.getAll(start_date, end_date, page, size);
            return ResponseEntittyHandler.allHandler(createRes(sample), "Berhasil", HttpStatus.OK, null);
        } else {
            List<BranchSampling> sample = branchSamplingService.getAllList(start_date, end_date);
            return ResponseEntittyHandler.allHandler(createListRes(sample), "Berhasil", HttpStatus.OK, null);
        }
    }

    public ResponseEntity<Object> getSamplingObj(Long id) {
        return ResponseEntittyHandler.allHandler(createObjRes(repo.findById(id).get()), "Berhasil", HttpStatus.OK,
                null);
    }

    public ResponseEntity<Object> createSampling(SamplingDto dto) {
        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        branchSamplingService.validation(dto.getBranch());
        collectorService.validation(dto.getCollectors());
        BranchSampling buildBranchSampling = branchSamplingService.create(dto.getBranch(), getUser.getId());
        collectorService.create(buildBranchSampling, dto.getCollectors());
        realizeService.createList(buildBranchSampling, dto.getSampling());
        return ResponseEntittyHandler.allHandler(null, "Berhasil", HttpStatus.OK, null);
    }

    public ResponseEntity<Object> updateSampling(Long id, SamplingUpdateDto dto) {
        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        branchSamplingService.validation(dto.getBranch());
        collectorService.validation(dto.getCollectors());
        BranchSampling buildBranchSampling = branchSamplingService.update(dto.getBranch(), getUser.getId(), id);
        collectorService.update(buildBranchSampling, dto.getCollectors(), id);
        realizeService.update(buildBranchSampling, dto.getSampling());
        return ResponseEntittyHandler.allHandler(null, "Berhasil", HttpStatus.OK, null);
    }

    public ResponseEntity<Object> deleteSampling(Long id) {
        BranchSampling res = repo.findById(id).get();
        res.setIs_delete(1);
        repo.save(res);
        return ResponseEntittyHandler.allHandler(null, "Berhasil", HttpStatus.OK, null);
    }

    public Object createListRes(List<BranchSampling> sample) {
        List<SamplingRes> res = new ArrayList<>();
        for (BranchSampling s : sample) {
            SamplingRes build = createObjRes(s);
            res.add(build);
        }
        return res;
    }

    public SamplingRes createObjRes(BranchSampling s) {
        SamplingRes build = new SamplingRes();
        build.setBranch(branchSamplingService.getBranchSamplingDtos(s));
        build.setCollectors(collectorService
                .getCollectorSamplingDtos(collectorService.getCollectorSamplingBySamplingId(s.getId())));
        build.setSampling(realizeService.getRealizeBySamplingId(s.getId()));
        build.setCreated_by(makeCreatedBy(userService.getUserbyIdObj(s.getCreated_by())));
        return build;
    }

    public DropDownUserDTO makeCreatedBy(User user) {
        if(user == null) return null;
        DropDownUserDTO build = new DropDownUserDTO();
        build.setId(user.getId());
        build.setInitial_name(user.getInitial_name());
        build.setFullname(user.getFullname());
        build.setOffice(null);
        return build;
    }

    public Object createRes(Page<BranchSampling> sample) {
        Map<String, Object> map = new LinkedHashMap<>();
        List<SamplingRes> res = new ArrayList<>();
        for (BranchSampling s : sample.getContent()) {
            SamplingRes build = createObjRes(s);
            res.add(build);
        }
        Map<String, Object> pageable = new LinkedHashMap<>();
        pageable.put("page_size", sample.getPageable().getPageSize());
        pageable.put("page_number", sample.getPageable().getPageNumber());
        pageable.put("sort", sample.getPageable().getSort());
        pageable.put("offset", sample.getPageable().getOffset());
        pageable.put("paged", sample.getPageable().isPaged());
        pageable.put("unpaged", sample.getPageable().isUnpaged());
        map.put("pageable", pageable);
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

    public void createSpareLifeSampling(BranchSampling sample, CollectorSamplingDto listUnit,
            List<RealizeDto> listRealize) {
        doCreateRealize(sample, listRealize);
        doCreateCollectors(sample, listUnit);
    }

    public void doCreateCollectors(BranchSampling sample, CollectorSamplingDto dto) {
        collectorService.create(sample, dto);
    }

    public void doCreateRealize(BranchSampling sample, List<RealizeDto> listDto) {
        realizeService.createList(sample, listDto);
    }

    public Branch getBranchById(Long id) {
        return branchRepository.findById(id).get();
    }

}
