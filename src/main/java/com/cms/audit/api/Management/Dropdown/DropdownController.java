package com.cms.audit.api.Management.Dropdown;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.audit.api.Clarifications.models.EPriority;
import com.cms.audit.api.Clarifications.models.EStatusClarification;
import com.cms.audit.api.InspectionSchedule.models.ECategory;
import com.cms.audit.api.InspectionSchedule.models.EStatus;
import com.cms.audit.api.Management.Case.services.CaseService;
import com.cms.audit.api.Management.CaseCategory.services.CaseCategoryService;
import com.cms.audit.api.Management.Level.services.LevelService;
import com.cms.audit.api.Management.Office.AreaOffice.services.AreaService;
import com.cms.audit.api.Management.Office.BranchOffice.services.BranchService;
import com.cms.audit.api.Management.Office.MainOffice.services.MainService;
import com.cms.audit.api.Management.Office.RegionOffice.services.RegionService;
import com.cms.audit.api.Management.Penalty.services.PenaltyService;
import com.cms.audit.api.Management.ReportType.services.ReportTypeService;
import com.cms.audit.api.Management.Role.services.RoleService;
import com.cms.audit.api.Management.User.services.UserService;
import com.cms.audit.api.common.constant.BasePath;
import com.cms.audit.api.common.response.GlobalResponse;
import com.cms.audit.api.common.response.ResponseEntittyHandler;

@RestController
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
    private AreaService areaService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private MainService mainService;

    @Autowired
    private PenaltyService penaltyService;

    @Autowired
    private ReportTypeService rtService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/users/{id}/region")
    public ResponseEntity<Object> getUserByRegion(@PathVariable("id") Long id) {
        GlobalResponse response = userService.dropDownByRegionId(id);
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/users/{id}/main")
    public ResponseEntity<Object> getUserByMain(@PathVariable("id") Long id) {
        GlobalResponse response = userService.dropDownByMainId(id);
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
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
    public ResponseEntity<Object> getCaseCategory() {
        GlobalResponse response = ccService.findSpecific();
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/case-category/{id}/case")
    public ResponseEntity<Object> getCaseCategoryByCase(@PathVariable("id") Long id) {
        GlobalResponse response = ccService.findSpecificByCasesId(id);
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
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
    public ResponseEntity<Object> getRegion() {
        GlobalResponse response = regionService.findSpecific();
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/region/{id}/main")
    public ResponseEntity<Object> getRegionByMain(@PathVariable("id") Long id) {
        GlobalResponse response = regionService.findSpecificByMainId(id);
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/area")
    public ResponseEntity<Object> getArea() {
        GlobalResponse response = areaService.findSpecific();
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/area/{id}/region")
    public ResponseEntity<Object> getAreaByRegion(@PathVariable("id") Long id) {
        GlobalResponse response = areaService.findSpecificByRegionId(id);
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/branch")
    public ResponseEntity<Object> getBranch() {
        GlobalResponse response = branchService.findSpecific();
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/branch/{id}/region")
    public ResponseEntity<Object> getBranchByRegion(@PathVariable("id") Long id) {
        GlobalResponse response = branchService.findSpecificByRegionId(id);
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
    }

    @GetMapping("/branch/{id}/area")
    public ResponseEntity<Object> getBranchByArea(@PathVariable("id") Long id) {
        GlobalResponse response = branchService.findSpecificByAreaId(id);
        if (response.getError() != null) {
            return ResponseEntittyHandler.allHandler(null, null, response.getStatus(), response.getError());
        }
        return ResponseEntittyHandler.allHandler(response.getData(), response.getMessage(), response.getStatus(), null);
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
