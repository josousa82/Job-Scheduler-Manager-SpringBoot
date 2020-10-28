package com.jobmanager.services;

import com.jobmanager.model.JobDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Created by sousaJ on 28/10/2020
 * in package - com.jobmanager.services
 **/
@Slf4j
@Service
@Transactional
public class EmailService {

    private final Scheduler scheduler;


    public EmailService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public JobDescriptor createJob(String group, JobDescriptor descriptor){
        descriptor.setGroup(group);
        JobDetail jobDetail = descriptor.buildJobDetail();
        Set<Trigger> triggersForJob = descriptor.buildTriggers();

        log.info("About to save job  with key {} ", jobDetail.getKey());

        try{

            scheduler.scheduleJob(jobDetail, triggersForJob, false);

            log.info("job with key - {} saved successfully", jobDetail.getKey());

        } catch (SchedulerException  e) {
            log.error("Could not save job with key - {}  due to error - {}",
                    jobDetail.getKey(), e.getLocalizedMessage());
            throw new IllegalArgumentException(e.getLocalizedMessage());
         }

        return descriptor;
    }

    public Optional<JobDescriptor> findJob(String group, String name){
        try{
            JobDetail jobDetail= scheduler.getJobDetail(JobKey.jobKey(name, group));
            if(Objects.nonNull(jobDetail))
                return Optional.of(
                        JobDescriptor.buildDescriptor(jobDetail,
                                scheduler.getTriggersOfJob(JobKey.jobKey(name, group))));

        } catch (SchedulerException e) {
            log.error("Could not find job with key - {}.{}  due to error - {}", group, name, e.getLocalizedMessage());
        }

        log.warn("Could not find job with key - {}.{}", group, name);
        return Optional.empty();
    }

    public void updateJob(String group, String name, JobDescriptor descriptor){
        try{

            JobDetail oldJobDetail = scheduler.getJobDetail(JobKey.jobKey(name, group));

            if(Objects.nonNull(oldJobDetail)){

                JobDataMap jobDataMap = oldJobDetail.getJobDataMap();
                jobDataMap.put("subject", descriptor.getSubject());
                jobDataMap.put("messageBody", descriptor.getMessageBody());
                jobDataMap.put("to", descriptor.getTo());
                jobDataMap.put("cc", descriptor.getCc());
                jobDataMap.put("bcc", descriptor.getBcc());

                JobBuilder jb = oldJobDetail.getJobBuilder();
                JobDetail newJobDetail = jb.usingJobData(jobDataMap).storeDurably().build();
                scheduler.addJob(newJobDetail, true);

                log.info("Updated job with key - {}", newJobDetail.getKey());
                return;
            }
            log.warn("Could not find job with key - {}.{} to update", group, name);
        } catch (SchedulerException e) {
            log.error("Could not find job with key {}.{} to update due to error - {}", group, name, e.getLocalizedMessage());
        }

    }

    public void deleteJob(String group, String name){
        try{
            scheduler.deleteJob(JobKey.jobKey(name, group));
            log.info("Deleted job with key - {}.{}", group, name);

        } catch (SchedulerException e) {
            log.error("Could not delete job with key {}.{} due to error - {}", group, name, e.getLocalizedMessage());
        }
    }

    public void pauseJob(String group, String name){
        try{
            scheduler.pauseJob(JobKey.jobKey(name, group));
            log.info("Paused job with key - {}.{}", group, name);

        } catch (SchedulerException e) {
            log.error("Could not pause job with key {}.{} due to error - {}", group, name, e.getLocalizedMessage());
        }
    }

    public void resumeJob(String group, String name, JobDescriptor descriptor){
            try{
                scheduler.resumeJob(JobKey.jobKey(name, group));
                log.info("Resumed job with key - {}.{}", group, name);
            } catch (SchedulerException e) {
                log.error("Could not resume job with key {}.{} due to error - {}", group, name, e.getLocalizedMessage());
            }
    }
}


















