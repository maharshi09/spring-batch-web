package com.demo.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by maharshi.bhatt on 16/4/17.
 */
@RestController
@RequestMapping(path = "/job")
public class JobController {
    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    JobExplorer jobExplorer;

    @Autowired
    JobLocator locator;

    AtomicInteger jobCount = new AtomicInteger(0);

    @GetMapping
    public List<String> listJobs(){

        return jobExplorer.getJobNames();

    }

    @GetMapping(path = "/launch")
    public ResponseEntity<String> launchJob(@RequestParam("job")String jobName){
        Job dbJob;
        try {
            int count = jobCount.incrementAndGet();
            dbJob = locator.getJob(jobName);
            JobParameters parameters = new JobParametersBuilder().addString("jobNo",Integer.toString(count)).toJobParameters();
            jobLauncher.run(dbJob,parameters);

            return ResponseEntity.ok("Success");

        }catch (Exception e){
            ResponseEntity<String> response = ResponseEntity.ok(e.getMessage());
            return response;
        }

    }
}
