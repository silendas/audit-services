package com.cms.audit.api.Report.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.util.ExcelUtil;
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

    public GlobalResponse getAll(int page, int size, Date start_date, Date end_date){
        try {
            Page<Clarification> response;
            if(start_date == null || end_date == null){
                response = pagRepository.findAll(PageRequest.of(page, size));
            }else{
                response = pagRepository.findClarificationInDateRange(start_date, end_date, PageRequest.of(page, size));
            }
            if(response.isEmpty()){
                return GlobalResponse.builder().message("Data Empty").status(HttpStatus.OK).build();
            }
            return GlobalResponse.builder().data(response).message("Success").status(HttpStatus.OK).build();
        } catch (Exception e) {
            return GlobalResponse.builder().error(e).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public GlobalResponse getAllByDate(Date start_date, Date end_date,int page, int size){
        try {
            Page<Clarification> response = pagRepository.findClarificationInDateRange(start_date, end_date, PageRequest.of(page, size));
            if(response.isEmpty()){
                return GlobalResponse.builder().message("Data Empty").status(HttpStatus.OK).build();
            }
            return GlobalResponse.builder().data(response).message("Success").status(HttpStatus.OK).build();
        } catch (Exception e) {
            return GlobalResponse.builder().error(e).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public GlobalResponse getOne(Long id){
        try {
            Optional<Clarification> response = repository.findById(id);
            if(!response.isPresent()){
                return GlobalResponse.builder().message("Data Empty").status(HttpStatus.OK).build();
            }
            return GlobalResponse.builder().data(response).message("Success").status(HttpStatus.OK).build();
        } catch (Exception e) {
            return GlobalResponse.builder().error(e).message("Success").status(HttpStatus.OK).build();
        }
    }

    public ByteArrayInputStream getDataDownload(Date start_date, Date end_date) throws IOException{
        List<Clarification> response = repository.findClarificationInDateRange(start_date, end_date);
        ByteArrayInputStream data = ExcelUtil.dataToExcel(response);
        return data;
    }

}
