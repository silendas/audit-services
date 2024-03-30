package com.cms.audit.api.Management.Dropdown;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Clarifications.models.EPriority;
import com.cms.audit.api.Clarifications.models.EStatusClarification;
import com.cms.audit.api.Common.constant.BasePath;
import com.cms.audit.api.Common.response.GlobalResponse;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.InspectionSchedule.models.ECategory;
import com.cms.audit.api.InspectionSchedule.models.EStatus;
import com.cms.audit.api.Management.Case.services.CaseService;
import com.cms.audit.api.Management.CaseCategory.services.CaseCategoryService;
import com.cms.audit.api.Management.Level.services.LevelService;
import com.cms.audit.api.Management.Office.AreaOffice.services.AreaService;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.BranchOffice.repository.BranchRepository;
import com.cms.audit.api.Management.Office.BranchOffice.services.BranchService;
import com.cms.audit.api.Management.Office.MainOffice.services.MainService;
import com.cms.audit.api.Management.Office.RegionOffice.services.RegionService;
import com.cms.audit.api.Management.Penalty.services.PenaltyService;
import com.cms.audit.api.Management.ReportType.services.ReportTypeService;
import com.cms.audit.api.Management.Role.services.RoleService;
import com.cms.audit.api.Management.User.dto.DropDownUserDTO;
import com.cms.audit.api.Management.User.dto.response.DropDownUser;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Management.User.repository.UserRepository;
import com.cms.audit.api.Management.User.services.UserService;

import jakarta.annotation.Nullable;

@RestController
@Validated
@RequestMapping(value = BasePath.BASE_PATH_DROP_DOWN)
public class DropdownController {

    @Autowired
    private UserService userService;

    @Autowired
    private CaseService caseService;

    @Autowired
    private CaseCategoryService ccService;

    @Autowired
    private LevelService levelService;

    @Autowired
    private BranchService branchService;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private AreaService areaService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private MainService mainService;

    @Autowired
    private PenaltyService penaltyService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportTypeService rtService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/users")
    public ResponseEntity<Object> getUser(
            @Nullable @RequestParam("region_id") Long regionId,
            @Nullable @RequestParam("main_id") Long mainId) {
        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        GlobalResponse response = null;
        System.out.println("nyampe sini");
        if (regionId != null) {
            System.out.println("kesini");
            response = userService.dropDownByRegionId(regionId);
        } else if (mainId != null) {
            System.out.println("kesini2");
            response = userService.dropDown();
        } else {
            System.out.println("kesini3");
            if (getUser.getLevel().getId() == 1) {
                response = userService.dropDown();
            } else if (getUser.getLevel().getId() == 2) {
                List<DropDownUserDTO> user = new ArrayList<>();
                if (getUser.getRegionId() != null) {
                    System.out.println("2");
                    List<User> userAgain = userRepository.findAll();
                    for (int u = 0; u < userAgain.size(); u++) {
                        for (int i = 0; i < getUser.getRegionId().size(); i++) {
                            System.out.println("2.1");
                            Long region_id = getUser.getRegionId().get(i);
                            System.out.println("2.2");
                            if (userAgain.get(u).getRegionId().size() == 0 || userAgain.get(u).getRegionId() == null) {
                                System.out.println("2.4");
                                if (userAgain.get(u).getBranchId() != null
                                        || userAgain.get(u).getBranchId().size() != 0) {
                                    System.out.println("2.4.2");

                                    for (int e = 0; e < userAgain.get(u).getBranchId()
                                            .size(); e++) {
                                        Optional<Branch> branchAgain = branchRepository
                                                .findById(userAgain.get(u)
                                                        .getBranchId()
                                                        .get(e));
                                        if (branchAgain.get().getArea().getRegion()
                                                .getId() == region_id) {
                                            System.out.println("2.4.OK");
                                            DropDownUserDTO builder = new DropDownUserDTO();
                                            builder.setFullname(userAgain.get(u).getFullname());
                                            builder.setId(userAgain.get(u).getId());
                                            builder.setInitial_name(userAgain.get(u).getInitial_name());
                                            user.add(builder);
                                        }

                                    }
                                }
                            } else {
                                System.out.println("2.5");
                                System.out.println(userAgain.get(u).getRegionId()
                                        .size());
                                for (int o = 0; o < userAgain.get(u).getRegionId()
                                        .size(); o++) {
                                    if (region_id == userAgain.get(u).getRegionId().get(o)) {
                                        System.out.println("2.5.OK");
                                        DropDownUserDTO builder = new DropDownUserDTO();
                                        builder.setFullname(userAgain.get(u).getFullname());
                                        builder.setId(userAgain.get(u).getId());
                                        builder.setInitial_name(userAgain.get(u).getInitial_name());
                                        user.add(builder);
                                    }
                                }
                            }
                        }
                    }
                    response = GlobalResponse.builder().data(user).message("Success").status(HttpStatus.OK).build();
                } else {
                    response = null;
                }
            }
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                    response.getError());
    }

    @GetMapping("/schedule-status")
    public ResponseEntity<Object> getScheduleStatus() {
        return ResponseEntittyHandler.allHandler(EStatus.values(), "Success", HttpStatus.OK, null);
    }

    @GetMapping("/schedule-category")
    public ResponseEntity<Object> getScheduleCategory() {
        return ResponseEntittyHandler.allHandler(ECategory.values(), "Success", HttpStatus.OK, null);
    }

    @GetMapping("/case")
    public ResponseEntity<Object> getCase() {
        GlobalResponse response = caseService.findSpecific();
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/case-category")
    public ResponseEntity<Object> getCaseCategory(@Nullable @RequestParam("caseId") Long id) {
        GlobalResponse response;
        if (id != null) {
            response = ccService.findSpecific();
        } else {
            response = ccService.findSpecificByCasesId(id);
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @GetMapping("/level")
    public ResponseEntity<Object> getLevel() {
        GlobalResponse response = levelService.findSpecific();
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/penalty")
    public ResponseEntity<Object> getPenalty() {
        GlobalResponse response = penaltyService.findSpecific();
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/report-type")
    public ResponseEntity<Object> getReportType() {
        GlobalResponse response = rtService.findSpecific();
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/role")
    public ResponseEntity<Object> getRole() {
        GlobalResponse response = roleService.findSpecific();
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/main")
    public ResponseEntity<Object> getMain() {
        GlobalResponse response = mainService.findSpecific();
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/region")
    public ResponseEntity<Object> getRegion(
            @Nullable @RequestParam("mainId") Long id) {
        GlobalResponse response;
        if (id == null) {
            response = regionService.findSpecific();
        } else {
            response = regionService.findSpecificByMainId(id);
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @GetMapping("/area")
    public ResponseEntity<Object> getArea(
            @Nullable @RequestParam("regionId") Long id) {
        GlobalResponse response;
        if (id == null) {
            response = areaService.findSpecific();
        } else {
            response = areaService.findSpecificByRegionId(id);
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @GetMapping("/branch")
    public ResponseEntity<Object> getBranch(
            @Nullable @RequestParam("areaId") Long areaId,
            @Nullable @RequestParam("regionId") Long regionId) {
        GlobalResponse response;
        if (areaId != null) {
            response = branchService.findSpecificByAreaId(areaId);
        } else if (regionId != null) {
            response = branchService.findSpecificByRegionId(regionId);
        } else {
            response = branchService.findSpecific();
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(),
                response.getError());
    }

    @GetMapping("/priority")
    public ResponseEntity<Object> getPriority() {
        return ResponseEntittyHandler.allHandler(EPriority.values(), "Success", HttpStatus.OK, null);
    }

    @GetMapping("/clarification-status")
    public ResponseEntity<Object> getStatusClarification() {
        return ResponseEntittyHandler.allHandler(EStatusClarification.values(), "Success", HttpStatus.OK, null);
    }

}
