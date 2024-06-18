package com.example.batchprocessing;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatchProcessingApplication implements ApplicationRunner {
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;

    public static void main(String[] args) {
        SpringApplication.run(BatchProcessingApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("runIdentifier", "1")
                .toJobParameters();
        jobLauncher.run(job, params);

        System.exit(0);
    }
}
