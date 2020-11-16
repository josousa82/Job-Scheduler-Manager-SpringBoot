package com.jobmanager.dynamicqwartzjobs.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by sousaJ on 16/11/2020
 * in package - com.jobmanager.dynamicqwartzjobs.config
 **/
@Configuration
public class QuartzConfig {

    private ApplicationContext applicationContext;
    private DataSource dataSource;

    public QuartzConfig(ApplicationContext applicationContext, DataSource dataSource) {
        this.applicationContext = applicationContext;
        this.dataSource = dataSource;
    }

}
