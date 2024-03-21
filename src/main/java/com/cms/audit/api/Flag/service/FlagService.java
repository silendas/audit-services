package com.cms.audit.api.Flag.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Common.exception.ResourceNotFoundException;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Flag.model.Flag;
import com.cms.audit.api.Flag.repository.FlagPag;
import com.cms.audit.api.Flag.repository.FlagRepo;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FlagService {
    
    @Autowired
    private FlagRepo repo;

    @Autowired
    private FlagPag pag;

    public GlobalResponse getAll(int page, int size){
        Page<Flag> response = pag.findAll(PageRequest.of(page, size));
        return GlobalResponse.builder().data(response).status(HttpStatus.OK).build();
    }

    public GlobalResponse getOne(Long id){
        Flag response = repo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Flag with id: " + id + " is undefined"));
        return GlobalResponse.builder().data(response).status(HttpStatus.OK).build();
    }

}
