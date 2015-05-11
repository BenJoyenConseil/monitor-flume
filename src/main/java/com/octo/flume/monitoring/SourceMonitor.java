package com.octo.flume.monitoring;

import org.apache.flume.*;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.conf.Configurable;
import org.apache.flume.lifecycle.LifecycleState;

/**
 * Powered by o<+o
 */

public class SourceMonitor implements Source, Configurable, PollableSource {

    private PollableSource wrappedSource;
    private ShieldCounters counters;
    private String name = "r1";

    public SourceMonitor() {
        super();
        counters = new ShieldCounters("shield.source");
    }

    @Override
    public void setChannelProcessor(ChannelProcessor channelProcessor) {
        wrappedSource.setChannelProcessor(channelProcessor);
    }

    @Override
    public ChannelProcessor getChannelProcessor() {
        return wrappedSource.getChannelProcessor();
    }

    @Override
    public void start() {
        counters.start();
        counters.setShieldStartTime(System.currentTimeMillis());
        wrappedSource.start();
    }

    @Override
    public void stop() {
        wrappedSource.stop();
        counters.stop();
    }

    @Override
    public LifecycleState getLifecycleState() {
        return wrappedSource.getLifecycleState();
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void configure(Context context) {
        String className = context.getString("wrappedClass");
        try {
            wrappedSource = (PollableSource) Class.forName(className).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ((Configurable)wrappedSource).configure(context);
    }

    @Override
    public Status process() throws EventDeliveryException {
        return wrappedSource.process();
    }
}
