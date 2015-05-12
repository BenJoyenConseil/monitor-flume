package com.octo.flume.monitoring;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.serialization.EventSerializer;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Powered by o<+o
 */

public class EventSerializerMonitor implements EventSerializer {
    private final EventSerializer wrapped;
    private final MonitorCounter counters;

    public EventSerializerMonitor(EventSerializer wrappedClass) {
        this.wrapped = wrappedClass;
        counters = new MonitorCounter("monitor.serializer." + System.currentTimeMillis());
        counters.start();
    }

    @Override
    public void afterCreate() throws IOException {
        wrapped.afterCreate();
    }

    @Override
    public void afterReopen() throws IOException {
        wrapped.afterReopen();
    }

    @Override
    public void write(Event event) throws IOException {
        wrapped.write(event);
        counters.incrementOut();
    }

    @Override
    public void flush() throws IOException {
        wrapped.flush();
    }

    @Override
    public void beforeClose() throws IOException {
        wrapped.beforeClose();
    }

    @Override
    public boolean supportsReopen() {
        return wrapped.supportsReopen();
    }

    @Override
    protected void finalize(){
        System.out.println("finalize serializer");
        counters.stop();
    }

    public static class Builder implements EventSerializer.Builder {

        @Override
        public EventSerializer build(Context context, OutputStream out) {
            EventSerializer.Builder wrappedClassBuilder = null;
            try {
                wrappedClassBuilder = (EventSerializer.Builder) Class.forName(context.getString("wrappedClass")).newInstance();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            EventSerializer wrapped = wrappedClassBuilder.build(context, out);

            return new EventSerializerMonitor(wrapped);
        }
    }
}
