package com.jobmanager.dynamicqwartzjobs.model;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

import static java.util.UUID.randomUUID;

/**
 * Created by sousaJ on 27/10/2020
 * in package - com.jobmanager.dynamicqwartzjobs.model
 **/

@Slf4j
@Data
public class TriggerDescriptor {

    @NotBlank
    private String name;

    private String group;
    private LocalDateTime fireTime;
    private String cron;

    private  Integer  startHour;
    private  Integer  startMinutes;
    private  Integer  startSeconds;
    private  Integer  startDayOfTheMonth;
    private  Integer  startMonth;
    private  Integer  startYear;

    private  Integer  endHour;
    private  Integer  endMinutes;
    private  Integer  endSeconds;
    private  Integer  endDay;
    private  Integer  endMonth;
    private  Integer  endYear;

    private Date scheduleStartDate;
    private Date scheduleEndDate;

    public static  TriggerDescriptor buildDescriptor(Trigger trigger){
        return new TriggerDescriptor()
                .setName(trigger.getKey().getName())
                .setGroup(trigger.getKey().getGroup())
                .setFireTime((LocalDateTime) trigger.getJobDataMap().get("fireTime"))
                .setCron(trigger.getJobDataMap().getString("cron"))
                .setScheduleStartDate(trigger.getStartTime())
                .setScheduleEndDate(trigger.getEndTime());

    }

    public TriggerDescriptor setScheduleStartDate(Date scheduleStartDate) {
        this.scheduleStartDate = scheduleStartDate;
        return this;
    }


//
//    public TriggerDescriptor setStartHour(Integer startHour) {
//        this.startHour = startHour;
//        return this;
//    }
//
//    public TriggerDescriptor setStartMinutes(Integer startMinutes) {
//        this.startMinutes = startMinutes;
//        return this;
//    }
//
//    public TriggerDescriptor setStartSeconds(Integer startSeconds) {
//        this.startSeconds = startSeconds;
//        return this;
//    }
//
//    public TriggerDescriptor setStartDayOfTheMonth(Integer startDayOfTheMonth) {
//        this.startDayOfTheMonth = startDayOfTheMonth;
//        return this;
//    }
//
//    public TriggerDescriptor setStartMonth(Integer startMonth) {
//        this.startMonth = startMonth;
//        return this;
//    }
//
//    public TriggerDescriptor setStartYear(Integer startYear) {
//        this.startYear = startYear;
//        return this;
//    }
//
//    public TriggerDescriptor setEndHour(Integer endHour) {
//        this.endHour = endHour;
//        return this;
//    }
//
//    public TriggerDescriptor setEndMinutes(Integer endMinutes) {
//        this.endMinutes = endMinutes;
//        return this;
//    }
//
//    public TriggerDescriptor setEndSeconds(Integer endSeconds) {
//        this.endSeconds = endSeconds;
//        return this;
//    }
//
//    public TriggerDescriptor setEndDay(Integer endDay) {
//        this.endDay = endDay;
//        return this;
//    }
//
//    public TriggerDescriptor setEndMonth(Integer endMonth) {
//        this.endMonth = endMonth;
//        return this;
//    }
//
//    public TriggerDescriptor setEndYear(Integer endYear) {
//        this.endYear = endYear;
//        return this;
//    }


    public TriggerDescriptor setName(String name) {
        this.name = name;
        return this;
    }

    public TriggerDescriptor setGroup(String group) {
        this.group = group;
        return this;
    }

    public TriggerDescriptor setFireTime(LocalDateTime fireTime) {
        this.fireTime = fireTime;
        return this;
    }

    public TriggerDescriptor setCron(String cron) {
        this.cron = cron;
        return this;
    }

    private String buildName(){
        return StringUtils.isEmpty(name) ? randomUUID().toString() : name;
    }

    public TriggerDescriptor setScheduleEndDate(Date scheduleEndDate) {
        this.scheduleEndDate = scheduleEndDate;
        return this;
    }

    public Trigger buildTrigger() {
        scheduleStartDate = DateBuilder.dateOf(startHour, startMinutes, startSeconds, startDayOfTheMonth, startMonth, startYear);
        scheduleEndDate = DateBuilder.dateOf(endHour, endMinutes, endSeconds, endDay, endMonth, endYear);

        if(!StringUtils.isEmpty(cron)){
            if(!CronExpression.isValidExpression(cron)){

                log.error("Cron not valid " + cron);

                throw new IllegalArgumentException("Provided expression " + cron + " is not a valid cron expression.");
            }
            return TriggerBuilder.newTrigger()
                    .withIdentity(buildName(), group)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron)
                                                    .withMisfireHandlingInstructionFireAndProceed()
                                                    .inTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault())))
                    .usingJobData("cron", cron)
                    .startAt(scheduleStartDate)
                    .endAt(scheduleEndDate)
                    .build();
        } else if (!StringUtils.isEmpty(fireTime)){

            log.warn("Cron in TriggerDescriptor is empty, but fireTime is populated");

            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("fireTime", fireTime);

            return TriggerBuilder.newTrigger()
                    .withIdentity(buildName(), group)
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                                                        .withMisfireHandlingInstructionNowWithExistingCount())
                    .startAt(Date.from(fireTime.atZone(ZoneId.systemDefault()).toInstant()))
                    .usingJobData(jobDataMap)
                    .build();
        }

        log.error("Unsupported trigger descriptor " + this);
        log.error("Trigger descriptor name was not build");

        throw new IllegalStateException("Unsupported trigger descriptor " + this);
    }
}


