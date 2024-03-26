package com.cms.audit.api.Report.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Common.constant.randomValueNumber;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Report.service.ReportService;

@RestController
@Validated
@RequestMapping(value = BasePath.BASE_PATH_REPORT)
public class ReportController {

        @Autowired
        private ReportService service;

        @GetMapping
        public ResponseEntity<Object> getAll(
                        @RequestParam("branch_id") Optional<Long> branch_id,
                        @RequestParam("name") Optional<String> name,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> start_date,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> end_date,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {
                GlobalResponse response = service.getAll(branch_id.orElse(null), name.orElse(null), page.orElse(0),
                                size.orElse(10), start_date.orElse(null),
                                end_date.orElse(null));
                return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(),
                                response.getStatus(),
                                response.getError());
        }

        @GetMapping("/{id}")
        public ResponseEntity<Object> getOne(@PathVariable("id") Long id) {
                GlobalResponse response = service.getOne(id);
                return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(),
                                response.getStatus(),
                                response.getError());
        }

        @GetMapping("/download")
        public ResponseEntity<InputStreamResource> download(
                        @RequestParam("name") Optional<String> name,
                        @RequestParam(required = false) Optional<Long> branch_id,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date start_date,
                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date end_date) throws IOException {
                String fileName = start_date + "-" + end_date + "-report.xlsx";
                ByteArrayInputStream inputStream = service.getDataDownload(name.orElse(null),branch_id.orElse(null), start_date,
                                end_date);
                InputStreamResource resource = new InputStreamResource(inputStream);

                ResponseEntity<InputStreamResource> response = ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                                .contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(resource);
                return response;
        }

}
