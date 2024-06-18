package com.example.batchprocessing;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@StepScope
public class CustomPartitioner implements Partitioner {
    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> partitionMap = new HashMap<>(3);
        for (int count = 1; count < 4; count++) { //Hardcoded to keep track of partitions. We want three here.
            ExecutionContext executionContext = new ExecutionContext();
            executionContext.put("unique-to-partition", "value" + count);
            partitionMap.put("party-" + count, executionContext);
        }
        return partitionMap;
    }
}
