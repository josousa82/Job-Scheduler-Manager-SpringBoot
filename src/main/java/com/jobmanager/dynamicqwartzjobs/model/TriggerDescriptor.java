package com.jobmanager.dynamicqwartzjobs.model;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    public Trigger buildTrigger(){
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

    public static  TriggerDescriptor buildDescriptor(Trigger trigger){
        return new TriggerDescriptor()
                .setName(trigger.getKey().getName())
                .setGroup(trigger.getKey().getGroup())
                .setFireTime((LocalDateTime) trigger.getJobDataMap().get("fireTime"))
                .setCron(trigger.getJobDataMap().getString("cron"));
    }

}
