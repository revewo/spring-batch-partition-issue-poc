package com.example.batchprocessing;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfiguration {
    @Bean
    public Job primaryJob(JobRepository jobRepository, Step firstTaskletStep, JobCompletionNotificationListener listener, Step partitionStep) {
        return new JobBuilder("primaryJob", jobRepository)
                .listener(listener)
                .start(firstTaskletStep)
                .next(partitionStep)
                .build();
    }

    @Bean
    public Step firstTaskletStep(JobRepository jobRepository, PlatformTransactionManager transactionManagerForH2,
                                 Tasklet firstTasklet) {
        return new StepBuilder("firstTaskletStep", jobRepository)
                .tasklet(firstTasklet, transactionManagerForH2)
                .build();
    }

    @Bean
    public Step partitionStep(JobRepository jobRepository, Step primaryFlowStep,
                              CustomPartitioner customPartitioner, TaskExecutor tpTaskExecutor) {
        return new StepBuilder("partitionStep", jobRepository)
                .partitioner("logSomethingStep", customPartitioner)
                .step(primaryFlowStep)
                .taskExecutor(tpTaskExecutor)
                .build();
    }

    @Bean
    protected Step primaryFlowStep(JobRepository jobRepository, Flow primaryFlow) {
        return new StepBuilder("primaryFlowStep", jobRepository)
                .flow(primaryFlow)
                .build();
    }

    @Bean
    protected Flow primaryFlow(Step secondTaskletStep, Step thirdTaskletStep) {
        return new FlowBuilder<Flow>("primaryFlow")
                .start(secondTaskletStep).on("COMPLETED")
                .to(thirdTaskletStep)
                .build();
    }

    @Bean
    public Step secondTaskletStep(JobRepository jobRepository, PlatformTransactionManager transactionManagerForH2, Tasklet secondTasklet) {
        return new StepBuilder("secondTaskletStep", jobRepository)
                .tasklet(secondTasklet, transactionManagerForH2)
                .build();
    }

    @Bean
    public Step thirdTaskletStep(JobRepository jobRepository, Tasklet thirdTasklet, PlatformTransactionManager transactionManagerForH2) {
        return new StepBuilder("thirdTaskletStep", jobRepository)
                .tasklet(thirdTasklet, transactionManagerForH2)
                .build();
    }

    @Bean(destroyMethod = "shutdown")
    protected ThreadPoolTaskExecutor tpTaskExecutor() {
        var taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(3);
        taskExecutor.setMaxPoolSize(4);
        taskExecutor.setThreadNamePrefix("TPTETHREAD-");
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }
}
