package com.cms.audit.api.Report.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.util.ExcelUtil;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Management.User.repository.UserRepository;
import com.cms.audit.api.Report.repository.PagReport;
import com.cms.audit.api.Report.repository.ReportRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReportService {

    @Autowired
    private ReportRepository repository;

    @Autowired
    private PagReport pagRepository;

    @Autowired
    private UserRepository userRepository;

    public GlobalResponse getAll(Long branchId, String name, int page, int size, Date start_date, Date end_date) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Page<Clarification> response;
            if (user.getLevel().getId() == 3) {
                response = pagRepository.findByUserId(user.getId(), PageRequest.of(page, size));
            } else if (user.getLevel().getId() == 2 || user.getLevel().getId() == 1) {
                if (branchId != null && name != null && start_date != null && end_date != null) {
                    return GlobalResponse.builder().data(pagRepository.findByAllFilter(name, branchId, start_date,
                            end_date, PageRequest.of(page, size))).status(HttpStatus.OK).message("Success").build();
                } else if (name != null) {
                    List<User> getUser = userRepository.findByFullnameLike(name);
                    Pageable pageable = PageRequest.of(page, size);
                    List<Clarification> clarificationList = new ArrayList<>();
                    for (int i = 0; i < getUser.size(); i++) {
                        clarificationList.add(repository.findByUserId(getUser.get(i).getId()).orElse(null));
                    }
                    try {
                        int start = (int) pageable.getOffset();
                        int end = Math.min((start + pageable.getPageSize()), clarificationList.size());
                        List<Clarification> pageContent = clarificationList.subList(start, end);
                        Page<Clarification> response2 = new PageImpl<>(pageContent, pageable, clarificationList.size());
                        return GlobalResponse
                                .builder()
                                .message("Success")
                                .data(response2)
                                .status(HttpStatus.OK)
                                .build();
                    } catch (Exception e) {
                        return GlobalResponse
                                .builder()
                                .error(e)
                                .status(HttpStatus.BAD_REQUEST)
                                .build();
                    }
                } else if (branchId != null) {
                    return getByBranchAllByDate(branchId, start_date, end_date, page, size);
                } else {
                    Pageable pageable = PageRequest.of(page, size);
                    List<Clarification> clarificationList = new ArrayList<>();
                    if (start_date == null || end_date == null) {
                        if (user.getLevel().getId() == 1) {
                            return GlobalResponse.builder().data(pagRepository.findAll(PageRequest.of(page, size)))
                                    .message("Success").status(HttpStatus.OK).build();
                        }
                        for (int i = 0; i < user.getRegionId().size(); i++) {
                            List<Clarification> getByRegion = repository.findByRegionId(user.getRegionId().get(i));
                            for (int u = 0; u < getByRegion.size(); u++) {
                                if (!clarificationList.contains(getByRegion.get(u))) {
                                    clarificationList.add(getByRegion.get(u));
                                }
                            }
                        }
                    } else {
                        if (user.getLevel().getId() == 1) {
                            return GlobalResponse.builder().data(pagRepository.findClarificationInDateRange(start_date, end_date, PageRequest.of(page, size))).message("Success").status(HttpStatus.OK).build();
                        }
                        for (int i = 0; i < user.getRegionId().size(); i++) {
                            List<Clarification> getByRegion = repository
                                    .findByRegionIdAndDate(user.getRegionId().get(i), start_date, end_date);
                            for (int u = 0; u < getByRegion.size(); u++) {
                                if (!clarificationList.contains(getByRegion.get(u))) {
                                    clarificationList.add(getByRegion.get(u));
                                }
                            }
                        }
                    }
                    try {
                        int start = (int) pageable.getOffset();
                        int end = Math.min((start + pageable.getPageSize()), clarificationList.size());
                        List<Clarification> pageContent = clarificationList.subList(start, end);
                        Page<Clarification> response2 = new PageImpl<>(pageContent, pageable, clarificationList.size());
                        return GlobalResponse
                                .builder()
                                .message("Success")
                                .data(response2)
                                .status(HttpStatus.OK)
                                .build();
                    } catch (Exception e) {
                        return GlobalResponse
                                .builder()
                                .error(e)
                                .status(HttpStatus.BAD_REQUEST)
                                .build();
                    }
                }
            } else {
                if (start_date == null || end_date == null) {
                    response = pagRepository.findAll(PageRequest.of(page, size));
                } else {
                    response = pagRepository.findClarificationInDateRange(start_date, end_date,
                            PageRequest.of(page, size));
                }
            }
            if (response.isEmpty()) {
                return GlobalResponse.builder().message("Data Empty").status(HttpStatus.OK).build();
            }
            return GlobalResponse.builder().data(response).message("Success").status(HttpStatus.OK).build();
        } catch (Exception e) {
            return GlobalResponse.builder().error(e).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public GlobalResponse getByUserAllByDate(Long userId, Date start_date, Date end_date, int page, int size) {
        try {
            Page<Clarification> response;
            if (start_date == null || end_date == null) {
                response = pagRepository.findByUserId(userId, PageRequest.of(page, size));
            } else {
                response = pagRepository.findByUserInDateRange(userId, start_date, end_date,
                        PageRequest.of(page, size));
            }
            if (response.isEmpty()) {
                return GlobalResponse.builder().message("Data Empty").status(HttpStatus.OK).build();
            }
            return GlobalResponse.builder().data(response).message("Success").status(HttpStatus.OK).build();
        } catch (Exception e) {
            return GlobalResponse.builder().error(e).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public GlobalResponse getByBranchAllByDate(Long branchId, Date start_date, Date end_date, int page, int size) {
        try {
            Page<Clarification> response;
            if (start_date == null || end_date == null) {
                response = pagRepository.findByBranchId(branchId, PageRequest.of(page, size));
            } else {
                response = pagRepository.findByBranchInDateRange(branchId, start_date, end_date,
                        PageRequest.of(page, size));
            }
            if (response.isEmpty()) {
                return GlobalResponse.builder().message("Data Empty").status(HttpStatus.OK).build();
            }
            return GlobalResponse.builder().data(response).message("Success").status(HttpStatus.OK).build();
        } catch (Exception e) {
            return GlobalResponse.builder().error(e).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public GlobalResponse getAllByDate(Date start_date, Date end_date, int page, int size) {
        try {
            Page<Clarification> response = pagRepository.findClarificationInDateRange(start_date, end_date,
                    PageRequest.of(page, size));
            if (response.isEmpty()) {
                return GlobalResponse.builder().message("Data Empty").status(HttpStatus.OK).build();
            }
            return GlobalResponse.builder().data(response).message("Success").status(HttpStatus.OK).build();
        } catch (Exception e) {
            return GlobalResponse.builder().error(e).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public GlobalResponse getOne(Long id) {
        try {
            Optional<Clarification> response = repository.findById(id);
            if (!response.isPresent()) {
                return GlobalResponse.builder().message("Data Empty").status(HttpStatus.OK).build();
            }
            return GlobalResponse.builder().data(response).message("Success").status(HttpStatus.OK).build();
        } catch (Exception e) {
            return GlobalResponse.builder().error(e).message("Success").status(HttpStatus.OK).build();
        }
    }

    public ByteArrayInputStream getDataDownload(String name, Long branchId,Date start_date, Date end_date) throws IOException {
        List<Clarification> response;
        if(name != null && branchId != null && start_date != null && end_date != null){
            response = repository.findByAllFilter(name, branchId, start_date, end_date);
        }else if(branchId != null && start_date != null && end_date != null){
            response = repository.findByBranchInDateRange(branchId, start_date, end_date);
        }else if(name != null){
            response = repository.findByFullname(name);
        } else if(branchId != null){
            response = repository.findByBranchId(branchId);
        } else if(start_date != null && end_date != null){
            response = repository.findClarificationInDateRange(start_date, end_date);
        } else {
            response = repository.findAll();
        }
        ByteArrayInputStream data = ExcelUtil.dataToExcel(response);
        return data;
    }

}
