package com.cms.audit.api.Clarifications.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Clarifications.repository.ClarificationRepository;

@Service
public class ClarificationService {
    
    @Autowired
    private ClarificationRepository repository;

}
