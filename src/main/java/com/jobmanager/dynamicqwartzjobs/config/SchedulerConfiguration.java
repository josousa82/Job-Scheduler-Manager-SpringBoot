package com.jobmanager.dynamicqwartzjobs.config;


import com.jobmanager.dynamicqwartzjobs.AutowiringSpringBeanJobFactory;
import org.springframework.context.ApplicationContext;
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
    public SchedulerFactoryBean schedulerFactory(ApplicationContext applicationContext){
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);

        factoryBean.setJobFactory(jobFactory);
        factoryBean.setApplicationContextSchedulerContextKey("applicationContext");
        return  factoryBean;
    }


}
