package com.socgen.shield;

import org.apache.flume.instrumentation.MonitoredCounterGroup;

public class ShieldCounters extends MonitoredCounterGroup implements MonitorCounterMXBean {

    private static final String COUNTER_EVENT_INPUT = "shield.in.events.count";
    private static final String COUNTER_EVENT_OUTPUT = "shield.out.events.count";
    private static final String COUNTER_REJECTED = "shield.rejected.event.count";
    private static final String START_TIME = "shield.start.time";
    private static final String COUNT_BATCH_SIZE_PROCESSED = "shield.batchSize.processed.count";

    private static final String[] ATTRIBUTES = {COUNTER_EVENT_INPUT, COUNTER_EVENT_OUTPUT, START_TIME, COUNTER_REJECTED, COUNT_BATCH_SIZE_PROCESSED};

    protected ShieldCounters(String name) {
        super(Type.SOURCE, name, ATTRIBUTES);
    }

    public void setShieldStartTime(long startTime) {
        set(START_TIME, startTime);
    }

    public long getShieldStartTime() {
        return get(START_TIME);
    }

    public long incrementIn(){
        return increment(COUNTER_EVENT_INPUT);
    }

    public long incrementOut(){
        return increment(COUNTER_EVENT_OUTPUT);
    }

    @Override
    public long getIn(){
        return get(COUNTER_EVENT_INPUT);
    }

    @Override
    public long getOut(){
        return get(COUNTER_EVENT_OUTPUT);
    }

    @Override
    public long getCountBatchSizeProcessed() {
        return get(COUNT_BATCH_SIZE_PROCESSED);
    }

    public long incrementCountBatchSizeProcessed(long increment) {
        return addAndGet(COUNT_BATCH_SIZE_PROCESSED, increment);
    }

    @Override
    public long getRejected() {
        return get(COUNTER_REJECTED);
    }

    public void incrementIn(long increment) {
        addAndGet(COUNTER_EVENT_INPUT, increment);
    }

    public void incrementOut(long increment) {
        addAndGet(COUNTER_EVENT_OUTPUT, increment);
    }

    public void incrementRejected(long increment) {
        addAndGet(COUNTER_REJECTED, increment);
    }
}