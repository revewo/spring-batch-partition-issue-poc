package com.example.batchprocessing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class ThirdTasklet implements Tasklet {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String key;

    public ThirdTasklet(@Value("#{stepExecutionContext['unique-to-partition']}") String key) {
        this.key = key;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        logger.info("Inside ThirdTasklet. unique-to-partition is {}", key);

//        if (key.equals("value1") || key.equals("value3")) {
//            throw new RuntimeException();
//        }

// Uncomment the block of code above and run the batch and observe the logs of logger above. Restart/resubmit the batch after commenting the above code and again observe the logs of logger above.
// In the restarted run, for two different partitions, value of unique-to-partition is same which is not what we want here.
        return RepeatStatus.FINISHED;
    }
}
