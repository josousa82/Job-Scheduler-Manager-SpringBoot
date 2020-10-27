package com.jobmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sousaJ on 27/10/2020
 * in package - com.jobmanager.model
 **/
public class JobDescriptor {
    private String name;
    private String group;
    private String subject;
    private String messageBody;
    private List<String> to;
    private List<String> cc;
    private List<String> bcc;
    private Map <String, Object> data = new LinkedHashMap<>();

    @JsonProperty("triggers")
    private List<TriggerDescriptor> triggerDescriptors = new ArrayList<>();



}
