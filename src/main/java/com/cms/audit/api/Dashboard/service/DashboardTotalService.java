package com.cms.audit.api.Dashboard.service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cms.audit.api.Clarifications.models.Clarification;
import com.cms.audit.api.Clarifications.models.EStatusClarification;
import com.cms.audit.api.Common.constant.SpecificationFIlter;
import com.cms.audit.api.Common.constant.convertDateToRoman;
import com.cms.audit.api.Common.response.ResponseEntittyHandler;
import com.cms.audit.api.Dashboard.dto.DateCompareDTO;
import com.cms.audit.api.Dashboard.repository.ClarificationDashboardRepo;
import com.cms.audit.api.Dashboard.repository.FollowUpDashboardRepo;
import com.cms.audit.api.Dashboard.repository.ScheduleDashboardRepo;
import com.cms.audit.api.Dashboard.repository.UserDashboardRepo;
import com.cms.audit.api.FollowUp.models.EStatusFollowup;
import com.cms.audit.api.FollowUp.models.FollowUp;
import com.cms.audit.api.InspectionSchedule.models.ECategory;
import com.cms.audit.api.InspectionSchedule.models.EStatus;
import com.cms.audit.api.InspectionSchedule.models.Schedule;
import com.cms.audit.api.Management.User.models.User;

@Service
public class DashboardTotalService {

    @Autowired
    private ClarificationDashboardRepo clarificationRepo;

    @Autowired
    private ScheduleDashboardRepo scheduleRepository;

    @Autowired
    private FollowUpDashboardRepo followUpRepo;

    @Autowired
    private UserDashboardRepo userRepo;

    public ResponseEntity<Object> dashboardTotal(Long year, Long month) {
        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Specification<Clarification> clarificationSpec = buildClarificationSpecification(year, month, getUser, false);
        Specification<FollowUp> followUpSpec = buildFollowUpSpecification(year, month, getUser, false);
        Specification<Clarification> allClarificationSpec = buildClarificationSpecification(year, month, getUser, true);
        Specification<FollowUp> allFollowUpSpec = buildFollowUpSpecification(year, month, getUser, true);
        Specification<Schedule> scheduleSpec = buildScheduleSpecification(year, month, getUser, false);

        List<Clarification> clarifications = clarificationRepo.findAll(clarificationSpec);
        List<Clarification> allClarifications = clarificationRepo.findAll(allClarificationSpec);
        Map<String, Object> clarificationData = prepareClarificationData(clarifications);

        List<FollowUp> followUps = followUpRepo.findAll(followUpSpec);
        List<FollowUp> allFollowUps = followUpRepo.findAll(allFollowUpSpec);
        Map<String, Object> followUpData = prepareFollowUpData(followUps);

        List<Schedule> schedules = scheduleRepository.findAll(scheduleSpec);
        Map<String, Object> scheduleData = prepareScheduleData(schedules);

        List<Map<String, Object>> top5 = prepareTopData(allClarifications);
        List<Map<String, Object>> bottom5 = prepareBottomData(allClarifications);
        Map<String, Object> topBotData = prepareTopBotResponse(top5, bottom5);

        List<Map<String, Object>> rankings = prepareRankingsData(allFollowUps);

        Map<String, Object> chartData = new HashMap<>();
        chartData.put("clarification", clarificationData);
        chartData.put("follow_up", followUpData);
        chartData.put("schedule", scheduleData);

        Map<String, Object> response = prepareDashboardResponse(year, month, chartData, topBotData, rankings);

        return returnResponse(response);
    }

    private Specification<Clarification> buildClarificationSpecification(Long year, Long month, User getUser,
            boolean isAll) {
        Specification<Clarification> clarificationSpec = Specification
                .where(new SpecificationFIlter<Clarification>().createdAtYear(year))
                .and(new SpecificationFIlter<Clarification>().isNotDeleted());

        if (month != null && month != 0) {
            DateCompareDTO monthSeparate = convertDateToRoman.getDateRange(month);
            clarificationSpec = clarificationSpec.and(
                    new SpecificationFIlter<Clarification>().dateRange(monthSeparate.getDate1(),
                            monthSeparate.getDate2()));
        }

        if (!isAll) {
            if (getUser.getLevel().getCode().equals("C")) {
                clarificationSpec = clarificationSpec
                        .and(new SpecificationFIlter<Clarification>().userId(getUser.getId()));
            } else if (getUser.getLevel().getCode().equals("B")) {
                Specification<Clarification> regionOrUserSpecClarification = Specification
                        .where(new SpecificationFIlter<Clarification>().getByRegionIds(getUser.getRegionId()))
                        .or(new SpecificationFIlter<Clarification>().userId(getUser.getId()));
                clarificationSpec = clarificationSpec.and(regionOrUserSpecClarification);
            }
        }

        return clarificationSpec;
    }

    private Specification<FollowUp> buildFollowUpSpecification(Long year, Long month, User getUser, boolean isAll) {
        Specification<FollowUp> followUpSpec = Specification
                .where(new SpecificationFIlter<FollowUp>().createdAtYear(year))
                .and(new SpecificationFIlter<FollowUp>().isNotDeleted());

        if (month != null && month != 0) {
            DateCompareDTO monthSeparate = convertDateToRoman.getDateRange(month);
            followUpSpec = followUpSpec.and(
                    new SpecificationFIlter<FollowUp>().dateRange(monthSeparate.getDate1(), monthSeparate.getDate2()));
        }

        if (!isAll) {
            if (getUser.getLevel().getCode().equals("C")) {
                followUpSpec = followUpSpec.and(new SpecificationFIlter<FollowUp>().userId(getUser.getId()));
            } else if (getUser.getLevel().getCode().equals("B")) {
                Specification<FollowUp> regionOrUserSpecFollowUp = Specification
                        .where(new SpecificationFIlter<FollowUp>().getByRegionIds(getUser.getRegionId()))
                        .or(new SpecificationFIlter<FollowUp>().userId(getUser.getId()));
                followUpSpec = followUpSpec.and(regionOrUserSpecFollowUp);
            }
        }

        return followUpSpec;
    }

    private Specification<Schedule> buildScheduleSpecification(Long year, Long month, User getUser, boolean isAll) {
        Specification<Schedule> scheduleSpec = Specification
                .where(new SpecificationFIlter<Schedule>().createdAtYear(year))
                .and(new SpecificationFIlter<Schedule>().isNotDeleted());

        if (month != null && month != 0) {
            DateCompareDTO monthSeparate = convertDateToRoman.getDateRange(month);
            scheduleSpec = scheduleSpec.and(
                    new SpecificationFIlter<Schedule>().dateBetween(monthSeparate.getDate1(), monthSeparate.getDate2()));
        }

        if (!isAll) {
            if (getUser.getLevel().getCode().equals("C")) {
                scheduleSpec = scheduleSpec.and(new SpecificationFIlter<Schedule>().userId(getUser.getId()));
            } else if (getUser.getLevel().getCode().equals("B")) {
                Specification<Schedule> regionOrUserSpecSchedule = Specification
                        .where(new SpecificationFIlter<Schedule>().getByRegionIds(getUser.getRegionId()))
                        .or(new SpecificationFIlter<Schedule>().userId(getUser.getId()));
                scheduleSpec = scheduleSpec.and(regionOrUserSpecSchedule);
            }
        }

        return scheduleSpec;
    }

    private Map<String, Object> prepareClarificationData(List<Clarification> clarifications) {
        Map<String, Object> clarificationData = prepareData(
                clarifications,
                c -> EStatusClarification.DONE.equals(c.getStatus()));

        return clarificationData;
    }

    private Map<String, Object> prepareFollowUpData(List<FollowUp> followUps) {
        Map<String, Object> followUpData = prepareData(
                followUps,
                f -> EStatusFollowup.CLOSE.equals(f.getStatus()));

        return followUpData;
    }

    private Map<String, Object> prepareScheduleData(List<Schedule> schedules) {
        // Filter schedules based on category REGULAR
        List<Schedule> regularSchedules = schedules.stream()
                .filter(s -> ECategory.REGULAR.equals(s.getCategory()))
                .collect(Collectors.toList());

        // Filter schedules based on category SPECIAL
        List<Schedule> specialSchedules = schedules.stream()
                .filter(s -> ECategory.SPECIAL.equals(s.getCategory()))
                .collect(Collectors.toList());

        // Prepare data for regular schedules
        Map<String, Object> regularData = prepareData(regularSchedules, s -> EStatus.DONE.equals(s.getStatus()));

        // Prepare data for special schedules
        Map<String, Object> specialData = prepareData(specialSchedules, s -> EStatus.DONE.equals(s.getStatus()));

        // Create the final map for schedule data
        Map<String, Object> scheduleData = new HashMap<>();
        scheduleData.put("regular", regularData);
        scheduleData.put("special", specialData);

        return scheduleData;
    }

    private <T> Map<String, Object> prepareData(List<T> items, Predicate<T> isDonePredicate) {
        long total = items.size();
        long done = items.stream().filter(isDonePredicate).count();

        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("done", done);

        return data;
    }

    private List<Map<String, Object>> prepareTopData(List<Clarification> clarifications) {
        Map<User, Long> userClarificationCount = new HashMap<>();
        Map<User, Long> userDoneClarificationCount = new HashMap<>();
        for (Clarification clarification : clarifications) {
            User user = clarification.getUser();
            if (user != null) {
                userClarificationCount.put(user, userClarificationCount.getOrDefault(user, 0L) + 1);
                if (EStatusClarification.DONE.equals(clarification.getStatus())) {
                    userDoneClarificationCount.put(user, userDoneClarificationCount.getOrDefault(user, 0L) + 1);
                }
            }
        }

        List<Map.Entry<User, Long>> sortedEntries = userClarificationCount.entrySet().stream()
                .sorted((e1, e2) -> {
                    int comparison = Long.compare(e2.getValue(), e1.getValue()); // Descending by total
                    if (comparison == 0) {
                        return Long.compare(userDoneClarificationCount.getOrDefault(e2.getKey(), 0L),
                                userDoneClarificationCount.getOrDefault(e1.getKey(), 0L)); // Descending by done
                    }
                    return comparison;
                })
                .collect(Collectors.toList());

        List<Map<String, Object>> result = new ArrayList<>();
        int count = 0;
        int maxResults = 5;
        for (Map.Entry<User, Long> entry : sortedEntries) {
            if (count >= maxResults)
                break;

            User user = entry.getKey();
            Long totalClarifications = entry.getValue();
            Long doneClarifications = userDoneClarificationCount.getOrDefault(user, 0L);

            Map<String, Object> userData = new HashMap<>();
            userData.put("name", user.getFullname());
            userData.put("total", totalClarifications);
            userData.put("done", doneClarifications);
            result.add(userData);

            count++;
        }

        return result;
    }

    private List<Map<String, Object>> prepareBottomData(List<Clarification> clarifications) {
        Map<User, Long> userClarificationCount = new HashMap<>();
        Map<User, Long> userDoneClarificationCount = new HashMap<>();
        for (Clarification clarification : clarifications) {
            User user = clarification.getUser();
            if (user != null) {
                userClarificationCount.put(user, userClarificationCount.getOrDefault(user, 0L) + 1);
                if (EStatusClarification.DONE.equals(clarification.getStatus())) {
                    userDoneClarificationCount.put(user, userDoneClarificationCount.getOrDefault(user, 0L) + 1);
                }
            }
        }

        List<Map.Entry<User, Long>> sortedEntries = userClarificationCount.entrySet().stream()
                .sorted((e1, e2) -> {
                    int comparison = Long.compare(e1.getValue(), e2.getValue()); // Ascending by total
                    if (comparison == 0) {
                        return Long.compare(userDoneClarificationCount.getOrDefault(e1.getKey(), 0L),
                                userDoneClarificationCount.getOrDefault(e2.getKey(), 0L)); // Ascending by done
                    }
                    return comparison;
                })
                .collect(Collectors.toList());

        List<Map<String, Object>> result = new ArrayList<>();
        int count = 0;
        int maxResults = 5;
        for (Map.Entry<User, Long> entry : sortedEntries) {
            if (count >= maxResults)
                break;

            User user = entry.getKey();
            Long totalClarifications = entry.getValue();
            Long doneClarifications = userDoneClarificationCount.getOrDefault(user, 0L);

            Map<String, Object> userData = new HashMap<>();
            userData.put("name", user.getFullname());
            userData.put("total", totalClarifications);
            userData.put("done", doneClarifications);
            result.add(userData);

            count++;
        }

        return result;
    }

    private Map<String, Object> prepareTopBotResponse(List<Map<String, Object>> top5,
            List<Map<String, Object>> bottom5) {
        Map<String, Object> topBotData = new HashMap<>();
        topBotData.put("top", top5);
        topBotData.put("bottom", bottom5);
        return topBotData;
    }

    private List<Map<String, Object>> prepareRankingsData(List<FollowUp> followUps) {
        // Spesifikasi untuk mengambil pengguna dengan level ID 2 yang tidak dihapus
        Specification<User> userSpec = Specification
                .where(new SpecificationFIlter<User>().isNotDeleted())
                .and(new SpecificationFIlter<User>().userLevelId(2L));
        List<User> listUser = userRepo.findAll(userSpec);

        // Jika daftar pengguna kosong, kembalikan daftar kosong
        if (listUser.isEmpty()) {
            return new ArrayList<>();
        }

        // Buat peta untuk menghitung jumlah tindak lanjut untuk setiap pengguna
        Map<User, Long> userFollowUpCount = new HashMap<>();
        for (FollowUp followUp : followUps) {
            if (followUp.getUpdated_by() != null) {
                User user = userRepo.findById(followUp.getUpdated_by()).orElse(null);
                if (user != null) {
                    userFollowUpCount.put(user, userFollowUpCount.getOrDefault(user, 0L) + 1);
                }
            } else {
                if (followUp.getCreated_by() != null) {
                    User createdBy = userRepo.findById(followUp.getCreated_by()).orElse(null);
                    if (createdBy != null) {
                        userFollowUpCount.put(createdBy, userFollowUpCount.getOrDefault(createdBy, 0L) + 1);
                    }
                }
            }
        }

        // Buat daftar hasil
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (User user : listUser) {
            Long totalFollowUps = userFollowUpCount.getOrDefault(user, 0L);
            Long closedFollowUps = followUps.stream()
                    .filter(f -> user.equals(f.getUser()) && EStatusFollowup.CLOSE.equals(f.getStatus()))
                    .count();

            Map<String, Object> userData = new HashMap<>();
            userData.put("name", user.getFullname());
            userData.put("total", totalFollowUps);
            userData.put("done", closedFollowUps);
            resultList.add(userData);
        }

        // Urutkan hasil berdasarkan jumlah total tindak lanjut (descending), dan jika
        // sama, berdasarkan jumlah done (descending)
        resultList.sort((u1, u2) -> {
            int comparison = Long.compare((Long) u2.get("total"), (Long) u1.get("total"));
            if (comparison == 0) {
                comparison = Long.compare((Long) u2.get("done"), (Long) u1.get("done"));
            }
            return comparison;
        });

        return resultList;
    }

    private Map<String, Object> prepareDashboardResponse(Long year, Long month, Map<String, Object> chartData,
            Map<String, Object> topBotData, List<Map<String, Object>> followUp) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("year", year != null ? year : convertDateToRoman.getLongYearNumber(new Date()));
        response.put("month", month != null ? convertDateToRoman.getMonthName(month)
                : convertDateToRoman.getMonthName(convertDateToRoman.getLongMonthNumber(new Date())));
        response.put("summary", chartData);

        Map<String, Object> rankingMap = new LinkedHashMap<>();
        rankingMap.put("clarification", topBotData);
        rankingMap.put("follow_up", followUp);
        response.put("rankings", rankingMap);

        return response;
    }

    public ResponseEntity<Object> returnResponse(Map<String, Object> obj) {
        if (obj == null || obj.isEmpty()) {
            return ResponseEntittyHandler.allHandler(obj, "Data tidak ditemukan", HttpStatus.OK, null);
        }
        return ResponseEntittyHandler.allHandler(obj, "Data berhasil ditampilkan", HttpStatus.OK, null);
    }
}
