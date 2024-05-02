package com.cms.audit.api.FollowUp.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.FollowUp.dto.FollowUpDTO;
import com.cms.audit.api.FollowUp.models.FollowUp;
import com.cms.audit.api.FollowUp.service.FollowupService;

@RestController
@Validated
@RequestMapping(value = BasePath.BASE_PATH_FOLLOW_UP)
public class FollowupController {

        @Autowired
        private FollowupService service;

        @GetMapping
        public ResponseEntity<Object> getAll(
                        @RequestParam(required = false) Optional<String> name,
                        @RequestParam(required = false) Optional<Long> branch_id,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> start_date,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> end_date,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {
                Long branchId;
                if (branch_id.isPresent()) {
                        if (branch_id.get().toString() != "") {
                                branchId = branch_id.get();
                        } else {
                                branchId = null;
                        }
                } else {
                        branchId = null;
                }
                String fullname;
                if (name.isPresent()) {
                        if (name.get().toString() != "") {
                                fullname = name.get();
                        } else {
                                fullname = null;
                        }
                } else {
                        fullname = null;
                }
                Date startDate;
                if (start_date.isPresent()) {
                        if (start_date.get().toString() != "") {
                                startDate = start_date.get();
                        } else {
                                startDate = null;
                        }
                } else {
                        startDate = null;
                }
                if (startDate != null) {
                        startDate = convertDateToRoman.setTimeToZero(startDate);
                }
                Date endDate;
                if (end_date.isPresent()) {
                        if (end_date.get().toString() != "") {
                                endDate = end_date.get();
                        } else {
                                endDate = null;
                        }
                } else {
                        endDate = null;
                }
                if (endDate != null) {
                        endDate = convertDateToRoman.setTimeToLastSecond(endDate);
                }
                GlobalResponse response = service.getAll(fullname, branchId, page.orElse(0), size.orElse(10), startDate,
                                endDate);
                return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(),
                                response.getStatus(),
                                response.getError());
        }

        @GetMapping("{id}")
        public ResponseEntity<Object> getOne(
                        @PathVariable("id") Long id) {
                GlobalResponse response = service.getOne(id);
                if(response.getStatus().equals(HttpStatus.BAD_REQUEST)){
                        return ResponseEntittyHandler.errorResponse(response.getErrorMessage(), response.getMessage(), response.getStatus());
                }else{
                        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(),
                                response.getStatus(),
                                response.getError());
                }
        }

        @GetMapping("/file/{fileName}")
        public ResponseEntity<InputStreamResource> getFileName(@PathVariable("fileName") String fileName)
                        throws IOException {
                FollowUp response = service.downloadFile(fileName);
                File file = new File("uploaded/followup/" + response.getFilename());
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
                FollowUp response = service.downloadFile(fileName);
                // File file = new File(path);
                // InputStream inputStream = new FileInputStream(file);

                File file = new File("uploaded/followup/" + response.getFilename());
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
        public ResponseEntity<Object> save(@RequestBody FollowUpDTO dto) {
                GlobalResponse response = service.save(dto);
                if (response.getStatus().value() == 400) {
                        return ResponseEntittyHandler.errorResponse(response.getErrorMessage(), response.getMessage(), response.getStatus());
                } else {
                        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                                        response.getError());
                }
        }

        @PostMapping(value = "/file")
        public ResponseEntity<Object> upload(@RequestParam(value = "file", required = false) MultipartFile file,
                        @ModelAttribute("id") Long id) {
                GlobalResponse response = service.uploadFile(file, id);
                if (response.getStatus().value() == 400) {
                        return ResponseEntittyHandler.errorResponse(response.getErrorMessage(), response.getMessage(), response.getStatus());
                } else {
                        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                                        response.getError());
                }
        }

}
