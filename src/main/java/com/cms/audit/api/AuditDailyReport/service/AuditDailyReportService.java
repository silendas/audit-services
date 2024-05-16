package com.cms.audit.api.AuditDailyReport.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cms.audit.api.AuditDailyReport.dto.AuditDailyReportDTO;
import com.cms.audit.api.AuditDailyReport.dto.EditAuditDailyReportDTO;
import com.cms.audit.api.AuditDailyReport.dto.LhaReportDTO;
import com.cms.audit.api.AuditDailyReport.dto.ListDetailDTO;
import com.cms.audit.api.AuditDailyReport.dto.response.DetailResponse;
import com.cms.audit.api.AuditDailyReport.models.AuditDailyReport;
import com.cms.audit.api.AuditDailyReport.models.AuditDailyReportDetail;
import com.cms.audit.api.AuditDailyReport.models.Revision;
import com.cms.audit.api.AuditDailyReport.repository.AuditDailyReportDetailRepository;
import com.cms.audit.api.AuditDailyReport.repository.AuditDailyReportRepository;
import com.cms.audit.api.AuditDailyReport.repository.RevisionRepository;
import com.cms.audit.api.AuditDailyReport.repository.pagAuditDailyReport;
import com.cms.audit.api.AuditWorkingPaper.models.AuditWorkingPaper;
import com.cms.audit.api.Clarifications.dto.response.NumberClarificationInterface;
import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Clarifications.models.EStatusClarification;
import com.cms.audit.api.Clarifications.repository.ClarificationRepository;
import com.cms.audit.api.Common.constant.SpecificationFIlter;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.exception.ResourceNotFoundException;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Flag.model.Flag;
import com.cms.audit.api.Flag.repository.FlagRepo;
import com.cms.audit.api.InspectionSchedule.models.EStatus;
import com.cms.audit.api.InspectionSchedule.models.Schedule;
import com.cms.audit.api.InspectionSchedule.repository.ScheduleRepository;
import com.cms.audit.api.Management.Case.models.Case;
import com.cms.audit.api.Management.Case.repository.CaseRepository;
import com.cms.audit.api.Management.CaseCategory.models.CaseCategory;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;
import com.cms.audit.api.Management.Office.RegionOffice.repository.RegionRepository;
import com.cms.audit.api.Management.ReportType.models.ReportType;
import com.cms.audit.api.Management.ReportType.repository.ReportTypeRepository;
import com.cms.audit.api.Management.User.models.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuditDailyReportService {

        @Autowired
        private AuditDailyReportRepository auditDailyReportRepository;

        @Autowired
        private AuditDailyReportDetailRepository auditDailyReportDetailRepository;

        @Autowired
        private ScheduleRepository scheduleRepository;

        @Autowired
        private ClarificationRepository clarificationRepository;

        @Autowired
        private CaseRepository caseRepository;

        @Autowired
        private ReportTypeRepository reportTypeRepository;

        @Autowired
        private RevisionRepository revisionRepo;

        @Autowired
        private RegionRepository regionRepository;

        @Autowired
        private FlagRepo flagRepo;

        @Autowired
        private pagAuditDailyReport pag;

        public GlobalResponse get(int page, int size, Date startDate, Date endDate, Long shcedule_id,
                        Long branch_id,
                        String name) {
                try {
                        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        Page<AuditDailyReport> response = null;
                        Specification<AuditDailyReport> spec = Specification
                                        .where(new SpecificationFIlter<AuditDailyReport>().nameLike(name))
                                        .and(new SpecificationFIlter<AuditDailyReport>().branchIdEqual(branch_id))
                                        .and(new SpecificationFIlter<AuditDailyReport>().dateRange(startDate,
                                                        endDate))
                                        .and(new SpecificationFIlter<AuditDailyReport>().isNotDeleted());
                        if (shcedule_id != null) {
                                spec = spec.and(new SpecificationFIlter<AuditDailyReport>()
                                                .scheduleIdEqual(shcedule_id));
                        }
                        if (getUser.getLevel().getCode().equals("C")) {
                                spec = spec.and(new SpecificationFIlter<AuditDailyReport>().userId(getUser.getId()));
                        } else if (getUser.getLevel().getCode().equals("B")) {
                                spec = spec.and(new SpecificationFIlter<AuditDailyReport>()
                                                .getByRegionIds(getUser.getRegionId()));
                        }
                        response = pag.findAll(spec, PageRequest.of(page, size));
                        if (response.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Data tidak ditemukan")
                                                .data(response)
                                                .status(HttpStatus.OK)
                                                .build();
                        }
                        List<Object> listLha = new ArrayList<>();
                        for (int i = 0; i < response.getContent().size(); i++) {
                                List<AuditDailyReportDetail> getDetail = auditDailyReportDetailRepository
                                                .findByLHAId(response.getContent().get(i).getId());
                                Integer ifFlow = 0;
                                Integer flag = 0;
                                for (int u = 0; u < getDetail.size(); u++) {
                                        if (getUser.getLevel().getCode().equals("A")) {
                                                if (getDetail.get(u).getStatus_flow() == 1) {
                                                        if (getDetail.isEmpty()) {
                                                                ifFlow = 1;
                                                                continue;
                                                        }
                                                        ifFlow = 0;
                                                } else {
                                                        ifFlow = 1;
                                                        continue;
                                                }
                                        }
                                        if (response.getContent().get(i).getIs_research() != 1) {
                                                if (getDetail.get(u).getIs_research() == 1) {
                                                        Flag isFlag = flagRepo.findOneByAuditDailyReportDetailId(
                                                                        getDetail.get(u).getId()).orElse(null);
                                                        if (isFlag != null) {
                                                                if (isFlag.getClarification().getFilename() == null) {
                                                                        flag = 1;
                                                                } else {
                                                                        flag = 0;
                                                                }
                                                        } else {
                                                                flag = 0;
                                                        }
                                                }
                                        }
                                }
                                if (ifFlow == 1) {
                                        continue;
                                }
                                Map<String, Object> responseS = new LinkedHashMap<>();
                                responseS.put("id", response.getContent().get(i).getId());
                                Map<String, Object> user = new LinkedHashMap<>();
                                user.put("id", response.getContent().get(i).getUser().getId());
                                user.put("fullname", response.getContent().get(i).getUser().getFullname());
                                user.put("email", response.getContent().get(i).getUser().getEmail());
                                user.put("initial_name", response.getContent().get(i).getUser().getInitial_name());
                                user.put("level", response.getContent().get(i).getUser().getLevel());
                                responseS.put("user", user);

                                Map<String, Object> branch = new LinkedHashMap<>();
                                branch.put("id", response.getContent().get(i).getBranch().getId());
                                branch.put("name", response.getContent().get(i).getBranch().getName());
                                responseS.put("branch", branch);

                                Map<String, Object> schedule = new LinkedHashMap<>();
                                schedule.put("id", response.getContent().get(i).getSchedule().getId());
                                schedule.put("start_date", response.getContent().get(i).getSchedule().getStart_date());
                                schedule.put("end_date", response.getContent().get(i).getSchedule().getEnd_date());
                                responseS.put("schedule", schedule);

                                responseS.put("created_at", response.getContent().get(i).getCreated_at());
                                responseS.put("is_research", flag);
                                responseS.put("is_flag", flag);
                                listLha.add(responseS);
                        }
                        Map<String, Object> parent = new LinkedHashMap<>();
                        parent.put("pageable", response.getPageable());
                        parent.put("totalPage", response.getTotalPages());
                        parent.put("totalElement", response.getTotalElements());
                        parent.put("size", response.getSize());
                        parent.put("number", response.getNumber());
                        parent.put("last", response.isLast());
                        parent.put("first", response.isFirst());
                        parent.put("numberOfElement", response.getNumberOfElements());
                        parent.put("empty", response.isEmpty());
                        parent.put("sort", response.getSort());
                        parent.put("content", listLha);
                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil menampilkan data")
                                        .data(parent)
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (ResponseStatusException e) {
                        return GlobalResponse
                                        .builder()
                                        .error(e)
                                        .status(HttpStatus.BAD_REQUEST)
                                        .build();
                } catch (DataException e) {
                        return GlobalResponse
                                        .builder()
                                        .error(e)
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse
                                        .builder()
                                        .error(e)
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }
        }

        public GlobalResponse getById(Long id) {
                try {
                        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                        Optional<AuditDailyReport> getLha = auditDailyReportRepository.findOneByLHAId(id);
                        if (!getLha.isPresent()) {
                                return GlobalResponse.builder().message("LHA with id: " + id + " is not found")
                                                .status(HttpStatus.BAD_REQUEST).build();
                        }
                        List<AuditDailyReportDetail> getDetail = new ArrayList<>();
                        if (getUser.getLevel().getCode().equals("C") || getUser.getLevel().getCode().equals("B")) {
                                getDetail = auditDailyReportDetailRepository
                                                .findByLHAId(id);
                        } else if (getUser.getLevel().getCode().equals("A")) {
                                getDetail = auditDailyReportDetailRepository.findByLHAIdLeader(id);
                        }

                        List<DetailResponse> details = new ArrayList<>();
                        Integer is_flag = 0;
                        for (int i = 0; i < getDetail.size(); i++) {
                                DetailResponse builder = new DetailResponse();
                                builder.setId(getDetail.get(i).getId());
                                builder.setCases(getDetail.get(i).getCases().getName());
                                builder.setCaseCategory(getDetail.get(i).getCaseCategory().getName());
                                builder.setDescription(getDetail.get(i).getDescription());
                                builder.setPermanent_recommendations(
                                                getDetail.get(i).getPermanent_recommendations());
                                builder.setTemporary_recommendations(
                                                getDetail.get(i).getTemporary_recommendations());
                                builder.setSuggestion(getDetail.get(i).getSuggestion());
                                if (getDetail.get(i).getIs_research() == 1) {
                                        Flag isFLag = flagRepo
                                                        .findOneByAuditDailyReportDetailId(
                                                                        getDetail.get(i).getId())
                                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                                        "Flag not found"));
                                        if (isFLag.getClarification().getFilename() != null) {
                                                builder.setIs_research(0);
                                        } else {
                                                builder.setIs_research(1);
                                                is_flag = 1;
                                        }
                                } else {
                                        builder.setIs_research(0);
                                }
                                if (getDetail.get(i).getStatus_flow() != null) {
                                        builder.setStatus_flow(getDetail.get(i).getStatus_flow());
                                } else {
                                        builder.setStatus_flow(0);
                                }
                                if (getDetail.get(i).getStatus_parsing() != null) {
                                        builder.setStatus_parsing(getDetail.get(i).getStatus_parsing());
                                } else {
                                        builder.setStatus_parsing(0);
                                }
                                if (getDetail.get(i).getIs_revision() != null) {
                                        builder.setIs_revision(getDetail.get(i).getIs_revision());
                                } else {
                                        builder.setIs_revision(0);
                                }
                                details.add(builder);
                        }

                        Map<String, Object> response = new LinkedHashMap<>();
                        response.put("id", getLha.get().getId());

                        Map<String, Object> user = new LinkedHashMap<>();
                        user.put("id", getLha.get().getUser().getId());
                        user.put("fullname", getLha.get().getUser().getFullname());
                        user.put("email", getLha.get().getUser().getEmail());
                        user.put("initial_name", getLha.get().getUser().getInitial_name());
                        user.put("level", getLha.get().getUser().getLevel());
                        response.put("user", user);

                        response.put("branch", getLha.get().getBranch());

                        Map<String, Object> schedule = new LinkedHashMap<>();
                        schedule.put("id", getLha.get().getSchedule().getId());
                        if (getLha.get().getSchedule().getStart_date() != null) {
                                schedule.put("start_date", convertDateToRoman
                                                .convertDateHehe(getLha.get().getSchedule().getStart_date()));
                        } else {
                                schedule.put("start_date", null);
                        }
                        if (getLha.get().getSchedule().getEnd_date() != null) {
                                schedule.put("end_date", convertDateToRoman
                                                .convertDateHehe(getLha.get().getSchedule().getEnd_date()));
                        } else {
                                schedule.put("end_date", null);
                        }
                        response.put("schedule", schedule);

                        response.put("is_research", is_flag);
                        response.put("lha_details", details);
                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil menampilkan data")
                                        .data(response)
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (ResponseStatusException e) {
                        return GlobalResponse
                                        .builder()
                                        .error(e)
                                        .status(HttpStatus.BAD_REQUEST)
                                        .build();
                } catch (DataException e) {
                        return GlobalResponse
                                        .builder()
                                        .error(e)
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse
                                        .builder()
                                        .error(e)
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }
        }

        public List<Object> mappingList(List<AuditDailyReportDetail> dto) {
                List<Object> response = new ArrayList<>();
                for (int i = 0; i < dto.size(); i++) {
                        Map<String, Object> parent = new LinkedHashMap<>();
                        Optional<Revision> getRevision = revisionRepo.findByDetailId(dto.get(i).getId());
                        if (getRevision.isPresent()) {
                                parent.put("id", getRevision.get().getId());
                                parent.put("revision_number", getRevision.get().getRevisionNumber());

                                Map<String, Object> lhaDetail = new LinkedHashMap<>();
                                lhaDetail.put("id", getRevision.get().getAuditDailyReportDetail().getId());
                                lhaDetail.put("created_at",
                                                getRevision.get().getAuditDailyReportDetail().getCreated_at());
                                parent.put("audit_daily_report_detail", lhaDetail);

                                Map<String, Object> cases = new LinkedHashMap<>();
                                cases.put("id", getRevision.get().getCases().getId());
                                cases.put("name", getRevision.get().getCases().getName());
                                cases.put("code", getRevision.get().getCases().getCode());
                                parent.put("cases", cases);

                                Map<String, Object> caseCategory = new LinkedHashMap<>();
                                caseCategory.put("id", getRevision.get().getCaseCategory().getId());
                                caseCategory.put("name", getRevision.get().getCaseCategory().getName());
                                parent.put("case_category", caseCategory);

                                parent.put("description", getRevision.get().getDescription());
                                parent.put("sugestion", getRevision.get().getSuggestion());
                                parent.put("temporary_recommendations",
                                                getRevision.get().getTemporary_recommendations());
                                parent.put("permanent_recommendations",
                                                getRevision.get().getPermanent_recommendations());
                                parent.put("is_research", getRevision.get().getIs_research());
                        } else {
                                parent.put("id", dto.get(i).getId());

                                Map<String, Object> cases = new LinkedHashMap<>();
                                cases.put("id", dto.get(i).getCases().getId());
                                cases.put("name", dto.get(i).getCases().getName());
                                cases.put("code", dto.get(i).getCases().getCode());
                                parent.put("cases", cases);

                                Map<String, Object> caseCategory = new LinkedHashMap<>();
                                caseCategory.put("id", dto.get(i).getCaseCategory().getId());
                                caseCategory.put("name", dto.get(i).getCaseCategory().getName());
                                parent.put("case_category", caseCategory);

                                parent.put("description", dto.get(i).getDescription());
                                parent.put("sugestion", dto.get(i).getSuggestion());
                                parent.put("temporary_recommendations", dto.get(i).getTemporary_recommendations());
                                parent.put("permanent_recommendations", dto.get(i).getPermanent_recommendations());
                                parent.put("is_research", dto.get(i).getIs_research());
                        }
                        response.add(parent);
                }
                return response;
        }

        public GlobalResponse getLhaReport(String name, Long areaId, Date start_date,
                        Date end_date, int page, int size) {
                User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                List<AuditDailyReport> response = new ArrayList<>();
                LhaReportDTO dto = new LhaReportDTO();
                List<ListDetailDTO> list = new ArrayList<>();
                List<LhaReportDTO> listAllReport = new ArrayList<>();
                Pageable pageable = PageRequest.of(page, size);

                if (name != null && areaId != null && start_date != null && end_date != null) {
                        response = auditDailyReportRepository.findLHAByAllFilter(areaId, name, start_date,
                                        end_date);
                } else if (areaId != null && start_date != null && end_date != null) {
                        response = auditDailyReportRepository.findLHAByRegionInDateRange(areaId, start_date,
                                        end_date);
                } else if (name != null && start_date != null && end_date != null) {
                        response = auditDailyReportRepository.findLHAByNameInDateRange(name, start_date,
                                        end_date);
                } else if (name != null) {
                        response = auditDailyReportRepository.findLHAByName(name);
                } else if (areaId != null) {
                        response = auditDailyReportRepository.findLHAByRegion(areaId);
                } else if (start_date != null && end_date != null) {
                        response = auditDailyReportRepository.findLHAInDateRange(start_date, end_date);
                } else {
                        if (getUser.getLevel().getCode().equals("B")) {
                                for (int i = 0; i < getUser.getRegionId().size(); i++) {
                                        List<AuditDailyReport> listLHA = auditDailyReportRepository
                                                        .findLHAByRegion(getUser.getRegionId().get(i));
                                        for (int u = 0; u < listLHA.size(); u++) {
                                                response.add(listLHA.get(u));
                                        }
                                }
                        } else if (getUser.getLevel().getCode().equals("C")) {
                                List<AuditDailyReport> listLHA = auditDailyReportRepository
                                                .findAllLHAByUserId(getUser.getId());
                                for (int u = 0; u < listLHA.size(); u++) {
                                        response.add(listLHA.get(u));
                                }
                        } else {
                                response = auditDailyReportRepository.findAll();
                        }
                }
                if (response.isEmpty()) {
                        return GlobalResponse
                                        .builder()
                                        .message("Data tidak ditemukan")
                                        .data(response)
                                        .status(HttpStatus.OK)
                                        .build();
                }
                if (areaId != null) {
                        Optional<Region> region = regionRepository.findOneRegionById(areaId);
                        if (!region.isPresent()) {
                                return null;
                        }
                        for (int i = 0; i < response.size(); i++) {
                                String fullName = response.get(i).getUser().getFullname();
                                List<AuditDailyReportDetail> detail = auditDailyReportDetailRepository
                                                .findByLHAId(response.get(i).getId());

                                boolean found = false;
                                for (int j = 0; j < list.size(); j++) {
                                        if (fullName.equals(list.get(j).getFullname())) {
                                                list.get(j).getDetails().addAll(detail);
                                                found = true;
                                                break;
                                        }
                                }

                                if (!found) {
                                        ListDetailDTO listLha = new ListDetailDTO();
                                        listLha.setFullname(fullName);
                                        listLha.setBranch(response.get(i).getBranch().getName());

                                        listLha.setDetails(mappingList(detail));
                                        list.add(listLha);
                                }
                        }
                        dto.setLha_detail(list);
                        dto.setArea_name(region.get().getName());
                        dto.setDate(convertDateToRoman.convertDateToString(new Date()));
                        list.clear();
                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil menampilkan data")
                                        .data(dto)
                                        .status(HttpStatus.OK)
                                        .build();

                } else {
                        for (int i = 0; i < response.size(); i++) {
                                List<AuditDailyReportDetail> detail = auditDailyReportDetailRepository
                                                .findByLHAId(response.get(i).getId());
                                String regionName = response.get(i).getBranch().getArea().getRegion().getName();
                                String fullName = response.get(i).getUser().getFullname();

                                boolean foundRegion = false;
                                for (int x = 0; x < listAllReport.size(); x++) {
                                        if (regionName.equals(listAllReport.get(x).getArea_name())) {
                                                foundRegion = true;
                                                List<ListDetailDTO> lhaDetails = listAllReport.get(x).getLha_detail();
                                                boolean foundUser = false;
                                                for (int y = 0; y < lhaDetails.size(); y++) {
                                                        if (fullName.equals(lhaDetails.get(y).getFullname())) {
                                                                lhaDetails.get(y).getDetails().addAll(detail);
                                                                foundUser = true;
                                                                break;
                                                        }
                                                }
                                                if (!foundUser) {
                                                        ListDetailDTO lhaDto = new ListDetailDTO();
                                                        lhaDto.setFullname(fullName);
                                                        lhaDto.setBranch(response.get(i).getBranch().getName());
                                                        lhaDto.setDetails(mappingList(detail));
                                                        lhaDetails.add(lhaDto);
                                                }
                                                break;
                                        }
                                }

                                if (!foundRegion) {
                                        LhaReportDTO reportDto = new LhaReportDTO();
                                        reportDto.setArea_name(regionName);
                                        reportDto.setDate(convertDateToRoman.convertDateToString(new Date()));
                                        List<ListDetailDTO> lhaDetailList = new ArrayList<>();
                                        ListDetailDTO lhaDto = new ListDetailDTO();
                                        lhaDto.setFullname(fullName);
                                        lhaDto.setBranch(response.get(i).getBranch().getName());
                                        lhaDto.setDetails(mappingList(detail));
                                        lhaDetailList.add(lhaDto);
                                        reportDto.setLha_detail(lhaDetailList);
                                        listAllReport.add(reportDto);
                                }
                        }
                        try {
                                int start = (int) pageable.getOffset();
                                int end = Math.min((start + pageable.getPageSize()), listAllReport.size());
                                List<LhaReportDTO> pageContent = listAllReport.subList(start, end);
                                Page<LhaReportDTO> result = new PageImpl<>(pageContent, pageable, listAllReport.size());
                                return GlobalResponse
                                                .builder()
                                                .message("Berhasil menampilkan data")
                                                .data(result)
                                                .status(HttpStatus.OK)
                                                .build();
                        } catch (Exception e) {
                                return GlobalResponse
                                                .builder()
                                                .message("Data tidak ditemukan")
                                                .status(HttpStatus.OK).data(response)
                                                .build();
                        }
                }
        }

        public GlobalResponse save(AuditDailyReportDTO dto) {
                try {
                        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                        List<Schedule> checkShcedule = scheduleRepository.CheckIfScheduleisNow(dto.getSchedule_id());
                        if (checkShcedule.isEmpty()) {
                                return GlobalResponse.builder()
                                                .message("Tidak bisa memproses jadwal karena jadwal belum dimulai")
                                                .errorMessage("Jadwal belum dimulai, tidak dapat diproses")
                                                .status(HttpStatus.BAD_REQUEST).build();
                        }

                        List<AuditDailyReport> checkLHA = auditDailyReportRepository
                                        .findByCurrentDay(dto.getSchedule_id());
                        if (!checkLHA.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("LHA untuk hari ini sudah ada")
                                                .errorMessage("LHA sudah ada untuk hari ini, input lagi besok")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }

                        Optional<Schedule> getschedule = scheduleRepository.findById(dto.getSchedule_id());
                        if (!getschedule.isPresent()) {
                                return GlobalResponse.builder().errorMessage("Jadwal tidak ditemukan")
                                                .message("Data jadwal dengan id : " + dto.getSchedule_id()
                                                                + " tidak ditemukan")
                                                .status(HttpStatus.BAD_REQUEST).build();
                        }

                        List<Schedule> scheduleList = scheduleRepository.findForScheduleList(
                                        getschedule.get().getUser().getId(),
                                        getschedule.get().getStart_date());
                        if (!scheduleList.isEmpty()) {
                                return GlobalResponse.builder().message(
                                                "tidak bisa memperoses jadwal karena jadwal sebelumnya belum selesai")
                                                .errorMessage("Tidak bisa memproses jadwal & LHA karena sebelumnya belum membuat KKA")
                                                .status(HttpStatus.BAD_REQUEST).build();
                        }

                        AuditDailyReport auditDailyReport = new AuditDailyReport(
                                        null,
                                        getschedule.get(),
                                        getschedule.get().getBranch(),
                                        user,
                                        0,
                                        0,
                                        user.getId(),
                                        user.getId(),
                                        new Date(),
                                        new Date());

                        if (getschedule.get().getStart_date_realization() == null) {
                                Schedule schedule = getschedule
                                                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
                                schedule.setStart_date_realization(new Date());
                                schedule.setStatus(EStatus.PROGRESS);
                                scheduleRepository.save(schedule);
                        }

                        AuditDailyReport response1 = auditDailyReportRepository.save(auditDailyReport);

                        AuditDailyReport setId = AuditDailyReport.builder().id(response1.getId()).build();

                        for (int i = 0; i < dto.getLha_detail().size(); i++) {
                                Case setCaseId = Case.builder().id(dto.getLha_detail().get(i).getCase_id()).build();
                                CaseCategory setCaseCategoryId = CaseCategory.builder()
                                                .id(dto.getLha_detail().get(i).getCase_category_id()).build();
                                if (dto.getLha_detail().get(i).getIs_research() == null) {
                                        dto.getLha_detail().get(i).setIs_research(0);
                                }
                                AuditDailyReportDetail auditDailyReportDetail = new AuditDailyReportDetail(
                                                null,
                                                setId,
                                                setCaseId,
                                                setCaseCategoryId,
                                                dto.getLha_detail().get(i).getDescription(),
                                                dto.getLha_detail().get(i).getSuggestion(),
                                                dto.getLha_detail().get(i).getTemporary_recommendations(),
                                                dto.getLha_detail().get(i).getPermanent_recommendations(),
                                                dto.getLha_detail().get(i).getIs_research(),
                                                0,
                                                0,
                                                0,
                                                0,
                                                user.getId(),
                                                user.getId(),
                                                new Date(),
                                                new Date());

                                AuditDailyReportDetail response2 = auditDailyReportDetailRepository
                                                .save(auditDailyReportDetail);

                                Revision revision = new Revision();
                                revision.setAuditDailyReportDetail(response2);
                                revision.setCases(response2.getCases());
                                revision.setCaseCategory(response2.getCaseCategory());
                                revision.setRevisionNumber(0L);
                                revision.setDescription(response2.getDescription());
                                revision.setSuggestion(response2.getSuggestion());
                                revision.setTemporary_recommendations(response2.getTemporary_recommendations());
                                revision.setPermanent_recommendations(response2.getPermanent_recommendations());
                                revision.setIs_research(response2.getIs_research());
                                revision.setIs_delete(response2.getIs_delete());
                                revision.setCreated_by(response2.getCreated_by());
                                revision.setCreated_at(response2.getCreated_at());

                                revisionRepo.save(revision);

                                if (dto.getLha_detail().get(i).getIs_research() == 1) {

                                        Case getCase = caseRepository.findById(dto.getLha_detail().get(i).getCase_id())
                                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK,
                                                                        "Data tidak ditemukan"));

                                        Long reportNumber = null;
                                        String rptNum = null;

                                        Optional<NumberClarificationInterface> checkClBefore = clarificationRepository
                                                        .checkNumberClarification(user.getId());
                                        if (checkClBefore.isPresent()) {
                                                if (checkClBefore.get().getCreated_Year().longValue() == Long
                                                                .valueOf(convertDateToRoman.getIntYear())) {
                                                        reportNumber = checkClBefore.get().getReport_Number() + 1;
                                                        if (reportNumber < 10) {
                                                                rptNum = "00" + reportNumber;
                                                        } else if (reportNumber < 100) {
                                                                rptNum = "0" + reportNumber;
                                                        } else {
                                                                rptNum = reportNumber.toString();
                                                        }
                                                } else {
                                                        rptNum = "001";
                                                        reportNumber = Long.valueOf(1);
                                                }
                                        } else {
                                                rptNum = "001";
                                                reportNumber = Long.valueOf(1);
                                        }

                                        String branchName = getschedule.get().getBranch().getName();
                                        String initialName = user.getInitial_name();
                                        String caseName = getCase.getCode();
                                        String lvlCode = user.getLevel().getCode();
                                        String romanMonth = convertDateToRoman.getRomanMonth();
                                        Integer thisYear = convertDateToRoman.getIntYear();

                                        ReportType reportType = reportTypeRepository.findByCode("CK")
                                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                                        "Report Type Nt found"));

                                        String reportCode = rptNum + lvlCode + "/" + initialName + "-" + caseName
                                                        + "/" + reportType.getCode() + "/" + branchName + "/"
                                                        + romanMonth + "/"
                                                        + thisYear;

                                        Clarification clarification = new Clarification(
                                                        null,
                                                        user,
                                                        getschedule.get().getBranch(),
                                                        setCaseId,
                                                        setCaseCategoryId,
                                                        reportType,
                                                        reportNumber,
                                                        reportCode,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        EStatusClarification.INPUT,
                                                        user.getId(),
                                                        new Date(),
                                                        new Date());

                                        Clarification response3 = clarificationRepository.save(clarification);

                                        Flag flag = new Flag();
                                        flag.setAuditDailyReportDetail(response2);
                                        flag.setClarification(response3);
                                        flag.setCreatedAt(new Date());
                                        flagRepo.save(flag);
                                }
                        }

                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil menambahkan data")
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (ResponseStatusException e) {
                        return GlobalResponse
                                        .builder()
                                        .error(e)
                                        .status(HttpStatus.BAD_REQUEST)
                                        .build();
                } catch (DataException e) {
                        return GlobalResponse
                                        .builder()
                                        .error(e)
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse
                                        .builder()
                                        .error(e)
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }
        }

        public GlobalResponse edit(EditAuditDailyReportDTO dto, Long id) {
                try {
                        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                        Optional<AuditDailyReport> getBefore = auditDailyReportRepository.findById(id);
                        if (!getBefore.isPresent()) {
                                return GlobalResponse
                                                .builder()
                                                .message("tidak ditemukan").errorMessage("Data lha tidak ditemukan")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }

                        Schedule scheduleId = Schedule.builder().id(dto.getSchedule_id()).build();
                        User userId = User.builder().id(dto.getUser_id()).build();
                        Branch branchId = Branch.builder().id(dto.getBranch_id()).build();

                        AuditDailyReport auditDailyReport = new AuditDailyReport(
                                        id,
                                        scheduleId,
                                        branchId,
                                        userId,
                                        null,
                                        0,
                                        getBefore.get().getCreated_by(),
                                        user.getId(),
                                        getBefore.get().getCreated_at(),
                                        new Date());

                        auditDailyReportRepository.save(auditDailyReport);
                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil mengubah data")
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (ResponseStatusException e) {
                        return GlobalResponse
                                        .builder()
                                        .error(e)
                                        .status(HttpStatus.BAD_REQUEST)
                                        .build();
                } catch (DataException e) {
                        return GlobalResponse
                                        .builder()
                                        .error(e)
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse
                                        .builder()
                                        .error(e)
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }
        }

        public GlobalResponse delete(Long id) {
                try {
                        Optional<AuditDailyReport> getBefore = auditDailyReportRepository.findById(id);
                        if (!getBefore.isPresent()) {
                                return GlobalResponse
                                                .builder()
                                                .message("tidak ditemukan").message("LHA tidak ditemukan")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }

                        Schedule scheduleId = Schedule.builder().id(getBefore.get().getSchedule().getId()).build();
                        User userId = User.builder().id(getBefore.get().getUser().getId()).build();
                        Branch branchId = Branch.builder().id(getBefore.get().getBranch().getId()).build();

                        AuditDailyReport auditDailyReport = new AuditDailyReport(
                                        id,
                                        scheduleId,
                                        branchId,
                                        userId,
                                        null,
                                        1,
                                        getBefore.get().getCreated_by(),
                                        getBefore.get().getCreated_by(),
                                        getBefore.get().getCreated_at(),
                                        new Date());

                        auditDailyReportRepository.save(auditDailyReport);
                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil menghapus data")
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (ResponseStatusException e) {
                        return GlobalResponse
                                        .builder()
                                        .error(e)
                                        .status(HttpStatus.BAD_REQUEST)
                                        .build();
                } catch (DataException e) {
                        return GlobalResponse
                                        .builder()
                                        .error(e)
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse
                                        .builder()
                                        .error(e)
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }
        }

}
