package com.support.core.profile;

import com.support.core.BaseLogger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class MillisecondsSimpleProfiler extends BaseLogger implements Profiler {

    private final Map<String, List<Long>> operationsExecuteTime = new HashMap<>();
    private final Map<String, Operation> startedOperations = new HashMap<>();

    @Override
    public void before(String operation) {
        Operation currentOperation = startedOperations.get(operation);
        if (currentOperation == null){
            currentOperation = new Operation();
            startedOperations.put(operation, currentOperation);
            currentOperation.start();
        } else {
            throw new RuntimeException("The operation " + operation + " has been started");
        }
    }

    @Override
    public void after(String operation) {
        Operation currentOperation = startedOperations.remove(operation);
        if (currentOperation != null){
            Long executeTime = currentOperation.stop();
            List<Long> operationExecuteTime = operationsExecuteTime.computeIfAbsent(operation, k -> new LinkedList<>());
            operationExecuteTime.add(executeTime);
        } else {
            throw new RuntimeException("The operation " + operation + " has NOT been started");
        }
    }

    @Override
    public void clear() {
        operationsExecuteTime.clear();
    }

    @Override
    public void clear(String operation) {
        operationsExecuteTime.remove(operation);
    }

    @Override
    public void printFullStatistic() {
        operationsExecuteTime.keySet().forEach(this::printStatistic);
    }

    @Override
    public void printStatistic(String operation) {
        List<Long> operationTime = operationsExecuteTime.get(operation);
        if (operationTime != null) {
            log("#######" + operation + "########");
            Long totalTimeToExecute = operationTime.stream().reduce(0L, (a, b) -> a + b);
            Double averageTimeOfExecution = (double) totalTimeToExecute / (double) operationTime.size();
            log("Operations count: " + operationTime.size());
            log("Total time to execute: " + totalTimeToExecute);
            log("Average time to execute: " + averageTimeOfExecution);
        } else {
            log("The operation " + operation + " was not profiled");
        }
    }

    private class Operation{
        Long startTime;
        void start(){
            startTime = System.currentTimeMillis();
        }
        Long stop(){
            if(startTime == null){
                throw new RuntimeException("Profiler exception, operation must be started before stop");
            }
            return System.currentTimeMillis() - startTime;
        }
    }
}
