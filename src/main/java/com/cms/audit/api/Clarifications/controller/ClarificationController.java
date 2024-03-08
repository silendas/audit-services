package com.cms.audit.api.Clarifications.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Clarifications.service.ClarificationService;
import com.cms.audit.api.common.constant.BasePath;

@RestController
@RequestMapping(value = BasePath.BASE_PATH_CLARIFICATION)
public class ClarificationController {
    
    @Autowired
    private ClarificationService service;

}
