package com.cms.audit.api.AuditDailyReport.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cms.audit.api.AuditDailyReport.dto.RevisionDTO;
import com.cms.audit.api.AuditDailyReport.dto.response.RevisionResponse;
import com.cms.audit.api.AuditDailyReport.models.AuditDailyReportDetail;
import com.cms.audit.api.AuditDailyReport.models.Revision;
import com.cms.audit.api.AuditDailyReport.repository.AuditDailyReportDetailRepository;
import com.cms.audit.api.AuditDailyReport.repository.RevisionRepository;
import com.cms.audit.api.Common.exception.ResourceNotFoundException;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Flag.model.Flag;
import com.cms.audit.api.Flag.repository.FlagRepo;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Management.User.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RevisionService {

    @Autowired
    private RevisionRepository repository;

    @Autowired
    private FlagRepo flagRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditDailyReportDetailRepository auditDailyReportDetailRepository;

    private RevisionResponse objRevision(Revision dto){
            RevisionResponse obj = new RevisionResponse();
            obj.setRevisionNumber(dto.getRevisionNumber());
            obj.setCaseCategory(dto.getCaseCategory());
            obj.setCases(dto.getCases());
            obj.setDescription(dto.getDescription());
            obj.setId(dto.getId());
            obj.setIs_research(dto.getIs_research());
            obj.setPermanent_recommendations(dto.getPermanent_recommendations());
            obj.setSuggestion(dto.getSuggestion());
            obj.setTemporary_recommendations(dto.getTemporary_recommendations());

            Optional<User> createdBy = userRepository.findById(dto.getCreated_by());
            Map<String, Object> objUser = new LinkedHashMap<>();
            objUser.put("id", createdBy.get().getId());
            objUser.put("fullname", createdBy.get().getFullname());
            objUser.put("initial_name", createdBy.get().getInitial_name());
            objUser.put("nip", createdBy.get().getNip());
            objUser.put("level", createdBy.get().getLevel());
            obj.setCreated_by(objUser);

        return obj;
    }

    private List<RevisionResponse> pageRevision(List<Revision> dto){
        List<RevisionResponse> response = new ArrayList<>();
        for(int i=0;i<dto.size();i++){
            RevisionResponse obj = new RevisionResponse();
            obj.setRevisionNumber(dto.get(i).getRevisionNumber());
            obj.setCaseCategory(dto.get(i).getCaseCategory());
            obj.setCases(dto.get(i).getCases());
            obj.setDescription(dto.get(i).getDescription());
            obj.setId(dto.get(i).getId());
            obj.setIs_research(dto.get(i).getIs_research());
            obj.setPermanent_recommendations(dto.get(i).getPermanent_recommendations());
            obj.setSuggestion(dto.get(i).getSuggestion());
            obj.setTemporary_recommendations(dto.get(i).getTemporary_recommendations());

            Optional<User> createdBy = userRepository.findById(dto.get(i).getCreated_by());
            Map<String, Object> objUser = new LinkedHashMap<>();
            objUser.put("id", createdBy.get().getId());
            objUser.put("fullname", createdBy.get().getFullname());
            objUser.put("initial_name", createdBy.get().getInitial_name());
            objUser.put("nip", createdBy.get().getNip());
            objUser.put("level", createdBy.get().getLevel());
            obj.setCreated_by(objUser);

            response.add(obj);
        }
        return response;
    }

    public GlobalResponse getAll(Long detaild) {
        try {
            if (detaild == null) {
                List<Revision> response = repository.findAll();
                if (response.isEmpty()) {
                    if (response.isEmpty()) {
                        return GlobalResponse.builder().message("Data not found").status(HttpStatus.OK).data(response)
                                .build();
                    }
                }
                return GlobalResponse.builder().data(response).message("Berhasil menampilkan data")
                        .status(HttpStatus.OK).build();
            } else {
                List<Revision> response = repository.findByDetailIdAll(detaild);
                if (response.isEmpty()) {
                    if (response.isEmpty()) {
                        return GlobalResponse.builder().message("Data not found").status(HttpStatus.OK).data(response)
                                .build();
                    }
                }
                for (int i = 0; i < response.size(); i++) {
                    if (response.get(i).getIs_research() == 1) {
                        Flag isFLag = flagRepo
                                .findOneByAuditDailyReportDetailId(
                                        detaild)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                        "Flag not found"));
                        if (isFLag.getClarification().getFilename() != null) {
                            response.get(i).setIs_research(0);
                        } else {
                            response.get(i).setIs_research(1);
                        }
                    } else {
                        response.get(i).setIs_research(0);
                    }
                }
                return GlobalResponse.builder().data(pageRevision(response)).message("Berhasil menampilkan data")
                        .status(HttpStatus.OK).build();
            }
        } catch (Exception e) {
            return GlobalResponse.builder().error(e).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public GlobalResponse getOne(Long id) {
        try {
            Optional<Revision> response = repository.findById(id);
            if (!response.isPresent()) {
                return GlobalResponse.builder().message("Data tidak ditemukan").status(HttpStatus.BAD_REQUEST).build();
            }
            if (response.get().getIs_research() == 1) {
                Flag isFLag = flagRepo
                        .findOneByAuditDailyReportDetailId(
                                response.get().getAuditDailyReportDetail().getId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Flag not found"));
                if (isFLag.getClarification().getFilename() != null) {
                    response.get().setIs_research(0);
                } else {
                    response.get().setIs_research(1);
                }
            } else {
                response.get().setIs_research(0);
            }
            return GlobalResponse.builder().message("Berhasil menampilkan data").data(objRevision(response.get())).status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return GlobalResponse.builder().error(e).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public GlobalResponse insertNewRevision(RevisionDTO dto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<AuditDailyReportDetail> detail = auditDailyReportDetailRepository
                .findById(dto.getAudit_daily_report_detail_id());
        if (!detail.isPresent()) {
            return GlobalResponse.builder().errorMessage("LHA detail tidak ditemukan")
                    .message("LHA detail with id: " + dto.getAudit_daily_report_detail_id() + " not found")
                    .status(HttpStatus.BAD_REQUEST).build();
        }

        if (user.getLevel().getCode().equals("C")) {
            if (detail.get().getIs_revision() == 1) {
                return GlobalResponse.builder()
                        .message(
                                "Karena sudah direvisi oleh audit area maka tidak dapat direvisi lagi oleh area")
                        .errorMessage("Tidak bisa merevisi karena sudah direvisi area")
                        .status(HttpStatus.BAD_REQUEST).build();
            }
        }

        if (user.getLevel().getCode().equals("B")) {
            if (detail.get().getIs_revision() == 2) {
                return GlobalResponse.builder()
                        .message(
                                "Karena sudah direvisi oleh pusat atau leader maka tidak dapat direvisi lagi oleh area")
                        .errorMessage("Tidak bisa merevisi karena sudah direvisi leader atau pusat")
                        .status(HttpStatus.BAD_REQUEST).build();
            }
        }

        AuditDailyReportDetail auditDailyReportDetail = detail.get();
        auditDailyReportDetail.setDescription(dto.getDescription());
        auditDailyReportDetail.setPermanent_recommendations(dto.getPermanent_recommendations());
        auditDailyReportDetail.setTemporary_recommendations(dto.getTemporary_recommendations());
        auditDailyReportDetail.setSuggestion(dto.getSuggestion());
        auditDailyReportDetail.setStatus_flow(1);
        auditDailyReportDetail.setUpdated_by(user.getId());
        auditDailyReportDetail.setUpdate_at(new Date());

        if (user.getLevel().getCode().equals("A")) {
            if (detail.get().getIs_revision() == null || detail.get().getIs_revision() == 0
                    || detail.get().getIs_revision() == 1) {
                auditDailyReportDetail.setIs_revision(2);
            }
        } else if (user.getLevel().getCode().equals("B")) {
            if (detail.get().getIs_revision() == null || detail.get().getIs_revision() == 0) {
                auditDailyReportDetail.setIs_revision(1);
            }
        }

        auditDailyReportDetailRepository.save(auditDailyReportDetail);

        Revision revision = new Revision();
        revision.setAuditDailyReportDetail(detail.get());
        revision.setCases(detail.get().getCases());
        revision.setCaseCategory(detail.get().getCaseCategory());
        revision.setDescription(dto.getDescription());
        revision.setIs_delete(0);
        revision.setPermanent_recommendations(dto.getPermanent_recommendations());
        revision.setTemporary_recommendations(dto.getTemporary_recommendations());
        revision.setSuggestion(dto.getSuggestion());
        revision.setCreated_by(user.getId());
        revision.setIs_research(detail.get().getIs_research());
        revision.setCreated_at(new Date());

        if (user.getId() == detail.get().getAuditDailyReport().getUser().getId()) {
            Optional<Revision> getRevision = repository.findByDetailId(dto.getAudit_daily_report_detail_id());
            if(getRevision.isPresent()){
                revision.setId(getRevision.get().getId());
                revision.setRevisionNumber(getRevision.get().getRevisionNumber());
            }
        }else{
            Optional<Revision> getRevision = repository.findByDetailId(detail.get().getId());
            if (getRevision.isPresent()) {
                revision.setRevisionNumber(getRevision.get().getRevisionNumber() + 1);
            } else {
                revision.setRevisionNumber(1L);
            }
        }

        try {
            repository.save(revision);
            return GlobalResponse.builder().message("Berhasil menambahkan data").status(HttpStatus.OK).build();
        } catch (Exception e) {
            return GlobalResponse.builder().error(e).status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
