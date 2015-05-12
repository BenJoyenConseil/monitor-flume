package com.octo.flume.monitoring;

import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.lifecycle.LifecycleState;
import org.apache.flume.sink.AbstractSink;

/**
 * Powered by o<+o
 */

public class SinkMonitor extends AbstractSink implements Sink, Configurable {

    private final MonitorCounter counters;
    private Sink wrapped;

    public SinkMonitor() {
        counters = new MonitorCounter("monitor.sink");
    }

    @Override
    public void setChannel(Channel channel) {
        wrapped.setChannel(channel);
    }

    @Override
    public Channel getChannel() {
        return wrapped.getChannel();
    }

    @Override
    public Status process() throws EventDeliveryException {
        Status status = wrapped.process();
        counters.incrementCountBatchSizeProcessed(1);
        return status;
    }

    @Override
    public void configure(Context context) {
        try {
            wrapped = (Sink) Class.forName(context.getString("wrappedClass")).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ((Configurable)wrapped).configure(context);
    }

    @Override
    public void start() {
        counters.start();
        wrapped.start();
    }

    @Override
    public void stop() {
        wrapped.stop();
        counters.stop();
    }

    @Override
    public LifecycleState getLifecycleState() {
        return wrapped.getLifecycleState();
    }
}
