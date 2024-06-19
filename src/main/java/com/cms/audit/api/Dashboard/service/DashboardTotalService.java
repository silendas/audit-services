package com.cms.audit.api.Dashboard.service;

import java.util.*;
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

        // Get clarifications and count statuses
        List<Clarification> clarifications = clarificationRepo.findAll(clarificationSpec);
        Map<String, Object> clarificationData = prepareClarificationData(clarifications);

        // Get follow-ups and count statuses
        List<FollowUp> followUps = followUpRepo.findAll(followUpSpec);
        Map<String, Object> followUpData = prepareFollowUpData(followUps);

        // Get schedules and count statuses
        List<Schedule> schedules = scheduleRepository.findAll(scheduleSpec);
        Map<String, Object> scheduleData = prepareScheduleData(schedules);

        // Prepare topbot data (top 5 and bottom 5 users based on clarification count)
        List<Map<String, Object>> top5 = prepareTopBotData(clarifications, true);
        List<Map<String, Object>> bottom5 = prepareTopBotData(clarifications, false);
        Map<String, Object> topBotData = prepareTopBotResponse(top5, bottom5);

        // Prepare rankings data (top users based on follow-up count)
        List<Map<String, Object>> rankings = prepareRankingsData(followUps);

        // Prepare response data
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
        long totalClarifications = clarifications.size();
        long doneClarifications = clarifications.stream().filter(c -> EStatusClarification.DONE.equals(c.getStatus())).count();
        double percentClarifications = calculatePercentage(doneClarifications, totalClarifications);

        Map<String, Object> clarificationData = new HashMap<>();
        clarificationData.put("total", totalClarifications);
        clarificationData.put("done", doneClarifications);
        clarificationData.put("percent", percentClarifications);

        return clarificationData;
    }

    private Map<String, Object> prepareFollowUpData(List<FollowUp> followUps) {
        long totalFollowUps = followUps.size();
        long closedFollowUps = followUps.stream().filter(f -> EStatusFollowup.CLOSE.equals(f.getStatus())).count();
        double percentFollowUps = calculatePercentage(closedFollowUps, totalFollowUps);

        Map<String, Object> followUpData = new HashMap<>();
        followUpData.put("total", totalFollowUps);
        followUpData.put("close", closedFollowUps);
        followUpData.put("percent", percentFollowUps);

        return followUpData;
    }

    private Map<String, Object> prepareScheduleData(List<Schedule> schedules) {
        long totalSchedules = schedules.size();
        long doneSchedules = schedules.stream().filter(s -> EStatus.DONE.equals(s.getStatus())).count();
        double percentSchedules = calculatePercentage(doneSchedules, totalSchedules);

        Map<String, Object> scheduleData = new HashMap<>();
        scheduleData.put("total", totalSchedules);
        scheduleData.put("done", doneSchedules);
        scheduleData.put("percent", percentSchedules);

        return scheduleData;
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
        for (Map.Entry<User, Long> entry : sortedEntries) {
            User user = entry.getKey();
            Long totalClarifications = entry.getValue();
            Long doneClarifications = clarifications.stream()
                    .filter(c -> user.equals(c.getUser()) && EStatusClarification.DONE.equals(c.getStatus()))
                    .count();

            Map<String, Object> userData = new HashMap<>();
            userData.put("number", ++count);
            userData.put("name", user.getFullname());
            userData.put("total_clarification", totalClarifications);
            userData.put("done", doneClarifications);
            result.add(userData);

            if (count == 5) {
                break;
            }
        }

        return result;
    }

    private Map<String, Object> prepareTopBotResponse(List<Map<String, Object>> top5, List<Map<String, Object>> bottom5) {
        Map<String, Object> topBotData = new HashMap<>();
        topBotData.put("top5", top5);
        topBotData.put("bottom5", bottom5);
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
        int count = 0;
        for (Map.Entry<User, Long> entry : sortedEntries) {
            User user = entry.getKey();
            Long totalFollowUps = entry.getValue();
            Long closedFollowUps = followUps.stream()
                    .filter(f -> user.equals(f.getUser()) && EStatusFollowup.CLOSE.equals(f.getStatus()))
                    .count();

            Map<String, Object> userData = new HashMap<>();
            userData.put("number", ++count);
            userData.put("name", user.getFullname());
            userData.put("total_followup", totalFollowUps);
            userData.put("close", closedFollowUps);
            resultList.add(userData);

            if (count == 5) {
                break;
            }
        }

        return resultList;
    }

    private Map<String, Object> prepareDashboardResponse(Long year, Long month, Map<String, Object> chartData,
            Map<String, Object> topBotData, List<Map<String, Object>> rankings) {
        Map<String, Object> response = new HashMap<>();
        response.put("year", year != null ? year : convertDateToRoman.getLongYearNumber(new Date()));
        response.put("month", month != null ? convertDateToRoman.getMonthName(month) : convertDateToRoman.getMonthName(convertDateToRoman.getLongMonthNumber(new Date())));
        response.put("chart", chartData);
        response.put("topbot", topBotData);
        response.put("rankings", rankings);
        return response;
    }

    private double calculatePercentage(long part, long total) {
        if (total == 0) {
            return 0.0;
        }
        return (double) part / total * 100;
    }

    public ResponseEntity<Object> returnResponse(Map<String, Object> obj) {
        if (obj == null || obj.isEmpty()) {
            return ResponseEntittyHandler.allHandler(obj, "Data tidak ditemukan", HttpStatus.OK, null);
        }
        return ResponseEntittyHandler.allHandler(obj, "Data berhasil ditampilkan", HttpStatus.OK, null);
    }
}
