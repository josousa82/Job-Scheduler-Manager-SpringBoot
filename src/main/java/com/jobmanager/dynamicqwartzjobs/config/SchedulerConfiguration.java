package com.jobmanager.dynamicqwartzjobs.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Created by sousaJ on 27/10/2020
 * in package - com.jobmanager.dynamicqwartzjobs.config
 **/
@Configuration
public class SchedulerConfiguration {

    @Bean
    public SchedulerFactoryBean schedulerFactory(){
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        return  factoryBean;
    }


}
