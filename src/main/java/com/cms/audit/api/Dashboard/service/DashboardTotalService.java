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

    public ResponseEntity<Object> dashboardTotal(Long year, Long month) {
        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Specification<Clarification> clarificationSpec = buildClarificationSpecification(year, month, getUser);
        Specification<FollowUp> followUpSpec = buildFollowUpSpecification(year, month, getUser);
        Specification<Schedule> scheduleSpec = buildScheduleSpecification(year, month, getUser);

        List<Clarification> clarifications = clarificationRepo.findAll(clarificationSpec);
        Map<String, Object> clarificationData = prepareClarificationData(clarifications);

        List<FollowUp> followUps = followUpRepo.findAll(followUpSpec);
        Map<String, Object> followUpData = prepareFollowUpData(followUps);

        List<Schedule> schedules = scheduleRepository.findAll(scheduleSpec);
        Map<String, Object> scheduleData = prepareScheduleData(schedules);

        List<Map<String, Object>> top5 = prepareTopBotData(clarifications, true);
        List<Map<String, Object>> bottom5 = prepareTopBotData(clarifications, false);
        Map<String, Object> topBotData = prepareTopBotResponse(top5, bottom5);

        List<Map<String, Object>> rankings = prepareRankingsData(followUps);

        Map<String, Object> chartData = new HashMap<>();
        chartData.put("clarification", clarificationData);
        chartData.put("follow_up", followUpData);
        chartData.put("schedule", scheduleData);

        Map<String, Object> response = prepareDashboardResponse(year, month, chartData, topBotData, rankings);

        return returnResponse(response);
    }

    private Specification<Clarification> buildClarificationSpecification(Long year, Long month, User getUser) {
        Specification<Clarification> clarificationSpec = Specification
                .where(new SpecificationFIlter<Clarification>().createdAtYear(year))
                .and(new SpecificationFIlter<Clarification>().isNotDeleted());

        if (month != null && month != 0) {
            DateCompareDTO monthSeparate = convertDateToRoman.getDateRange(month);
            clarificationSpec = clarificationSpec.and(
                    new SpecificationFIlter<Clarification>().dateRange(monthSeparate.getDate1(), monthSeparate.getDate2()));
        }

        if (getUser.getLevel().getCode().equals("C")) {
            clarificationSpec = clarificationSpec.and(new SpecificationFIlter<Clarification>().userId(getUser.getId()));
        } else if (getUser.getLevel().getCode().equals("B")) {
            Specification<Clarification> regionOrUserSpecClarification = Specification
                    .where(new SpecificationFIlter<Clarification>().getByRegionIds(getUser.getRegionId()))
                    .or(new SpecificationFIlter<Clarification>().userId(getUser.getId()));
            clarificationSpec = clarificationSpec.and(regionOrUserSpecClarification);
        }

        return clarificationSpec;
    }

    private Specification<FollowUp> buildFollowUpSpecification(Long year, Long month, User getUser) {
        Specification<FollowUp> followUpSpec = Specification
                .where(new SpecificationFIlter<FollowUp>().createdAtYear(year))
                .and(new SpecificationFIlter<FollowUp>().isNotDeleted());

        if (month != null && month != 0) {
            DateCompareDTO monthSeparate = convertDateToRoman.getDateRange(month);
            followUpSpec = followUpSpec.and(
                    new SpecificationFIlter<FollowUp>().dateRange(monthSeparate.getDate1(), monthSeparate.getDate2()));
        }

        if (getUser.getLevel().getCode().equals("C")) {
            followUpSpec = followUpSpec.and(new SpecificationFIlter<FollowUp>().userId(getUser.getId()));
        } else if (getUser.getLevel().getCode().equals("B")) {
            Specification<FollowUp> regionOrUserSpecFollowUp = Specification
                    .where(new SpecificationFIlter<FollowUp>().getByRegionIds(getUser.getRegionId()))
                    .or(new SpecificationFIlter<FollowUp>().userId(getUser.getId()));
            followUpSpec = followUpSpec.and(regionOrUserSpecFollowUp);
        }

        return followUpSpec;
    }

    private Specification<Schedule> buildScheduleSpecification(Long year, Long month, User getUser) {
        Specification<Schedule> scheduleSpec = Specification
                .where(new SpecificationFIlter<Schedule>().createdAtYear(year))
                .and(new SpecificationFIlter<Schedule>().isNotDeleted());

        if (month != null && month != 0) {
            DateCompareDTO monthSeparate = convertDateToRoman.getDateRange(month);
            scheduleSpec = scheduleSpec.and(
                    new SpecificationFIlter<Schedule>().dateRange(monthSeparate.getDate1(), monthSeparate.getDate2()));
        }

        if (getUser.getLevel().getCode().equals("C")) {
            scheduleSpec = scheduleSpec.and(new SpecificationFIlter<Schedule>().userId(getUser.getId()));
        } else if (getUser.getLevel().getCode().equals("B")) {
            Specification<Schedule> regionOrUserSpecSchedule = Specification
                    .where(new SpecificationFIlter<Schedule>().getByRegionIds(getUser.getRegionId()))
                    .or(new SpecificationFIlter<Schedule>().userId(getUser.getId()));
            scheduleSpec = scheduleSpec.and(regionOrUserSpecSchedule);
        }

        return scheduleSpec;
    }

    private Map<String, Object> prepareClarificationData(List<Clarification> clarifications) {
        Map<String, Object> clarificationData = prepareData(
            clarifications, 
            c -> EStatusClarification.DONE.equals(c.getStatus())
        );

        return clarificationData;
    }

    private Map<String, Object> prepareFollowUpData(List<FollowUp> followUps) {
        Map<String, Object> followUpData = prepareData(
            followUps, 
            f -> EStatusFollowup.CLOSE.equals(f.getStatus())
        );

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
    private List<Map<String, Object>> prepareTopBotData(List<Clarification> clarifications, boolean isTop) {
        // Sorting users based on clarification count
        Map<User, Long> userClarificationCount = new HashMap<>();
        for (Clarification clarification : clarifications) {
            User user = clarification.getUser();
            if (user != null) {
                userClarificationCount.put(user, userClarificationCount.getOrDefault(user, 0L) + 1);
            }
        }
    
        // Sorting users by clarification count
        List<Map.Entry<User, Long>> sortedEntries = userClarificationCount.entrySet().stream()
                .sorted((e1, e2) -> isTop ? Long.compare(e2.getValue(), e1.getValue()) : Long.compare(e1.getValue(), e2.getValue()))
                .collect(Collectors.toList());
    
        // Prepare top or bottom 5 users
        List<Map<String, Object>> result = new ArrayList<>();
        int count = 0;
        int maxResults = 5; // Adjust the number of users you want to retrieve
        for (Map.Entry<User, Long> entry : sortedEntries) {
            User user = entry.getKey();
            Long totalClarifications = entry.getValue();
            Long doneClarifications = clarifications.stream()
                    .filter(c -> user.equals(c.getUser()) && EStatusClarification.DONE.equals(c.getStatus()))
                    .count();
    
            Map<String, Object> userData = new HashMap<>();
            userData.put("name", user.getFullname());
            userData.put("total", totalClarifications);
            userData.put("done", doneClarifications);
            result.add(userData);
    
            count++;
            if (isTop && count >= maxResults) {
                break;
            }
        }
    
        if (!isTop) {
            // If fetching bottom users, adjust the list to start from the end
            Collections.reverse(result);
            result = result.subList(0, Math.min(maxResults, result.size()));
        }
    
        return result;
    }
    

    private Map<String, Object> prepareTopBotResponse(List<Map<String, Object>> top5, List<Map<String, Object>> bottom5) {
        Map<String, Object> topBotData = new HashMap<>();
        topBotData.put("top", top5);
        topBotData.put("bottom", bottom5);
        return topBotData;
    }

    private List<Map<String, Object>> prepareRankingsData(List<FollowUp> followUps) {
        // Count closed follow-ups per user
        Map<User, Long> userFollowUpCount = new HashMap<>();
        for (FollowUp followUp : followUps) {
            User user = followUp.getUser();
            if (user != null) {
                userFollowUpCount.put(user, userFollowUpCount.getOrDefault(user, 0L) + 1);
            }
        }

        // Sorting users based on closed follow-up count
        List<Map.Entry<User, Long>> sortedEntries = userFollowUpCount.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .collect(Collectors.toList());

        // Prepare rankings data
        List<Map<String, Object>> resultList = new ArrayList<>();
        //int count = 0;
        for (Map.Entry<User, Long> entry : sortedEntries) {
            User user = entry.getKey();
            Long totalFollowUps = entry.getValue();
            Long closedFollowUps = followUps.stream()
                    .filter(f -> user.equals(f.getUser()) && EStatusFollowup.CLOSE.equals(f.getStatus()))
                    .count();

            Map<String, Object> userData = new HashMap<>();
            //userData.put("number", ++count);
            userData.put("name", user.getFullname());
            userData.put("total", totalFollowUps);
            userData.put("close", closedFollowUps);
            resultList.add(userData);

            // if (count == 5) {
            //     break;
            // }
        }

        return resultList;
    }

    private Map<String, Object> prepareDashboardResponse(Long year, Long month, Map<String, Object> chartData,
            Map<String, Object> topBotData, List<Map<String, Object>> followUp) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("year", year != null ? year : convertDateToRoman.getLongYearNumber(new Date()));
        response.put("month", month != null ? convertDateToRoman.getMonthName(month) : convertDateToRoman.getMonthName(convertDateToRoman.getLongMonthNumber(new Date())));
        response.put("summary", chartData);
        // response.put("topbot", topBotData);
        // response.put("followUp", followUp);

        Map<String, Object> rankingMap = new LinkedHashMap<>();
        rankingMap.put("clarification", topBotData);
        rankingMap.put("follow_up", followUp);
        response.put("rankings", rankingMap);

        return response;
    }

    // private double calculatePercentage(long part, long total) {
    //     if (total == 0) {
    //         return 0.0;
    //     }
    //     return (double) part / total * 100;
    // }

    public ResponseEntity<Object> returnResponse(Map<String, Object> obj) {
        if (obj == null || obj.isEmpty()) {
            return ResponseEntittyHandler.allHandler(obj, "Data tidak ditemukan", HttpStatus.OK, null);
        }
        return ResponseEntittyHandler.allHandler(obj, "Data berhasil ditampilkan", HttpStatus.OK, null);
    }
}
