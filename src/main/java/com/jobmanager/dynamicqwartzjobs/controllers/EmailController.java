package com.jobmanager.dynamicqwartzjobs.controllers;

import com.jobmanager.dynamicqwartzjobs.model.JobDescriptor;
import com.jobmanager.dynamicqwartzjobs.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by sousaJ on 28/10/2020
 * in package - com.jobmanager.dynamicqwartzjobs.controllers
 **/
@RestController
@RequestMapping("/api/v1.0")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping(path = "/groups/{group}/jobs")
    public ResponseEntity<JobDescriptor> createJob(@PathVariable String group, @RequestBody JobDescriptor jobDescriptor){
        return new ResponseEntity<>(emailService.createJob(group, jobDescriptor), HttpStatus.CREATED);
    }

    @PostMapping(path = "/groups/{group}/jobs/{name}")
    public ResponseEntity<JobDescriptor> findJob(@PathVariable String group, @PathVariable String name){
        return emailService.findJob(group, name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/groups/{group}/jobs/{name}/update")
    public ResponseEntity<Void> updateJob(@PathVariable String group, @PathVariable String name, @RequestBody JobDescriptor jobDescriptor){
        emailService.updateJob(group, name, jobDescriptor);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/groups/{group}/jobs/{name}/delete")
    public ResponseEntity<Void> deleteJob(@PathVariable String group, @PathVariable String name){
        emailService.deleteJob(group, name);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/groups/{group}/jobs/{name}/pause")
    public ResponseEntity<Void> pauseJob(@PathVariable String group, @PathVariable String name){
        emailService.pauseJob(group, name);
        return ResponseEntity.noContent().build();
    }
    @PostMapping(path = "/groups/{group}/jobs/{name}/resume")
    public ResponseEntity<Void> resumeJob(@PathVariable String group, @PathVariable String name){
        emailService.resumeJob(group, name);
        return ResponseEntity.noContent().build();
    }
}


















