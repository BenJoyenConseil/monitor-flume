package com.octo.flume.monitoring;

import org.apache.flume.instrumentation.MonitoredCounterGroup;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Powered by o<+o
 */

public class MonitorCounterUTest {

    @Test
    public void constructor_shouldCallSuperWith_typeOther(){
        // Given

        // When
        MonitoredCounterGroup.Type result = MonitoredCounterGroup.Type.valueOf(new MonitorCounter("name").getType());

        // Then
        assertThat(result).isEqualTo(MonitoredCounterGroup.Type.OTHER);
    }

}
