package com.demo.controller;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;
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

    @Autowired
    JobOperator jobOperator;

    Random random = new Random();

    @GetMapping(path = "/status/{id}")
    public String getJobExecutionStatus(@PathVariable(name="id") Long id){
        try {
            return jobOperator.getSummary(id);
        }catch (NoSuchJobExecutionException e){
            return "error";
        }
    }

    @GetMapping
    public List<String> listJobs(){

        return jobExplorer.getJobNames();

    }

    @GetMapping(path = "/launch")
    public ResponseEntity<String> launchJob(@RequestParam("job")String jobName){
        Job dbJob;
        try {
            int count = random.nextInt();
            dbJob = locator.getJob(jobName);
            JobParameters parameters = new JobParametersBuilder().addString("jobNo",Integer.toString(count)).toJobParameters();
            JobExecution jobExecution = jobLauncher.run(dbJob,parameters);

            return ResponseEntity.ok(jobExecution.toString());

        }catch (Exception e){
            ResponseEntity<String> response = ResponseEntity.ok(e.getMessage());
            return response;
        }

    }
}
