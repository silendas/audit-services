package com.cms.audit.api.Sampling.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Common.constant.SpecificationFIlter;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.Management.Office.BranchOffice.services.BranchService;
import com.cms.audit.api.Management.User.models.User;
import com.cms.audit.api.Sampling.dto.request.RandomTableDto;
import com.cms.audit.api.Sampling.model.RandomTable;
import com.cms.audit.api.Sampling.repository.PagRandomTable;
import com.cms.audit.api.Sampling.repository.RandomTableRepo;

@Service
public class RandomTableService {

    @Autowired
    private RandomTableRepo randomTableRepo;

    @Autowired
    private BranchService branchService;

    @Autowired
    private PagRandomTable pagRandomTable;

    public ResponseEntity<Object> getRandomTables(boolean pageable, int page, int size, Date start_date,
            Date end_date) {
        Specification<RandomTable> spec = Specification
                .where(new SpecificationFIlter<RandomTable>().isNotDeleted())
                .and(new SpecificationFIlter<RandomTable>().dateRange(start_date, end_date))
                .and(new SpecificationFIlter<RandomTable>().orderByIdDesc());
        if (pageable) {
            Page<RandomTable> data = pagRandomTable.findAll(spec, PageRequest.of(page, size));
            return ResponseEntittyHandler.allHandler(data, "Berhasil", HttpStatus.OK, null);
        }
        return ResponseEntittyHandler.allHandler(randomTableRepo.findAll(spec), "Berhasil", HttpStatus.OK, null);
    }

    public ResponseEntity<Object> getRandomTable(Long id) {
        return ResponseEntittyHandler.allHandler(randomTableRepo.findById(id), "Berhasil", HttpStatus.OK, null);
    }

   private Date getFirstDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    // Calculate the last day of the current month
    private Date getLastDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public ResponseEntity<Object> createRandomTable(RandomTableDto dto) {
        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Get the first and last day of the current month
        Date startDate = getFirstDayOfMonth();
        Date endDate = getLastDayOfMonth();

        // Validation: Check if a RandomTable exists for the same branch in the current month
        boolean exists = randomTableRepo.existsByBranchAndCreatedAtBetween(dto.getBranch(), startDate, endDate);
        if (exists) {
            return ResponseEntittyHandler.allHandler(null, "RandomTable for this branch already exists for the current month", HttpStatus.BAD_REQUEST, null);
        }

        RandomTable randomTable = new RandomTable();
        randomTable.setValue(dto.getValue());
        randomTable.setBranch(branchService.getBranchById(dto.getBranch()));
        randomTable.setMargin_error(dto.getMargin_error());
        randomTable.setSlovin_result(dto.getSlovin_result());
        randomTable.setRandom_sampling(dto.getRandom_sampling());
        randomTable.setIs_delete(0);
        randomTable.setCreated_at(new Date());
        randomTable.setCreated_by(getUser.getId());
        randomTableRepo.save(randomTable);
        return ResponseEntittyHandler.allHandler(null, "Berhasil", HttpStatus.OK, null);
    }

    public ResponseEntity<Object> updateRandomTable(Long id, RandomTableDto dto) {
        RandomTable randomTable = randomTableRepo.findById(id).orElse(null);
        if (randomTable == null) {
            return ResponseEntittyHandler.allHandler(null, "RandomTable not found", HttpStatus.NOT_FOUND, null);
        }

        // Get the first and last day of the current month
        Date startDate = getFirstDayOfMonth();
        Date endDate = getLastDayOfMonth();

        // Validation: Check if a RandomTable exists for the same branch in the current month, excluding the current ID
        boolean exists = randomTableRepo.existsByBranchAndCreatedAtBetweenAndIdNot(dto.getBranch(), startDate, endDate, id);
        if (exists) {
            return ResponseEntittyHandler.allHandler(null, "RandomTable for this branch already exists for the current month", HttpStatus.BAD_REQUEST, null);
        }

        randomTable.setValue(dto.getValue());
        randomTable.setBranch(branchService.getBranchById(dto.getBranch()));
        randomTable.setMargin_error(dto.getMargin_error());
        randomTable.setSlovin_result(dto.getSlovin_result());
        randomTable.setRandom_sampling(dto.getRandom_sampling());
        randomTable.setUpdated_at(new Date());
        randomTableRepo.save(randomTable);
        return ResponseEntittyHandler.allHandler(null, "Berhasil", HttpStatus.OK, null);
    }

    public ResponseEntity<Object> deleteRandomTable(Long id) {
        RandomTable res = randomTableRepo.findById(id).get();
        res.setIs_delete(1);
        randomTableRepo.save(res);
        return ResponseEntittyHandler.allHandler(null, "Berhasil", HttpStatus.OK, null);
    }

}
