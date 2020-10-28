package com.jobmanager.dynamicqwartzjobs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jobmanager.dynamicqwartzjobs.jobs.EmailJob;
import lombok.Data;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.*;

/**
 * Created by sousaJ on 27/10/2020
 * in package - com.jobmanager.dynamicqwartzjobs.model
 **/
@Data
public class JobDescriptor {

    @NotBlank
    private String name;
    private String group;

    @NotEmpty
    private String subject;

    @NotEmpty
    private String messageBody;

//    Class<? extends Job> jobClass;

    @NotEmpty
    private List<String> to;
    private List<String> cc;
    private List<String> bcc;
    private Map <String, Object> data = new LinkedHashMap<>();

    @JsonProperty("triggers")
    private List<TriggerDescriptor> triggerDescriptors = new ArrayList<>();

    public JobDescriptor setName(final String name) {
        this.name = name;
        return this;
    }

    public JobDescriptor setGroup(final String group) {
        this.group = group;
        return this;
    }

    public JobDescriptor setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public JobDescriptor setMessageBody(String messageBody) {
        this.messageBody = messageBody;
        return this;
    }

    public JobDescriptor setTo(List<String> to) {
        this.to = to;
        return this;
    }

    public JobDescriptor setCc(List<String> cc) {
        this.cc = cc;
        return this;
    }

    public JobDescriptor setBcc(List<String> bcc) {
        this.bcc = bcc;
        return this;
    }

    public JobDescriptor setData(final Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public JobDescriptor setTriggerDescriptors(final List<TriggerDescriptor> triggerDescriptors) {
        this.triggerDescriptors = triggerDescriptors;
        return this;
    }

    @JsonIgnore
    public Set<Trigger> buildTriggers(){
        Set<Trigger> triggers = new LinkedHashSet<>();
        for (TriggerDescriptor descriptor: triggerDescriptors){
            triggers.add(descriptor.buildTrigger());
        }
        return triggers;
    }

    public JobDetail buildJobDetail(){
        JobDataMap jobDataMap = new JobDataMap(getData());
        jobDataMap.put("subject", subject);
        jobDataMap.put("messageBody", messageBody);
        jobDataMap.put("to", to);
        jobDataMap.put("cc", cc);
        jobDataMap.put("bcc", bcc);

        return JobBuilder.newJob(EmailJob.class)
                .withIdentity(getName(), getGroup())
                .usingJobData(jobDataMap)
                .build();
    }

    @SuppressWarnings("unchecked")
    public static JobDescriptor buildDescriptor(JobDetail jobDetail, List<? extends Trigger> triggersOfJob) {

        List<TriggerDescriptor> triggerDescriptors = new ArrayList<>();

        for (Trigger trigger : triggersOfJob){
            triggerDescriptors.add(TriggerDescriptor.buildDescriptor(trigger));
        }
       return new JobDescriptor()
               .setName(jobDetail.getKey().getName())
               .setGroup(jobDetail.getKey().getGroup())
               .setSubject(jobDetail.getJobDataMap().getString("subject"))
               .setMessageBody(jobDetail.getJobDataMap().getString("messageBody"))
               .setTo( (List<String>) jobDetail.getJobDataMap().get("to"))
               .setCc((List<String>) jobDetail.getJobDataMap().get("cc"))
               .setBcc((List<String>) jobDetail.getJobDataMap().get("bcc"))
               .setBcc((List<String>) jobDetail.getJobDataMap().get("bcc"))
               .setTriggerDescriptors(triggerDescriptors);
    }

}
