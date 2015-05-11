package com.octo.flume.monitoring;

import javax.management.MXBean;

/**
 * Powered by o<+o
 */

public interface MonitorCounterMXBean {
    long getShieldStartTime();
    long getIn();
    long getOut();
    long getCountBatchSizeProcessed();
    long getRejected();
}
