package com.cms.audit.api.Common.constant;

import java.util.Date;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.jpa.domain.Specification;

import com.cms.audit.api.InspectionSchedule.models.ECategory;
import com.cms.audit.api.InspectionSchedule.models.EStatus;
import com.cms.audit.api.Management.Office.AreaOffice.models.Area;
import com.cms.audit.api.Management.Office.BranchOffice.models.Branch;
import com.cms.audit.api.Management.Office.RegionOffice.models.Region;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class SpecificationFIlter<T> {
    public Specification<T> nameLike(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name != null) {
                return criteriaBuilder.like(root.get("user").get("fullname"), "%" +name+"%");
            }
            return null;
        };
    }

    public Specification<T> byNameLike(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name != null) {
                return criteriaBuilder.like(root.get("name"), "%" +name+"%");
            }
            return null;
        };
    }

    public Specification<T> userId(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id != null) {
                return criteriaBuilder.equal(root.get("user").get("id"), id);
            }
            return null;
        };
    }

    public Specification<T> statusFlow(Long status) {
        return (root, query, criteriaBuilder) -> {
            if (status != null) {
                return criteriaBuilder.equal(root.get("status_flow"), status);
            }
            return null;
        };
    }

    public Specification<T> getByRegionIdList(List<Long> regionIds) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (regionIds != null && !regionIds.isEmpty()) {
                return root.get("area").get("region").get("id").in(regionIds);
            }
            return null;
        };
    }


    public Specification<T> lhaId(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id != null) {
                return criteriaBuilder.equal(root.get("auditDailyReport").get("id"), id);
            }
            return null;
        };
    }

    public Specification<T> branchIdEqual(Long branchId) {
        return (root, query, criteriaBuilder) -> branchId == null ? null
                : criteriaBuilder.equal(root.get("branch").get("id"), branchId);
    }

    public Specification<T> branchNameLike(String branchName) {
        return (root, query, criteriaBuilder) -> branchName == null ? null
                : criteriaBuilder.like(root.get("branch").get("name"), "%" +branchName+"%");
    }

    public Specification<T> regionIdEqual(Long branchId) {
        return (root, query, criteriaBuilder) -> branchId == null ? null
                : criteriaBuilder.equal(root.get("region").get("id"), branchId);
    }

    public Specification<T> regionNameLike(String regionName) {
        return (root, query, criteriaBuilder) -> regionName == null ? null
                : criteriaBuilder.like(root.get("region").get("name"), "%" +regionName+"%");
    }

    public Specification<T> areaIdEqual(Long areaId) {
        return (root, query, criteriaBuilder) -> areaId == null ? null
                : criteriaBuilder.equal(root.get("area").get("id"), areaId);
    }

    public Specification<T> areaNameLike(String areaName) {
        return (root, query, criteriaBuilder) -> areaName == null ? null
                : criteriaBuilder.like(root.get("area").get("name"), "%" +areaName+"%");
    }

    public Specification<T> mainIdEqual(Long mainId) {
        return (root, query, criteriaBuilder) -> mainId == null ? null
                : criteriaBuilder.equal(root.get("main").get("id"), mainId);
    }

    public Specification<T> mainNameLike(String mainName) {
        return (root, query, criteriaBuilder) -> mainName == null ? null
                : criteriaBuilder.like(root.get("main").get("name"), "%" +mainName+"%");
    }


    public Specification<T> codeLike(String code) {
        return (root, query, criteriaBuilder) -> code == null ? null
                : criteriaBuilder.like(root.get("code"), "%" +code+"%");
    }
    
    
    public Specification<T> scheduleIdEqual(Long shceduleId) {
        return (root, query, criteriaBuilder) -> shceduleId == null ? null
                : criteriaBuilder.equal(root.get("schedule").get("id"), shceduleId);
    }

    public Specification<T> dateRange(Date start_date, Date end_date) {
        return (root, query, criteriaBuilder) -> {
            return start_date == null && end_date == null ? null : criteriaBuilder.between(root.get("created_at"), start_date, end_date);
        };
    }

    public Specification<T> dateBetween(Date startDate, Date endDate) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate startDatePredicate = criteriaBuilder.between(root.get("start_date"), startDate, endDate);
            Predicate endDatePredicate = criteriaBuilder.between(root.get("end_date"), startDate, endDate);
            return criteriaBuilder.or(startDatePredicate, endDatePredicate);
        };
    }

    public Specification<T> orderByIdDesc() {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("id"))); // Urutkan berdasarkan ID secara descending
            return criteriaBuilder.conjunction();
        };
    }

    public Specification<T> orderByIdAsc() {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get("id"))); // Urutkan berdasarkan ID secara descending
            return criteriaBuilder.conjunction();
        };
    }

    public Specification<T> getByRegionIds(List<Long> regionIds) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Join<T, Area> areaJoin =root.join("area");
            Join<Area, Region> regionJoin = areaJoin.join("region");
            
            // Creating predicate for region IDs
            Predicate predicate = regionJoin.get("id").in(regionIds);

            // Returning the predicate
            return predicate;
        };
    }

    public Specification<T> getByStatus(List<EStatus> statuses) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (statuses != null && !statuses.isEmpty()) {
                return root.get("status").in(statuses);
            }
            return null;
        };
    }

    public Specification<T> getByCategory(ECategory category) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (category != null) {
                return criteriaBuilder.equal(root.get("category"), category);
            }
            return null;
        };
    }

    public Specification<T> getByCasesId(Long id) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (id != null) {
                return criteriaBuilder.equal(root.get("cases").get("id"), id);
            }
            return null;
        };
    }


    public Specification<T> getByBranchIds(List<Long> branchIds) {
        return (root, query, criteriaBuilder) -> {
            if (branchIds == null || branchIds.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            // Create a predicate that checks if the branch ID array contains any of the provided branch IDs
            String branchIdsString = branchIds.toString().replaceAll("[\\[\\]]", ""); // Convert list to comma-separated string
            return criteriaBuilder.isTrue(criteriaBuilder.function(
                "arrayoverlap",
                Boolean.class,
                root.get("branchId"),
                criteriaBuilder.literal("{" + branchIdsString + "}")
            ));
        };
    }

    public Specification<T> isNotDeleted() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.notEqual(root.get("is_delete"), 1);
        };
    }
}
