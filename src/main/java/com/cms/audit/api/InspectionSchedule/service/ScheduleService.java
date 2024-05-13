package com.cms.audit.api.InspectionSchedule.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cms.audit.api.AuditDailyReport.models.AuditDailyReport;
import com.cms.audit.api.AuditDailyReport.models.AuditDailyReportDetail;
import com.cms.audit.api.AuditDailyReport.repository.AuditDailyReportDetailRepository;
import com.cms.audit.api.AuditDailyReport.repository.AuditDailyReportRepository;
import com.cms.audit.api.AuditWorkingPaper.models.AuditWorkingPaper;
import com.cms.audit.api.AuditWorkingPaper.repository.AuditWorkingPaperRepository;
import com.cms.audit.api.Common.constant.SpecificationFIlter;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.exception.ResourceNotFoundException;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Flag.model.Flag;
import com.cms.audit.api.Flag.repository.FlagRepo;
import com.cms.audit.api.InspectionSchedule.dto.EditScheduleDTO;
import com.cms.audit.api.InspectionSchedule.dto.RequestReschedule;
import com.cms.audit.api.InspectionSchedule.dto.ScheduleDTO;
import com.cms.audit.api.InspectionSchedule.dto.ScheduleRequest;
import com.cms.audit.api.InspectionSchedule.models.ECategory;
import com.cms.audit.api.InspectionSchedule.models.EStatus;
import com.cms.audit.api.InspectionSchedule.models.Schedule;
import com.cms.audit.api.InspectionSchedule.models.ScheduleTrx;
import com.cms.audit.api.InspectionSchedule.repository.PagSchedule;
import com.cms.audit.api.InspectionSchedule.repository.ScheduleRepository;
import com.cms.audit.api.InspectionSchedule.repository.ScheduleTrxRepo;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.BranchOffice.repository.BranchRepository;
import com.cms.audit.api.Management.User.dto.UserResponseOther;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Management.User.repository.UserRepository;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@Service
public class ScheduleService {

        @Autowired
        private ScheduleRepository repository;

        @Autowired
        private LogScheduleService logService;

        @Autowired
        private PagSchedule pag;

        @Autowired
        private FlagRepo flagRepo;

        @Autowired
        private AuditDailyReportRepository auditDailyReportRepository;

        @Autowired
        private AuditDailyReportDetailRepository auditDailyReportDetailRepository;

        @Autowired
        private AuditWorkingPaperRepository auditWorkingPaperRepository;

        @Autowired
        private BranchRepository branchRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private ScheduleTrxRepo scheduleTrxRepo;

        public GlobalResponse getReschedule(Long branch_id, String name, int page, int size, Date start_date,
                        Date end_date, EStatus status) {

                User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Specification<Schedule> spec = Specification
                                .where(new SpecificationFIlter<Schedule>().nameLike(name))
                                .and(new SpecificationFIlter<Schedule>().branchIdEqual(branch_id))
                                .and(new SpecificationFIlter<Schedule>().isNotDeleted())
                                .and(new SpecificationFIlter<Schedule>().orderByIdDesc());
                if (start_date != null && end_date != null) {
                        spec = spec.and(new SpecificationFIlter<Schedule>().dateBetween(start_date, end_date));
                }
                if (status != null) {
                        List<EStatus> statuses = Arrays.asList(status);
                        spec = spec.and(new SpecificationFIlter<Schedule>().getByStatus(statuses));
                } else if (getUser.getLevel().getCode().equals("B")) {
                        List<EStatus> statuses = Arrays.asList(EStatus.APPROVE, EStatus.REQUEST, EStatus.PENDING);
                        spec = spec.and(new SpecificationFIlter<Schedule>().getByStatus(statuses));
                } else if (getUser.getLevel().getCode().equals("A")) {
                        List<EStatus> statuses = Arrays.asList(EStatus.APPROVE, EStatus.REQUEST, EStatus.REJECTED);
                        spec = spec.and(new SpecificationFIlter<Schedule>().getByStatus(statuses));
                }
                Page<Schedule> response = pag.findAll(spec, PageRequest.of(page, size));
                if (response.isEmpty()) {
                        return GlobalResponse
                                        .builder()
                                        .message("Data schedule tidak ditemukan")
                                        .data(response)
                                        .status(HttpStatus.OK)
                                        .build();
                }
                return GlobalResponse
                                .builder()
                                .message("Data berhasil ditampilkan")
                                .data(mappingPageSchedule(response))
                                .status(HttpStatus.OK)
                                .build();

        }

        public GlobalResponse getSchedule(Long branch_id, String name, int page, int size, Date start_date,
                        Date end_date, ECategory category, EStatus status) {
                try {
                        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        Specification<Schedule> spec = Specification
                                        .where(new SpecificationFIlter<Schedule>().nameLike(name))
                                        .and(new SpecificationFIlter<Schedule>().branchIdEqual(branch_id))
                                        .and(new SpecificationFIlter<Schedule>().getByCategory(category))
                                        .and(new SpecificationFIlter<Schedule>().isNotDeleted());
                        if (start_date != null && end_date != null) {
                                spec = spec.and(new SpecificationFIlter<Schedule>().dateBetween(start_date, end_date));
                        }
                        if (status != null) {
                                List<EStatus> statuses = Arrays.asList(status);
                                spec = spec.and(new SpecificationFIlter<Schedule>().getByStatus(statuses));
                        } else {
                                List<EStatus> statuses = Arrays.asList(EStatus.TODO, EStatus.PROGRESS, EStatus.DONE,
                                                EStatus.PENDING);
                                spec = spec.and(new SpecificationFIlter<Schedule>().getByStatus(statuses));
                        }
                        if (getUser.getLevel().getCode().equals("C")) {
                                spec = spec.and(new SpecificationFIlter<Schedule>().userId(getUser.getId()));
                        }
                        if (getUser.getLevel().getCode().equals("B")) {
                                spec = spec.and(new SpecificationFIlter<Schedule>()
                                                .getByRegionIds(getUser.getRegionId()));
                        }
                        spec = spec.and(new SpecificationFIlter<Schedule>().orderByIdDesc());
                        Page<Schedule> response = pag.findAll(spec, PageRequest.of(page, size));
                        if (response.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Data schedule tidak ditemukan")
                                                .data(response)
                                                .status(HttpStatus.OK)
                                                .build();
                        }
                        return GlobalResponse
                                        .builder()
                                        .message("Data berhasil ditampilkan")
                                        .data(mappingPageSchedule(response))
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }

        }

        public GlobalResponse getById(Long id) {
                try {
                        Optional<Schedule> getSchedule = repository.findOneScheduleById(id);
                        if (!getSchedule.isPresent()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Data schedule with id :" + id + " is not found")
                                                .status(HttpStatus.OK)
                                                .build();
                        }
                        Map<String, Object> response = new LinkedHashMap<>();
                        response.put("schedule", mappingSchedule(getSchedule.get()));

                        List<AuditDailyReport> getLha = auditDailyReportRepository.findByScheduleId(id);
                        List<Object> listLha = new ArrayList<>();
                        if (!getLha.isEmpty()) {
                                for (int i = 0; i < getLha.size(); i++) {
                                        Map<String, Object> lha = new LinkedHashMap<>();
                                        lha.put("id", getLha.get(i).getId());

                                        Map<String, Object> lhaBranch = new LinkedHashMap<>();
                                        lhaBranch.put("id", getLha.get(i).getBranch().getId());
                                        lhaBranch.put("name", getLha.get(i).getBranch().getName());
                                        lha.put("branch", lhaBranch);

                                        List<AuditDailyReportDetail> detail = auditDailyReportDetailRepository
                                                        .findByLHAId(getLha.get(i).getId());
                                        Integer flag = 0;
                                        for (int u = 0; u < detail.size(); u++) {
                                                if (getLha.get(i).getIs_research() != 1) {
                                                        if (detail.get(u).getIs_research() == 1) {
                                                                Flag isFlag = flagRepo
                                                                                .findOneByAuditDailyReportDetailId(
                                                                                                detail.get(u).getId())
                                                                                .orElse(null);
                                                                if (isFlag != null) {
                                                                        if (isFlag.getClarification()
                                                                                        .getFilename() == null) {
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
                                        lha.put("is_flag", flag);

                                        listLha.add(lha);
                                }
                                response.put("lha", listLha);
                        } else {
                                response.put("lha", null);
                        }

                        Optional<AuditWorkingPaper> getKka = auditWorkingPaperRepository.findByScheduleId(id);
                        Map<String, Object> kka = new LinkedHashMap<>();
                        if (getKka.isPresent()) {
                                kka.put("id", getKka.get().getId());
                                kka.put("filename", getKka.get().getFilename());
                                kka.put("file_path", getKka.get().getFile_path());
                                response.put("kka", kka);
                        } else {
                                response.put("kka", null);
                        }

                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil menampilkan data")
                                        .data(response)
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse.builder()
                                        .error(e)
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }
        }

        public List<Object> mappingListSchedule(List<Schedule> response) {
                List<Object> listSchedule = new ArrayList<>();
                for (int i = 0; i < response.size(); i++) {
                        Map<String, Object> mapParent = new LinkedHashMap<>();
                        mapParent.put("id", response.get(i).getId());

                        UserResponseOther setUser = new UserResponseOther();
                        setUser.setId(response.get(i).getUser().getId());
                        setUser.setEmail(response.get(i).getUser().getEmail());
                        setUser.setFullname(response.get(i).getUser().getFullname());
                        setUser.setInitial_name(response.get(i).getUser().getInitial_name());
                        setUser.setNip(response.get(i).getUser().getNip());
                        setUser.setLevel(response.get(i).getUser().getLevel());
                        mapParent.put("user", setUser);

                        mapParent.put("branch", response.get(i).getBranch());

                        mapParent.put("description", response.get(i).getDescription());
                        mapParent.put("status", response.get(i).getStatus());
                        mapParent.put("category", response.get(i).getCategory());
                        if (response.get(i).getScheduleTrx() != null) {
                                Optional<ScheduleTrx> trxCheck = scheduleTrxRepo
                                                .findById(response.get(i).getScheduleTrx().getId());
                                if (trxCheck.isPresent()) {
                                        mapParent.put("new_start_date", response.get(i).getStart_date());
                                        mapParent.put("new_end_date", response.get(i).getEnd_date());
                                }
                        }
                        mapParent.put("start_date", response.get(i).getStart_date());
                        mapParent.put("end_date", response.get(i).getEnd_date());
                        mapParent.put("start_date_realization", response.get(i).getStart_date_realization());
                        mapParent.put("end_date_realization", response.get(i).getEnd_date_realization());

                        Optional<AuditWorkingPaper> getKka = auditWorkingPaperRepository
                                        .findByScheduleId(response.get(i).getId());
                        Map<String, Object> kka = new LinkedHashMap<>();
                        if (getKka.isPresent()) {
                                kka.put("id", getKka.get().getId());
                                kka.put("filename", getKka.get().getFilename());
                                kka.put("file_path", getKka.get().getFile_path());
                                mapParent.put("kka", kka);
                        } else {
                                mapParent.put("kka", null);
                        }
                        Map<String, Object> getUserBy = new LinkedHashMap<>();
                        Optional<User> getUser = userRepository.findById(response.get(i).getCreatedBy());
                        if (getUser.isPresent()) {
                                Map<String, Object> getLevelBy = new LinkedHashMap<>();
                                getLevelBy.put("id", getUser.get().getLevel().getId());
                                getLevelBy.put("name", getUser.get().getLevel().getName());
                                getLevelBy.put("code", getUser.get().getLevel().getCode());

                                getUserBy.put("id", getUser.get().getId());
                                getUserBy.put("fullname", getUser.get().getFullname());
                                getUserBy.put("initial_name", getUser.get().getInitial_name());
                                getUserBy.put("level", getLevelBy);
                                mapParent.put("created_by", getUserBy);
                        } else {
                                mapParent.put("created_by", null);
                        }

                        listSchedule.add(mapParent);
                }
                return listSchedule;
        }

        public Map<String, Object> mappingSchedule(Schedule response) {
                Map<String, Object> map = new LinkedHashMap<>();
                UserResponseOther setUser = new UserResponseOther();

                map.put("id", response.getId());

                setUser.setId(response.getUser().getId());
                setUser.setEmail(response.getUser().getEmail());
                setUser.setFullname(response.getUser().getFullname());
                setUser.setInitial_name(response.getUser().getInitial_name());
                setUser.setNip(response.getUser().getNip());
                setUser.setLevel(response.getUser().getLevel());
                map.put("user", setUser);

                map.put("branch", response.getBranch());

                map.put("description", response.getDescription());
                map.put("status", response.getStatus());
                map.put("category", response.getCategory());
                if (response.getScheduleTrx() != null) {
                        Optional<ScheduleTrx> trxCheck = scheduleTrxRepo
                                        .findById(response.getScheduleTrx().getId());
                        if (trxCheck.isPresent()) {
                                map.put("new_start_date", trxCheck.get().getStart_date());
                                map.put("new_end_date", trxCheck.get().getEnd_date());
                        }
                }
                map.put("start_date", response.getStart_date());
                map.put("end_date", response.getEnd_date());
                map.put("start_date_realization", response.getStart_date_realization());
                map.put("end_date_realization", response.getEnd_date_realization());

                Map<String, Object> getUserBy = new LinkedHashMap<>();
                Optional<User> getUser = userRepository.findById(response.getCreatedBy());
                if (getUser.isPresent()) {
                        Map<String, Object> getLevelBy = new LinkedHashMap<>();
                        getLevelBy.put("id", getUser.get().getLevel().getId());
                        getLevelBy.put("name", getUser.get().getLevel().getName());
                        getLevelBy.put("code", getUser.get().getLevel().getCode());

                        getUserBy.put("id", getUser.get().getId());
                        getUserBy.put("fullname", getUser.get().getFullname());
                        getUserBy.put("initial_name", getUser.get().getInitial_name());
                        getUserBy.put("level", getLevelBy);
                        map.put("created_by", getUserBy);
                } else {
                        map.put("created_by", null);
                }

                return map;
        }

        public Map<String, Object> mappingPageSchedule(Page<Schedule> response) {
                Map<String, Object> parentMap = new LinkedHashMap<>();
                List<Object> listSchedule = new ArrayList<>();
                for (int i = 0; i < response.getContent().size(); i++) {
                        Map<String, Object> map = new LinkedHashMap<>();
                        map.put("id", response.getContent().get(i).getId());

                        UserResponseOther setUser = new UserResponseOther();
                        setUser.setId(response.getContent().get(i).getUser().getId());
                        setUser.setEmail(response.getContent().get(i).getUser().getEmail());
                        setUser.setFullname(response.getContent().get(i).getUser().getFullname());
                        setUser.setInitial_name(response.getContent().get(i).getUser().getInitial_name());
                        setUser.setNip(response.getContent().get(i).getUser().getNip());
                        setUser.setLevel(response.getContent().get(i).getUser().getLevel());
                        map.put("user", setUser);

                        map.put("branch", response.getContent().get(i).getBranch());

                        map.put("description", response.getContent().get(i).getDescription());
                        map.put("status", response.getContent().get(i).getStatus());
                        map.put("category", response.getContent().get(i).getCategory());

                        if (response.getContent().get(i).getScheduleTrx() != null) {
                                Optional<ScheduleTrx> trxCheck = scheduleTrxRepo
                                                .findById(response.getContent().get(i).getScheduleTrx().getId());
                                if (trxCheck.isPresent()) {
                                        map.put("new_start_date", trxCheck.get().getStart_date());
                                        map.put("new_end_date", trxCheck.get().getEnd_date());
                                }
                        }
                        map.put("start_date", response.getContent().get(i).getStart_date());
                        map.put("end_date", response.getContent().get(i).getEnd_date());
                        map.put("start_date_realization", response.getContent().get(i).getStart_date_realization());
                        map.put("end_date_realization", response.getContent().get(i).getEnd_date_realization());

                        Optional<AuditWorkingPaper> getKka = auditWorkingPaperRepository
                                        .findByScheduleId(response.getContent().get(i).getId());
                        Map<String, Object> kka = new LinkedHashMap<>();
                        if (getKka.isPresent()) {
                                kka.put("id", getKka.get().getId());
                                kka.put("filename", getKka.get().getFilename());
                                kka.put("file_path", getKka.get().getFile_path());
                                map.put("kka", kka);
                        } else {
                                map.put("kka", null);
                        }

                        List<Schedule> scheduleList = repository.findForScheduleList(
                                        response.getContent().get(i).getUser().getId(),
                                        response.getContent().get(i).getStart_date());
                        if (scheduleList.isEmpty()) {
                                map.put("is_active", 1);
                        } else {
                                map.put("is_active", 0);
                        }

                        Map<String, Object> getUserBy = new LinkedHashMap<>();
                        Optional<User> getUser = userRepository.findById(response.getContent().get(i).getCreatedBy());
                        if (getUser.isPresent()) {
                                Map<String, Object> getLevelBy = new LinkedHashMap<>();
                                getLevelBy.put("id", getUser.get().getLevel().getId());
                                getLevelBy.put("name", getUser.get().getLevel().getName());
                                getLevelBy.put("code", getUser.get().getLevel().getCode());

                                getUserBy.put("id", getUser.get().getId());
                                getUserBy.put("fullname", getUser.get().getFullname());
                                getUserBy.put("initial_name", getUser.get().getInitial_name());
                                getUserBy.put("level", getLevelBy);
                                map.put("created_by", getUserBy);
                        } else {
                                map.put("created_by", null);
                        }

                        listSchedule.add(map);
                }
                parentMap.put("pageable", response.getPageable());
                parentMap.put("totalPage", response.getTotalPages());
                parentMap.put("totalElement", response.getTotalElements());
                parentMap.put("size", response.getSize());
                parentMap.put("number", response.getNumber());
                parentMap.put("last", response.isLast());
                parentMap.put("first", response.isFirst());
                parentMap.put("numberOfElement", response.getNumberOfElements());
                parentMap.put("empty", response.isEmpty());
                parentMap.put("sort", response.getSort());
                parentMap.put("content", listSchedule);
                return parentMap;
        }

        public Date getDateNow() {
                Date today = new Date();
                Calendar todayCal = Calendar.getInstance();
                todayCal.setTime(today);
                todayCal.set(Calendar.HOUR_OF_DAY, 0);
                todayCal.set(Calendar.MINUTE, 0);
                todayCal.set(Calendar.SECOND, 0);
                todayCal.set(Calendar.MILLISECOND, 0);
                Date todayStart = todayCal.getTime();
                return todayStart;
        }

        @Transactional(value = TxType.REQUIRES_NEW)
        public GlobalResponse insertRegularSchedule(ScheduleRequest scheduleDTO, String username) {
                try {
                        User getUser = userRepository.findByUsername(username)
                                        .orElseThrow(() -> new ResourceNotFoundException("user not found"));
                        List<ScheduleDTO> listRequestSchedule = new ArrayList<>();
                        for (int i = 0; i < scheduleDTO.getSchedules().size(); i++) {

                                if (listRequestSchedule.isEmpty()) {
                                        listRequestSchedule.add(scheduleDTO.getSchedules().get(i));
                                } else {
                                        for (ScheduleDTO existingSchedule : listRequestSchedule) {
                                                // Periksa apakah jadwal baru bertabrakan dengan jadwal yang ada
                                                if (scheduleDTO.getSchedules().get(i).getUser_id()
                                                                .equals(existingSchedule.getUser_id())
                                                                && !scheduleDTO.getSchedules().get(i).getEnd_date()
                                                                                .before(existingSchedule
                                                                                                .getStart_date())
                                                                && !scheduleDTO.getSchedules().get(i).getStart_date()
                                                                                .after(existingSchedule
                                                                                                .getEnd_date())) {
                                                        return GlobalResponse.builder()
                                                                        .message("Anda menginput jadwal yang sama ")
                                                                        .errorMessage("Tidak bisa menambahkan jadwal yang bersilangan")
                                                                        .status(HttpStatus.BAD_REQUEST)
                                                                        .build();
                                                }
                                                // Periksa apakah jadwal baru sama persis dengan jadwal yang ada
                                                else if (scheduleDTO.getSchedules().get(i).getUser_id()
                                                                .equals(existingSchedule.getUser_id())
                                                                && scheduleDTO.getSchedules().get(i).getEnd_date()
                                                                                .equals(existingSchedule
                                                                                                .getStart_date())
                                                                && scheduleDTO.getSchedules().get(i).getStart_date()
                                                                                .equals(existingSchedule
                                                                                                .getEnd_date())) {
                                                        return GlobalResponse.builder()
                                                                        .message("Anda menginput jadwal yang sama")
                                                                        .errorMessage("Tidak bisa menambahkan jadwal yang sama")
                                                                        .status(HttpStatus.BAD_REQUEST)
                                                                        .build();
                                                }
                                        }
                                        // Jika tidak ada tabrakan atau kesamaan, tambahkan jadwal baru ke dalam
                                        // listRequestSchedule
                                        listRequestSchedule.add(scheduleDTO.getSchedules().get(i));
                                }

                                if (scheduleDTO.getSchedules().get(i).getStart_date().before(getDateNow())
                                                || scheduleDTO.getSchedules().get(i).getEnd_date()
                                                                .before(getDateNow())) {
                                        return GlobalResponse
                                                        .builder()
                                                        .message("Tidak boleh input jadwal kemarin")
                                                        .errorMessage("Tidak bisa menambahkan jadwal kemarin")
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }

                                List<Schedule> checkIfExist = repository.findScheduleInAllCheckNoCheck(
                                                scheduleDTO.getSchedules().get(i).getUser_id(),
                                                scheduleDTO.getSchedules().get(i).getBranch_id(),
                                                scheduleDTO.getSchedules().get(i).getStart_date(),
                                                scheduleDTO.getSchedules().get(i).getEnd_date());
                                if (!checkIfExist.isEmpty()) {
                                        return GlobalResponse
                                                        .builder()
                                                        .message("Anda menginput tanggal yang sudah ada")
                                                        .errorMessage("Tanggal duplikat")
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }

                                if (scheduleDTO.getSchedules().get(i).getStart_date()
                                                .after(scheduleDTO.getSchedules().get(i).getEnd_date())) {
                                        return GlobalResponse
                                                        .builder()
                                                        .message("Tanggal mulai lebih besar dari tanggal berakhir")
                                                        .errorMessage("Start date lebih besar dari end date")
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }

                                List<Schedule> checkDatefExist = repository.findScheduleInDateRangeByUserIdNoCategory(
                                                scheduleDTO.getSchedules().get(i).getUser_id(),
                                                scheduleDTO.getSchedules().get(i).getStart_date(),
                                                scheduleDTO.getSchedules().get(i).getEnd_date());
                                if (!checkDatefExist.isEmpty()) {
                                        return GlobalResponse
                                                        .builder()
                                                        .message("Tanggal sudah tersedia")
                                                        .errorMessage("Tanggal dengan start_date:"
                                                                        + convertDateToRoman.convertDateHehe(scheduleDTO
                                                                                        .getSchedules().get(i)
                                                                                        .getStart_date())
                                                                        + " and end_date:"
                                                                        + convertDateToRoman.convertDateHehe(scheduleDTO
                                                                                        .getSchedules().get(i)
                                                                                        .getEnd_date())
                                                                        + ", sudah terbuat sebelumnya")
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }
                        }

                        for (int i = 0; i < scheduleDTO.getSchedules().size(); i++) {
                                Optional<Branch> branchId = branchRepository
                                                .findById(scheduleDTO.getSchedules().get(i).getBranch_id());
                                if (!branchId.isPresent()) {
                                        return GlobalResponse.builder()
                                                        .message("Branch tidak ditemukan")
                                                        .errorMessage("Branch with id: "
                                                                        + scheduleDTO.getSchedules().get(i)
                                                                                        .getBranch_id()
                                                                        + " is not found")
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }

                                Optional<User> userId = userRepository
                                                .findById(scheduleDTO.getSchedules().get(i).getUser_id());
                                if (!userId.isPresent()) {
                                        return GlobalResponse.builder()
                                                        .message("User tidak ditemukan")
                                                        .errorMessage("User with id: "
                                                                        + scheduleDTO.getSchedules().get(i).getUser_id()
                                                                        + " is not found")
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }
                                Schedule schedule = new Schedule(
                                                null,
                                                null,
                                                userId.get(),
                                                branchId.get(),
                                                scheduleDTO.getSchedules().get(i).getDescription(),
                                                scheduleDTO.getSchedules().get(i).getStart_date(),
                                                scheduleDTO.getSchedules().get(i).getEnd_date(),
                                                null,
                                                null,
                                                EStatus.TODO,
                                                ECategory.REGULAR,
                                                0,
                                                null,
                                                getUser.getId(),
                                                new Date(),
                                                new Date());

                                // check if schedule already exist?

                                Schedule response = repository.save(schedule);
                                logService.save(response.getCreatedBy(), response.getDescription(), response.getId(),
                                                ECategory.REGULAR, response.getStatus());
                        }

                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil menambahkan data")
                                        .status(HttpStatus.OK)
                                        .build();

                } catch (DataException e) {
                        return GlobalResponse
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }
        }

        @Transactional(value = TxType.REQUIRES_NEW)
        public GlobalResponse insertSpecialSchedule(ScheduleRequest scheduleDTO, String username) {
                try {
                        User getUser = userRepository.findByUsername(username)
                                        .orElseThrow(() -> new ResourceNotFoundException("user not found"));

                        List<ScheduleDTO> listRequestSchedule = new ArrayList<>();
                        for (int i = 0; i < scheduleDTO.getSchedules().size(); i++) {
                                if (listRequestSchedule.isEmpty()) {
                                        listRequestSchedule.add(scheduleDTO.getSchedules().get(i));
                                } else {
                                        for (ScheduleDTO existingSchedule : listRequestSchedule) {
                                                // Periksa apakah jadwal baru bertabrakan dengan jadwal yang ada
                                                if (scheduleDTO.getSchedules().get(i).getUser_id()
                                                                .equals(existingSchedule.getUser_id())
                                                                && !scheduleDTO.getSchedules().get(i).getEnd_date()
                                                                                .before(existingSchedule
                                                                                                .getStart_date())
                                                                && !scheduleDTO.getSchedules().get(i).getStart_date()
                                                                                .after(existingSchedule
                                                                                                .getEnd_date())) {
                                                        return GlobalResponse.builder()
                                                                        .message("Anda menginput jadwal yang sama ")
                                                                        .errorMessage("Tidak bisa menambahkan jadwal yang bersilangan")
                                                                        .status(HttpStatus.BAD_REQUEST)
                                                                        .build();
                                                }
                                                // Periksa apakah jadwal baru sama persis dengan jadwal yang ada
                                                else if (scheduleDTO.getSchedules().get(i).getUser_id()
                                                                .equals(existingSchedule.getUser_id())
                                                                && scheduleDTO.getSchedules().get(i).getEnd_date()
                                                                                .equals(existingSchedule
                                                                                                .getStart_date())
                                                                && scheduleDTO.getSchedules().get(i).getStart_date()
                                                                                .equals(existingSchedule
                                                                                                .getEnd_date())) {
                                                        return GlobalResponse.builder()
                                                                        .message("Anda menginput jadwal yang sama")
                                                                        .errorMessage("Tidak bisa menambahkan jadwal yang sama")
                                                                        .status(HttpStatus.BAD_REQUEST)
                                                                        .build();
                                                }
                                        }
                                        // Jika tidak ada tabrakan atau kesamaan, tambahkan jadwal baru ke dalam
                                        // listRequestSchedule
                                        listRequestSchedule.add(scheduleDTO.getSchedules().get(i));
                                }

                                if (scheduleDTO.getSchedules().get(i).getStart_date().before(getDateNow())
                                                || scheduleDTO.getSchedules().get(i).getEnd_date()
                                                                .before(getDateNow())) {
                                        return GlobalResponse
                                                        .builder()
                                                        .message("Tanggal yang diinput adalah tanggal sebelumnya")
                                                        .errorMessage("Tidak bisa menambahkan jadwal kemarin")
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }

                                List<Schedule> checkIfExist = repository.findScheduleInAllCheckNoCheck(
                                                scheduleDTO.getSchedules().get(i).getUser_id(),
                                                scheduleDTO.getSchedules().get(i).getBranch_id(),
                                                scheduleDTO.getSchedules().get(i).getStart_date(),
                                                scheduleDTO.getSchedules().get(i).getEnd_date());
                                if (!checkIfExist.isEmpty()) {
                                        return GlobalResponse
                                                        .builder()
                                                        .message("Tanggal sudah ada")
                                                        .errorMessage("Tanggal duplikat")
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }

                                if (scheduleDTO.getSchedules().get(i).getStart_date()
                                                .after(scheduleDTO.getSchedules().get(i).getEnd_date())) {
                                        return GlobalResponse
                                                        .builder()
                                                        .message("Tanggal mulai lebih besar dari tanggal berakhir")
                                                        .errorMessage("Start date lebih besar dari end date")
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }
                        }
                        for (int i = 0; i < scheduleDTO.getSchedules().size(); i++) {
                                Optional<Branch> branchId = branchRepository
                                                .findById(scheduleDTO.getSchedules().get(i).getBranch_id());
                                if (!branchId.isPresent()) {
                                        return GlobalResponse.builder()
                                                        .message("Branch tidak ditemukan")
                                                        .errorMessage("Branch with id: "
                                                                        + scheduleDTO.getSchedules().get(i)
                                                                                        .getBranch_id()
                                                                        + " is not found")
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }

                                Optional<User> userId = userRepository
                                                .findById(scheduleDTO.getSchedules().get(i).getUser_id());
                                if (!userId.isPresent()) {
                                        return GlobalResponse.builder()
                                                        .message("User tidak ditemukan")
                                                        .errorMessage("User with id: "
                                                                        + scheduleDTO.getSchedules().get(i).getUser_id()
                                                                        + " is not found")
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }
                                Schedule schedule = new Schedule(
                                                null,
                                                null,
                                                userId.get(),
                                                branchId.get(),
                                                scheduleDTO.getSchedules().get(i).getDescription(),
                                                scheduleDTO.getSchedules().get(i).getStart_date(),
                                                scheduleDTO.getSchedules().get(i).getEnd_date(),
                                                null,
                                                null,
                                                EStatus.TODO,
                                                ECategory.SPECIAL,
                                                0,
                                                null,
                                                getUser.getId(),
                                                new Date(),
                                                new Date());

                                // change all todo or progress status to pending status
                                repository.editStatusPendingScheduleByDate(
                                                scheduleDTO.getSchedules().get(i).getUser_id(),
                                                getUser.getId(), scheduleDTO.getSchedules().get(i).getStart_date(),
                                                scheduleDTO.getSchedules().get(i).getEnd_date());
                                Schedule response = repository.save(schedule);

                                logService.save(response.getCreatedBy(), response.getDescription(), response.getId(),
                                                ECategory.SPECIAL, response.getStatus());
                        }

                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil menambahkan data")
                                        .status(HttpStatus.OK)
                                        .build();

                } catch (DataException e) {
                        return GlobalResponse
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }
        }

        public GlobalResponse requestSchedule(RequestReschedule dto, String username) {
                try {

                        if (dto.getStart_date().before(getDateNow())
                                        || dto.getEnd_date()
                                                        .before(getDateNow())) {
                                return GlobalResponse
                                                .builder()
                                                .message("Tanggal yang diinputkan adalah tanggal sebelumnya")
                                                .errorMessage("Tidak bisa menambahkan jadwal kemarin")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }

                        List<Schedule> checkIfExist = repository.findScheduleInAllCheckNoCheck(
                                        dto.getUser_id(),
                                        dto.getBranch_id(),
                                        dto.getStart_date(),
                                        dto.getEnd_date());
                        if (!checkIfExist.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Jadwal sudah ada")
                                                .errorMessage("Jadwal duplikat")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }
                        if (dto.getStart_date().after(dto.getEnd_date())) {
                                return GlobalResponse
                                                .builder()
                                                .message("Tanggal mulai lebih besar dari tanggal berakhir")
                                                .errorMessage("Start date lebih besar dari end date")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }

                        List<Schedule> checkDatefExist = repository.findScheduleInDateRangeByUserIdNoCategory(
                                        dto.getUser_id(),
                                        dto.getStart_date(),
                                        dto.getEnd_date());
                        if (!checkDatefExist.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Tanggal sudah tersedia")
                                                .errorMessage("Tanggal dengan start_date:"
                                                                + convertDateToRoman
                                                                                .convertDateHehe(dto.getStart_date())
                                                                + " and end_date:"
                                                                + convertDateToRoman.convertDateHehe(dto.getEnd_date())
                                                                + ", sudah terbuat sebelumnya")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }
                        User getUser = userRepository.findByUsername(username)
                                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                        Schedule getSchedule = repository.findById(dto.getSchedule_id())
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Schedule with id: " + dto.getSchedule_id() + " is not found"));

                        Optional<Branch> branchId = branchRepository
                                        .findById(dto.getBranch_id());
                        if (!branchId.isPresent()) {
                                return GlobalResponse.builder()
                                                .message("Branch dengan id: " + dto.getBranch_id() + " tidak ada")
                                                .errorMessage("Branch tidak ditemukan")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }

                        Optional<User> userId = userRepository
                                        .findById(dto.getUser_id());
                        if (!userId.isPresent()) {
                                return GlobalResponse.builder()
                                                .message("user dengan id: "
                                                                + dto.getUser_id()
                                                                + " tidak ditemukan")
                                                .errorMessage("User tidak ditemukan")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }

                        ScheduleTrx scheduleTrx = new ScheduleTrx();
                        scheduleTrx.setUser(userId.get());
                        scheduleTrx.setBranch(branchId.get());
                        scheduleTrx.setDescription(dto.getDescription());
                        scheduleTrx.setStart_date(dto.getStart_date());
                        scheduleTrx.setEnd_date(dto.getEnd_date());
                        scheduleTrx.setStatus(EStatus.TODO);
                        scheduleTrx.setCategory(ECategory.REGULAR);
                        scheduleTrx.setCreatedBy(getUser.getId());
                        scheduleTrx.setCreated_at(new Date());
                        ScheduleTrx response1 = scheduleTrxRepo.save(scheduleTrx);

                        Schedule schedule1 = getSchedule;
                        schedule1.setScheduleTrx(response1);
                        schedule1.setStatus(EStatus.REQUEST);
                        schedule1.setUpdatedBy(getUser.getId());
                        schedule1.setUpdated_at(new Date());
                        Schedule response2 = repository.save(schedule1);

                        logService.save(response2.getUpdatedBy(), response2.getDescription(), response2.getId(),
                                        response2.getCategory(), response2.getStatus());

                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil request jadwal")
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        return GlobalResponse
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }
        }

        public GlobalResponse editSchedule(EditScheduleDTO dto, Long id, ECategory category, String username) {
                try {
                        Schedule getSchedule = repository.findById(id)
                                        .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));

                        if (dto.getStart_date().before(getDateNow())
                                        || dto.getEnd_date().before(getDateNow())) {
                                return GlobalResponse
                                                .builder()
                                                .message("Jadwal yang dimasukkan adalah jadwal sebelumnya")
                                                .errorMessage("Tidak bisa menambahkan jadwal kemarin")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }

                        List<Schedule> checkIfExist = repository.findScheduleInAllCheckNoCheckForEdit(
                                        dto.getUser_id(),
                                        dto.getDescription(),
                                        dto.getBranch_id(),
                                        dto.getStart_date(),
                                        dto.getEnd_date());
                        if (!checkIfExist.isEmpty()) {
                                return GlobalResponse
                                                .builder()
                                                .message("Jadwal sudah ada ")
                                                .errorMessage("Jadwal duplikat")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }
                        if (dto.getStart_date().after(dto.getEnd_date())) {
                                return GlobalResponse
                                                .builder()
                                                .message("Tanggal mulai lebih besar dari tanggal berakhir")
                                                .errorMessage("Start date lebih besar dari end date")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }

                        if (getSchedule.getStart_date() != dto.getStart_date()
                                        && getSchedule.getEnd_date() != dto.getEnd_date()) {
                                List<Schedule> checkDatefExist = repository
                                                .findScheduleInDateRangeByUserIdNoCategory(
                                                                dto.getUser_id(),
                                                                dto.getStart_date(),
                                                                dto.getEnd_date());
                                if (!checkDatefExist.isEmpty()) {
                                        return GlobalResponse
                                                        .builder()
                                                        .message("Jadwal sudah ada ")
                                                        .errorMessage("Tanggal dengan start_date:"
                                                                        + convertDateToRoman
                                                                                        .convertDateHehe(dto
                                                                                                        .getStart_date())
                                                                        + " and end_date:"
                                                                        + convertDateToRoman.convertDateHehe(
                                                                                        dto.getEnd_date())
                                                                        + ", sudah terbuat sebelumnya")
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build();
                                }
                        }

                        User getUser = userRepository.findByUsername(username)
                                        .orElseThrow(() -> new ResourceNotFoundException("user not found"));

                        Branch branchId = Branch.builder()
                                        .id(dto.getBranch_id())
                                        .build();

                        User userId = User.builder()
                                        .id(dto.getUser_id())
                                        .build();

                        Schedule schedule = getSchedule;
                        schedule.setUser(userId);
                        schedule.setBranch(branchId);
                        schedule.setDescription(dto.getDescription());
                        schedule.setStart_date(dto.getStart_date());
                        schedule.setEnd_date(dto.getEnd_date());
                        schedule.setUpdatedBy(getUser.getId());
                        schedule.setUpdated_at(new Date());

                        Schedule response = repository.save(schedule);

                        logService.edit(response.getCreatedBy(), response.getDescription(), response.getId(),
                                        response.getCategory(), response.getStatus());

                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil mengubah data")
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        return GlobalResponse
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }
        }

        public GlobalResponse approve(Long id) throws Exception {
                User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (!user.getLevel().getName().equals("LEAD")) {
                        return GlobalResponse.builder().message("Selain audit leader tidak dapat akses")
                                        .status(HttpStatus.UNAUTHORIZED).build();
                }
                Optional<Schedule> getSchedule = repository.findById(id);
                if (!getSchedule.isPresent()) {
                        return GlobalResponse.builder().message("Schedule with id : " + id + " is not found")
                                        .errorMessage("Jadwal tidak ditemukan").status(HttpStatus.BAD_REQUEST).build();
                }

                ScheduleTrx getTrx = scheduleTrxRepo.findById(getSchedule.get().getScheduleTrx().getId())
                                .orElseThrow(() -> new Exception("Not found"));

                if (getTrx.getStart_date().before(getDateNow())
                                || getTrx.getEnd_date()
                                                .before(getDateNow())) {
                        return GlobalResponse
                                        .builder()
                                        .message("Jadwal yang dimasukkan adalah jadwal sebelumnya")
                                        .errorMessage("Tidak bisa menambahkan jadwal kemarin")
                                        .status(HttpStatus.BAD_REQUEST)
                                        .build();
                }

                List<Schedule> checkIfExist = repository.findScheduleInAllCheckNoCheck(
                                getTrx.getUser().getId(),
                                getTrx.getBranch().getId(),
                                getTrx.getStart_date(),
                                getTrx.getEnd_date());
                if (!checkIfExist.isEmpty()) {
                        return GlobalResponse
                                        .builder()
                                        .message("Jadwal sudah ada")
                                        .errorMessage("Jadwal duplikat")
                                        .status(HttpStatus.BAD_REQUEST)
                                        .build();
                }
                if (getTrx.getStart_date().after(getTrx.getEnd_date())) {
                        return GlobalResponse
                                        .builder()
                                        .message("Tanggal mulai lebih besar dari tanggal berakhir")
                                        .errorMessage("Start date lebih besar dari end date")
                                        .status(HttpStatus.BAD_REQUEST)
                                        .build();
                }

                List<Schedule> checkDatefExist = repository.findScheduleInDateRangeByUserIdNoCategory(
                                getTrx.getUser().getId(),
                                getTrx.getStart_date(),
                                getTrx.getEnd_date());
                if (!checkDatefExist.isEmpty()) {
                        return GlobalResponse
                                        .builder()
                                        .message("Tanggal sudah ada")
                                        .errorMessage("Tanggal dengan start_date:"
                                                        + convertDateToRoman.convertDateHehe(getTrx.getStart_date())
                                                        + " and end_date:"
                                                        + convertDateToRoman.convertDateHehe(getTrx.getEnd_date())
                                                        + ", sudah terbuat sebelumnya")
                                        .status(HttpStatus.BAD_REQUEST)
                                        .build();
                }

                Schedule schedule = new Schedule();
                schedule.setUser(getTrx.getUser());
                schedule.setBranch(getTrx.getBranch());
                schedule.setDescription(getTrx.getDescription());
                schedule.setStart_date(getTrx.getStart_date());
                schedule.setStatus(EStatus.TODO);
                schedule.setCategory(getTrx.getCategory());
                schedule.setEnd_date(getTrx.getEnd_date());
                schedule.setIs_delete(0);
                schedule.setCreatedBy(user.getId());
                schedule.setUpdatedBy(user.getId());
                schedule.setCreated_at(new Date());
                schedule.setUpdated_at(new Date());
                repository.save(schedule);

                Schedule editSchedule = getSchedule.get();
                editSchedule.setStatus(EStatus.APPROVE);
                editSchedule.setUpdatedBy(user.getId());
                editSchedule.setUpdated_at(new Date());
                repository.save(editSchedule);

                return GlobalResponse.builder().message("Berhasil approve jadwal").status(HttpStatus.OK).build();

        }

        public GlobalResponse rejected(Long id) throws Exception {
                User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (!user.getLevel().getName().equals("LEAD")) {
                        return GlobalResponse.builder().message("Selain audit leader tidak dapat akses")
                                        .status(HttpStatus.UNAUTHORIZED).build();
                }
                Optional<Schedule> getSchedule = repository.findById(id);
                if (!getSchedule.isPresent()) {
                        return GlobalResponse.builder().errorMessage("Schedule with id : " + id + " is not found")
                                        .message("Jadwal tidak ditemukan").status(HttpStatus.BAD_REQUEST).build();
                }

                Schedule editSchedule = getSchedule.get();
                editSchedule.setStatus(EStatus.REJECTED);
                editSchedule.setUpdatedBy(user.getId());
                editSchedule.setUpdated_at(new Date());
                repository.save(editSchedule);

                return GlobalResponse.builder().message("Berhasil reject jadwal").status(HttpStatus.OK).build();

        }

        public GlobalResponse editStatus(Long id, EStatus status, String username) {
                try {
                        User getUser = userRepository.findByUsername(username)
                                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                        if (getUser.getLevel().getCode().equals("A")) {
                                return GlobalResponse.builder().message("Selain audit leader tidak dapat akses")
                                                .status(HttpStatus.UNAUTHORIZED).build();
                        }

                        Schedule getSchedule = repository.findById(id)
                                        .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));

                        Schedule schedule = getSchedule;
                        schedule.setStatus(status);
                        schedule.setUpdatedBy(getUser.getId());
                        schedule.setUpdated_at(new Date());

                        Schedule response = repository.save(schedule);
                        logService.edit(response.getCreatedBy(), response.getDescription(), response.getId(),
                                        response.getCategory(), response.getStatus());

                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil mengubah data")
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        return GlobalResponse
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .build();
                } catch (Exception e) {
                        return GlobalResponse
                                        .builder()
                                        .message("Exception :" + e.getMessage())
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .build();
                }
        }

        public GlobalResponse delete(Long id) {
                try {
                        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                        Optional<Schedule> getSchedule = repository.findById(id);
                        if (!getSchedule.isPresent()) {
                                return GlobalResponse.builder().message(
                                                "Shcedule with id :" + getSchedule.get().getId() + " is not found")
                                                .status(HttpStatus.BAD_REQUEST)
                                                .build();
                        }
                        Schedule schedule = getSchedule.get();
                        schedule.setIs_delete(1);
                        schedule.setUpdatedBy(getUser.getId());
                        schedule.setUpdated_at(new Date());

                        Schedule response = repository.save(schedule);
                        logService.delete(response.getCreatedBy(), response.getDescription(), response.getId(),
                                        response.getCategory(), response.getStatus());
                        return GlobalResponse
                                        .builder()
                                        .message("Berhasil menghapus data")
                                        .status(HttpStatus.OK)
                                        .build();
                } catch (DataException e) {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Data error");
                } catch (Exception e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");
                }
        }

}
