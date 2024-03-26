package com.cms.audit.api.AuditWorkingPaper.controller;

import com.cms.audit.api.AuditWorkingPaper.models.AuditWorkingPaper;
import com.cms.audit.api.AuditWorkingPaper.service.AuditWorkingPaperService;
import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Validated
@RequestMapping(value = BasePath.BASE_PATH_WORKING_PAPER)
public class AuditWorkingPaperController {

        @Autowired
        private AuditWorkingPaperService service;

        @GetMapping
        public ResponseEntity<Object> get(
                        @RequestParam("schedule_id") Optional<Long> schedule_id,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> start_date,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> end_date,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {
                GlobalResponse response = service.getAll(schedule_id.orElse(null), page.orElse(0), size.orElse(10),
                                start_date.orElse(null),
                                end_date.orElse(null));
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

        @GetMapping("/file/{fileName}")
        public ResponseEntity<InputStreamResource> getFileName(@PathVariable("fileName") String fileName)
                        throws IOException {
                AuditWorkingPaper response = service.downloadFile(fileName);
                String path = response.getFile_path();
                File file = new File(path);
                InputStream inputStream = new FileInputStream(file);
                InputStreamResource isr = new InputStreamResource(inputStream);

                HttpHeaders httpHeaders = new HttpHeaders();

                httpHeaders.setContentType(
                                MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
                httpHeaders.set("Content-Disposition", "attachment; filename=" + response.getFilename());

                return new ResponseEntity<InputStreamResource>(isr, httpHeaders, HttpStatus.ACCEPTED);
        }

        @PostMapping(value = "/upload/{id}", consumes = {
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" })
        public ResponseEntity<Object> upload(@RequestParam(value = "file", required = true) MultipartFile file,
                        @PathVariable("id") Long id) {
                GlobalResponse response = service.uploadFile(file, id);
                return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(),
                                response.getStatus(),
                                response.getError());
        }

}
