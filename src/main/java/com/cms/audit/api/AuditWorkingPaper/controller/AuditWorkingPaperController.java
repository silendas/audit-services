package com.cms.audit.api.AuditWorkingPaper.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cms.audit.api.AuditWorkingPaper.models.AuditWorkingPaper;
import com.cms.audit.api.AuditWorkingPaper.service.AuditWorkingPaperService;
import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;

@RestController
@Validated
@RequestMapping(value = BasePath.BASE_PATH_WORKING_PAPER)
public class AuditWorkingPaperController {

        @Autowired
        private AuditWorkingPaperService service;

        @GetMapping
        public ResponseEntity<Object> get(
                        @RequestParam(required = false) Optional<String> name,
                        @RequestParam(required = false) Optional<Long> branch_id,
                        @RequestParam("schedule_id") Optional<Long> schedule_id,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> start_date,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> end_date,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {
                Long branchId = branch_id.orElse(null);
                Long scheduleId = schedule_id.orElse(null);
                String fullname = name.orElse(null);
                Date startDate = start_date.orElse(null);
                Date endDate = end_date.orElse(null);
                if (startDate != null) {
                        startDate = convertDateToRoman.setTimeToZero(startDate);
                }
                if (endDate != null) {
                        endDate = convertDateToRoman.setTimeToLastSecond(endDate);
                }
                GlobalResponse response = service.getAll(fullname, branchId, scheduleId, page.orElse(0),
                                size.orElse(10),
                                startDate,
                                endDate);
                return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(),
                                response.getStatus(),
                                response.getError());
        }

        @GetMapping("/{id}")
        public ResponseEntity<Object> get(@PathVariable("id") Long id) {
                GlobalResponse response = service.getOneById(id);
                return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(),
                                response.getStatus(),
                                response.getError());
        }

        @GetMapping("/download/{fileName}")
        public ResponseEntity<byte[]> getFileName(@PathVariable("fileName") String fileName)
                        throws IOException {
                try {
                        AuditWorkingPaper response = service.downloadFile(fileName);

                        File file = new File("uploaded/kka/" + response.getFilename());
                        InputStream inputStream = new FileInputStream(file);

                        byte[] out = org.apache.commons.io.IOUtils.toByteArray(inputStream);

                        HttpHeaders responseHeaders = new HttpHeaders();
                        responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                        responseHeaders.setContentLength(out.length);
                        responseHeaders.setContentDispositionFormData("attachment", fileName);

                        return new ResponseEntity<>(out, responseHeaders, HttpStatus.OK);
                } catch (IOException e) {
                        e.printStackTrace();
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
        }

        @PostMapping(value = "/upload")
        public ResponseEntity<Object> upload(@RequestParam(value = "file", required = true) MultipartFile file,
                        @ModelAttribute("schedule_id") Long id) {
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
