package com.cms.audit.api.Clarifications.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cms.audit.api.Clarifications.dto.InputClarificationDTO;
import com.cms.audit.api.Clarifications.dto.GenerateCKDTO;
import com.cms.audit.api.Clarifications.dto.IdentificationDTO;
import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Clarifications.service.ClarificationService;
import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@RestController
@Validated
@RequestMapping(value = BasePath.BASE_PATH_CLARIFICATION)
public class ClarificationController {

    @Autowired
    private ClarificationService service;

    @GetMapping
    public ResponseEntity<Object> get(
            @RequestParam("branch_id") Optional<Long> branch_id,
            @RequestParam("name") Optional<String> name,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> start_date,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> end_date,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        Long branchId = branch_id.orElse(null);
        String fullname = name.orElse(null);
        Date startDate = start_date.orElse(null);
        Date endDate = end_date.orElse(null);
        if (startDate != null) {
            startDate = convertDateToRoman.setTimeToZero(startDate);
        }
        if (endDate != null) {
            endDate = convertDateToRoman.setTimeToLastSecond(endDate);
        }
        GlobalResponse response = service.getAll(fullname, branchId, page.orElse(0), size.orElse(10), startDate,
                endDate);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") Long id) {
        GlobalResponse response = service.getOneById(id);
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @GetMapping("/file/{fileName}")
    public ResponseEntity<InputStreamResource> getFileName(@PathVariable("fileName") String fileName)
            throws IOException {
        Clarification response = service.getFile(fileName);
        File file = new File("uploaded/clarification/" + response.getFilename());
        InputStream inputStream = new FileInputStream(file);
        InputStreamResource isr = new InputStreamResource(inputStream);

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.valueOf("application/pdf"));
        httpHeaders.set("Content-Disposition", "inline; filename=" + response.getFilename());

        return new ResponseEntity<InputStreamResource>(isr, httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<InputStreamResource> download(@PathVariable("fileName") String fileName)
            throws IOException {
        Clarification response = service.downloadFile(fileName);
        File file = new File("uploaded/clarification/" + response.getFilename());
        InputStream inputStream = new FileInputStream(file);
        InputStreamResource isr = new InputStreamResource(inputStream);

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.valueOf("application/pdf"));
        httpHeaders.set("Content-Disposition", "attachment; filename=" + response.getFilename());

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(isr);
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody InputClarificationDTO dto) {
        GlobalResponse response = service.inputClarification(dto);
        if (response.getStatus().value() == 400) {
            return ResponseEntittyHandler.errorResponse(response.getErrorMessage(), response.getMessage(),
                    response.getStatus());
        } else {
            return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                    response.getError());
        }
    }

    @PostMapping("/identification")
    public ResponseEntity<Object> saveIdentification(@RequestBody IdentificationDTO dto) {
        GlobalResponse response = service.identificationClarification(dto);
        if (response.getStatus().value() == 400) {
            return ResponseEntittyHandler.errorResponse(response.getErrorMessage(), response.getMessage(),
                    response.getStatus());
        } else {
            return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                    response.getError());
        }
    }

    @PostMapping("/generate")
    public ResponseEntity<Object> generateNumber(@RequestBody GenerateCKDTO dto) {
        GlobalResponse response = service.generateCK(dto);
        if (response.getStatus().value() == 400) {
            return ResponseEntittyHandler.errorResponse(response.getErrorMessage(), response.getMessage(),
                    response.getStatus());
        } else {
            return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(),
                    response.getError());
        }
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<Object> upload(@RequestParam(value = "file", required = false) MultipartFile file,
            @ModelAttribute("clarification_id") Long id) {
        GlobalResponse response = service.uploadFile(file, id);
        if (response.getStatus().value() == 400) {
            return ResponseEntittyHandler.errorResponse(response.getErrorMessage(), response.getMessage(),
                    response.getStatus());
        } else {
            return ResponseEntittyHandler.allHandler(null, response.getMessage(), response.getStatus(),
                    response.getError());
        }
    }

}
