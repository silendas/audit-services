package com.cms.audit.api.Common.constant;

import java.util.Date;
import java.util.List;

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

public class SpecificationFIlter<T> {
    public Specification<T> nameLike(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name != null) {
                return criteriaBuilder.like(root.get("user").get("fullname"), "%" +name+"%");
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
            Join<T, Branch> branchJoin = root.join("branch");
            Join<Branch, Area> areaJoin = branchJoin.join("area");
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

    public Specification<T> isNotDeleted() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.notEqual(root.get("is_delete"), 1);
        };
    }
}
